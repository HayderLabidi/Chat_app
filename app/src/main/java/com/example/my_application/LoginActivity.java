package com.example.my_application;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.my_application.databinding.ActivityLoginBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Setting up binding
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initializing Firebase Auth
        auth = FirebaseAuth.getInstance();

        // Adding onClickListener to the Continue button
        binding.continueBtn.setOnClickListener(v -> {
            String email = binding.email.getText().toString().trim();
            String password = binding.password.getText().toString().trim();

            if (validateInputs(email, password)) {
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this, task -> {
                            if (task.isSuccessful()) {
                                Log.d("LoginActivity", "signInWithEmail:success"); // Logs to console
                                Toast.makeText(LoginActivity.this, "Login successful!", Toast.LENGTH_SHORT).show(); // Toast feedback

                                // Current user
                                FirebaseUser user = auth.getCurrentUser();

                                // Navigate to MainActivity
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Log.w("LoginActivity", "signInWithEmail:failure", task.getException()); // Logs failure
                                Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show(); // Failure toast
                            }

                        });
            }
        });

        // Move to SignupActivity when text link is clicked
        binding.move.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(intent);
        });
    }

    private boolean validateInputs(String email, String password) {
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
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
