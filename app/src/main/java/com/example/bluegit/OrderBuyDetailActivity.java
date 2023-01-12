package com.example.bluegit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Paint;
import android.os.Bundle;
import android.widget.TextView;

import com.example.bluegit.adapters.OrderProductAdapter;
import com.example.bluegit.model.Product;

import java.util.ArrayList;

public class OrderBuyDetailActivity extends AppCompatActivity {
    ArrayList<Product> productList = new ArrayList<>();
    ArrayList<Integer> quantityList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        TextView oldPrice = findViewById(R.id.totalPriceBeforeDiscount);
        oldPrice.setPaintFlags(oldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // TODO: retrieve and  process data here

        RecyclerView recyclerView = findViewById(R.id.orderProductList);
        OrderProductAdapter adapter = new OrderProductAdapter(productList, quantityList, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}