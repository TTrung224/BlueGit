package com.example.bluegit;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Paint;
import android.os.Bundle;
import android.widget.TextView;

public class OrderDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        TextView oldPrice = findViewById(R.id.totalPriceBeforeDiscount);
        oldPrice.setPaintFlags(oldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
    }
}