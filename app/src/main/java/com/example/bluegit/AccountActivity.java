package com.example.bluegit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bluegit.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class AccountActivity extends AppCompatActivity{
    private String uId;
    private String email;

    TextView nameTV;
    EditText nameEdit;
    EditText phoneNumberEdit;
    ImageView profileImgView;
    Button updateBtn;
    Spinner shippingInfoSpin;
    ConstraintLayout addAddressForm;
    Uri imgUri;
    String currentImg;

    ProgressBar progressBar;
    List<String> addressList;

    ArrayAdapter<String> adapter;

    FireStoreManager fireStoreManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        fireStoreManager = new FireStoreManager(this, FirebaseAuth.getInstance().getCurrentUser());

        uId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        email = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        nameTV = (TextView) findViewById(R.id.nameTextView);
        nameEdit = (EditText) findViewById(R.id.nameEditText);
        phoneNumberEdit = (EditText) findViewById(R.id.phoneEditText);
        profileImgView = (ImageView) findViewById(R.id.profileImg);

        updateBtn = (Button) findViewById(R.id.updateBtn);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        shippingInfoSpin = findViewById(R.id.shippingAddressSpin);

        addAddressForm = findViewById(R.id.addShippingInfoForm);

        fireStoreManager.getUserById(uId, new FireStoreManager.GetUserDataCallBack() {
            @Override
            public void onSuccess(User result) {
                nameEdit.setText(result.getDisplayName());
                nameTV.setText(result.getDisplayName());
                phoneNumberEdit.setText(result.getPhoneNumber());
                currentImg = result.getProfileImageSrc();
                Picasso.get().load(result.getProfileImageSrc()).into(profileImgView);
                addressList = result.getAddress();
                if (addressList == null) {
                    Log.d("TESTING", "addressList is null");
                    List<String> emptyAddress= new ArrayList<>();
                    addressList = new ArrayList<>();
                    addressList.add("There is not any shipping information, Let's add one!");
                    adapter = new ArrayAdapter<String>(AccountActivity.this,
                            android.R.layout.simple_spinner_item,
                            addressList);

                    adapter.setDropDownViewResource(android.R.layout.simple_expandable_list_item_1);
                    shippingInfoSpin.setAdapter(adapter);
                } else {
                    Log.d("testing", result.getAddress().toString());
                    addressList = result.getAddress();
                    adapter = new ArrayAdapter<String>(AccountActivity.this,
                            android.R.layout.simple_spinner_item,
                            addressList);

                    adapter.setDropDownViewResource(android.R.layout.simple_expandable_list_item_1);
                    shippingInfoSpin.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Exception e) {
                Log.d("GetUserDataException", e.getMessage());
                Toast.makeText(AccountActivity.this, "Fail to load your profile, try again", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    public void addAddress(View view) {
        addAddressForm.setVisibility(View.VISIBLE);
    }

    public void closeForm(View view) {
        addAddressForm.setVisibility(View.GONE);
    }

    public void submitAdd(View view) {
        EditText nameEdit = findViewById(R.id.receiverName);
        EditText phoneEdit = findViewById(R.id.receiverPhone);
        EditText addressEdit = findViewById(R.id.address);

        String name = nameEdit.getText().toString();
        String phone = phoneEdit.getText().toString();
        String address = addressEdit.getText().toString();

        if(name.equals("")){
            nameEdit.setError("Please enter name.");
            nameEdit.requestFocus();
        } else if (phone.equals("")) {
            phoneEdit.setError("Please enter phone number.");
            phoneEdit.requestFocus();
        } else if (address.equals("")) {
            addressEdit.setError("Please enter address.");
            addressEdit.requestFocus();
        } else {

            String info = (name + " | " + phone + " | " + address);
            FirebaseFirestore.getInstance()
                .collection("users")
                .document(uId).update("address", FieldValue.arrayUnion(info))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(AccountActivity.this, "Add info successfully", Toast.LENGTH_SHORT).show();
                        addAddressForm.setVisibility(View.GONE);
                        if(addressList.get(0).equals("There is not any shipping information, Let's add one!")){
                            addressList.clear();
                        }
                        addressList.add(info);
//                        adapter = new ArrayAdapter<String>(AccountActivity.this,
//                                android.R.layout.simple_spinner_item,
//                                addressList);
                        shippingInfoSpin.setAdapter(adapter);
//                        refresh(shippingInfoSpin);
                    } else {
                        Toast.makeText(AccountActivity.this, "Fail to add, please try again later", Toast.LENGTH_SHORT).show();
                        addAddressForm.setVisibility(View.GONE);
                    }
                }
            });
        }
    }

    public void editImg(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 3);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK && data != null){
            imgUri = data.getData();
            profileImgView.setImageURI(imgUri);
        }
    }

    public void updateAccount(View view) {
        String name = nameEdit.getText().toString();
        String phoneNumber = phoneNumberEdit.getText().toString();

        if (name.equals("")) {
            nameEdit.setError("Please enter name.");
            nameEdit.requestFocus();
        } else if (phoneNumber.equals("")) {
            phoneNumberEdit.setError("Please enter phone number.");
            phoneNumberEdit.requestFocus();
        } else {
            progressBar.setVisibility(View.VISIBLE);
            User user = new User(uId, name, email, phoneNumber, currentImg);
            user.setAddress(addressList);
            fireStoreManager.updateUser(user, imgUri, new FireStoreManager.UpdateUserDataCallBack() {
                @Override
                public void onSuccess() {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(AccountActivity.this,
                            "Update profile successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }

                @Override
                public void onFailure(Exception e) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(AccountActivity.this,
                            "Fail to update profile, try again later", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        }
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