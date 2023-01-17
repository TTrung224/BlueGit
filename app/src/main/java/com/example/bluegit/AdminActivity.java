package com.example.bluegit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Locale;

public class AdminActivity extends AppCompatActivity {
    //Admin
    public static final int NAV_TO_VOUCHER = 1;
    public static final int NAV_TO_MANAGE_ACCOUNT = 2;
    public static final int NAV_TO_MANAGE_PRODUCT = 3;
    public static final int NAV_TO_ADMIN_HOME = 4;
    public static final int NAV_TO_MANAGE_ORDERS = 5;

    FireStoreManager fireStoreManager;
    TextView balance;
    FirebaseUser currentUser;

    GoogleSignInOptions gso;
    GoogleApiClient googleApiClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        balance = findViewById(R.id.adminBalanceDisplay);


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

        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if(currentUser != null){
            String welcomeMessage = "WELCOME! " + currentUser.getDisplayName();
            Toast.makeText(AdminActivity.this, welcomeMessage, Toast.LENGTH_SHORT).show();
        }else {
            finish();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FireStoreManager fireStoreManager = new FireStoreManager(AdminActivity.this, currentUser);
        fireStoreManager.getUserById(currentUser.getUid(), new FireStoreManager.GetUserDataCallBack() {
            @Override
            public void onSuccess(User result) {
                NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
                String balanceA = nf.format(result.getBalance());
                balance.setText(balanceA);
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(AdminActivity.this, "Fail to load Admin balance", Toast.LENGTH_SHORT).show();
            }
        });
    }




    public void signOutClick(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(AdminActivity.this);
        builder.create();
        builder.setTitle("SIGN OUT CONFIRMATION")
                .setMessage("Are you sure you want to sign out?")
                .setPositiveButton("SIGN OUT", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
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
    }}).setNegativeButton("CANCEL", null).show();}
    public void refresh(View view) {
        this.recreate();
    }

    public void toVoucher(View view){
        Intent intent = new Intent(this,AdminVoucherActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivityForResult(intent, 100);
    }

    public void toManageAccount(View view){
        Intent intent = new Intent(this, AdminAccountActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivityForResult(intent,100);
    }

    public void toManageProduct(View view){
        Intent intent = new Intent(this, AdminProductActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivityForResult(intent,100);
    }

    public void toManageOrders(View view){
        Intent intent = new Intent(this, AdminOrdersActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivityForResult(intent,100);
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

    public void toManageOrders(){
        Intent intent = new Intent(this, AdminOrdersActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivityForResult(intent,100);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            switch (Integer.parseInt(data.getExtras().get("navTo").toString())) {
                case NAV_TO_MANAGE_ORDERS:
                    toManageOrders();
                    break;
                case NAV_TO_MANAGE_ACCOUNT:
                    toManageAccount();
                    break;
                case NAV_TO_MANAGE_PRODUCT:
                    toManageProduct();
                    break;
                case NAV_TO_VOUCHER:
                    toVoucher();
                    break;
            }
        }
    }
}