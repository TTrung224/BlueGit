package com.example.bluegit;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bluegit.adapters.OrderSellAdapter;
import com.example.bluegit.model.Order;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class OrdersSellActivity extends AppCompatActivity {

    FireStoreManager fireStoreManager;
    ProgressBar progressBar;
    TextView noOrderMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_sell);
        progressBar = findViewById(R.id.orderProgress);
        noOrderMsg = findViewById(R.id.no_order_msg);

        fireStoreManager = new FireStoreManager(this, FirebaseAuth.getInstance().getCurrentUser());
    }

    @Override
    protected void onStart() {
        super.onStart();
        progressBar.setVisibility(View.VISIBLE);
        fireStoreManager.getSellOrders(new FireStoreManager.GetOrdersCallBack() {
            @Override
            public void onSuccess(ArrayList<Order> result) {
                progressBar.setVisibility(View.GONE);

                RecyclerView recyclerView = findViewById(R.id.buyerOrders);
                OrderSellAdapter adapter = new OrderSellAdapter(result, OrdersSellActivity.this);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(OrdersSellActivity.this));
                if(adapter.getItemCount() < 1){
                    noOrderMsg.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Exception e) {
                progressBar.setVisibility(View.GONE);
                noOrderMsg.setVisibility(View.VISIBLE);
                Toast.makeText(OrdersSellActivity.this, "Error loading orders", Toast.LENGTH_SHORT).show();
                Log.d("Get Buy Order Exception", e.getMessage());
            }
        });

    }

    public void goBack(View view) {
        setResult(RESULT_OK);
        finish();
    }
}