package com.example.my_application;

import android.os.Bundle;

import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView chatRecyclerView;
    private EditText messageInput;
    private ImageButton sendButton;
    private TextView chatUsername;

    private MessageAdapter messageAdapter;
    private List<AppMessage> messageList;

    private String currentUserId;
    private String chatPartnerId;
    private String chatPartnerUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // Initialize views
        chatRecyclerView = findViewById(R.id.chat_recycler_view);
        messageInput = findViewById(R.id.message_input);
        sendButton = findViewById(R.id.send_button);
        chatUsername = findViewById(R.id.chat_username);

        // Retrieve the passed data
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid(); // Dynamically set current user ID
        chatPartnerId = getIntent().getStringExtra("chatPartnerId");
        chatPartnerUsername = getIntent().getStringExtra("username");

        // Set the chat partner's username in the TextView
        chatUsername.setText(chatPartnerUsername);

        // Initialize message list and adapter
        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(this, messageList, currentUserId);

        // Setup RecyclerView
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatRecyclerView.setAdapter(messageAdapter);

        // Setup send button click listener
        sendButton.setOnClickListener(v -> sendMessage());
    }

    // Send message and update RecyclerView
    private void sendMessage() {
        String messageText = messageInput.getText().toString().trim();
        if (!messageText.isEmpty()) {
            long timestamp = System.currentTimeMillis();
            // Create the message
            AppMessage message = new AppMessage(currentUserId, chatPartnerId, messageText, timestamp);

            // Add the message to the list and notify the adapter
            messageList.add(message);
            messageAdapter.notifyItemInserted(messageList.size() - 1); // Notify that a new message was added

            // Scroll to the latest message
            chatRecyclerView.scrollToPosition(messageList.size() - 1);

            // Clear the input field
            messageInput.setText("");
        }
    }
}
