package com.example.bluegit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.bluegit.adapters.AdminAccountAdapter;
import com.example.bluegit.model.User;

import java.util.ArrayList;

public class AdminAccountActivity extends AppCompatActivity {
    private ArrayList<User> users;
    Intent navIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_account);

        navIntent = new Intent(this, AdminActivity.class);
    }
    @Override
    protected void onStart() {
        super.onStart();

        // TODO: retrieve and process data here

        RecyclerView recyclerView = findViewById(R.id.adminOrderList);
//        AdminAccountAdapter adapter = new AdminAccountAdapter(users, this);
//        recyclerView.setAdapter(adapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void adminGoBack(View view){
        setResult(RESULT_OK);
        finish();
    }
    public void toManageProduct(View view){
        navIntent.putExtra("navTo", AdminActivity.NAV_TO_MANAGE_PRODUCT);
        setResult(RESULT_OK, navIntent);
        finish();
        overridePendingTransition(0, 0);
    }

    public void toManageOrder(View view){
        navIntent.putExtra("navTo", AdminActivity.NAV_TO_MANAGE_ORDERS);
        setResult(RESULT_OK, navIntent);
        finish();
        overridePendingTransition(0, 0);
    }

    public void toVoucher(View view){
        navIntent.putExtra("navTo", AdminActivity.NAV_TO_VOUCHER);
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