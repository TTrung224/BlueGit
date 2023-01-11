package com.example.bluegit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.bluegit.adapters.ProductDisplayAdapter;
import com.example.bluegit.model.Product;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final int NAV_TO_BUSINESS = 1;
    public static final int NAV_TO_ORDERS = 2;
    public static final int NAV_TO_HOME = 3;
    public static final int NAV_TO_CART = 4;
    public static final int NAV_TO_ACCOUNT = 5;

    //Admin
    public static final int NAV_TO_VOUCHER = 6;
    public static final int NAV_TO_MANAGE_ACCOUNT = 7;
    public static final int NAV_TO_MANAGE_PRODUCT = 8;
    public static final int NAV_TO_ADMIN_HOME = 9;



    ImageView profilePic;
    FireStoreManager fireStoreManager;

    GoogleSignInOptions gso;
    GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        profilePic = findViewById(R.id.main_profile_pic);
        fireStoreManager = new FireStoreManager(this, FirebaseAuth.getInstance().getCurrentUser());

        gso =  new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleApiClient=new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {}
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if(currentUser != null){
            String welcomeMessage = "WELCOME! " + currentUser.getDisplayName();
            Picasso.get()
                    .load(currentUser.getPhotoUrl())
                    .into(profilePic);
            Toast.makeText(MainActivity.this, welcomeMessage, Toast.LENGTH_SHORT).show();
        }else{
            finish();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser() == null){
            finish();
        }

        RecyclerView productDisplay = findViewById(R.id.items_display);

        fireStoreManager.getAllProducts(new GetProductsCallBack() {
            @Override
            public void onSuccess(ArrayList<Product> result) {
                productDisplay.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
                ProductDisplayAdapter adapter = new ProductDisplayAdapter(result, MainActivity.this);
                productDisplay.setAdapter(adapter);
            }

            @Override
            public void onFailure(Exception e) {
                Log.d("GetAllProductException", e.getMessage());
            }
        });

    }


    public void onSignOutClick(View view) {
        FirebaseAuth.getInstance().signOut();

        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                if(status.isSuccess()){
                    googleApiClient.disconnect();
                    finish();
                }
            }
        });

        finish();
    }


    public void toBusiness(View view) {
        Intent intent = new Intent(this, ProductManageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivityForResult(intent, 100);
    }

    public void toOrders(View view) {
        Intent intent = new Intent(this, OrdersBuyActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivityForResult(intent, 100);
    }

    public void refresh(View view) {
        this.recreate();
    }

    public void toCart(View view) {
        Intent intent = new Intent(this, CartActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivityForResult(intent, 100);
    }

    public void toAccount(View view) {
        Intent intent = new Intent(this, AccountActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivityForResult(intent, 100);
    }

    public void toBusiness() {
        Intent intent = new Intent(this, ProductManageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivityForResult(intent, 100);
    }

    public void toOrders() {
        Intent intent = new Intent(this, OrdersBuyActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivityForResult(intent, 100);
    }

    public void toCart() {
        Intent intent = new Intent(this, CartActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivityForResult(intent, 100);
    }

    public void toAccount() {
        Intent intent = new Intent(this, AccountActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivityForResult(intent, 100);
    }

    public void toVoucher(){
        Intent intent = new Intent(this,AdminVoucherActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivityForResult(intent, 100);
    }

    public void toManageAccount(){
        Intent intent = new Intent(this, AdminAccountActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivityForResult(intent,100);
    }

    public void toManageProduct(){
        Intent intent = new Intent(this, AdminProductActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivityForResult(intent,100);
    }

    public void toAdminHome(){
        Intent intent = new Intent(this, AdminActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivityForResult(intent, 100);
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100 && resultCode == RESULT_OK && data != null){
            switch (Integer.parseInt(data.getExtras().get("navTo").toString())){
                case NAV_TO_BUSINESS: toBusiness(); break;
                case NAV_TO_ORDERS: toOrders(); break;
                case NAV_TO_CART: toCart(); break;
                case NAV_TO_ACCOUNT: toAccount(); break;
                case NAV_TO_ADMIN_HOME: toAdminHome(); break;
                case NAV_TO_MANAGE_ACCOUNT: toManageAccount(); break;
                case NAV_TO_MANAGE_PRODUCT: toManageProduct(); break;
                case NAV_TO_VOUCHER: toVoucher(); break;
            }
        }
    }

    public void testChat(View view) {
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("otherUserId", "TncOqL0OPdesMbXbHW0ZCtTEbs63");
        startActivity(intent);
    }
}