package com.example.bluegit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.bluegit.adapters.OrderGeneralAdapter;
import com.example.bluegit.model.Order;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class AdminOrdersActivity extends AppCompatActivity {
    Intent navIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_orders);
        navIntent = new Intent(this, AdminActivity.class);
        RecyclerView orderList = findViewById(R.id.adminorderList);

        FireStoreManager fireStoreManager = new FireStoreManager(this, FirebaseAuth.getInstance().getCurrentUser());
        fireStoreManager.getAllOrders(new FireStoreManager.GetOrdersCallBack() {
            @Override
            public void onSuccess(ArrayList<Order> result) {
                OrderGeneralAdapter adapter = new OrderGeneralAdapter(result, AdminOrdersActivity.this);
                orderList.setAdapter(adapter);
                orderList.setLayoutManager(new LinearLayoutManager(AdminOrdersActivity.this));
            }

            @Override
            public void onFailure(Exception e) {
                Log.d("Get All Orders Exception", e.getMessage());
            }
        });

    }
    public void returnBack(View view){
        setResult(RESULT_OK);
        finish();
    }

    public void toVoucher(View view){
        navIntent.putExtra("navTo", AdminActivity.NAV_TO_VOUCHER);
        setResult(RESULT_OK, navIntent);
        finish();
        overridePendingTransition(0, 0);
    }
    public void toManageProduct(View view){
        navIntent.putExtra("navTo", AdminActivity.NAV_TO_MANAGE_PRODUCT);
        setResult(RESULT_OK, navIntent);
        finish();
        overridePendingTransition(0, 0);
    }

    public void toManageAccount(View view){
        navIntent.putExtra("navTo", AdminActivity.NAV_TO_MANAGE_ACCOUNT);
        setResult(RESULT_OK, navIntent);
        finish();
        overridePendingTransition(0, 0);
    }

    public void toHome(View view){
        setResult(RESULT_OK);
        finish();
        overridePendingTransition(0, 0);
    }

    public void refresh(View view) {
        this.recreate();
    }

}