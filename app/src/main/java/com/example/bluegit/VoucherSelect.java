package com.example.bluegit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.example.bluegit.adapters.ChooseVoucherAdapter;
import com.example.bluegit.adapters.RecyclerViewOnClickListener;
import com.example.bluegit.model.Voucher;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class VoucherSelect extends AppCompatActivity {

    FireStoreManager fireStoreManager;
    RecyclerView vDisplay;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voucher_select);
        vDisplay = findViewById(R.id.voucherRecycler);
        progressBar = findViewById(R.id.progressBar);

        fireStoreManager = new FireStoreManager(this, FirebaseAuth.getInstance().getCurrentUser());
        Intent intent = getIntent();
        int totalPrice = intent.getIntExtra("totalPrice", 0);

        fireStoreManager.getEligibleVoucher(totalPrice, new FireStoreManager.GetVouchersCallBack() {
            @Override
            public void onSuccess(ArrayList<Voucher> result) {
                progressBar.setVisibility(View.GONE);
                ChooseVoucherAdapter adapter = new ChooseVoucherAdapter(result, VoucherSelect.this, new RecyclerViewOnClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        Intent intent = new Intent(VoucherSelect.this, CartActivity.class);
                        Bundle bundle = new Bundle();
                        int discounted = totalPrice;
                        Voucher voucher = result.get(position);

                        discounted = discounted * (100 - voucher.getDiscountPercent()) /100;
                        bundle.putSerializable("voucher", voucher);
                        bundle.putInt("newPrice", discounted);
                        intent.putExtras(bundle);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                });
                vDisplay.setLayoutManager(new LinearLayoutManager(VoucherSelect.this));
                vDisplay.setAdapter(adapter);
            }
            @Override
            public void onFailure(Exception e) {
                Log.d("Eligible Voucher Exception", e.getMessage());
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    public void goBack(View view) {
        finish();
    }
}