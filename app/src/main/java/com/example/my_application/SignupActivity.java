package com.example.my_application;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.my_application.databinding.ActivitySignupBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {
    private ActivitySignupBinding binding;
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        binding.continueBtn.setOnClickListener(v -> {
            String username = binding.username.getText().toString().trim();
            String email = binding.email.getText().toString().trim();
            String password = binding.password.getText().toString().trim();

            if (validateInputs(username, email, password)) {
                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, task -> {
                            if (task.isSuccessful()) {
                                FirebaseUser user = auth.getCurrentUser();

                                if (user != null) {
                                    Map<String, Object> userMap = new HashMap<>();
                                    userMap.put("username", username);
                                    userMap.put("email", email);

                                    db.collection("users")
                                            .document(user.getUid())
                                            .set(userMap)
                                            .addOnSuccessListener(unused -> {
                                                Log.d("SignupActivity", "Username added to Firestore successfully");
                                                Toast.makeText(SignupActivity.this, "Signup successful. Please log in.", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                                                startActivity(intent);
                                                finish();
                                            })
                                            .addOnFailureListener(e -> {
                                                Log.w("SignupActivity", "Error adding username to Firestore", e);
                                                Toast.makeText(SignupActivity.this, "Failed to save username", Toast.LENGTH_SHORT).show();
                                            });
                                }
                            } else {
                                Log.w("SignupActivity", "createUserWithEmail:failure", task.getException());
                                Toast.makeText(SignupActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        binding.move.setOnClickListener(v -> {
            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }

    private boolean validateInputs(String username, String email, String password) {
        if (username.isEmpty()) {
            binding.username.setError("Username is required");
            binding.username.requestFocus();
            return false;
        }
        if (email.isEmpty()) {
            binding.email.setError("Email is required");
            binding.email.requestFocus();
            return false;
        }
        if (password.isEmpty()) {
            binding.password.setError("Password is required");
            binding.password.requestFocus();
            return false;
        }
        if (password.length() < 6) {
            binding.password.setError("Password must be at least 6 characters");
            binding.password.requestFocus();
            return false;
        }
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }
}