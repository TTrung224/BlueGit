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

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ProductManageAdapter extends RecyclerView.Adapter<ProductManageAdapter.ViewHolder>{

    private ArrayList<Product> products;
    private LayoutInflater inflater;
    private static RecyclerViewOnClickListener recyclerViewOnClickListener;

    public ProductManageAdapter(ArrayList<Product> products, Context ctx,RecyclerViewOnClickListener recyclerViewOnClickListener) {
        this.products = products;
        this.inflater = LayoutInflater.from(ctx);
        this.recyclerViewOnClickListener = recyclerViewOnClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.layout_product_manage, parent, false);
        return new ProductManageAdapter.ViewHolder(view,recyclerViewOnClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Picasso.get().load(products.get(position).getImageSource()).into(holder.productImage);
        holder.productName.setText(products.get(position).getProductName());
        NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        String price = nf.format(products.get(position).getProductPrice());
        holder.productPrice.setText("PRICE: " + price);
        holder.productCount.setText("QUANTITY: " + String.valueOf(products.get(position).getQuantity()));
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView productImage;
        public TextView productName;
        public TextView productPrice;
        public TextView productCount;

        public ViewHolder(@NonNull View itemView, RecyclerViewOnClickListener recyclerViewOnClickListener) {
            super(itemView);

            productImage = itemView.findViewById(R.id.display_product_image);
            productName = itemView.findViewById(R.id.display_product_name);
            productPrice = itemView.findViewById(R.id.display_product_price);
            productCount = itemView.findViewById(R.id.display_product_count);

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

}
