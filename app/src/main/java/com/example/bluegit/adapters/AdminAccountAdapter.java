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

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;


public class AdminAccountAdapter  extends RecyclerView.Adapter<AdminAccountAdapter.ViewHolder>{
    private ArrayList<User> users;
    private LayoutInflater inflater;
    NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

    public AdminAccountAdapter(ArrayList<User> users, Context context) {
        this.users = users;
        this.inflater = LayoutInflater.from(context);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView accountImage;
        public TextView accountName;
        public TextView accountEmail;
        public TextView accountPhone;
        public TextView accountBalance;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            accountImage = itemView.findViewById(R.id.adminAccountImage);
            accountName = itemView.findViewById(R.id.name);
            accountEmail = itemView.findViewById(R.id.email);
            accountPhone = itemView.findViewById(R.id.phone);
            accountBalance = itemView.findViewById(R.id.balance);
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
        holder.accountEmail.setText(users.get(position).getEmail());

        String phoneDisplayStr = "phone: " + users.get(position).getPhoneNumber();
        holder.accountPhone.setText(phoneDisplayStr);

        String balanceStr = "balance: " + nf.format(users.get(position).getBalance());
        holder.accountBalance.setText(balanceStr);
    }



    @Override
    public int getItemCount() {
        return users.size();
    }
}