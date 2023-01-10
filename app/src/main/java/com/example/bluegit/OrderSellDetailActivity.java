package com.example.bluegit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.bluegit.adapters.OrderProductAdapter;
import com.example.bluegit.model.Product;

import java.util.ArrayList;

public class OrderSellDetailActivity extends AppCompatActivity {
    ArrayList<Product> productList = new ArrayList<>();
    ArrayList<Integer> quantityList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_sell_detail);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // TODO: retrieve and  process data here

        RecyclerView recyclerView = findViewById(R.id.orderProductSellList);
        OrderProductAdapter adapter = new OrderProductAdapter(productList, quantityList, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}