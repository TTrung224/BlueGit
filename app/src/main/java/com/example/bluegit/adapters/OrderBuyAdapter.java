package com.example.bluegit.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bluegit.R;
import com.example.bluegit.model.Order;

import org.threeten.bp.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Locale;

public class OrderBuyAdapter extends RecyclerView.Adapter<OrderBuyAdapter.ViewHolder> {
    private ArrayList<Order> orders;
    private LayoutInflater inflater;

    public OrderBuyAdapter(ArrayList<Order> order, Context context) {
        this.orders = order;
        this.inflater = LayoutInflater.from(context);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView orderId;
        public TextView quantity;
        public TextView orderPrice;
        public TextView createdDate;
        public TextView orderStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            orderId = itemView.findViewById(R.id.orderId);
            quantity = itemView.findViewById(R.id.orderQuantity);
            orderPrice = itemView.findViewById(R.id.orderPrice);
            createdDate = itemView.findViewById(R.id.createdDate);
            orderStatus = itemView.findViewById(R.id.orderStatus);
        }
    }

    @NonNull
    @Override
    public OrderBuyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.layout_order_buy, parent, false);
        return new OrderBuyAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderBuyAdapter.ViewHolder holder, int position) {
        String orderId = "Order "+orders.get(position).getId();
        holder.orderId.setText(orderId);

        String quantity = Integer.toString(orders.get(position).getTotalProductQuantity());
        holder.quantity.setText(quantity);

        int price = Math.round(orders.get(position).getTotalPrice());
        String formattedPrice = String.format(Locale.US, "%,d", price) + "₫";
        holder.orderPrice.setText(formattedPrice);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        holder.createdDate.setText(orders.get(position).getCreatedDate().format(formatter));

        holder.orderStatus.setText(orders.get(position).getStatus());
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

}