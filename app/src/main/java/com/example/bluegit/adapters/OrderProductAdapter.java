package com.example.bluegit.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bluegit.R;
import com.example.bluegit.model.Order;
import com.example.bluegit.model.Product;
import com.squareup.picasso.Picasso;

import org.threeten.bp.format.DateTimeFormatter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Locale;

public class OrderProductAdapter extends RecyclerView.Adapter<OrderProductAdapter.ViewHolder>{
    private ArrayList<Product> productList;
    private ArrayList<Integer> amountList;
    private LayoutInflater inflater;

    public OrderProductAdapter(ArrayList<Product> productList, ArrayList<Integer> amountList, Context context) {
        this.productList = productList;
        this.amountList = amountList;
        this.inflater = LayoutInflater.from(context);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView img;
        public TextView price;
        public TextView quantity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.orderProductImg);
            price = itemView.findViewById(R.id.itemListProductPrice);
            quantity = itemView.findViewById(R.id.itemListProductQuantity);
        }
    }

    @NonNull
    @Override
    public OrderProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.layout_order_product, parent, false);
        return new OrderProductAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderProductAdapter.ViewHolder holder, int position) {
        Picasso.get().load(productList.get(position).getImageSource()).into(holder.img);

        int price = Math.round(productList.get(position).getProductPrice()
                        * amountList.get(position));
        String formattedPrice = String.format(Locale.US, "%,d", price) + "₫";
        holder.price.setText(formattedPrice);

        String quantityStr = Integer.toString(amountList.get(position));
        holder.quantity.setText(quantityStr);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
}
