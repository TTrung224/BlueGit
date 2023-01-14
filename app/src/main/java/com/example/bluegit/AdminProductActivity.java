package com.example.bluegit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.bluegit.adapters.AdminAccountAdapter;
import com.example.bluegit.adapters.AdminProductAdapter;
import com.example.bluegit.adapters.OrderBuyAdapter;
import com.example.bluegit.model.Product;
import com.example.bluegit.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class AdminProductActivity extends AppCompatActivity {

    private ArrayList<Product> products;
    Intent navIntent;

    RecyclerView recyclerView;
    FireStoreManager fireStoreManager;

    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_product);

        navIntent = new Intent(this, AdminActivity.class);
        recyclerView = findViewById(R.id.adminProductList);
        fireStoreManager = new FireStoreManager(AdminProductActivity.this,
                FirebaseAuth.getInstance().getCurrentUser());

        currentUser = FirebaseAuth.getInstance().getCurrentUser();


    }
    @Override
    protected void onStart() {
        super.onStart();

        // TODO: retrieve and process data here

        fireStoreManager.getAllProductForAdmin(new FireStoreManager.getAllProductForAdminCallBack() {
            @Override
            public void onSuccess(ArrayList<Product> result) {
                AdminProductAdapter adapter = new AdminProductAdapter(result, AdminProductActivity.this, currentUser);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(AdminProductActivity.this));
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(AdminProductActivity.this, "Fail to load users data, please try again", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }

    public void adminGoBack(View view){
        setResult(RESULT_OK);
        finish();
    }

    public void toVoucher(View view){
        navIntent.putExtra("navTo", AdminActivity.NAV_TO_VOUCHER);
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
    public void toManageOrder(View view){
        navIntent.putExtra("navTo", AdminActivity.NAV_TO_MANAGE_ORDERS);
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