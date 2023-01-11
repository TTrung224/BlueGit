package com.example.bluegit;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
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
import androidx.appcompat.app.AppCompatActivity;

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
    public int GOOGLE_SIGN_UP_REQUEST = 101;
    private final int GOOGLE_INTENT_REQUEST = 103;

    EditText tEmail;
    EditText tPassword;
    public FirebaseAuth mAuth;
    public FirebaseUser currentUser;
    private FireStoreManager fireStoreManager;
    GoogleApiClient googleApiClient;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tEmail = findViewById(R.id.login_email);
        tPassword = findViewById(R.id.login_password);

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
                        Log.d("DEBUGGING", connectionResult.getErrorMessage());
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        tPassword.setText("");
        currentUser = null;
        currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
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
                                    fireStoreManager.addNewUser(user, new AddUserDataCallBack() {
                                        @Override
                                        public void onSuccess() {
                                            Toast.makeText(LoginActivity.this, "Register Complete", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                        }

                                        @Override
                                        public void onFailure(Exception e) {
                                            Toast.makeText(LoginActivity.this, "Unable to register to database", Toast.LENGTH_SHORT).show();
                                            currentUser.delete();
                                            mAuth.signOut();
                                        }
                                    });
                                } else {
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
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
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

        Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intent, GOOGLE_INTENT_REQUEST);
    }
}