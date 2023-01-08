package com.example.bluegit;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class ProductActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        TextView oldPrice = findViewById(R.id.oldPrice);
        oldPrice.setPaintFlags(oldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

    }

    public void minusQuantity(View view) {
        EditText quantityTextEdit = (EditText) findViewById(R.id.quantity);
        int quantity = Integer.parseInt(quantityTextEdit.getText().toString());
        if(quantity > 1){
            quantity--;
            quantityTextEdit.setText(Integer.toString(quantity));
        }
    }

    public void bonusQuantity(View view) {
        EditText quantityTextEdit = (EditText) findViewById(R.id.quantity);
        int quantity = Integer.parseInt(quantityTextEdit.getText().toString()) + 1;
        quantityTextEdit.setText(Integer.toString(quantity));
    }
}