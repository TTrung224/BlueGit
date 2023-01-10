package com.example.bluegit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.bluegit.model.Account;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class AccountActivity extends AppCompatActivity {
    private String uId;

    TextView nameTV;
    EditText nameEdit;
    EditText ageEdit;
    EditText phoneNumberEdit;
    EditText addressNew;
    Button updateBtn;
    Account account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        uId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        nameTV = (TextView) findViewById(R.id.nameTextView);
        nameEdit = (EditText) findViewById(R.id.nameEditText);
        ageEdit = (EditText) findViewById(R.id.ageEditText);
        phoneNumberEdit = (EditText) findViewById(R.id.phoneEditText);
        addressNew = (EditText) findViewById(R.id.addNewAddress);
        updateBtn = (Button) findViewById(R.id.updateBtn);

        String uId = getIntent().getStringExtra("userId");
//        account = getAccountDb(uId);
//        nameTV.setText(account.getName());
//        nameEdit.setText(account.getName());
//        ageEdit.setText(account.getAge());
//        phoneNumberEdit.setText(account.getPhone());
//        addressNew.setText(account.getAddress());



    }
    public void update(){
        account.setName(nameTV.getText().toString());
        account.setName(nameEdit.getText().toString());
        account.setAge(Integer.parseInt(ageEdit.getText().toString()));
        account.setPhone(phoneNumberEdit.getText().toString());
        account.setAddress(addressNew.getText().toString());

//        updateAccountDb(account);
    }

    public void goBack(View view) {
        setResult(RESULT_OK);
        finish();
    }

    public void toBusiness(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("navTo", MainActivity.NAV_TO_BUSINESS);
        setResult(RESULT_OK, intent);
        finish();
        overridePendingTransition(0, 0);
    }

    public void toOrders(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("navTo", MainActivity.NAV_TO_ORDERS);
        setResult(RESULT_OK, intent);
        finish();
        overridePendingTransition(0, 0);
    }

    public void toHome(View view) {
        setResult(RESULT_OK);
        finish();
        overridePendingTransition(0, 0);
    }

    public void toCart(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("navTo", MainActivity.NAV_TO_CART);
        setResult(RESULT_OK, intent);
        finish();
        overridePendingTransition(0, 0);
    }

    public void refresh(View view) {
        this.recreate();
    }


}