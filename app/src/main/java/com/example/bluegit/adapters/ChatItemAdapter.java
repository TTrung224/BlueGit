package com.example.bluegit.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bluegit.R;
import com.example.bluegit.model.Message;
import com.example.bluegit.model.User;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ChatItemAdapter extends RecyclerView.Adapter<ChatItemAdapter.ViewHolder> {

    private RecyclerViewOnClickListener recyclerViewOnClickListener;
    private final ArrayList<Message> lastMessages;
    private final ArrayList<User> users;
    private final LayoutInflater inflater;

    public ChatItemAdapter(ArrayList<Message> lastMessages, ArrayList<User> users,
           Context context, RecyclerViewOnClickListener recyclerViewOnClickListener) {
        this.lastMessages = lastMessages;
        this.users = users;
        this.inflater = LayoutInflater.from(context);
        this.recyclerViewOnClickListener = recyclerViewOnClickListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView otherImg;
        public TextView name;
        public TextView lastMessage;
        public TextView lastMessageTime;

        public ViewHolder(@NonNull View itemView, RecyclerViewOnClickListener recyclerViewOnClickListener) {
            super(itemView);
            otherImg = itemView.findViewById(R.id.otherImg);
            name = itemView.findViewById(R.id.otherName);
            lastMessage = itemView.findViewById(R.id.lastMsg);
            lastMessageTime = itemView.findViewById(R.id.lastMsgTime);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (recyclerViewOnClickListener != null) {
                        int pos = getAdapterPosition();
                        if(pos != RecyclerView.NO_POSITION){
                            recyclerViewOnClickListener.onItemClick(pos);
                        }
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public ChatItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.layout_chat, parent, false);
        return new ChatItemAdapter.ViewHolder(view, recyclerViewOnClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatItemAdapter.ViewHolder holder, int position) {
        String name = users.get(position).getDisplayName();
        holder.name.setText(name);

        Picasso.get().load(Uri.parse(users.get(position).getProfileImageSrc())).into(holder.otherImg);

        if(lastMessages.get(position)!=null){
            String message = lastMessages.get(position).getMessage();
            String messageDisplay;
            if(message.length()>20) {
                messageDisplay = message.substring(0, 20) + "...";
            } else {messageDisplay = message;}
            holder.lastMessage.setText(messageDisplay);

            @SuppressLint("SimpleDateFormat") DateFormat formatter = new SimpleDateFormat("MMM dd - HH:mm");
            String time = formatter.format(lastMessages.get(position).getSentTime().toDate());
            holder.lastMessageTime.setText(time);
        } else {
            holder.lastMessage.setText("No message yet");
            holder.lastMessageTime.setText("");
        }
    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}
