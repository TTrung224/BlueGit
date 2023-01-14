package com.example.bluegit.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bluegit.ChatActivity;
import com.example.bluegit.FireStoreManager;
import com.example.bluegit.OrderDetailActivity;
import com.example.bluegit.R;
import com.example.bluegit.model.Order;
import com.google.firebase.auth.FirebaseAuth;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class OrderBuyAdapter extends RecyclerView.Adapter<OrderBuyAdapter.ViewHolder> {
    private final ArrayList<Order> orders;
    private final LayoutInflater inflater;
    private final FireStoreManager fireStoreManager;

    public OrderBuyAdapter(ArrayList<Order> orders, Context context) {
        Collections.sort(orders);
        this.orders = orders;
        this.inflater = LayoutInflater.from(context);
        fireStoreManager = new FireStoreManager(context, FirebaseAuth.getInstance().getCurrentUser());
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView orderId;
        public TextView quantity;
        public TextView orderPrice;
        public TextView createdDate;
        public TextView orderStatus;
        public Button detailBtn;
        public Button receivedBtn;
        public ImageButton chatBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            orderId = itemView.findViewById(R.id.orderId);
            quantity = itemView.findViewById(R.id.orderQuantity);
            orderPrice = itemView.findViewById(R.id.orderPrice);
            createdDate = itemView.findViewById(R.id.createdDate);
            orderStatus = itemView.findViewById(R.id.orderStatus);
            detailBtn = itemView.findViewById(R.id.detailBtn);
            receivedBtn = itemView.findViewById(R.id.receivedBtn);
            chatBtn = itemView.findViewById(R.id.chatBtn);
        }
    }

    @NonNull
    @Override
    public OrderBuyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.layout_order_buy, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderBuyAdapter.ViewHolder holder, int position) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy", new Locale("vi, VN"));
        holder.orderId.setText(orders.get(position).getId().substring(0, 7).toUpperCase());

        String quantity = Integer.toString(orders.get(position).getProductQuantity());
        holder.quantity.setText(quantity);

        String formattedPrice = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"))
                .format(orders.get(position).getTotalPrice());
        holder.orderPrice.setText(formattedPrice);

        holder.createdDate.setText(dateFormatter.format(orders.get(position).getCreatedDate().toDate()));

        String status = orders.get(position).getStatus();
        String statusText = status.substring(0, 1).toUpperCase() + status.substring(1);
        holder.orderStatus.setText(statusText);

        if(orders.get(position).getStatus().equals("pending")){
            holder.receivedBtn.setAlpha(0.5f);
            holder.receivedBtn.setEnabled(false);
            holder.orderStatus.setTextColor(Color.rgb(245,184,114));
        }else if(orders.get(position).getStatus().equals("completed")){
            holder.receivedBtn.setVisibility(View.GONE);
            holder.receivedBtn.setEnabled(false);
            holder.orderStatus.setTextColor(Color.GRAY);
        }

        holder.chatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ChatActivity.class);
                intent.putExtra("otherUserId", orders.get(holder.getAdapterPosition()).getSellerId().getId());
                v.getContext().startActivity(intent);
            }
        });

        holder.receivedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fireStoreManager.updateOrderStatus(orders.get(holder.getAdapterPosition()).getId(), "completed", new FireStoreManager.AddOrdersCallBack() {
                    @Override
                    public void onSuccess() {
                        orders.get(holder.getAdapterPosition()).setStatus("completed");
                        notifyItemChanged(holder.getAdapterPosition());
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Log.d("Update Order Status Exception", e.getMessage());
                    }
                });
            }
        });

        holder.detailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), OrderDetailActivity.class);
                intent.putExtra("orderId", orders.get(holder.getAdapterPosition()).getId());
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

}