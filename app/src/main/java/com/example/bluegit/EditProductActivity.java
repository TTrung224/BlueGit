package com.example.bluegit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

import com.example.bluegit.model.Order;
import com.example.bluegit.model.Product;
import com.squareup.picasso.Picasso;

public class EditProductActivity extends AppCompatActivity {
    Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        ImageView addImage = findViewById(R.id.productImgEdit);

        String productId = getIntent().getStringExtra("productId");
//        product = productController.getById(productId);

        Picasso.get().load(product.getImageSource()).into(addImage);
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
        if (resultCode == RESULT_OK && data != null) {
            Uri selectedImg = data.getData();
            ImageView addImage = findViewById(R.id.productImgAdd);
            addImage.setImageURI(selectedImg);
        }
    }
}