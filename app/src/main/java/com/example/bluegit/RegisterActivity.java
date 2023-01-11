package com.example.bluegit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.bluegit.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class RegisterActivity extends AppCompatActivity {

    public static String DEFAULT_PIC_SRC = "https://firebasestorage.googleapis.com/v0/b/bluegit-c8e08.appspot.com/o/user.png?alt=media&token=53098173-427e-4a05-beb6-d1c62ba9df6f";

    EditText tUserName;
    EditText tEmail;
    EditText tPassword;
    EditText tPhone;
    ImageView profilePicture;
    ProgressBar progressBar;

    FirebaseAuth mAuth;
    FirebaseStorage storage;
    StorageReference storageReference;
    FireStoreManager fireStoreManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        fireStoreManager = new FireStoreManager(this);

        tUserName = findViewById(R.id.register_name);
        tEmail = findViewById(R.id.register_email);
        tPassword = findViewById(R.id.register_password);
        tPhone = findViewById(R.id.register_phone);
        profilePicture = findViewById(R.id.register_picture);

        progressBar = findViewById(R.id.register_progress);
        progressBar.setVisibility(View.GONE);
    }

    public void onRegisterSubmitClick(View view) {
        String name;
        String email;
        String password;
        String phone;

        name = tUserName.getText().toString();
        email = tEmail.getText().toString();
        password = tPassword.getText().toString();
        phone = tPhone.getText().toString();

        if(name.equals("")){
            tUserName.setError("Please enter your name.");
            tUserName.requestFocus();
        } else if (email.equals("")) {
            tEmail.setError("Please enter your email.");
            tEmail.requestFocus();
        } else if(password.equals("")) {
            tPassword.setError("Please enter your password.");
            tPassword.requestFocus();
        } else if(phone.equals("")){
            tPhone.setError("Please enter your phone number.");
            tPhone.requestFocus();
        } else {
            registerAccount(email, password, name, phone);
        }

    }

    private void registerAccount(String email, String password, String name, String phone){
        progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            String id = user.getUid();
                            User userDat = new User(id, name, email, phone, DEFAULT_PIC_SRC);
                            storageReference = storage.getReference();
                            StorageReference pictureRef = storageReference.child("/users/" + user.getUid() + "/profilePicture.png/");

                            profilePicture.setDrawingCacheEnabled(true);
                            Bitmap bitmap = ((BitmapDrawable) profilePicture.getDrawable()).getBitmap();
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                            byte[] imageData = baos.toByteArray();
                            UploadTask uploadTask = pictureRef.putBytes(imageData);

                            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                    pictureRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Uri> task) {
                                            if(task.isSuccessful()){
                                                userDat.setProfileImageSrc(task.getResult().toString());
                                            }

                                            user.updateProfile(new UserProfileChangeRequest.Builder()
                                                    .setDisplayName(userDat.getDisplayName())
                                                    .setPhotoUri(Uri.parse(userDat.getProfileImageSrc()))
                                                    .build()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    progressBar.setVisibility(View.GONE);
                                                    fireStoreManager.addNewUser(userDat, new AddUserDataCallBack() {
                                                        @Override
                                                        public void onSuccess() {
                                                            Toast.makeText(RegisterActivity.this, "Register Complete", Toast.LENGTH_SHORT).show();
                                                            Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                                                            setResult(RESULT_OK, i);
                                                            finish();
                                                        }
                                                        @Override
                                                        public void onFailure(Exception e) {
                                                            Toast.makeText(RegisterActivity.this, "Unable to register to database.", Toast.LENGTH_SHORT).show();
                                                            Log.d("DBException", e.getLocalizedMessage());
                                                            pictureRef.delete();
                                                            user.delete();
                                                            mAuth.signOut();
                                                        }
                                                    });

                                                }
                                            });
                                        }
                                    });

                                }
                            });

                        } else {
                            progressBar.setVisibility(View.GONE);
                            try{
                                throw task.getException();
                            } catch(FirebaseAuthWeakPasswordException e){
                                tPassword.setError(e.getReason());
                                tPassword.requestFocus();
                            } catch (FirebaseAuthUserCollisionException e) {
                                tEmail.setError("Email already existed.");
                                tEmail.requestFocus();
                            } catch (FirebaseAuthInvalidCredentialsException e){
                                tEmail.setError("Invalid email address.");
                                tEmail.requestFocus();
                            } catch (Exception e){
                                Toast.makeText(RegisterActivity.this, "Unable to create this account.", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }
                });
    }

    public void onChangeProfileClick(View view) {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(i, "Select your Profile Picture"), 201);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 201){
            if(resultCode == RESULT_OK){
                Uri selectedImage = data.getData();
                if(selectedImage != null){
                    profilePicture.setImageURI(selectedImage);
                }
            }
        }
    }

}