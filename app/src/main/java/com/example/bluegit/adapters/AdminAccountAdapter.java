package com.example.bluegit.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bluegit.R;
import com.example.bluegit.model.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;



public class AdminAccountAdapter  extends RecyclerView.Adapter<AdminAccountAdapter.ViewHolder>{
    private ArrayList<User> users;
    private LayoutInflater inflater;

    public AdminAccountAdapter(ArrayList<User> users, Context context) {
        this.users = users;
        this.inflater = LayoutInflater.from(context);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView accountImage;
        public TextView accountName;
        public TextView accountAddress;
        public TextView accountPhone;
        public Button accountDeleteBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            accountImage = itemView.findViewById(R.id.adminAccountImage);
            accountName = itemView.findViewById(R.id.adminAccountName);
            accountAddress = itemView.findViewById(R.id.adminAccountAddress);
            accountPhone = itemView.findViewById(R.id.adminAccountPhone);
            accountDeleteBtn = itemView.findViewById(R.id.adminAccountDelete);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.layout_admin_account, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Picasso.get().load(users.get(position).getProfileImageSrc()).into(holder.accountImage);
        holder.accountName.setText(users.get(position).getDisplayName());
        holder.accountPhone.setText(users.get(position).getPhoneNumber());

    }



    @Override
    public int getItemCount() {
        return users.size();
    }
}