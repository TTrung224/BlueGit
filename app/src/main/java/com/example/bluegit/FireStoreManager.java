package com.example.bluegit;


import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.bluegit.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class FireStoreManager {
    private final FirebaseFirestore db;
    private final Context ctx;

    public FireStoreManager(Context ctx, FirebaseFirestore db) {
        this.ctx = ctx;
        this.db = db;
    }

    public void addNewUser(User user){
        CollectionReference dbUsers = db.collection("users");


        dbUsers.document(user.getId()).set(user).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ctx, "Unable to add user to database.", Toast.LENGTH_SHORT).show();
                        Toast.makeText(ctx, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
        });

    }
}
