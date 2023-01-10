package com.example.bluegit;

import android.content.Intent;
import android.graphics.Color;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.Objects;


public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    EditText tEmail;
    EditText tPassword;
    FirebaseUser currentUser;

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
    }

    @Override
    protected void onStart() {
        super.onStart();
        tPassword.setText("");
        currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Log.d("User", FirebaseAuth.getInstance().getCurrentUser().getUid());
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        }
    }

    public void onRegisterClick(View view) {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivityForResult(intent, 100);
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
        if(requestCode == 100){
            if(resultCode == RESULT_OK){
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
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
}