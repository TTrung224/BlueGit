package com.example.bluegit;

import static org.threeten.bp.zone.ZoneRulesProvider.refresh;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.bluegit.adapters.ChatItemAdapter;
import com.example.bluegit.adapters.RecyclerViewOnClickListener;
import com.example.bluegit.model.Message;
import com.example.bluegit.model.Product;
import com.example.bluegit.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Map;

public class ChatListActivity extends AppCompatActivity {

    FireStoreManager fireStoreManager;
    ArrayList<String> othersIdArray;
    ArrayList<DocumentReference> chatRefArray;

    ArrayList<User> userList;
    ArrayList<Message> messageList;

    ProgressBar progressBar;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        progressBar = findViewById(R.id.progressBar2);

        fireStoreManager = new FireStoreManager(ChatListActivity.this,
                FirebaseAuth.getInstance().getCurrentUser());

        recyclerView = findViewById(R.id.chatListRecyclerView);

        fireStoreManager.getChatWithOfAUser(new FireStoreManager.getChatWithOfAUserCallBack() {
            @Override
            public void onSuccess(ArrayList<DocumentReference> chatRefList) {
                if(chatRefList != null) {
                    fireStoreManager.getDataForChatAdapter(chatRefList, new FireStoreManager.getDataForChatAdapterCallBack() {
                        @Override
                        public void onSuccess(Map<User, Message> data) {
                            userList = new ArrayList<>(data.keySet());
                            messageList = new ArrayList<>(data.values());

                            Log.d("TESTING", userList.toString());
                            Log.d("TESTING", messageList.toString());

                            ChatItemAdapter adapter = new ChatItemAdapter(messageList, userList,
                                    ChatListActivity.this, new RecyclerViewOnClickListener() {
                                @Override
                                public void onItemClick(int position) {
                                    String otherId = userList.get(position).getId();
                                    Intent intent = new Intent(ChatListActivity.this, ChatActivity.class);
                                    intent.putExtra("otherUserId", otherId);
                                    startActivityForResult(intent, 100);
                                }
                            });
                            recyclerView.setAdapter(adapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(ChatListActivity.this));
                            progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onFailure(Exception e) {
                            Toast.makeText(ChatListActivity.this, "fail to load chats, please try again", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });

                } else {
                    findViewById(R.id.noChatAnnounce).setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(ChatListActivity.this, "fail to load chats, please try again", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    public void goBack(View view){
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==RESULT_OK && requestCode==100){
            finish();
            overridePendingTransition(0, 0);
            startActivity(getIntent());
            overridePendingTransition(0, 0);
        }
    }
}