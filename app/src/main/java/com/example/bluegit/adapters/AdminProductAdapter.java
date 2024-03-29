package com.example.bluegit.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bluegit.FireStoreManager;
import com.example.bluegit.R;
import com.example.bluegit.model.Product;
import com.example.bluegit.model.User;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdminProductAdapter extends RecyclerView.Adapter<AdminProductAdapter.ViewHolder>{
    private ArrayList<Product> products;
    private LayoutInflater inflater;
    private Context context;
    private FirebaseUser currentUser;
    public AdminProductAdapter(ArrayList<Product> products, Context context, FirebaseUser  currentUser) {
        this.products = products;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView productImage;
        public TextView productName;
        public TextView productPrice;
        public TextView productOwner;
        public TextView productQuantityTxt;
        public Button productDeleteBtn;
        public TextView productQuantity;
        public ImageView sellerImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            productImage = itemView.findViewById(R.id.adminProductImageItem);
            productName = itemView.findViewById(R.id.adminProductNameItem);
            productPrice = itemView.findViewById(R.id.adminProductPriceItem);
            productOwner = itemView.findViewById(R.id.adminProductSellerItem);
            productDeleteBtn = itemView.findViewById(R.id.adminProductDeleteItem);
            productQuantityTxt = itemView.findViewById(R.id.textView8);
            productQuantity = itemView.findViewById(R.id.adminProductQuantity);
            sellerImage = itemView.findViewById(R.id.otherImg);


        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.layout_product_admin, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FireStoreManager fireStoreManager = new FireStoreManager(context, currentUser);

        fireStoreManager.getUserById(products.get(position).getSellerId().getId(), new FireStoreManager.GetUserDataCallBack() {
            @Override
            public void onSuccess(User result) {
                holder.productOwner.setText(result.getDisplayName());

                Picasso.get().load(result.getProfileImageSrc()).into(holder.sellerImage);
            }

            @Override
            public void onFailure(Exception e) {

            }
        });


        Picasso.get().load(products.get(position).getImageSource()).into(holder.productImage);

        holder.productName.setText(products.get(position).getProductName());
        String price = products.get(position).getProductPrice() + " đ";
        holder.productPrice.setText(price);
        String quantity = Integer.toString(products.get(position).getQuantity());
        holder.productQuantity.setText(quantity);

        if (products.get(holder.getAdapterPosition()).isDisabled()) {
            holder.productDeleteBtn.setVisibility(View.INVISIBLE);
        }
        holder.productDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.create();
                builder.setTitle("DISABLE CONFIRMATION")
                        .setMessage("Are you sure you want to disable this product? When you accept you cannot undo it.")
                        .setPositiveButton("DISABLE", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                fireStoreManager.disableProduct(products.get(holder.getAdapterPosition()).getProductId(), new FireStoreManager.disableProductCallBack() {
                                    @Override
                                    public void onSuccess() {
                                        v.findViewById(R.id.adminProductDeleteItem).setVisibility(View.INVISIBLE);
                                    }

                                    @Override
                                    public void onFailure(Exception e) {
                                        Log.d("Product Deleted ", e.getLocalizedMessage());
                                    }
                                });
                            }
                        }).setNegativeButton("CANCEL", null).show();
            }
        });
    }




    @Override
    public int getItemCount() {
        return products.size();
    }
}



