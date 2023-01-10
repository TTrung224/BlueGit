package com.example.bluegit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class ProductManageActivity extends AppCompatActivity {
    private String uId;
    Intent navIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_manage);

        navIntent = new Intent(this, MainActivity.class);
        uId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
    }

    @Override
    protected void onStart() {
        super.onStart();
        RecyclerView productDisplay = findViewById(R.id.items_display);
//        ProductDisplayAdapter adapter = new ProductDisplayAdapter(products, this);
//        productDisplay.setAdapter(adapter);
//        productDisplay.setLayoutManager(new GridLayoutManager(this, 2));
    }

    public void goBack(View view) {
        setResult(RESULT_OK);
        finish();
    }

    public void refresh(View view) {
        this.recreate();
    }

    public void toOrders(View view) {
        navIntent.putExtra("navTo", MainActivity.NAV_TO_ORDERS);
        setResult(RESULT_OK, navIntent);
        finish();
        overridePendingTransition(0, 0);
    }

    public void toHome(View view) {
        setResult(RESULT_OK);
        finish();
        overridePendingTransition(0, 0);
    }

    public void toCart(View view) {
        navIntent.putExtra("navTo", MainActivity.NAV_TO_CART);
        setResult(RESULT_OK, navIntent);
        finish();
        overridePendingTransition(0, 0);
    }

    public void toAccount(View view) {
        navIntent.putExtra("navTo", MainActivity.NAV_TO_ACCOUNT);
        setResult(RESULT_OK, navIntent);
        finish();
        overridePendingTransition(0, 0);
    }
}