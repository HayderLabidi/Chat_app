package com.example.my_application;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<AppMessage> messageList;
    private final String currentUserId;

    private static final int TYPE_OUTGOING = 1;
    private static final int TYPE_INCOMING = 2;

    public MessageAdapter(Context context, List<AppMessage> messageList, String currentUserId) {
        this.messageList = messageList;
        this.currentUserId = currentUserId;
    }

    @Override
    public int getItemViewType(int position) {
        AppMessage message = messageList.get(position);
        return message.getSenderId().equals(currentUserId) ? TYPE_OUTGOING : TYPE_INCOMING;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_OUTGOING) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_outgoing, parent, false);
            return new OutgoingMessageViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_incoming, parent, false);
            return new IncomingMessageViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        AppMessage message = messageList.get(position);

        if (holder instanceof OutgoingMessageViewHolder) {
            ((OutgoingMessageViewHolder) holder).outgoingMessage.setText(message.getMessageText());
        } else {
            ((IncomingMessageViewHolder) holder).incomingMessage.setText(message.getMessageText());
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    // This method allows adding a new message and notifying the adapter to refresh the view
    public void addMessage(AppMessage message) {
        messageList.add(message);
        notifyItemInserted(messageList.size() - 1);  // Notify that a new item has been added
    }

    public static class OutgoingMessageViewHolder extends RecyclerView.ViewHolder {
        TextView outgoingMessage;

        public OutgoingMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            outgoingMessage = itemView.findViewById(R.id.outgoing_message);
        }
    }

    public static class IncomingMessageViewHolder extends RecyclerView.ViewHolder {
        TextView incomingMessage;

        public IncomingMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            incomingMessage = itemView.findViewById(R.id.incoming_message);
        }
    }
}
