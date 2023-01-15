package com.example.bluegit;

import androidx.annotation.NonNull;
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
import com.example.bluegit.model.User;
import com.example.bluegit.model.Voucher;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;

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
    TextView tVoucher;
    TextView tDiscount;
    TextView tBuyerEmail;
    TextView tSellerEmail;
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
        tVoucher = findViewById(R.id.voucher);
        tDiscount = findViewById(R.id.discountPrice);
        tBuyerEmail = findViewById(R.id.buyerEmail);
        tSellerEmail = findViewById(R.id.sellerEmail);

        orderProductList = findViewById(R.id.orderProductList);

        fireStoreManager.getOrderAndProductsById(orderId, new FireStoreManager.GetOrderAndProductsCallBack() {
            @Override
            public void onSuccess(Order result, ArrayList<Product> products) {
                result.getSellerId().get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            tSellerEmail.setText(task.getResult().getString("email"));
                        }
                    }
                });

                result.getCustomerId().get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            tBuyerEmail.setText(task.getResult().getString("email"));
                        }
                    }
                });

                String header = "Order " + result.getId().substring(0, 7).toUpperCase();
                tOrderHeader.setText(header);

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", new Locale("vi, VN"));
                tOrderCreatedDate.setText(dateFormat.format(result.getCreatedDate().toDate()));

                String[] addressArray = result.getAddress().split("\\|");
                String shippingInfo = "Name: " + addressArray[0] +
                        "\nPhone: " + addressArray[1] +
                        "\nAddress: " + addressArray[2];
                tOrderShippingInfo.setText(shippingInfo);

                NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
                tTotalPrice.setText(nf.format(result.getTotalPrice()));

                Voucher voucher = result.getVoucher();
                if(voucher != null){
                    String voucherInfo = "Apply -" + voucher.getDiscountPercent() + "% for this order.";
                    tVoucher.setText(voucherInfo);
                    tDiscount.setText(nf.format(result.discountedTotal()));
                }else {
                    tVoucher.setText("None");
                    tDiscount.setText("None");
                }

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