package com.example.bluegit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;


public class AdminVoucherActivity extends AppCompatActivity {
    ImageButton addNewVoucher;
    LinearLayout inputNewVoucher;
    Intent navIntent;
    TextView voucherID;
    EditText addNameVoucher, addDiscountVoucher, addQuantityVoucher, addExpireDateVoucher;
    Button submitVoucher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_voucher);
        addNewVoucher = (ImageButton) findViewById(R.id.addNewVoucher);
        inputNewVoucher = (LinearLayout) findViewById(R.id.inputVoucher);
        addNameVoucher = (EditText) findViewById(R.id.addVoucherName);
        addDiscountVoucher = (EditText) findViewById(R.id.addDiscountVoucher);
        addQuantityVoucher = (EditText) findViewById(R.id.addQuantityVoucher);
        addExpireDateVoucher = (EditText) findViewById(R.id.addExpireDateVoucher);
        submitVoucher = (Button) findViewById(R.id.submitVoucher);
//        voucherID = (TextView) findViewById(R.id.voucherID);

        navIntent = new Intent(this, AdminActivity.class);

    }

    public void addClick(View view){
        inputNewVoucher.setVisibility(View.VISIBLE);
    }

    public void cancel(View view){
        inputNewVoucher.setVisibility(View.GONE);
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