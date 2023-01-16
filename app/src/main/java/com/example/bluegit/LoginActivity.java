package com.example.bluegit;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.core.app.ActivityCompat;

import com.example.bluegit.model.User;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.Objects;


public class LoginActivity extends AppCompatActivity {

    public int SIGN_UP_REQUEST = 100;
    private final int GOOGLE_INTENT_REQUEST = 103;
    private final String[] permissionArrays = {
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.INTERNET,
            Manifest.permission.POST_NOTIFICATIONS,
            Manifest.permission.FOREGROUND_SERVICE};

    EditText tEmail;
    EditText tPassword;
    Button googleLoginBtn;
    public FirebaseAuth mAuth;
    public FirebaseUser currentUser;
    private FireStoreManager fireStoreManager;
    GoogleApiClient googleApiClient;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if((ActivityCompat.checkSelfPermission(this, permissionArrays[0]) != PackageManager.PERMISSION_GRANTED) ||
//                (ActivityCompat.checkSelfPermission(this, permissionArrays[1]) != PackageManager.PERMISSION_GRANTED) ||
                (ActivityCompat.checkSelfPermission(this, permissionArrays[2]) != PackageManager.PERMISSION_GRANTED) ||
//                (ActivityCompat.checkSelfPermission(this, permissionArrays[3]) != PackageManager.PERMISSION_GRANTED) ||
                (ActivityCompat.checkSelfPermission(this, permissionArrays[4]) != PackageManager.PERMISSION_GRANTED)){
            ActivityCompat.requestPermissions(this, permissionArrays, 99);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if(ActivityCompat.checkSelfPermission(this, permissionArrays[3]) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, permissionArrays, 99);
            }
        }

        tEmail = findViewById(R.id.login_email);
        tPassword = findViewById(R.id.login_password);
        googleLoginBtn = findViewById(R.id.google_login);

        TextView signUpText = findViewById(R.id.sign_up);
        String signUpTextTemp = signUpText.getText().toString();
        Spannable spannable = new SpannableString(signUpTextTemp);
        spannable.setSpan(new ForegroundColorSpan(Color.BLUE), signUpTextTemp.length() - 13, signUpTextTemp.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        signUpText.setText(spannable);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        fireStoreManager = new FireStoreManager(this, mAuth.getCurrentUser());
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, 1, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//                        Log.d("DEBUGGING", connectionResult.getErrorMessage());
                        Toast.makeText(LoginActivity.this, "Unable to connect to Goolge Client", Toast.LENGTH_SHORT).show();
                        recreate();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();

//        ConnectivityManager connMgr = getSystemService(ConnectivityManager.class);
//        android.net.NetworkInfo networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
//        if(!networkInfo.isConnected()){

        ConnectivityManager connectivityManager
                = (ConnectivityManager) LoginActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;
        if(activeNetworkInfo == null || !activeNetworkInfo.isConnected()){
            new AlertDialog.Builder(this)
                    .setTitle("Lost Internet Connection")
                    .setMessage("No internet connection. Please reconnect and log in again.")
                    .setPositiveButton("Understood", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }

                    })
                    .setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            finish();
                        }
                    })
                    .show();
        }else{
            tPassword.setText("");
            currentUser = null;
            currentUser = mAuth.getCurrentUser();
            if(currentUser != null){
                if(currentUser.getUid().equals("kJrA6qCYxISIJ2R2u74214UrcMA3")){
                    startActivity(new Intent(LoginActivity.this, AdminActivity.class));
                } else {
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                }
            }
        }
    }

    public void onRegisterClick(View view) {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivityForResult(intent, SIGN_UP_REQUEST);
    }

    public void onLoginClick(View view) {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);

        String email = tEmail.getText().toString();
        String password = tPassword.getText().toString();

        if(email.isEmpty()){
            tEmail.setError("Please enter your email");
        }else if(password.isEmpty()){
            tPassword.setError("Please enter your password");
        }else {
            signIn(email, password);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_CANCELED){
            if(requestCode == GOOGLE_INTENT_REQUEST) {googleLoginBtn.setEnabled(true);}
        }
        if(resultCode == RESULT_OK){
            if(requestCode == SIGN_UP_REQUEST){
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
            }
            if(requestCode == GOOGLE_INTENT_REQUEST) {
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                if(result.isSuccess()){
                    GoogleSignInAccount account = result.getSignInAccount();
                    String token = account.getIdToken();
                    String name = account.getDisplayName();
                    String email = account.getEmail();
                    String profileImage = RegisterActivity.DEFAULT_PIC_SRC;
                    if(account.getPhotoUrl() != null){
                        profileImage = account.getPhotoUrl().toString();
                    }


                    AuthCredential credential = GoogleAuthProvider.getCredential(token, null);

                    String finalProfileImage = profileImage;
                    mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                if(task.getResult().getAdditionalUserInfo().isNewUser()){
                                    FirebaseUser currentUser = mAuth.getCurrentUser();

                                    User user = new User(currentUser.getUid(), name, email, "", finalProfileImage);
                                    fireStoreManager.addNewUser(user, new FireStoreManager.AddUserDataCallBack() {
                                        @Override
                                        public void onSuccess() {
                                            Toast.makeText(LoginActivity.this, "Register Complete", Toast.LENGTH_SHORT).show();
                                            googleLoginBtn.setEnabled(true);
                                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                        }

                                        @Override
                                        public void onFailure(Exception e) {
                                            Toast.makeText(LoginActivity.this, "Unable to register to database", Toast.LENGTH_SHORT).show();
                                            googleLoginBtn.setEnabled(true);
                                            currentUser.delete();
                                            mAuth.signOut();
                                        }
                                    });
                                } else {
                                    googleLoginBtn.setEnabled(true);
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                }
                            }
                        }
                    });
                }
            }
        }
    }

    private void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this, "Login Successfully!", Toast.LENGTH_SHORT).show();
                        if(mAuth.getUid().equals("kJrA6qCYxISIJ2R2u74214UrcMA3")){
                            startActivity(new Intent(LoginActivity.this, AdminActivity.class));
                        }else {
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        }
                    } else {
                        try{
                            throw task.getException();

                        } catch(FirebaseAuthInvalidUserException e) {
                            tPassword.setError(e.getMessage());
                            tPassword.requestFocus();
                        } catch(Exception e){
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                }
            });
    }

    @Override
    public void onPause() {
        super.onPause();
        googleApiClient.stopAutoManage(this);
        googleApiClient.disconnect();
    }

    public void onContinueGoogleClick(View view) {
        googleLoginBtn.setEnabled(false);
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intent, GOOGLE_INTENT_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 99){
            if(grantResults.length > 0){
                for(int result : grantResults){

                    if(result != PackageManager.PERMISSION_GRANTED){
                        new AlertDialog.Builder(this)
                                .setTitle("Permissions Denied")
                                .setMessage("Following permissions are required for the application to work\n\n" +
                                        "INTERNET ACCESS\nIMAGE_ACCESS")
                                .setPositiveButton("Understood", new DialogInterface.OnClickListener()
                                {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                    }

                                })
                                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialog) {
                                        finish();
                                    }
                                })
                                .show();
                    }
                }
            }
        }else {
            finish();
        }
    }
}