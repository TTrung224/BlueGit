package com.example.bluegit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class AdminAccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_account);
    }
    public void toAdminProduct(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("navTo", MainActivity.NAV_TO_MANAGE_PRODUCT);
        setResult(RESULT_OK, intent);
        finish();
        overridePendingTransition(0, 0);
    }

    public void toVoucher(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("navTo", MainActivity.NAV_TO_VOUCHER);
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