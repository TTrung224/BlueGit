package com.example.bluegit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;



public class AdminVoucher extends AppCompatActivity {
    ImageButton addNewVoucher;
    LinearLayout inputNewVoucher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_voucher);
        addNewVoucher = (ImageButton) findViewById(R.id.addNewVoucher);
        inputNewVoucher = (LinearLayout) findViewById(R.id.inputVoucher);
    }

    public void addClick(View v){
        inputNewVoucher.setVisibility(View.VISIBLE);
    }

    public void removeClick(View v){
        inputNewVoucher.setVisibility(View.GONE);
    }
}