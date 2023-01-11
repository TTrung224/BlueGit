package com.example.bluegit.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bluegit.R;
import com.example.bluegit.model.Product;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;



public class CartAdapter  extends RecyclerView.Adapter<CartAdapter.ViewHolder>{
    private ArrayList<Product> products;
    private LayoutInflater inflater;

    public CartAdapter(ArrayList<Product> products, Context context) {
        this.products = products;
        this.inflater = LayoutInflater.from(context);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView productImage;
        public TextView productName;
        public TextView productPrice;
        public Button increaseQuantityBtn;
        public Button decreaseQuantityBtn;
        public EditText productQuantity;
        public Button productDeleteItemBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            productImage = itemView.findViewById(R.id.orderProductImg);
            productName = itemView.findViewById(R.id.itemName);
            productPrice = itemView.findViewById(R.id.itemPrice);
            productDeleteItemBtn = itemView.findViewById(R.id.deleteItemBtn);
            increaseQuantityBtn = itemView.findViewById(R.id.increaseQuantityBtn3);
            decreaseQuantityBtn = itemView.findViewById(R.id.decreaseQuantityBtn3);
            productQuantity = itemView.findViewById(R.id.quantity2);

        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.layout_cart_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Picasso.get().load(products.get(position).getImageSource()).into(holder.productImage);
        holder.productName.setText(products.get(position).getProductName());
        String price = products.get(position).getProductPrice() + " đ";
        holder.productPrice.setText(price);

    }



    @Override
    public int getItemCount() {
        return products.size();
    }
}
