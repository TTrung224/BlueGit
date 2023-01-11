package com.example.bluegit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.bluegit.model.Product;
import com.google.firebase.auth.FirebaseAuth;

public class AddProductActivity extends AppCompatActivity {
    EditText name;
    EditText description;
    EditText specification;
    EditText price;
    EditText quantity;
    Uri imgUri;

    FireStoreManager fireStoreManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        fireStoreManager = new FireStoreManager(this);
        name = findViewById(R.id.productNameAdd);
        description = findViewById(R.id.productDescriptionAdd);
        specification = findViewById(R.id.productSpecificationAdd);
        price = findViewById(R.id.productPriceAdd);
        quantity = findViewById(R.id.productQuantityAdd);

        ImageView addImage = findViewById(R.id.productImgAdd);
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
            Log.d("Uir", imgUri.toString());
        }
    }


    public void submit(View view) {
        String nameStr = name.getText().toString();
        String descriptionStr = description.getText().toString();
        String specificationStr = specification.getText().toString();
        int quantityInt = Integer.parseInt(quantity.getText().toString());
        float priceFloat = Float.parseFloat(price.getText().toString());
        String sellerId = FirebaseAuth.getInstance().getUid();

        if(!nameStr.equals("")
                && !descriptionStr.equals("")
                && !specificationStr.equals("")
                && !quantity.getText().toString().equals("")
                && !price.getText().toString().equals("")
                && imgUri != null) {

            Product product = new Product(nameStr, descriptionStr, specificationStr,
                    priceFloat, imgUri.toString(), quantityInt, sellerId);

            fireStoreManager.addProduct(product, new AddProductCallBack() {
                @Override
                public void onSuccess() {
                    Toast.makeText(AddProductActivity.this, "Successfully list your product!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Exception e) {
                    Toast.makeText(AddProductActivity.this, "Unable to list your product", Toast.LENGTH_SHORT).show();
                    Log.d("AddProductException", e.getMessage());
                }
            });
        }

    }
}