package com.example.bluegit;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bluegit.model.Order;
import com.example.bluegit.model.Product;
import com.example.bluegit.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import org.checkerframework.checker.units.qual.A;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FireStoreManager {
    private FirebaseUser currentUser;
    private final FirebaseFirestore db;
    private final Context ctx;

    public FireStoreManager(Context ctx, FirebaseUser currentUser) {
        this.ctx = ctx;
        this.db = FirebaseFirestore.getInstance();
        this.currentUser = currentUser;
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

    public void updateUser(User user, Uri img, UpdateUserDataCallBack callBack) {
        CollectionReference dbUsers = db.collection("users");

        if (img != null) {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference();
            StorageReference pictureRef = storageReference.child("/users/" + user.getId() + "/profilePicture.png/");

            UploadTask uploadTask = pictureRef.putFile(img);
            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                        pictureRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if (task.isSuccessful()) {
                                    user.setProfileImageSrc(task.getResult().toString());
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
                                } else {
                                    callBack.onFailure(task.getException());
                                }
                            }
                        });
                    } else {
                        callBack.onFailure(task.getException());
                    }
                }
            });
        } else {
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
    }

    public void getUserById(String uID, GetUserDataCallBack callBack){
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

    public void getCurrentUser(GetUserDataCallBack callBack){
        CollectionReference dbUsers = db.collection("users");
        User user = new User();

        dbUsers.document(currentUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot doc = task.getResult();
                    if(doc.exists()){
                        User user = doc.toObject(User.class);
                        callBack.onSuccess(user);
                    }else {
                        callBack.onFailure(new NoUserInDatabaseException("No User In Database   "));
                    }
                }else {
                    callBack.onFailure(task.getException());
                }
            }
        });
    }

    public void getProductsFromString(String searchStr, GetProductsCallBack callBack){
        CollectionReference dbProducts = db.collection("products");
        String[] searchWords = searchStr.toLowerCase().trim().split(" ");
        dbProducts.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    ArrayList<Product> products = new ArrayList<>();
                    try{
                        for(QueryDocumentSnapshot snapshot : task.getResult()){
                            Product product = snapshot.toObject(Product.class);
                            if(!product.isDisabled() && !product.getSellerId().getId().equals(currentUser.getUid())){
                                if(searchWords.length > 0){
                                    for(String word : searchWords){
                                        if(product.getProductName().toLowerCase().contains(word)){
                                            products.add(product);
                                            break;
                                        }
                                    }
                                }else {
                                    products.add(product);
                                }

                            }
                        }
                        callBack.onSuccess(products);
                    }catch (Exception e){
                        callBack.onFailure(e);
                    }
                }else {callBack.onFailure(task.getException());}
            }
        });
    }

    public void addProduct(Product product, AddProductCallBack callBack){
        DocumentReference dbProducts = db.collection("products").document(product.getProductId());
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference riversRef = storageReference.child("users/"+product.getSellerId().getId()+"/productsImg/"+
                product.getProductId() + ".png");

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
                            product.setImageSource(img.toString());

                            dbProducts.set(product).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
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

    public void getProductById(String id, GetProductCallBack callBack){
        DocumentReference dbProduct = db.collection("products").document(id);
        dbProduct.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    try{
                        Product product = task.getResult().toObject(Product.class);
                        callBack.onSuccess(product);
                    } catch (Exception e){
                        callBack.onFailure(e);
                    }
                } else {
                    callBack.onFailure(task.getException());
                }
            }
        });
    }

    public void getUserProducts(GetProductsCallBack callBack){
        CollectionReference dbProducts = db.collection("products");
        dbProducts.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    ArrayList<Product> products = new ArrayList<>();
                    try{
                        for(QueryDocumentSnapshot snapshot : task.getResult()){
                            Product product = snapshot.toObject(Product.class);
                            if(product.getSellerId().getId().equals(currentUser.getUid()) && !product.isDisabled()){
                                products.add(product);
                            }
                        }
                        callBack.onSuccess(products);
                    }catch (Exception e){
                        callBack.onFailure(e);
                    }
                }else {callBack.onFailure(task.getException());}
            }
        });
    }

    public void getAllProducts(GetProductsCallBack callBack) {
        CollectionReference dbProducts = db.collection("products");
        dbProducts.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    ArrayList<Product> products = new ArrayList<>();
                    try{
                        for(QueryDocumentSnapshot snapshots : task.getResult()){
                            Product product = snapshots.toObject(Product.class);
                            if(!product.isDisabled() && !product.getSellerId().getId().equals(currentUser.getUid())){
                                products.add(product);
                            }
                        }
                        callBack.onSuccess(products);
                    }catch (Exception e){
                        callBack.onFailure(e);
                    }
                }else {
                    callBack.onFailure(task.getException());
                }
            }
        });
    }

    public void getProductsFromCart(Map<String, Object> cart, GetProductsFromCartCallBack callBack){
        CollectionReference dbProducts = db.collection("products");
        dbProducts.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    Map<Product, Integer> result = new HashMap<>();
                    try{
                        for(QueryDocumentSnapshot snapshots : task.getResult()){
                            Product product = snapshots.toObject(Product.class);
                            if(cart.containsKey(product.getProductId()) && !product.isDisabled() && product.getQuantity() > 0){
                                result.put(product, ((Long)cart.get(product.getProductId())).intValue() );
                            }
                        }
                        callBack.onSuccess(result);
                    }catch (Exception e){
                        callBack.onFailure(e);
                    }
                }else {
                    callBack.onFailure(task.getException());
                }
            }
        });

