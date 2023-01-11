package com.example.bluegit;


import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.bluegit.model.Product;
import com.example.bluegit.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.UUID;

public class FireStoreManager {
    private final FirebaseFirestore db;
    private final Context ctx;

    public FireStoreManager(Context ctx) {
        this.ctx = ctx;
        db = FirebaseFirestore.getInstance();
    }

    public void addNewUser(User user, AddUserDataCallBack callBack){
        CollectionReference dbUsers = db.collection("users");


        dbUsers.document(user.getId()).set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        callBack.onSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callBack.onFailure(e);
                    }
        });

    }

    public void getUser(String uID, GetUserDataCallBack callBack){
        CollectionReference dbUsers = db.collection("users");
        User user = new User();

        dbUsers.document(uID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot doc = task.getResult();
                    if(doc.exists()){
                        User user = doc.toObject(User.class);
                        callBack.onSuccess(user);
                    }
                }else {
                    callBack.onFailure(task.getException());
                }
            }
        });
    }

    public void addProduct(Product product, AddProductCallBack callBack){
        CollectionReference dbProducts = db.collection("products");
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference riversRef = storageReference.child("users/"+product.getSellerId()+"/productsImg/"+
                UUID.randomUUID().toString() + ".png");


        // Register observers to listen for when the download is done or if it fails
        UploadTask uploadTask = riversRef.putFile(Uri.parse(product.getImageSource()));
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                riversRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful()){
                            Uri img = task.getResult();
                            Log.d("debugging", "url: "+ img);

                            dbProducts.add(product)
                                    .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentReference> task) {
                                            if(task.isSuccessful()){
                                                callBack.onSuccess();
                                            }else{
                                                riversRef.delete();
                                                callBack.onFailure(task.getException());
                                            }
                                        }
                                    });
                        }else {
                            callBack.onFailure(task.getException());
                        }
                    }
                });
            }
        });

    }

    public void getAllProducts(GetAllProductsCallBack callBack) {
        CollectionReference dbProducts = db.collection("products");
        dbProducts.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    ArrayList<Product> products = new ArrayList<>();
                    try{
                        for(QueryDocumentSnapshot snapshots : task.getResult()){
                            Product product = snapshots.toObject(Product.class);
                            if(!product.isDisabled()){
                                products.add(product);
                            }
                        }
                    }catch (Exception e){
                        callBack.onFailure(e);
                    }

                    callBack.onSuccess(products);
                }else {
                    callBack.onFailure(task.getException());
                }
            }
        });


    }
}

interface GetAllProductsCallBack {
    void onSuccess(ArrayList<Product> result);
    void onFailure(Exception e);
}

interface GetUserDataCallBack {
    void onSuccess(User result);
    void onFailure(Exception e);
}

interface AddUserDataCallBack {
    void onSuccess();
    void onFailure(Exception e);
}

interface AddProductCallBack {
    void onSuccess();
    void onFailure(Exception e);
}
