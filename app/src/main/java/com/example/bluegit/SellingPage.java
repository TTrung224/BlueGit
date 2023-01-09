package com.example.bluegit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.bluegit.model.Seller;

public class SellingPage extends AppCompatActivity {
    TextView numCompleted;
    TextView numConfirmed;
    TextView numDelivery;
    TextView numPending;
    Seller seller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_selling);

        numCompleted = findViewById(R.id.numCompleted);
        numConfirmed = findViewById(R.id.numConfirmed);
        numDelivery = findViewById(R.id.numDelivery);
        numPending = findViewById(R.id.numPending);

        numCompleted.setText(seller.getNumCompleted());
        numConfirmed.setText(seller.getNumConfirmed());
        numDelivery.setText(seller.getNumDelivery());
        numPending.setText(seller.getNumPending());
    }
}