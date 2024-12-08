package com.example.my_application;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private UserAdapter userAdapter;
    private List<AppUser> userList;
    private List<AppUser> filteredUserList;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views and variables
        ListView listView = findViewById(R.id.imageListView);
        Button logoutButton = findViewById(R.id.logoutButton);
       SearchView searchView = findViewById(R.id.searchView); // SearchView
        db = FirebaseFirestore.getInstance();
        userList = new ArrayList<>();
        filteredUserList = new ArrayList<>();
        userAdapter = new UserAdapter(this, filteredUserList);

        listView.setAdapter(userAdapter);

        // Fetch users from Firestore
        fetchUsersFromFirestore();

        // Log out action
        logoutButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        // Handle item clicks
        listView.setOnItemClickListener((parent, view, position, id) -> {
            AppUser clickedUser = filteredUserList.get(position);
            String username = clickedUser.getUsername();
            Intent intent = new Intent(MainActivity.this, ChatActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
        });

        // Search bar filtering

       searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterUsers(query);  // Apply filter when the user submits the query
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterUsers(newText);  // Filter as the user types
                return false;
            }
        });
    }

    private void fetchUsersFromFirestore() {
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        CollectionReference usersCollection = db.collection("users");

        usersCollection.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        userList.clear(); // Clear previous data
                        for (DocumentSnapshot document : querySnapshot) {
                            String uid = document.getId();
                            if (!uid.equals(currentUserId)) {
                                String username = document.getString("username");
                                AppUser user = new AppUser(username, uid);
                                userList.add(user);
                            }
                        }

                        // Initially, show all users
                        filterUsers(""); // Passing an empty query will show all users
                    } else {
                        Log.e("MainActivity", "Error getting users", task.getException());
                    }
                });
    }

    private void filterUsers(String query) {
        filteredUserList.clear(); // Clear the filtered list

        if (query.isEmpty()) {
            // If the query is empty, show all users
            filteredUserList.addAll(userList);
        } else {
            // Otherwise, filter users based on the query
            for (AppUser user : userList) {
                if (user.getUsername().toLowerCase().contains(query.toLowerCase())) {
                    filteredUserList.add(user);
                }
            }
        }

        // Notify the adapter that the data has changed
        userAdapter.notifyDataSetChanged();
    }
}
