package com.example.bluegit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.accounts.Account;
import android.os.Bundle;
import android.view.View;

import com.example.bluegit.adapters.AdminProductAdapter;

import java.util.ArrayList;

public class AdminAccountActivity extends AppCompatActivity {
    private ArrayList<Account> accounts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_account);
    }
    @Override
    protected void onStart() {
        super.onStart();

        // TODO: retrieve and process data here

        RecyclerView recyclerView = findViewById(R.id.adminAccountList);
        AdminProductAdapter adapter = new AdminProductAdapter(accounts, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
    public void toAdminProduct(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("navTo", AdminActivity.NAV_TO_MANAGE_PRODUCT);
        setResult(RESULT_OK, intent);
        finish();
        overridePendingTransition(0, 0);
    }

    public void toVoucher(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("navTo", AdminActivity.NAV_TO_VOUCHER);
        setResult(RESULT_OK, intent);
        finish();
        overridePendingTransition(0, 0);
    }
    public void toHome(){
        setResult(RESULT_OK);
        finish();
        overridePendingTransition(0, 0);
    }

    public void refresh(View view) {
        this.recreate();
    }
}