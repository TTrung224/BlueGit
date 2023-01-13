package com.example.bluegit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bluegit.adapters.AdminProductAdapter;
import com.example.bluegit.adapters.CartAdapter;
import com.example.bluegit.model.Product;
import com.google.firebase.auth.FirebaseAuth;

import org.checkerframework.checker.units.qual.A;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class CartActivity extends AppCompatActivity {

    FireStoreManager fireStoreManager;
    NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
    TextView emptyMessage;
    TextView orderTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        fireStoreManager = new FireStoreManager(this, FirebaseAuth.getInstance().getCurrentUser());
        emptyMessage = findViewById(R.id.empty_message);
        orderTotal = findViewById(R.id.order_total);

    }
    @Override
    protected void onStart() {
        super.onStart();

        fireStoreManager.getCart(new FireStoreManager.GetCartCallBack() {
            @Override
            public void onSuccess(Map<String, Object> cartResult) {
                fireStoreManager.getProductsFromCart(cartResult, new FireStoreManager.GetProductsFromCartCallBack() {
                    @Override
                    public void onSuccess(Map<Product, Integer> result) {
                        ArrayList<Product> products = new ArrayList<>();
                        ArrayList<Integer> amounts = new ArrayList<>();

                        for(Map.Entry<Product, Integer> entry : result.entrySet()){
                            products.add(entry.getKey());
                            amounts.add(entry.getValue());
                        }

                        if(products.size() > 0){
                            emptyMessage.setVisibility(View.GONE);
                        }

                        RecyclerView recyclerView = findViewById(R.id.itemList);
                        CartAdapter adapter = new CartAdapter(products, amounts, CartActivity.this, orderTotal);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(CartActivity.this));
                    }
                    @Override
                    public void onFailure(Exception e) {
                        Log.d("DisplayCartEx", e.getLocalizedMessage());
                    }
                });
            }
            @Override
            public void onFailure(Exception e) {
                Log.d("DisplayCartEx", e.getLocalizedMessage());
                Toast.makeText(CartActivity.this, "Unable to load cart", Toast.LENGTH_SHORT).show();
            }
        });




    }

    public void goBack(View view) {
        setResult(RESULT_OK);
        finish();
    }

    public void toBusiness(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("navTo", MainActivity.NAV_TO_BUSINESS);
        setResult(RESULT_OK, intent);
        finish();
        overridePendingTransition(0, 0);
    }

    public void toOrders(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("navTo", MainActivity.NAV_TO_ORDERS);
        setResult(RESULT_OK, intent);
        finish();
        overridePendingTransition(0, 0);
    }

    public void toHome(View view) {
        setResult(RESULT_OK);
        finish();
        overridePendingTransition(0, 0);
    }

    public void refresh(View view) {
        this.recreate();
    }

    public void toAccount(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("navTo", MainActivity.NAV_TO_ACCOUNT);
        setResult(RESULT_OK, intent);
        finish();
        overridePendingTransition(0, 0);
    }
}