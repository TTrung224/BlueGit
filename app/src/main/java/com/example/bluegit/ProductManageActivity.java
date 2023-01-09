package com.example.bluegit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

public class ProductManageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_manage);
    }

    @Override
    protected void onStart() {
        super.onStart();
        RecyclerView productDisplay = findViewById(R.id.items_display);
//        ProductDisplayAdapter adapter = new ProductDisplayAdapter(products, this);
//        productDisplay.setAdapter(adapter);
//        productDisplay.setLayoutManager(new GridLayoutManager(this, 2));
    }
}