//        ArrayList<DocumentReference> sellerRefs = new ArrayList<>();
//
//        Map<String, Order> doc = new HashMap<>();
//        for(Product product : cart.keySet()){
//            if(!sellerRefs.contains(product.getSellerId())){
//                sellerRefs.add(product.getSellerId());
//            }
//        }
//
//        WriteBatch batch = db.batch();
//        int netTotal = 0;
//        for(DocumentReference seller : sellerRefs){
//            String id = UUID.randomUUID().toString();
//            ArrayList<String> productIDs = new ArrayList<>();
//            ArrayList<Integer> amount = new ArrayList<>();
//            int total = 0;
//
//            for(Map.Entry<Product, Integer> entry : cart.entrySet()){
//                if(entry.getKey().getSellerId().equals(seller)){
//                    productIDs.add(entry.getKey().getProductId());
//                    amount.add(entry.getValue());
//                    total += entry.getKey().getProductPrice() * entry.getValue();
//                }
//            }
//            netTotal += total;
//            Order order = new Order(id, productIDs, amount, total,
//                    "", dbUsers.document(currentUser.getUid()), seller);
//
//            batch.set(dbOrders.document(id), order);
//            batch.update(seller, "orderRef", FieldValue.arrayUnion(dbOrders.document(id)));
//
//        }
//
//        int finalNetTotal = netTotal;
//        getCurrentUser(new GetUserDataCallBack() {
//            @Override
//            public void onSuccess(User result) {
//                if(result.getBalance() < finalNetTotal){
//                    // callBack.onFailure(new InsufficientBalanceException("Insufficient Balance"));
//                }else{
//                    batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//                            if (task.isSuccessful()){
//                                callBack.onSuccess();
//                            }else{
//                                callBack.onFailure(task.getException());
//                            }
//                        }
//                    });
//                }
//            }
//            @Override
//            public void onFailure(Exception e) {
//                callBack.onFailure(e);
//            }
//        });



    }

    public void addToCart(Map<String, Integer> products, CartCallBack callBack){
        DocumentReference dbUserCart = db.collection("users").document(currentUser.getUid())
                .collection("cart").document("1");

        dbUserCart.set(products, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    callBack.onSuccess();
                }else{callBack.onFailure(task.getException());}
            }
        });
    }

    public void deleteItemFromCart(String itemId, CartCallBack callBack){
        DocumentReference dbUserCart = db.collection("users").document(currentUser.getUid())
                .collection("cart").document("1");

        Map<String, Object> update = new HashMap<>();
        update.put(itemId, FieldValue.delete());
        dbUserCart.update(update).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    callBack.onSuccess();
                }else{callBack.onFailure(task.getException());}
            }
        });
    }

    public void getCart(GetCartCallBack callBack){
        DocumentReference dbUserCart = db.collection("users").document(currentUser.getUid())
                .collection("cart").document("1");
        dbUserCart.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    callBack.onSuccess(task.getResult().getData());
                }else {
                    callBack.onFailure(task.getException());
                }
            }
        });
    }

    public void emptyCart(){
        DocumentReference dbUserCart = db.collection("users").document(currentUser.getUid())
                .collection("cart").document("1");
        dbUserCart.delete();
    }

    public void addOrders(Map<Product, Integer> cart, AddOrdersCallBack callBack){
        CollectionReference dbOrders = db.collection("orders");
        CollectionReference dbUsers = db.collection("users");
        CollectionReference dbProducts = db.collection("products");

        db.runTransaction(new Transaction.Function<Void>() {
            @Nullable
            @Override
            public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {

                ArrayList<DocumentReference> sellerRefs = new ArrayList<>();
                int netTotal = 0;

                for(Map.Entry<Product, Integer> entry : cart.entrySet()){

                    // Check if product has stock
                    DocumentSnapshot snapshot = transaction.get(dbProducts.document(entry.getKey().getProductId()));
                    Long realQuant = snapshot.getLong("quantity");
                    if(realQuant < entry.getValue()){
                        throw new OutOfStockException("Run out of stock for " + snapshot.getString("productName"), FirebaseFirestoreException.Code.ABORTED);
                    }

                    netTotal += snapshot.getLong("productPrice");
                    if(!sellerRefs.contains(entry.getKey().getSellerId())){
                        sellerRefs.add(entry.getKey().getSellerId());
                    }
                }
                int finalNetTotal = netTotal;

                // Check if user has sufficient fund
                Long currentBalance = transaction.get(dbUsers.document(currentUser.getUid())).getLong("balance");
                if(currentBalance < finalNetTotal){
                    throw new InsufficientBalanceException("Insufficient Balance", FirebaseFirestoreException.Code.ABORTED);
                }

                transaction.update(dbUsers.document(currentUser.getUid()), "balance", FieldValue.increment(-netTotal));

                for(DocumentReference seller : sellerRefs){
                    String id = UUID.randomUUID().toString();
                    ArrayList<String> productIDs = new ArrayList<>();
                    ArrayList<Integer> amount = new ArrayList<>();
                    int total = 0;

                    for(Map.Entry<Product, Integer> entry : cart.entrySet()){
                        if(entry.getKey().getSellerId().equals(seller)){
                            productIDs.add(entry.getKey().getProductId());
                            amount.add(entry.getValue());
                            total += entry.getKey().getProductPrice() * entry.getValue();
                            transaction.update(dbProducts.document(entry.getKey().getProductId()),
                                    "quantity", FieldValue.increment(-entry.getValue()));
                        }
                    }

                    Order order = new Order(id, productIDs, amount, total,
                            "", dbUsers.document(currentUser.getUid()), seller);

                    transaction.set(dbOrders.document(id), order);
                    transaction.update(seller, "balance", FieldValue.increment(total));
                    transaction.update(seller, "orderRef", FieldValue.arrayUnion(dbOrders.document(id)));
                }
                return null;
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                callBack.onSuccess();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callBack.onFailure(e);
            }
        });
    }

    public interface AddOrdersCallBack{
        void onSuccess();
        void onFailure(Exception e);
    }

    public interface GetProductsCallBack {
        void onSuccess(ArrayList<Product> result);
        void onFailure(Exception e);
    }

    public interface GetProductsFromCartCallBack{
        void onSuccess(Map<Product, Integer> result);
        void onFailure(Exception e);
    }

    public interface GetProductCallBack{
        void onSuccess(Product product);
        void onFailure(Exception e);
    }

    public interface GetUserDataCallBack {
        void onSuccess(User result);
        void onFailure(Exception e);
    }

    public interface AddUserDataCallBack {
        void onSuccess();
        void onFailure(Exception e);
    }

    public interface CartCallBack {
        void onSuccess();
        void onFailure(Exception e);
    }

    public interface GetCartCallBack {
        void onSuccess(Map<String,Object> cartResult);
        void onFailure(Exception e);
    }

    interface UpdateUserDataCallBack{
        void onSuccess();
        void onFailure(Exception e);
    }

    public interface AddProductCallBack {
        void onSuccess();
        void onFailure(Exception e);
    }
}

class NoUserInDatabaseException extends Exception{
    public NoUserInDatabaseException(String messageError){
        super(messageError);
    }
}

class InsufficientBalanceException extends FirebaseFirestoreException{
    public InsufficientBalanceException(String messageError, Code code){ super(messageError, code); }
}

class OutOfStockException extends FirebaseFirestoreException{
    public OutOfStockException(String messageError, Code code){ super(messageError, code); }
}



