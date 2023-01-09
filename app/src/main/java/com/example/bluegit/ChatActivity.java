package com.example.bluegit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.bluegit.adapters.BuyerOrderAdapter;
import com.example.bluegit.adapters.MessageAdapter;
import com.example.bluegit.model.Message;
import com.jakewharton.threetenabp.AndroidThreeTen;

import org.threeten.bp.LocalDateTime;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    private List<Message> messageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidThreeTen.init(this);

        setContentView(R.layout.activity_chat);
        messageList = new ArrayList<>();
        messageList.add(new Message("Halo", "1", "2"));
        messageList.add(new Message("I'm Khoi Tran bla bla bla bla bla bla bla bla bla", "1", "2"));
        messageList.add(new Message("hello", "2", "1"));
        messageList.add(new Message("hu hu hu hu hu hu hu hu hu hu hu hu hu hu hu hu hu hu " +
                "hu hu hu hu hu hu hu hu hu hu hu hu ", "2", "1"));
        messageList.add(new Message("hu hu hu hu hu hu hu hu hu hu hu hu hu hu hu hu hu hu " +
                "hu hu hu hu hu hu hu hu hu hu hu hu ", "2", "1"));
        messageList.add(new Message("hu hu hu hu hu hu hu hu hu hu hu hu hu hu hu hu hu hu " +
                "hu hu hu hu hu hu hu hu hu hu hu hu ", "2", "1"));
        messageList.add(new Message("hu hu hu hu hu hu hu hu hu hu hu hu hu hu hu hu hu hu " +
                "hu hu hu hu hu hu hu hu hu hu hu hu ", "2", "1"));
        messageList.add(new Message("hu hu hu hu hu hu hu hu hu hu hu hu hu hu hu hu hu hu " +
                "hu hu hu hu hu hu hu hu hu hu hu hu ", "2", "1"));
        messageList.add(new Message("hu hu hu hu hu hu hu hu hu hu hu hu hu hu hu hu hu hu " +
                "hu hu hu hu hu hu hu hu hu hu hu hu ", "2", "1"));

    }

    @Override
    protected void onStart() {
        super.onStart();
        RecyclerView recyclerView = findViewById(R.id.chatRecyclerView);
        MessageAdapter adapter = new MessageAdapter(this, messageList, "1");
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.scrollToPosition(messageList.size() - 1);
    }
}