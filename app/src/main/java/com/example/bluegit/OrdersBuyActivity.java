package com.example.bluegit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bluegit.adapters.OrderBuyAdapter;
import com.example.bluegit.model.Order;
import com.example.bluegit.model.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.jakewharton.threetenabp.AndroidThreeTen;

import java.util.ArrayList;
import java.util.Objects;

public class OrdersBuyActivity extends AppCompatActivity {

    FireStoreManager fireStoreManager;
    ProgressBar progressBar;
    TextView noOrderMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidThreeTen.init(this);
        setContentView(R.layout.activity_orders_buy);;
        progressBar = findViewById(R.id.orderProgress);
        noOrderMsg = findViewById(R.id.no_order_msg);

        fireStoreManager = new FireStoreManager(this, FirebaseAuth.getInstance().getCurrentUser());
    }

    @Override
    protected void onStart() {
        super.onStart();
        progressBar.setVisibility(View.VISIBLE);
        fireStoreManager.getBuyOrders(new FireStoreManager.GetOrdersCallBack() {
            @Override
            public void onSuccess(ArrayList<Order> result) {
                progressBar.setVisibility(View.GONE);

                RecyclerView recyclerView = findViewById(R.id.buyerOrders);
                OrderBuyAdapter adapter = new OrderBuyAdapter(result, OrdersBuyActivity.this);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(OrdersBuyActivity.this));
                if(adapter.getItemCount() < 1){
                    noOrderMsg.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Exception e) {
                progressBar.setVisibility(View.GONE);
                noOrderMsg.setVisibility(View.VISIBLE);
                Toast.makeText(OrdersBuyActivity.this, "Error loading orders", Toast.LENGTH_SHORT).show();
                Log.d("Get Buy Order Exception", e.getMessage());
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

    public void refresh(View view) {
        this.recreate();
    }

    public void toHome(View view) {
        setResult(RESULT_OK);
        finish();
        overridePendingTransition(0, 0);
    }

    public void toCart(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("navTo", MainActivity.NAV_TO_CART);
        setResult(RESULT_OK, intent);
        finish();
        overridePendingTransition(0, 0);
    }

    public void toAccount(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("navTo", MainActivity.NAV_TO_ACCOUNT);
        setResult(RESULT_OK, intent);
        finish();
        overridePendingTransition(0, 0);
    }
}