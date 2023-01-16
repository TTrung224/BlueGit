package com.example.bluegit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
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

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    public static final int NAV_TO_BUSINESS = 1;
    public static final int NAV_TO_ORDERS = 2;
    public static final int NAV_TO_HOME = 3;
    public static final int NAV_TO_CART = 4;
    public static final int NAV_TO_ACCOUNT = 5;

    NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
    ImageView profilePic;
    FireStoreManager fireStoreManager;
    ProgressBar progressBar;
    EditText tSearchBar;
    TextView tUserName;
    TextView tUserBalance;
    ImageButton searchBtn;
    RecyclerView productDisplay;
    FirebaseUser currentUser;

    GoogleSignInOptions gso;
    GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        profilePic = findViewById(R.id.otherImg);
        progressBar = findViewById(R.id.display_progress);
        tSearchBar = findViewById(R.id.search_bar);
        tUserName = findViewById(R.id.sellerName);
        tUserBalance = findViewById(R.id.user_balance);
        searchBtn = findViewById(R.id.search_button);
        productDisplay = findViewById(R.id.items_display);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        IntentFilter intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        MyBroadcastReceiver broadcastReceiver = new MyBroadcastReceiver();
        this.registerReceiver(broadcastReceiver, intentFilter);

        tSearchBar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                startSearch(v);
                return true;
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, 1, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Log.d("DEBUGGING", connectionResult.getErrorMessage());
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        if(currentUser!=null){
            Intent intent = new Intent(this, NotificationService.class);
            intent.putExtra("uid", currentUser.getUid());
            this.startForegroundService(intent);
        } else {
            finish();
        }
        fireStoreManager = new FireStoreManager(this, currentUser);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(currentUser != null){
            fireStoreManager.getCurrentUser(new FireStoreManager.GetUserDataCallBack() {
                @Override
                public void onSuccess(User result) {
                    tUserName.setText(result.getDisplayName());
                    tUserBalance.setText(nf.format(result.getBalance()));
                    Picasso.get()
                            .load(result.getProfileImageSrc())
                            .into(profilePic);
                }

                @Override
                public void onFailure(Exception e) {
                    Log.d("GetCurrentUserEx", e.getMessage());
                    if(e instanceof NoUserInDatabaseException){
                        currentUser.delete();
                        FirebaseAuth.getInstance().signOut();
                        Toast.makeText(MainActivity.this, "User is not in database, please re-create account", Toast.LENGTH_SHORT).show();
                    }
                    finish();
                }
            });
        }else{
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

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.create();
        builder.setTitle("SIGN OUT CONFIRMATION")
                .setMessage("Are you sure you want to sign out?")
                .setPositiveButton("SIGN OUT", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        stopService(new Intent(MainActivity.this, NotificationService.class));
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
                    }})
                .setNegativeButton("CANCEL", null).show();}


    public void onChatsClick(View view) {
        Intent intent = new Intent(this, ChatListActivity.class);
        startActivity(intent);
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