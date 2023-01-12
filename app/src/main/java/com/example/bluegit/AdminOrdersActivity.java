package com.example.bluegit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class AdminOrdersActivity extends AppCompatActivity {
    Intent navIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_orders);
        navIntent = new Intent(this, AdminActivity.class);
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