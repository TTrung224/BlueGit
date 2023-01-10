package com.example.bluegit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.bluegit.adapters.OrderBuyAdapter;
import com.example.bluegit.model.Order;
import com.example.bluegit.model.Product;
import com.jakewharton.threetenabp.AndroidThreeTen;

import java.util.ArrayList;

public class OrdersBuyActivity extends AppCompatActivity {
    ArrayList<Order> orders = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidThreeTen.init(this);
        setContentView(R.layout.activity_orders_buy);

        Product p = new Product("Pink dress with Strawberry on it", 100000,
                "https://cdn.shopify.com/s/files/1/0011/9783/4252/products/20_375a8763-f5d7-4184-a352-4523ef713733.jpg?v=1576267132");
        ArrayList<Product> productList = new ArrayList<>();
        productList.add(p);
        ArrayList<Integer> amountList = new ArrayList<>();
        amountList.add(2);
        
        ArrayList<Integer> amountList1 = new ArrayList<>();
        amountList1.add(5);

        orders.add(new Order("12345", productList, amountList,
                null, "1", "1"));
        orders.add(new Order("12346", productList, amountList1,
                null, "1", "1"));
    }

    @Override
    protected void onStart() {
        super.onStart();
        RecyclerView recyclerView = findViewById(R.id.buyerOrders);
        OrderBuyAdapter adapter = new OrderBuyAdapter(orders, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}