package com.example.bluegit.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bluegit.FireStoreManager;
import com.example.bluegit.R;
import com.example.bluegit.model.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class CartAdapter  extends RecyclerView.Adapter<CartAdapter.ViewHolder>{
    public TextView showTotal;
    public int totalPrice;
    public final ArrayList<Product> products;
    public final ArrayList<Integer> productCounts;
    private final LayoutInflater inflater;

    public CartAdapter(ArrayList<Product> products, ArrayList<Integer> productCounts, Context context) {
        this.products = products;
        this.inflater = LayoutInflater.from(context);
        this.productCounts = productCounts;
    }

    public CartAdapter(ArrayList<Product> products, ArrayList<Integer> productCounts, Context context, TextView showTotal) {
        this.products = products;
        this.inflater = LayoutInflater.from(context);
        this.productCounts = productCounts;
        this.showTotal = showTotal;

    }

    public void showTotalPrice(){
        int total = 0;
        for(int i = 0; i < getItemCount(); i++){
            total += products.get(i).getProductPrice() * productCounts.get(i);
        }
        this.totalPrice = total;
        if(showTotal != null){
            NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            showTotal.setText(nf.format(total));
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView productImage;
        public TextView productName;
        public TextView productPrice;
        public ImageButton increaseQuantityBtn;
        public ImageButton decreaseQuantityBtn;
        public EditText productQuantity;
        public ImageButton productDeleteItemBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            productImage = itemView.findViewById(R.id.profileImg);
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
        FireStoreManager fireStoreManager = new FireStoreManager(this.inflater.getContext(), FirebaseAuth.getInstance().getCurrentUser());

        Picasso.get().load(products.get(position).getImageSource()).into(holder.productImage);
        holder.productName.setText(products.get(position).getProductName());
        NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        String price = nf.format(products.get(position).getProductPrice());
        holder.productPrice.setText(price);
        holder.productQuantity.setText(String.valueOf(productCounts.get(position)));
        showTotalPrice();

        holder.productDeleteItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fireStoreManager.deleteItemFromCart(products.get(holder.getAdapterPosition()).getProductId(), new FireStoreManager.CartCallBack() {
                    @Override
                    public void onSuccess() {
                        products.remove(holder.getAdapterPosition());
                        productCounts.remove(holder.getAdapterPosition());
                        notifyItemRemoved(holder.getAdapterPosition());
                        notifyItemRangeChanged(holder.getAdapterPosition(), products.size());
                        showTotalPrice();
                    }
                    @Override
                    public void onFailure(Exception e) {
                        Log.d("DeleteCartItemEx", e.getLocalizedMessage());
                    }
                });

            }
        });

        holder.decreaseQuantityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quant = productCounts.get(holder.getAdapterPosition());

                if(quant > 1) {
                    quant--;
                    Map<String, Integer> newValue = new HashMap<>();
                    String id = products.get(holder.getAdapterPosition()).getProductId();
                    newValue.put(id, quant);
                    fireStoreManager.addToCart(newValue, new FireStoreManager.CartCallBack() {
                        @Override
                        public void onSuccess() {
                            Integer temp = productCounts.get(holder.getAdapterPosition());
                            temp--;
                            productCounts.set(holder.getAdapterPosition(), temp);
                            notifyItemChanged(holder.getAdapterPosition(), productCounts);
                            showTotalPrice();
                        }

                        @Override
                        public void onFailure(Exception e) {Log.d("DecreaseCartQuantEx", e.getLocalizedMessage());}
                    });

                }
            }
        });

        holder.increaseQuantityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int maxCount = products.get(holder.getAdapterPosition()).getQuantity();
                int quant = productCounts.get(holder.getAdapterPosition());

                if(quant < maxCount){
                    quant++;
                    Map<String, Integer> newValue = new HashMap<>();
                    String id = products.get(holder.getAdapterPosition()).getProductId();
                    newValue.put(id, quant);
                    fireStoreManager.addToCart(newValue, new FireStoreManager.CartCallBack() {
                        @Override
                        public void onSuccess() {
                            Integer temp = productCounts.get(holder.getAdapterPosition());
                            temp++;
                            productCounts.set(holder.getAdapterPosition(), temp);
                            notifyItemChanged(holder.getAdapterPosition(), productCounts);
                            showTotalPrice();
                        }

                        @Override
                        public void onFailure(Exception e) {Log.d("IncreaseCartQuantEx", e.getLocalizedMessage());}
                    });
                }
            }
        });
    }




    @Override
    public int getItemCount() {
        return products.size();
    }
}
