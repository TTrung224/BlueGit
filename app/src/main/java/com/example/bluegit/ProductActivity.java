package com.example.bluegit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bluegit.model.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class ProductActivity extends AppCompatActivity {

    ImageView iImage;
    TextView tName;
    TextView tPrice;
    TextView tDescription;
    TextView tSpec;
    TextView productQuantity;
    FirebaseFirestore db;
    DocumentReference docRef;
    String productId;
    String userId;

    Map<String, String> products = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //TODO: WHEN ADD TO CART, ADD PRODUCT ID INTO IT, WHEN CREATE ORDER, PASS IN DOCUMENTREFERENCE, MAYBE

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        FireStoreManager fireStoreManager = new FireStoreManager(this, FirebaseAuth.getInstance().getCurrentUser());

        iImage = findViewById(R.id.productImg);
        tName = findViewById(R.id.productName);
        tPrice = findViewById(R.id.productPrice);
        tDescription = findViewById(R.id.productDescContent);
        tSpec = findViewById(R.id.productSpec);
        productQuantity = findViewById(R.id.quantity);
        userId = FirebaseAuth.getInstance().getUid();

        Intent intent = getIntent();
        String productId = intent.getStringExtra("productId");
        fireStoreManager.getProductById(productId, new GetProductCallBack() {
            @Override
            public void onSuccess(Product product) {
                Picasso.get().load(Uri.parse(product.getImageSource())).into(iImage);
                tName.setText(product.getProductName());
                NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
                String price = nf.format(product.getProductPrice());
                tPrice.setText(price);
                tDescription.setText(product.getDescription());
                tSpec.setText(product.getSpecification());
            }

            @Override
            public void onFailure(Exception e) {
                Log.d("GetProductByIdProductActivity", e.getLocalizedMessage());
                finish();
            }
        });

    }

    public void minusQuantity(View view) {
        EditText quantityTextEdit = (EditText) findViewById(R.id.quantity);
        int quantity = Integer.parseInt(quantityTextEdit.getText().toString());
        if(quantity > 1){
            quantity--;
            quantityTextEdit.setText(Integer.toString(quantity));
        }
    }

    public void bonusQuantity(View view) {
        EditText quantityTextEdit = (EditText) findViewById(R.id.quantity);
        int quantity = Integer.parseInt(quantityTextEdit.getText().toString()) + 1;
        quantityTextEdit.setText(Integer.toString(quantity));
    }

    public void onBackButtonClick(View view) {
        finish();
    }

    public void addCartHandler(View view){
        String quantity = productQuantity.getText().toString();
        products.put(productId,quantity);
        docRef = FirebaseFirestore.getInstance().
                collection("users").document(userId);
        docRef.set(products);


    }



}