package com.example.bluegit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.bluegit.adapters.AdminProductAdapter;
import com.example.bluegit.adapters.OrderBuyAdapter;
import com.example.bluegit.model.Product;

import java.util.ArrayList;

public class AdminProductActivity extends AppCompatActivity {

    private ArrayList<Product> products;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_product);
    }
    @Override
    protected void onStart() {
        super.onStart();

        // TODO: retrieve and process data here

        RecyclerView recyclerView = findViewById(R.id.adminProductList);
        AdminProductAdapter adapter = new AdminProductAdapter(products, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}