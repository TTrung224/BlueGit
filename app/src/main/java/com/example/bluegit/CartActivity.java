package com.example.bluegit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.bluegit.adapters.AdminProductAdapter;
import com.example.bluegit.model.Product;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Objects;

public class CartActivity extends AppCompatActivity {
    private String uId;
    private ArrayList<Product> products;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        uId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

    }
    @Override
    protected void onStart() {
        super.onStart();

        // TODO: retrieve and process data here

        RecyclerView recyclerView = findViewById(R.id.itemList);
        AdminProductAdapter adapter = new AdminProductAdapter(products, this);
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