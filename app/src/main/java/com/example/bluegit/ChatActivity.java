package com.example.bluegit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.accounts.Account;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bluegit.adapters.MessageAdapter;
import com.example.bluegit.adapters.ProductDisplayAdapter;
import com.example.bluegit.model.Message;

import com.example.bluegit.model.Product;
import com.example.bluegit.model.User;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.jakewharton.threetenabp.AndroidThreeTen;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ChatActivity extends AppCompatActivity {
    String docId;
    String docId2;
    FirebaseFirestore db;
    DocumentReference meRef;
    DocumentReference otherRef;
    DocumentReference chatRef;
    String meId;
    String otherId;
    FirestoreRecyclerAdapter<Message, MessageAdapter.MessageHolder> adapter;
    FireStoreManager fireStoreManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidThreeTen.init(this);
        setContentView(R.layout.activity_chat);

        fireStoreManager = new FireStoreManager(ChatActivity.this, FirebaseAuth.getInstance().getCurrentUser());

        meId = FirebaseAuth.getInstance().getUid();
        otherId = getIntent().getStringExtra("otherUserId");
        docId = meId + "_" + otherId;
        docId2 = otherId + "_" + meId;

//        Check if chat existed
        db = FirebaseFirestore.getInstance();
        meRef = db.collection("users").document(meId);
        otherRef = db.collection("users").document(otherId);

        fireStoreManager.getUserById(otherId ,new FireStoreManager.GetUserDataCallBack() {
            @Override
            public void onSuccess(User otherUser) {
                TextView otherNameTextView = findViewById(R.id.otherName);
                otherNameTextView.setText(otherUser.getDisplayName());
                ImageView otherImg = findViewById(R.id.otherImg);
                Picasso.get().load(Uri.parse(otherUser.getProfileImageSrc())).into(otherImg);

            }

            @Override
            public void onFailure(Exception e) {
                Log.d("GetUserDataException", e.getMessage());
            }
        });

        meRef.collection("chatWith")
            .document(otherId).get()
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists() && document.get(otherId)!= null) {
                        chatRef = (DocumentReference) document.get(otherId);
                    } else {
                        createChatRef();
                    }
                } else {
                    Toast.makeText(ChatActivity.this,
                            "error loading message",
                             Toast.LENGTH_SHORT).show();
                    finish();
                }
            })
            .addOnCompleteListener(task -> {
                Query query = chatRef.collection("messages").orderBy("sentTime").limit(50);
                FirestoreRecyclerOptions<Message> options = new FirestoreRecyclerOptions.Builder<Message>()
                        .setQuery(query, Message.class)
                        .build();

                adapter = new MessageAdapter(options, meId);

                RecyclerView recyclerView = findViewById(R.id.chatRecyclerView);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(ChatActivity.this));
                adapter.startListening();
            });
}

    @Override
    protected void onStart() {
        super.onStart();
//        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    public void sendMsg(View view) {
        EditText messageInput = findViewById(R.id.messageInput);
        if (messageInput.getText().toString().equals("")) messageInput.setError("no message");
        else {
            String messageId = UUID.randomUUID().toString();
            String msgStr = messageInput.getText().toString();
            Message msg = new Message(msgStr, meId, otherId);

            DocumentReference msgRef = chatRef.collection("messages").document(messageId);

            chatRef.collection("messages").document(messageId).set(msg)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Map<String, Object> data = new HashMap<>();
                            data.put("lastMsg", msgRef);
                            chatRef.update(data);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ChatActivity.this,
                                "Error, message can not send", Toast.LENGTH_SHORT).show();
                    }
                });
            messageInput.setText("");
        }
    }

    private void createChatRef(){
        Map<String, String> members = new HashMap<>();
        members.put("m1", meId);
        members.put("m2", otherId);

        chatRef = FirebaseFirestore.getInstance().
                collection("chat").document(UUID.randomUUID().toString());
        chatRef.set(members);

        Map<String, DocumentReference> meChat = new HashMap<>();
        meChat.put(otherId, chatRef);
        meRef.collection("chatWith").document(otherId).set(meChat);

        Map<String, DocumentReference> otherChat = new HashMap<>();
        otherChat.put(meId, chatRef);
        otherRef.collection("chatWith").document(meId).set(otherChat);
    }

    private class MsgHolder extends RecyclerView.ViewHolder {
        TextView message;
        TextView dateTime;

        MsgHolder(View itemView) {
            super(itemView);
            message = (TextView) itemView.findViewById(R.id.Msg);
            dateTime = (TextView) itemView.findViewById(R.id.sentDateTime);
        }

        void bind(Message messageObj) {
            message.setText(messageObj.getMessage());

//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd - HH:mm");
            DateFormat formatter = new SimpleDateFormat("MMM dd - HH:mm");
            dateTime.setText(formatter.format(messageObj.getSentTime().toDate()));
        }
    }

    public void goBack(View view){
        finish();
    }
}
