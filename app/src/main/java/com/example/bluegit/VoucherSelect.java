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
        progressBar = findViewById(R.id.progressBar);

        fireStoreManager = new FireStoreManager(this, FirebaseAuth.getInstance().getCurrentUser());
        Intent intent = getIntent();
        int totalPrice = intent.getIntExtra("totalPrice", 0);

//        fireStoreManager.getEligibleVoucher(totalPrice, new FireStoreManager.GetVouchersCallBack() {
//            @Override
//            public void onSuccess(ArrayList<Voucher> result) {
//                progressBar.setVisibility(View.GONE);
//                ChooseVoucherAdapter adapter = new ChooseVoucherAdapter(result, VoucherSelect.this);
//                vDisplay.setAdapter(adapter);
//                vDisplay.setLayoutManager(new LinearLayoutManager(VoucherSelect.this));
//            }
//            @Override
//            public void onFailure(Exception e) {
//                Log.d("Eligible Voucher Exception", e.getMessage());
//                progressBar.setVisibility(View.GONE);
//            }
//        });
    }

    public void goBack(View view) {
        finish();
    }
}