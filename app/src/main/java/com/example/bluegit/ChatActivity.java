package com.example.bluegit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bluegit.adapters.MessageAdapter;
import com.example.bluegit.model.Message;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.SnapshotParser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.jakewharton.threetenabp.AndroidThreeTen;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import okhttp3.internal.cache.DiskLruCache;

public class ChatActivity extends AppCompatActivity {
    private List<Message> messageList;
    String docId;
    String docId2;
    FirebaseFirestore db;
    DocumentReference meRef;
    DocumentReference otherRef;
    DocumentReference chatRef;
    String meId;
    String otherId;
    FirestoreRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidThreeTen.init(this);
        setContentView(R.layout.activity_chat);

        meId = FirebaseAuth.getInstance().getUid();
        otherId = getIntent().getStringExtra("otherUserId");
        docId = meId + "_" + otherId;
        docId2 = otherId + "_" + meId;

//        Check if chat existed
        db = FirebaseFirestore.getInstance();
        meRef = db.collection("users").document(meId);
        otherRef = db.collection("users").document(otherId);
        meRef.collection("chatWith").document(otherId).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
           @Override
           public void onComplete(@NonNull Task<DocumentSnapshot> task) {
               if (task.isSuccessful()) {
                   DocumentSnapshot document = task.getResult();
                   if (document.exists() && document.get(otherId)!= null) {
                       chatRef = (DocumentReference) document.get(otherId);
                       Log.d("TESTING", chatRef.getPath());

//                       chatRef = db.document(document.get(otherId).toString());
                   } else {
                       createChatRef();
                   }
               } else {
                   Toast.makeText(ChatActivity.this, "error loading message", Toast.LENGTH_SHORT).show();;
                   finish();
               }
           }
        });

//        RecyclerView recyclerView = findViewById(R.id.chatRecyclerView);
//        recyclerView.setAdapter(adapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();
//        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
//        adapter.stopListening();
    }

    public void sendMsg(View view) {
        EditText messageInput = findViewById(R.id.messageInput);
        if (messageInput.getText().toString().equals("")) messageInput.setError("no message");
        else {
            String messageId = UUID.randomUUID().toString();
            String msgStr = messageInput.getText().toString();
            Message msg = new Message(msgStr, meId, otherId);

            Map<String, Object> nestedData = new HashMap<>();
            nestedData.put(messageId, msg);

            chatRef.collection("messages").document(messageId).set(nestedData)
                    .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ChatActivity.this,
                                "Error, message can not send", Toast.LENGTH_SHORT).show();
                        ;
                    }
                });
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
        otherChat.put(otherId, chatRef);
        otherRef.collection("chatWith").document(otherId).set(meChat);
    }

    private class MsgHolder extends RecyclerView.ViewHolder {
        TextView message;
        TextView dateTime;

        MsgHolder(View itemView) {
            super(itemView);
            message = (TextView) itemView.findViewById(R.id.Msg);
            dateTime = (TextView) itemView.findViewById(R.id.sentDateTime);
        }
    }
}
