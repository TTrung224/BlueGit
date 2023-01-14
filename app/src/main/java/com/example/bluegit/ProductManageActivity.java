package com.example.bluegit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.bluegit.adapters.ProductManageAdapter;
import com.example.bluegit.adapters.RecyclerViewOnClickListener;
import com.example.bluegit.model.Product;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Objects;

public class ProductManageActivity extends AppCompatActivity {
    private String uId;
    Intent navIntent;

    FireStoreManager fireStoreManager;
    TextView emptyMessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_manage);
        fireStoreManager = new FireStoreManager(this, FirebaseAuth.getInstance().getCurrentUser());
        emptyMessage = findViewById(R.id.empty_message);

        navIntent = new Intent(this, MainActivity.class);
    }

    @Override
    protected void onStart() {
        super.onStart();
        RecyclerView productDisplay = findViewById(R.id.product_manager_recyclerview);
        fireStoreManager.getUserProducts(new FireStoreManager.GetProductsCallBack() {
            @Override
            public void onSuccess(ArrayList<Product> result) {
                if(result.size() > 0){
                    emptyMessage.setVisibility(View.GONE);
                    ProductManageAdapter adapter = new ProductManageAdapter(result, ProductManageActivity.this, new RecyclerViewOnClickListener(){
                        @Override
                        public void onItemClick(int position) {
                            Product product = result.get(position);
                            Log.d("TESTING", product.getProductId());
                            Intent intent = new Intent(ProductManageActivity.this, EditProductActivity.class);
                            intent.putExtra("productId", product.getProductId());
                            startActivity(intent);
                    }});

                    productDisplay.setAdapter(adapter);
                    productDisplay.setLayoutManager(new LinearLayoutManager(ProductManageActivity.this));
                }
            }

            @Override
            public void onFailure(Exception e) {
                Log.d("GetUserProductException", e.getLocalizedMessage());
            }
        });
    }

    public void goBack(View view) {
        setResult(RESULT_OK);
        finish();
    }

    public void refresh(View view) {
        this.recreate();
    }

    public void toOrders(View view) {
        navIntent.putExtra("navTo", MainActivity.NAV_TO_ORDERS);
        setResult(RESULT_OK, navIntent);
        finish();
        overridePendingTransition(0, 0);
    }

    public void toHome(View view) {
        setResult(RESULT_OK);
        finish();
        overridePendingTransition(0, 0);
    }

    public void toCart(View view) {
        navIntent.putExtra("navTo", MainActivity.NAV_TO_CART);
        setResult(RESULT_OK, navIntent);
        finish();
        overridePendingTransition(0, 0);
    }

    public void toAccount(View view) {
        navIntent.putExtra("navTo", MainActivity.NAV_TO_ACCOUNT);
        setResult(RESULT_OK, navIntent);
        finish();
        overridePendingTransition(0, 0);
    }

    public void addProduct(View view) {
        Intent intent = new Intent(ProductManageActivity.this, AddProductActivity.class);
        startActivity(intent);
    }

    public void viewSellOrders(View view) {
        Intent intent = new Intent(ProductManageActivity.this, OrdersSellActivity.class);
        startActivity(intent);
    }
}