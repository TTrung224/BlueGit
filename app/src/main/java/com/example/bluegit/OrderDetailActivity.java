package com.example.bluegit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.bluegit.adapters.OrderProductAdapter;
import com.example.bluegit.model.Order;
import com.example.bluegit.model.Product;
import com.google.firebase.auth.FirebaseAuth;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class OrderDetailActivity extends AppCompatActivity {
    FireStoreManager fireStoreManager;

    TextView tOrderHeader;
    TextView tOrderCreatedDate;
    TextView tOrderShippingInfo;
    TextView tTotalPrice;
    RecyclerView orderProductList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        fireStoreManager = new FireStoreManager(this, FirebaseAuth.getInstance().getCurrentUser());

        Intent intent = getIntent();
        String orderId = intent.getStringExtra("orderId");

        tOrderHeader = findViewById(R.id.orderHeader);
        tOrderCreatedDate = findViewById(R.id.orderCreatedDate);
        tOrderShippingInfo = findViewById(R.id.orderShippingInfo);
        tTotalPrice = findViewById(R.id.totalPrice);

        orderProductList = findViewById(R.id.orderProductList);

        fireStoreManager.getOrderAndProductsById(orderId, new FireStoreManager.GetOrderAndProductsCallBack() {
            @Override
            public void onSuccess(Order result, ArrayList<Product> products) {
                String header = "Order " + result.getId().substring(0, 7).toUpperCase();
                tOrderHeader.setText(header);

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", new Locale("vi, VN"));
                tOrderCreatedDate.setText(dateFormat.format(result.getCreatedDate().toDate()));

                String[] addressArray = result.getAddress().split("\\|");
                String shippingInfo = "\nName: " + addressArray[0] +
                        "\nPhone: " + addressArray[1] +
                        "\nAddress: " + addressArray[2];
                tOrderShippingInfo.setText(shippingInfo);

                tTotalPrice.setText(NumberFormat.getCurrencyInstance(new Locale("vi", "VN"))
                        .format(result.getTotalPrice()));
                OrderProductAdapter adapter = new OrderProductAdapter(products, result.getAmount(), OrderDetailActivity.this);
                orderProductList.setAdapter(adapter);
                orderProductList.setLayoutManager(new LinearLayoutManager(OrderDetailActivity.this));
            }

            @Override
            public void onFailure(Exception e) {

            }
        });

    }

    public void goBack(View view) {
        finish();
    }
}