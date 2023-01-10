package com.example.bluegit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.bluegit.adapters.ProductDisplayAdapter;
import com.example.bluegit.model.Product;
import com.example.bluegit.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<Product> products = new ArrayList<>();
    FirebaseAuth firebaseAuth;
    ImageView profilePic;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseAuth = FirebaseAuth.getInstance();
        profilePic = findViewById(R.id.main_profile_pic);

        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if(currentUser != null){
            currentUser.reload();
            user = new User(currentUser.getDisplayName(), currentUser.getEmail(), currentUser.getPhotoUrl());
            String welcomeMessage = "WELCOME! " + user.getDisplayName();
            Toast.makeText(this, welcomeMessage, Toast.LENGTH_SHORT).show();
        }else{
            finish();
        }

        Picasso.get().load(user.getProfileImageSrc()).into(profilePic);

        Product p1 = new Product("Very Nice Hat", 10000, "https://i.imgur.com/vve6kCY.jpeg");
        Product p2 = new Product("Green T-Shirt", 50000, "https://i.imgur.com/M8lSriJ.jpeg");

        products.add(p1);
        products.add(p2);
        products.add(new Product("Pink dress with Strawberry on it", 100000, "https://cdn.shopify.com/s/files/1/0011/9783/4252/products/20_375a8763-f5d7-4184-a352-4523ef713733.jpg?v=1576267132"));
        products.add(new Product("It's a box of strawberries", 80000, "https://www.shutterstock.com/image-photo/box-strawberries-260nw-733675327.jpg"));
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(firebaseAuth.getCurrentUser() == null){
            finish();
        }

        RecyclerView productDisplay = findViewById(R.id.items_display);
        ProductDisplayAdapter adapter = new ProductDisplayAdapter(products, this);
        productDisplay.setAdapter(adapter);
        productDisplay.setLayoutManager(new GridLayoutManager(this, 2));
    }

    public void onSignOutClick(View view) {
        firebaseAuth.signOut();
        finish();
    }
}