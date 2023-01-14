package com.example.bluegit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bluegit.adapters.AdminVoucherAdapter;
import com.example.bluegit.model.Voucher;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import java.util.UUID;


public class AdminVoucherActivity extends AppCompatActivity {
    ImageButton addNewVoucher;
    LinearLayout inputNewVoucher;
    Intent navIntent;
    TextView voucherID;
    EditText addNameVoucher, addDiscountPercent, addMinOrder, addMaxDiscount;
    Button submitVoucher;
    RecyclerView recyclerView;
    FireStoreManager fireStoreManager;
    ProgressBar adminVoucherProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_voucher);
        addNewVoucher = (ImageButton) findViewById(R.id.addNewVoucher);
        inputNewVoucher = (LinearLayout) findViewById(R.id.inputVoucher);
        addNameVoucher = (EditText) findViewById(R.id.addVoucherName);
        addDiscountPercent = (EditText) findViewById(R.id.addDiscountPercent);
        addMinOrder = (EditText) findViewById(R.id.addMinOrder);
        addMaxDiscount = (EditText) findViewById(R.id.addMaxDiscount);
        submitVoucher = (Button) findViewById(R.id.submitVoucher);
//        voucherID = (TextView) findViewById(R.id.voucherID);

        navIntent = new Intent(this, AdminActivity.class);
        recyclerView = findViewById(R.id.adminVoucherList);
        fireStoreManager = new FireStoreManager(AdminVoucherActivity.this, FirebaseAuth.getInstance().getCurrentUser());
        adminVoucherProgress = findViewById(R.id.voucherProgressBar);

    }
    @Override
    protected void onStart(){
        super.onStart();
        fireStoreManager.getAllVoucher(new FireStoreManager.GetAllVoucherCallBack() {
            @Override
            public void onSuccess(ArrayList<Voucher> result){
                RecyclerView recyclerView = findViewById(R.id.adminVoucherList);
                AdminVoucherAdapter adapter = new AdminVoucherAdapter(result, AdminVoucherActivity.this);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(AdminVoucherActivity.this));
                adminVoucherProgress.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Exception e){
                Toast.makeText(AdminVoucherActivity.this, "Fail to load vouchers data, please try again", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    public void addClick(View view){
        inputNewVoucher.setVisibility(View.VISIBLE);
    }

    public void addNewVoucher(View view){
        String voucherId = UUID.randomUUID().toString();
        String name = addNameVoucher.getText().toString();

        String discountPercentStr = addDiscountPercent.getText().toString();

        String maxAmountStr = addMaxDiscount.getText().toString();

        String minOrderValueStr = addMinOrder.getText().toString();

        if(name.equals("")) {
            addNameVoucher.setError("Please enter name.");
            addNameVoucher.requestFocus();
        } else if(discountPercentStr.equals("")) {
            addDiscountPercent.setError("Please enter discount percent.");
            addDiscountPercent.requestFocus();
        } else if(maxAmountStr.equals("")) {
            addMaxDiscount.setError("Please enter max discount amount.");
            addMaxDiscount.requestFocus();
        } else if(minOrderValueStr.equals("")) {
            addMinOrder.setError("Please enter min spend.");
            addMinOrder.requestFocus();
        } else {
            int discountPercent = Integer.parseInt(discountPercentStr);
            int maxAmount = Integer.parseInt(maxAmountStr);
            int minOrderValue = Integer.parseInt(minOrderValueStr);

            if(discountPercent>100) {
                addDiscountPercent.setError("The maximum number can input is 100.");
                addDiscountPercent.requestFocus();
                } else {

                Voucher voucher = new Voucher(voucherId, name, discountPercent,
                        minOrderValue, maxAmount);
                fireStoreManager.addVoucher(voucher, new FireStoreManager.AddVoucherCallBack() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(AdminVoucherActivity.this,
                                "Add voucher successfully", Toast.LENGTH_SHORT).show();
                        inputNewVoucher.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(AdminVoucherActivity.this,
                                        "Fail to add voucher, please try again later", Toast.LENGTH_SHORT)
                                .show();
                        inputNewVoucher.setVisibility(View.GONE);
                    }
                });
            }
        }

    }

    public void cancel(View view){
        inputNewVoucher.setVisibility(View.GONE);
    }

    public void adminGoBack(View view){
        setResult(RESULT_OK);
        finish();
    }

    public void toManageProduct(View view){
        navIntent.putExtra("navTo", AdminActivity.NAV_TO_MANAGE_PRODUCT);
        setResult(RESULT_OK, navIntent);
        finish();
        overridePendingTransition(0, 0);
    }
    public void toManageOrder(View view){
        navIntent.putExtra("navTo", AdminActivity.NAV_TO_MANAGE_ORDERS);
        setResult(RESULT_OK, navIntent);
        finish();
        overridePendingTransition(0, 0);
    }

    public void toManageAccount(View view){
        navIntent.putExtra("navTo", AdminActivity.NAV_TO_MANAGE_ACCOUNT);
        setResult(RESULT_OK, navIntent);
        finish();
        overridePendingTransition(0, 0);
    }
    public void toHome(View view){
        setResult(RESULT_OK);
        finish();
        overridePendingTransition(0, 0);
    }

    public void refresh(View view) {
        this.recreate();
    }

}