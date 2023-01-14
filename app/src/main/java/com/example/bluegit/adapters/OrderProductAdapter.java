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
import com.example.bluegit.model.Product;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;

public class OrderProductAdapter extends RecyclerView.Adapter<OrderProductAdapter.ViewHolder>{
    private final ArrayList<Product> productList;
    private final ArrayList<Integer> amountList;
    private final LayoutInflater inflater;

    public OrderProductAdapter(ArrayList<Product> productList, ArrayList<Integer> amountList, Context context) {
        this.productList = productList;
        this.amountList = amountList;
        this.inflater = LayoutInflater.from(context);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView img;
        public TextView price;
        public TextView quantity;
        public TextView productName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.product_name);
            img = itemView.findViewById(R.id.otherImg);
            price = itemView.findViewById(R.id.lastMsg);
            quantity = itemView.findViewById(R.id.itemListProductQuantity);
        }
    }

    @NonNull
    @Override
    public OrderProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.layout_order_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderProductAdapter.ViewHolder holder, int position) {
        Picasso.get().load(productList.get(position).getImageSource()).into(holder.img);

        holder.productName.setText(productList.get(position).getProductName());
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
