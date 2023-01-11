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

public class ProductDisplayAdapter extends RecyclerView.Adapter<ProductDisplayAdapter.ViewHolder>{

    private RecyclerViewOnClickListener recyclerViewOnClickListener;
    private ArrayList<Product> products;
    private LayoutInflater inflater;

    public ProductDisplayAdapter(ArrayList<Product> products, Context context, RecyclerViewOnClickListener recyclerViewOnClickListener) {
        this.products = products;
        this.inflater = LayoutInflater.from(context);
        this.recyclerViewOnClickListener = recyclerViewOnClickListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView productImage;
        public TextView productName;
        public TextView productPrice;

        public ViewHolder(@NonNull View itemView, RecyclerViewOnClickListener recyclerViewOnClickListener) {
            super(itemView);

            productImage = itemView.findViewById(R.id.display_product_image);
            productName = itemView.findViewById(R.id.display_product_name);
            productPrice = itemView.findViewById(R.id.display_product_price);

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
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.layout_item_display, parent, false);
        return new ViewHolder(view, recyclerViewOnClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Picasso.get().load(products.get(position).getImageSource()).into(holder.productImage);
        holder.productName.setText(products.get(position).getProductName());
        NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        String price = nf.format(products.get(position).getProductPrice());
        holder.productPrice.setText(price);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

}
