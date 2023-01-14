package com.example.bluegit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.bluegit.model.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.UUID;

public class AddProductActivity extends AppCompatActivity {
    EditText name;
    EditText description;
    EditText specification;
    EditText price;
    EditText quantity;
    Uri imgUri;
    Button productCreate;
    ProgressBar progressBar;
    ImageView addImage;

    FireStoreManager fireStoreManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        productCreate = findViewById(R.id.productCreateButton);
        progressBar = findViewById(R.id.progress_add_product);
        fireStoreManager = new FireStoreManager(this, FirebaseAuth.getInstance().getCurrentUser());
        name = findViewById(R.id.productNameAdd);
        description = findViewById(R.id.productDescriptionAdd);
        specification = findViewById(R.id.productSpecificationAdd);
        price = findViewById(R.id.productPriceAdd);
        quantity = findViewById(R.id.productQuantityAdd);

        addImage = findViewById(R.id.productImgAdd);
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 3);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK && data != null){
            imgUri = data.getData();
            ImageView addImage = findViewById(R.id.productImgAdd);
            addImage.setImageURI(imgUri);
        }
    }


    public void submit(View view) {

        String nameStr = name.getText().toString();
        String descriptionStr = description.getText().toString();
        String specificationStr = specification.getText().toString();
        DocumentReference sellerId = FirebaseFirestore.getInstance().document("users/"+FirebaseAuth.getInstance().getUid());

        if(nameStr.equals("")){
            name.setError("Please enter name.");
            name.requestFocus();
        } else if(descriptionStr.equals("")) {
            description.setError("Please enter description.");
            description.requestFocus();
        } else if(specificationStr.equals("")) {
            specification.setError("Please enter specification.");
            specification.requestFocus();
        } else if(quantity.getText().toString().equals("")) {
            quantity.setError("Please enter quantity.");
            quantity.requestFocus();
        } else if(price.getText().toString().equals("")) {
            price.setError("Please enter price.");
            price.requestFocus();
        } else if(imgUri == null){
            name.setError("Please select image.");
            name.requestFocus();
        }else{
            productCreate.setEnabled(false);
            productCreate.setAlpha(0.5f);
            progressBar.setVisibility(View.VISIBLE);
            int quantityInt = Integer.parseInt(quantity.getText().toString());
            int priceFloat = Integer.parseInt(price.getText().toString());
            Product product = new Product(UUID.randomUUID().toString(),nameStr, descriptionStr, specificationStr,
                    priceFloat, imgUri.toString(), quantityInt, sellerId);

            fireStoreManager.addProduct(product, new FireStoreManager.AddProductCallBack() {
                @Override
                public void onSuccess() {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(AddProductActivity.this, "Successfully list your product!", Toast.LENGTH_SHORT).show();
                    finish();
                }

                @Override
                public void onFailure(Exception e) {
                    progressBar.setVisibility(View.GONE);
                    productCreate.setAlpha(1f);
                    Toast.makeText(AddProductActivity.this, "Unable to list your product", Toast.LENGTH_SHORT).show();
                    productCreate.setEnabled(true);
                    Log.d("AddProductException", e.getMessage());
                }
            });
        }

    }

    public void goBack(View view) {
        finish();
    }
}
