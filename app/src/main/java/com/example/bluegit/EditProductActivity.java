package com.example.bluegit;

import static java.lang.Float.parseFloat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.bluegit.model.Order;
import com.example.bluegit.model.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class EditProductActivity extends AppCompatActivity {
    String productIdStr;
    FireStoreManager fireStoreManager;
    ImageView addImage;
    EditText addName;
    EditText addPrice;
    EditText addQuantity;
    EditText addDescription;
    EditText addSpecification;
    Uri selectedImg;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        progressBar = findViewById(R.id.progressBar);
        addImage = findViewById(R.id.productImgEdit);
        addName = findViewById(R.id.productNameEdit);
        addPrice = findViewById(R.id.productPriceEdit);
        addQuantity = findViewById(R.id.productQuantityEdit);
        addDescription = findViewById(R.id.productDescriptionEdit);
        addSpecification = findViewById(R.id.productConfigurationEdit);

        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 3);
            }
        });

        Intent intent = getIntent();
        productIdStr = intent.getStringExtra("productId");
        Log.d("TESTING", productIdStr);

        fireStoreManager.getProductById(productIdStr, new FireStoreManager.GetProductCallBack() {
            @Override
            public void onSuccess(Product product) {

                Picasso.get().load(product.getImageSource()).into(addImage);
                addName.setText(product.getProductName());
                addPrice.setText(Float.toString(product.getProductPrice()));
                addQuantity.setText(product.getQuantity());
                addDescription.setText(product.getDescription());
                addSpecification.setText(product.getSpecification());
            }

            @Override
            public void onFailure(Exception e) {
                Log.d("GetProductByIdProductActivity", e.getLocalizedMessage());
                finish();
            }
        });

        fireStoreManager = new FireStoreManager(this, FirebaseAuth.getInstance().getCurrentUser());

        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 3);
            }
        });

    }

    public void updateItemHandler(){
        String sellerId = FirebaseAuth.getInstance().getUid();
        assert sellerId != null;
        DocumentReference sellerRef = FirebaseFirestore.getInstance().
                collection("users").document(sellerId);

        progressBar.setVisibility(View.VISIBLE);

        String newImageSource = selectedImg.toString();
        String newName = addName.getText().toString();
        String newDescription = addDescription.getText().toString();
        String newSpecification = addSpecification.getText().toString();

        if(addName.getText().toString().equals("")){
            addName.setError("Please enter your new name.");
            addName.requestFocus();
        } else if (addPrice.getText().toString().equals("")) {
            addPrice.setError("Please enter your newPrice.");
            addPrice.requestFocus();
        } else if(addQuantity.getText().toString().equals("")) {
            addQuantity.setError("Please enter your password.");
            addQuantity.requestFocus();
        } else if(addDescription.getText().toString().equals("")){
            addDescription.setError("Please enter your new description.");
            addDescription.requestFocus();
        } else if(addSpecification.getText().toString().equals("")){
            addSpecification.setError("Please enter your new specification.");
            addSpecification.requestFocus();
        } else {
            int newPrice = Integer.parseInt(addPrice.getText().toString());
            int newQuantity = Integer.parseInt(addQuantity.getText().toString());

            Product productNew = new Product(productIdStr,newName,newDescription,
                    newSpecification,newPrice,newImageSource,newQuantity,sellerRef);

            fireStoreManager.addProduct(productNew, new FireStoreManager.AddProductCallBack() {
                @Override
                public void onSuccess() {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(EditProductActivity.this, "Successfully update your product", Toast.LENGTH_SHORT).show();
                    finish();
                }

                @Override
                public void onFailure(Exception e) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(EditProductActivity.this, "Unable to update your product", Toast.LENGTH_SHORT).show();

                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            selectedImg = data.getData();
            addImage.setImageURI(selectedImg);
        }
    }
}