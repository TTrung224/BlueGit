package com.example.bluegit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.bluegit.adapters.OrderBuyAdapter;
import com.example.bluegit.model.Order;
import com.example.bluegit.model.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.jakewharton.threetenabp.AndroidThreeTen;

import java.util.ArrayList;
import java.util.Objects;

public class OrdersBuyActivity extends AppCompatActivity {
    private String uId;
    private ArrayList<Order> orders = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidThreeTen.init(this);
        setContentView(R.layout.activity_orders_buy);

        uId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

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

        // TODO: retrieve and process data here

        RecyclerView recyclerView = findViewById(R.id.buyerOrders);
        OrderBuyAdapter adapter = new OrderBuyAdapter(orders, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
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