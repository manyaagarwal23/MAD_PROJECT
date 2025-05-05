package com.example.unimingle;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    private TextView tvUserName;
    private RecyclerView recyclerViewMessages;
    private EditText etMessage;
    private Button btnSend;
    private MessageAdapter messageAdapter;
    private List<Message> messageList;
    private String currentUid, chatUserId, chatUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        tvUserName = findViewById(R.id.tvUserName);
        recyclerViewMessages = findViewById(R.id.recyclerViewMessages);
        etMessage = findViewById(R.id.etMessage);
        btnSend = findViewById(R.id.btnSend);

        chatUserId = getIntent().getStringExtra("userId");
        chatUserName = getIntent().getStringExtra("userName");
        tvUserName.setText(chatUserName);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            finish();
            return;
        }
        currentUid = user.getUid();

        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(messageList, currentUid);
        recyclerViewMessages.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewMessages.setAdapter(messageAdapter);

        loadMessages();

        btnSend.setOnClickListener(v -> {
            String msg = etMessage.getText().toString().trim();
            if (!TextUtils.isEmpty(msg)) {
                sendMessage(msg);
                etMessage.setText("");
            }
        });
    }

    private void loadMessages() {
        DatabaseReference chatsRef = FirebaseDatabase.getInstance().getReference("Chats");
        messageList.clear();

        // Listen for messages sent from currentUid to chatUserId
        chatsRef.child(currentUid).child(chatUserId)
            .addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    messageList.clear();
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Message msg = ds.getValue(Message.class);
                        if (msg != null) messageList.add(msg);
                    }
                    // Now also get messages sent from chatUserId to currentUid
                    chatsRef.child(chatUserId).child(currentUid)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot ds : snapshot.getChildren()) {
                                    Message msg = ds.getValue(Message.class);
                                    if (msg != null) messageList.add(msg);
                                }
                                // Sort messages by timestamp
                                java.util.Collections.sort(messageList, (a, b) -> Long.compare(a.getTimestamp(), b.getTimestamp()));
                                messageAdapter.notifyDataSetChanged();
                                recyclerViewMessages.scrollToPosition(messageList.size() - 1);
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {}
                        });
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {}
            });
    }

    private void sendMessage(String msg) {
        DatabaseReference chatRef = FirebaseDatabase.getInstance()
                .getReference("Chats")
                .child(chatUserId)
                .child(currentUid)
                .push();

        Message message = new Message(currentUid, chatUserId, msg, System.currentTimeMillis());
        chatRef.setValue(message);
    }
} 