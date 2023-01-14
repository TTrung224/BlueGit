package com.example.bluegit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bluegit.model.Product;
import com.example.bluegit.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ProductActivity extends AppCompatActivity {

    ImageView iImage;
    TextView tName;
    TextView tPrice;
    TextView tDescription;
    TextView tSpec;
    TextView productQuantityEdit;
    TextView tStock;
    TextView tSellerName;
    ImageView iSellerImg;

    FirebaseFirestore db;
    DocumentReference docRef;
    String productId;
    String sellerId;


    Map<String, Integer> product = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        FireStoreManager fireStoreManager = new FireStoreManager(this, FirebaseAuth.getInstance().getCurrentUser());

        iImage = findViewById(R.id.productImg);
        tName = findViewById(R.id.productName);
        tPrice = findViewById(R.id.productPrice);
        tDescription = findViewById(R.id.productDescContent);
        tSpec = findViewById(R.id.productSpec);
        productQuantityEdit = findViewById(R.id.quantity);
        tStock = findViewById(R.id.productQuantity);
        tSellerName = findViewById(R.id.sellerName);
        iSellerImg = findViewById(R.id.otherImg);

        Intent intent = getIntent();
        productId = intent.getStringExtra("productId");
        fireStoreManager.getProductById(productId, new FireStoreManager.GetProductCallBack() {
            @Override
            public void onSuccess(Product product) {
                Picasso.get().load(Uri.parse(product.getImageSource())).into(iImage);
                tName.setText(product.getProductName());
                NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
                String price = nf.format(product.getProductPrice());
                tPrice.setText(price);
                tDescription.setText(product.getDescription());
                tSpec.setText(product.getSpecification());
                tStock.setText(String.valueOf(product.getQuantity()));
                sellerId = product.getSellerId().getId();
                fireStoreManager.getUserById(sellerId, new FireStoreManager.GetUserDataCallBack() {
                    @Override
                    public void onSuccess(User result) {
                        tSellerName.setText(result.getDisplayName());
                        Picasso.get().load(Uri.parse(result.getProfileImageSrc())).into(iSellerImg);
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(ProductActivity.this, "Fail to load product, please try again", Toast.LENGTH_SHORT).show();
                        Log.d("GetUserByIdProductActivity", e.getLocalizedMessage());
                        finish();
                    }
                });
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(ProductActivity.this, "Fail to load product, please try again", Toast.LENGTH_SHORT).show();
                Log.d("GetProductByIdProductActivity", e.getLocalizedMessage());
                finish();
            }
        });

    }

    public void minusQuantity(View view) {
        EditText quantityTextEdit = findViewById(R.id.quantity);
        int quantity = Integer.parseInt(quantityTextEdit.getText().toString());
        if(quantity > 1){
            quantity--;
            quantityTextEdit.setText(String.valueOf(quantity));
        }
    }

    public void bonusQuantity(View view) {
        EditText quantityTextEdit = findViewById(R.id.quantity);
        int maxQuant = Integer.parseInt(tStock.getText().toString());
        int quantity = Integer.parseInt(quantityTextEdit.getText().toString());
        if (quantity < maxQuant){
            quantity ++;
            quantityTextEdit.setText(String.valueOf(quantity));
        }
    }

    public void onBackButtonClick(View view) {
        finish();
    }

    public void addCartHandler(View view){
        int quantity = Integer.parseInt(productQuantityEdit.getText().toString());

        EditText quantityTextEdit = findViewById(R.id.quantity);
        int maxQuant = Integer.parseInt(tStock.getText().toString());
        if(quantity > maxQuant){
            Toast.makeText(this, "Don't have enough item in stock", Toast.LENGTH_SHORT).show();
            productQuantityEdit.setText(String.valueOf(maxQuant));
            return;
        } else if(quantity < 1){
            Toast.makeText(this, "Please enter a valid quantity", Toast.LENGTH_SHORT).show();
            productQuantityEdit.setText("1");
            return;
        }
        productQuantityEdit.setEnabled(false);

        product.put(productId, quantity);
        FireStoreManager fireStoreManager = new FireStoreManager(this, FirebaseAuth.getInstance().getCurrentUser());
        fireStoreManager.addToCart(product, new FireStoreManager.CartCallBack() {
            @Override
            public void onSuccess() {
                Toast.makeText(ProductActivity.this, "Item added to cart", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(ProductActivity.this, "Unable to add item", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    public void chatWithSeller(View view) {
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("otherUserId", sellerId);
        startActivity(intent);
    }
}