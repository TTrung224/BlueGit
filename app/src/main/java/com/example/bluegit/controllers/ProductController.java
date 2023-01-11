package com.example.bluegit.controllers;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.bluegit.AddProductActivity;
import com.example.bluegit.model.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.net.URI;
import java.util.UUID;

public class ProductController {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference dbProducts = db.collection("products");
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();

    public boolean addProduct(String name, String description, String specification,
                           float price, Uri imgSrc, int quantity, String uid){

        final boolean[] status = {true};
        StorageReference riversRef = storageReference.child("users/"+uid+"/productsImg/"+
                UUID.randomUUID().toString() + ".png");
        UploadTask uploadTask = riversRef.putFile(imgSrc);

        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                riversRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        Uri img = task.getResult();
                        Log.d("debugging", "url: "+ img);
                        Product product = new Product(name, description, specification, price, img, quantity, uid);

                        dbProducts.add(product).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                status[0] = false;
                            }
                        });
                    }
                });
            }
        });

        return status[0];
    }
}
