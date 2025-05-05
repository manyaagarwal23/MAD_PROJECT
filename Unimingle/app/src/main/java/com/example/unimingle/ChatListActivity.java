package com.example.unimingle;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
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

public class ChatListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ChatListAdapter adapter;
    private ArrayList<User> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        recyclerView = findViewById(R.id.recyclerViewChatList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        userList = new ArrayList<>();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }
        String currentUid = currentUser.getUid();

        // Fetch all users from UserEmails node except current user
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("UserEmails");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String uid = ds.getKey();
                    String email = ds.getValue(String.class);
                    if (!uid.equals(currentUid)) {
                        userList.add(new User(uid, email, ""));
                    }
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        adapter = new ChatListAdapter(userList, new ChatListAdapter.OnUserClickListener() {
            @Override
            public void onUserClick(User user) {
                Intent intent = new Intent(ChatListActivity.this, ChatActivity.class);
                intent.putExtra("userId", user.getId());
                intent.putExtra("userName", user.getName());
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);
    }
} 