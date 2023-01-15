package com.example.bluegit.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
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

public class OrderGeneralAdapter extends RecyclerView.Adapter<OrderGeneralAdapter.ViewHolder> {
    private final ArrayList<Order> orders;
    private final LayoutInflater inflater;

    public OrderGeneralAdapter(ArrayList<Order> orders, Context context) {
        this.orders = orders;
        this.inflater = LayoutInflater.from(context);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView orderId;
        public TextView quantity;
        public TextView orderPrice;
        public TextView orderOldPrice;
        public TextView createdDate;
        public TextView orderStatus;
        public Button detailBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            orderId = itemView.findViewById(R.id.orderId);
            quantity = itemView.findViewById(R.id.orderQuantity);
            orderPrice = itemView.findViewById(R.id.orderPrice);
            orderOldPrice = itemView.findViewById(R.id.orderOldPrice);
            createdDate = itemView.findViewById(R.id.createdDate);
            orderStatus = itemView.findViewById(R.id.orderStatus);
            detailBtn = itemView.findViewById(R.id.detailBtn);
        }
    }

    @NonNull
    @Override
    public OrderGeneralAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.layout_order_general, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderGeneralAdapter.ViewHolder holder, int position) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy", new Locale("vi, VN"));
        holder.orderId.setText(orders.get(position).getId().substring(0, 7).toUpperCase());

        String quantity = Integer.toString(orders.get(position).perProductQuantity());
        holder.quantity.setText(quantity);

        NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        if(orders.get(holder.getAdapterPosition()).getVoucher() != null){
            holder.orderOldPrice.setText(nf.format(orders.get(position).getTotalPrice()));
            holder.orderOldPrice.setPaintFlags(holder.orderOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }else{
            holder.orderOldPrice.setVisibility(View.GONE);
        }
        String formattedPrice = nf.format(orders.get(position).discountedTotal());
        holder.orderPrice.setText(formattedPrice);

        holder.createdDate.setText(dateFormatter.format(orders.get(position).getCreatedDate().toDate()));

        String status = orders.get(position).getStatus();
        String statusText = status.substring(0, 1).toUpperCase() + status.substring(1);
        holder.orderStatus.setText(statusText);

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