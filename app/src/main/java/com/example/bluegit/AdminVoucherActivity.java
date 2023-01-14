package com.example.bluegit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bluegit.model.Voucher;
import com.google.firebase.auth.FirebaseAuth;

import java.util.UUID;


public class AdminVoucherActivity extends AppCompatActivity {
    ImageButton addNewVoucher;
    LinearLayout inputNewVoucher;
    Intent navIntent;
    TextView voucherID;
    EditText addNameVoucher, addDiscountPercent, addMaxDiscountAmount, addMinOrderValue;
    Button submitVoucher;
    FireStoreManager fireStoreManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_voucher);
        addNameVoucher = (EditText) findViewById(R.id.addVoucherName);
        addDiscountPercent = (EditText) findViewById(R.id.addDiscountPercent);
        addMaxDiscountAmount = (EditText) findViewById(R.id.addMaxDiscountAmount);
        addMinOrderValue = (EditText) findViewById(R.id.addMinOrderValue);

        inputNewVoucher = (LinearLayout) findViewById(R.id.inputVoucher);
        addNewVoucher = (ImageButton) findViewById(R.id.addNewVoucher);

        fireStoreManager = new FireStoreManager(AdminVoucherActivity.this,
                FirebaseAuth.getInstance().getCurrentUser());

//        submitVoucher = (Button) findViewById(R.id.cancel);
//        voucherID = (TextView) findViewById(R.id.voucherID);

        navIntent = new Intent(this, AdminActivity.class);

    }

    public void addClick(View view){
        inputNewVoucher.setVisibility(View.VISIBLE);
    }

    public void addVoucher(View view){
        String voucherId = UUID.randomUUID().toString();
        String name = addNameVoucher.getText().toString();

        String discountPercentStr = addDiscountPercent.getText().toString();

        String maxAmountStr = addMaxDiscountAmount.getText().toString();

        String minOrderValueStr = addMinOrderValue.getText().toString();


        int discountPercent = Integer.parseInt(discountPercentStr);
        int maxAmount = Integer.parseInt(maxAmountStr);
        int minOrderValue = Integer.parseInt(minOrderValueStr);

        if(name.equals("")) {
            addNameVoucher.setError("Please enter name.");
            addNameVoucher.requestFocus();
        } else if(discountPercentStr.equals("")) {
            addDiscountPercent.setError("Please enter discount percent.");
            addDiscountPercent.requestFocus();
        } else if(maxAmountStr.equals("")) {
            addMaxDiscountAmount.setError("Please enter max discount amount.");
            addMaxDiscountAmount.requestFocus();
        } else if(minOrderValueStr.equals("")) {
            addMinOrderValue.setError("Please enter min spend.");
            addMinOrderValue.requestFocus();
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