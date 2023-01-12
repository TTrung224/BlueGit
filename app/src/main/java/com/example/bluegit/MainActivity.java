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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.bluegit.adapters.ProductDisplayAdapter;
import com.example.bluegit.adapters.RecyclerViewOnClickListener;
import com.example.bluegit.model.Product;
import com.example.bluegit.model.User;
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

    ImageView profilePic;
    FireStoreManager fireStoreManager;
    ProgressBar progressBar;
    EditText tSearchBar;
    ImageButton searchBtn;
    RecyclerView productDisplay;

    GoogleSignInOptions gso;
    GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        profilePic = findViewById(R.id.main_profile_pic);
        progressBar = findViewById(R.id.display_progress);
        tSearchBar = findViewById(R.id.search_bar);
        searchBtn = findViewById(R.id.search_button);
        productDisplay = findViewById(R.id.items_display);
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
            fireStoreManager.getCurrentUser(new FireStoreManager.GetUserDataCallBack() {
                @Override
                public void onSuccess(User result) {
                    String welcomeMessage = "WELCOME! " + result.getDisplayName();
                    Picasso.get()
                            .load(result.getProfileImageSrc())
                            .into(profilePic);
                    Toast.makeText(MainActivity.this, welcomeMessage, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Exception e) {
                    Log.d("GetCurrentUserEx", e.getLocalizedMessage());
                    finish();
                }
            });

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

        fireStoreManager.getAllProducts(new FireStoreManager.GetProductsCallBack() {
            @Override
            public void onSuccess(ArrayList<Product> result) {
                progressBar.setVisibility(View.GONE);
                productDisplay.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
                ProductDisplayAdapter adapter = new ProductDisplayAdapter(result, MainActivity.this, new RecyclerViewOnClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        Product product = result.get(position);
                        Intent intent = new Intent(MainActivity.this, ProductActivity.class);
                        intent.putExtra("productId", product.getProductId());
                        startActivity(intent);
                    }
                });
                productDisplay.setAdapter(adapter);
            }

            @Override
            public void onFailure(Exception e) {
                progressBar.setVisibility(View.GONE);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100 && resultCode == RESULT_OK && data != null){
            switch (Integer.parseInt(data.getExtras().get("navTo").toString())){
                case NAV_TO_BUSINESS: toBusiness(); break;
                case NAV_TO_ORDERS: toOrders(); break;
                case NAV_TO_CART: toCart(); break;
                case NAV_TO_ACCOUNT: toAccount(); break;
            }
        }
    }

    public void testChat(View view) {
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("otherUserId", "TncOqL0OPdesMbXbHW0ZCtTEbs63");
        startActivity(intent);
    }

    public void startSearch(View view) {
        String searchString = tSearchBar.getText().toString();
        progressBar.setVisibility(View.VISIBLE);

        fireStoreManager.getProductsFromString(searchString, new FireStoreManager.GetProductsCallBack() {
            @Override
            public void onSuccess(ArrayList<Product> result) {
                progressBar.setVisibility(View.GONE);
                productDisplay.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
                ProductDisplayAdapter adapter = new ProductDisplayAdapter(result, MainActivity.this, new RecyclerViewOnClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        Product product = result.get(position);
                        Intent intent = new Intent(MainActivity.this, ProductActivity.class);
                        intent.putExtra("productId", product.getProductId());
                        startActivity(intent);
                    }
                });
                productDisplay.setAdapter(adapter);
            }

            @Override
            public void onFailure(Exception e) {
                progressBar.setVisibility(View.GONE);
                Log.d("GetStringProductException", e.getMessage());
            }
        });

    }
}