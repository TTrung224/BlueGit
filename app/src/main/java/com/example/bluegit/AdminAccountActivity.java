package com.example.bluegit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.bluegit.adapters.AdminAccountAdapter;
import com.example.bluegit.model.User;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class AdminAccountActivity extends AppCompatActivity {
    Intent navIntent;

    RecyclerView recyclerView;
    FireStoreManager fireStoreManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_account);

        navIntent = new Intent(this, AdminActivity.class);

        recyclerView = findViewById(R.id.adminAccountList);
        fireStoreManager = new FireStoreManager(AdminAccountActivity.this,
                FirebaseAuth.getInstance().getCurrentUser());
    }

    @Override
    protected void onStart() {
        super.onStart();

        fireStoreManager.getAllUser(new FireStoreManager.getAllUserCallBack() {
            @Override
            public void onSuccess(ArrayList<User> result) {
                AdminAccountAdapter adapter = new AdminAccountAdapter(result, AdminAccountActivity.this);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(AdminAccountActivity.this));
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(AdminAccountActivity.this, "Fail to load users data, please try again", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
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

    public void toVoucher(View view){
        navIntent.putExtra("navTo", AdminActivity.NAV_TO_VOUCHER);
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