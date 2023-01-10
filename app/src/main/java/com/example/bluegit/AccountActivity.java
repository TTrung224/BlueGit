package com.example.bluegit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.bluegit.model.Account;

public class AccountActivity extends AppCompatActivity {
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
        nameTV = (TextView) findViewById(R.id.nameTextView);
        nameEdit = (EditText) findViewById(R.id.nameEditText);
        ageEdit = (EditText) findViewById(R.id.ageEditText);
        phoneNumberEdit = (EditText) findViewById(R.id.phoneEditText);
        addressNew = (EditText) findViewById(R.id.addNewAddress);
        updateBtn = (Button) findViewById(R.id.updateBtn);

        String uId = getIntent().getStringExtra("userId");
//        account = getAccountDb(uId);
        nameTV.setText(account.getName());
        nameEdit.setText(account.getName());
        ageEdit.setText(account.getAge());
        phoneNumberEdit.setText(account.getPhone());
        addressNew.setText(account.getAddress());



    }
    public void update(){
        account.setName(nameTV.getText().toString());
        account.setName(nameEdit.getText().toString());
        account.setAge(Integer.parseInt(ageEdit.getText().toString()));
        account.setPhone(phoneNumberEdit.getText().toString());
        account.setAddress(addressNew.getText().toString());

//        updateAccountDb(account);
    }



}