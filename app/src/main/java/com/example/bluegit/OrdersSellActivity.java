package com.example.bluegit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class OrdersSellActivity extends AppCompatActivity {
    TextView numCompleted;
    TextView numConfirmed;
    TextView numDelivery;
    TextView numPending;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_sell);

        numCompleted = findViewById(R.id.numCompleted);
        numConfirmed = findViewById(R.id.numConfirmed);
        numDelivery = findViewById(R.id.numDelivery);
        numPending = findViewById(R.id.numPending);


    }
}