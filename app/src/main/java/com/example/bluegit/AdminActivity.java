package com.example.bluegit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

public class AdminActivity extends AppCompatActivity {
    //Admin
    public static final int NAV_TO_VOUCHER = 1;
    public static final int NAV_TO_MANAGE_ACCOUNT = 2;
    public static final int NAV_TO_MANAGE_PRODUCT = 3;
    public static final int NAV_TO_ADMIN_HOME = 4;

    FireStoreManager fireStoreManager;

    GoogleSignInOptions gso;
    GoogleApiClient googleApiClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

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
            Toast.makeText(AdminActivity.this, welcomeMessage, Toast.LENGTH_SHORT).show();
        }else{
            finish();
        }
    }

    public void signOutClick(){
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
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            switch (Integer.parseInt(data.getExtras().get("navTo").toString())) {
                case NAV_TO_ADMIN_HOME:
                    toAdminHome();
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