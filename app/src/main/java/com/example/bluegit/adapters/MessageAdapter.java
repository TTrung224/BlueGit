package com.example.bluegit.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bluegit.R;
import com.example.bluegit.model.Message;

import org.threeten.bp.format.DateTimeFormatter;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter {
    private static final int VIEW_TYPE_MESSAGE_SENDER = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVER = 2;

    private List<Message> messageList;
    private String currentUserId;
    private LayoutInflater inflater;

    public MessageAdapter(Context context, List<Message> messageList, String currentUserId) {
        this.messageList = messageList;
        this.currentUserId = currentUserId;
        this.inflater = LayoutInflater.from(context);
    }

    private class ReceiverMsgHolder extends RecyclerView.ViewHolder {
        TextView message, dateTime;

        ReceiverMsgHolder(View itemView) {
            super(itemView);
            message = (TextView) itemView.findViewById(R.id.receiverMsg);
            dateTime = (TextView) itemView.findViewById(R.id.sentDateTime);
        }

        void bind(Message messageObj) {
            message.setText(messageObj.message);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd - HH:mm");
            dateTime.setText(messageObj.sentTime.format(formatter));
        }
    }

    private class SenderMsgHolder extends RecyclerView.ViewHolder {
        TextView message, dateTime;

        SenderMsgHolder(View itemView) {
            super(itemView);
            message = (TextView) itemView.findViewById(R.id.senderMsg);
            dateTime = (TextView) itemView.findViewById(R.id.sentDateTime);
        }

        void bind(Message messageObj) {
            message.setText(messageObj.message);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd - HH:mm");
            dateTime.setText(messageObj.sentTime.format(formatter));
        }
    }

    @Override
    public int getItemViewType(int position) {
        Message message = (Message) messageList.get(position);

        if (message.fromId.equals(currentUserId)) {
            return VIEW_TYPE_MESSAGE_SENDER;
        } else {
            return VIEW_TYPE_MESSAGE_RECEIVER;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        if (viewType == VIEW_TYPE_MESSAGE_SENDER) {
            view = inflater.inflate(R.layout.layout_sender_message, parent, false);
            return new SenderMsgHolder(view);
        } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVER) {
            view = inflater.inflate(R.layout.layout_receiver_message, parent, false);
            return new ReceiverMsgHolder(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = (Message) messageList.get(position);

        switch (holder.getItemViewType()) {
            case VIEW_TYPE_MESSAGE_SENDER:
                ((SenderMsgHolder) holder).bind(message);
                break;
            case VIEW_TYPE_MESSAGE_RECEIVER:
                ((ReceiverMsgHolder) holder).bind(message);
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }
}