package com.example.bluegit.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bluegit.ChatActivity;
import com.example.bluegit.R;
import com.example.bluegit.model.Message;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class MessageAdapter extends FirestoreRecyclerAdapter<Message, MessageAdapter.MessageHolder>{

    private static final int VIEW_TYPE_MESSAGE_SENDER = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVER = 2;
    String meId;

    public MessageAdapter(@NonNull FirestoreRecyclerOptions<Message> options, String meId) {
        super(options);
        this.meId = meId;
    }

    @Override
    public void onBindViewHolder(MessageHolder holder, int position, Message model) {
        holder.bind(model);
    }

    @NonNull
    @Override
    public MessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        if (viewType == VIEW_TYPE_MESSAGE_SENDER) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_sender_message, parent, false);
            return new MessageHolder(view);
        } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVER) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_receiver_message, parent, false);
            return new MessageHolder(view);
        }

        return null;
    }

    @Override
    public int getItemViewType(int position) {
        Message message = getItem(position);

        if (message.getFromId().equals(meId)) {
            return VIEW_TYPE_MESSAGE_SENDER;
        } else {
            return VIEW_TYPE_MESSAGE_RECEIVER;
        }
    }

    public class MessageHolder extends RecyclerView.ViewHolder {
        TextView message;
        TextView dateTime;

        MessageHolder(View itemView) {
            super(itemView);
            message = (TextView) itemView.findViewById(R.id.Msg);
            dateTime = (TextView) itemView.findViewById(R.id.sentDateTime);
        }

        void bind(Message messageObj) {
            message.setText(messageObj.getMessage());

//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd - HH:mm");
            @SuppressLint("SimpleDateFormat") DateFormat formatter = new SimpleDateFormat("MMM dd - HH:mm");
            dateTime.setText(formatter.format(messageObj.getSentTime().toDate()));
        }
    }
}