package com.example.unimingle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    private List<Message> messageList;
    private String currentUserId;

    public MessageAdapter(List<Message> messageList, String currentUserId) {
        this.messageList = messageList;
        this.currentUserId = currentUserId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Message message = messageList.get(position);
        holder.tvMessage.setText(message.getMessage());
        // Align message based on sender
        if (message.getSender().equals(currentUserId)) {
            holder.tvMessage.setBackgroundResource(R.drawable.bg_message_sent);
            holder.tvMessage.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
        } else {
            holder.tvMessage.setBackgroundResource(R.drawable.bg_message_received);
            holder.tvMessage.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvMessage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMessage = itemView.findViewById(R.id.tvMessage);
        }
    }
} 