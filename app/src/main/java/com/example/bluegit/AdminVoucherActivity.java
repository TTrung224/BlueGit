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
import android.widget.TextView;
import android.widget.Toast;

import com.example.bluegit.adapters.AdminVoucherAdapter;
import com.example.bluegit.model.Voucher;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;


public class AdminVoucherActivity extends AppCompatActivity {
    ImageButton addNewVoucher;
    LinearLayout inputNewVoucher;
    Intent navIntent;
    TextView voucherID;
    EditText addNameVoucher, addDiscountPercent, addMinOrder, addMaxDiscount;
    Button submitVoucher;
    RecyclerView recyclerView;
    FireStoreManager fireStoreManager;

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

    }
    @Override
    protected void onStart(){
        super.onStart();
        fireStoreManager.getAllVoucher(new FireStoreManager.getAllVoucherCallBack() {
            @Override
            public void onSuccess(ArrayList<Voucher> result){
                RecyclerView recyclerView = findViewById(R.id.adminVoucherList);
                AdminVoucherAdapter adapter = new AdminVoucherAdapter(result, AdminVoucherActivity.this);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(AdminVoucherActivity.this));
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