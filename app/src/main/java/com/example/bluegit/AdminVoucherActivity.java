package com.example.bluegit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;



public class AdminVoucherActivity extends AppCompatActivity {
    ImageButton addNewVoucher,removeInputVoucher;
    LinearLayout inputNewVoucher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_voucher);
        addNewVoucher = (ImageButton) findViewById(R.id.addNewVoucher);
        inputNewVoucher = (LinearLayout) findViewById(R.id.inputVoucher);
        removeInputVoucher = (ImageButton) findViewById(R.id.removeInputVoucher);
    }

    public void addClick(View v){
        inputNewVoucher.setVisibility(View.VISIBLE);
    }

    public void removeClick(View v){
        inputNewVoucher.setVisibility(View.GONE);
    }
}