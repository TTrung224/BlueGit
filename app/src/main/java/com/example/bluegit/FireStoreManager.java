package com.example.bluegit;


import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.bluegit.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class FireStoreManager {
    private final FirebaseFirestore db;
    private final Context ctx;

    public FireStoreManager(Context ctx) {
        this.ctx = ctx;
        db = FirebaseFirestore.getInstance();
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

    public void getUser(String uID, GetUserDataCallBack callBack){
        CollectionReference dbUsers = db.collection("users");
        User user = new User();

        dbUsers.document(uID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot doc = task.getResult();
                    if(doc.exists()){
                        User user = new User(
                                doc.getString("id"),
                                doc.getString("displayName"),
                                doc.getString("email"),
                                doc.getString("phoneNumber"),
                                Uri.parse(doc.getString("profileImageSrc")));
                        callBack.onSuccess(user);
                    }
                }else {
                    callBack.onFailure();
                }
            }
        });
    }
}

interface GetUserDataCallBack {
    void onSuccess(User result);
    void onFailure();
}
