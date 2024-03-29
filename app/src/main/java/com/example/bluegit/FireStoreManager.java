package com.example.bluegit;


import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bluegit.model.Message;
import com.example.bluegit.model.Order;
import com.example.bluegit.model.Product;
import com.example.bluegit.model.User;
import com.example.bluegit.model.Voucher;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class FireStoreManager {
    private final String ADMIN_ID = "kJrA6qCYxISIJ2R2u74214UrcMA3";
    int SELLER_SHARE = 80;
    int ADMIN_SHARE = 100 - SELLER_SHARE;

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
        Map<String, Object> data = new HashMap<>();
        data.put("displayName", user.getDisplayName());
        data.put("phoneNumber", user.getPhoneNumber());

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
                                    data.put("profileImageSrc", user.getProfileImageSrc());

                                    dbUsers.document(user.getId()).update(data)
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
            dbUsers.document(user.getId()).update(data)
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

    public void getAllUser(GetAllUserCallBack callBack){
        CollectionReference dbUsers = db.collection("users");

        dbUsers.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    ArrayList<User> users = new ArrayList<>(task.getResult().toObjects(User.class));
                    callBack.onSuccess(users);
                } else {
                    callBack.onFailure(task.getException());
                }
            }
        });
    }

    public void getAllProductForAdmin(getAllProductForAdminCallBack callBack){
        CollectionReference dbProducts = db.collection("products");

        dbProducts.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    ArrayList<Product> products = new ArrayList<>(task.getResult().toObjects(Product.class));
                    callBack.onSuccess(products);
                } else {
                    callBack.onFailure(task.getException());
                }
            }
        });
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

    public void getAllVoucher(GetAllVoucherCallBack callBack){
        CollectionReference dbUsers = db.collection("vouchers");

        dbUsers.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    ArrayList<Voucher> vouchers = new ArrayList<>(task.getResult().toObjects(Voucher.class));
                    callBack.onSuccess(vouchers);
                } else {
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

    public void updateProductQuantityById(String productId, int newQuantity, updateProductCallBack callBack){
        DocumentReference dbProducts = db.collection("products").document(productId);

        Map<String, Object> data = new HashMap<>();
        data.put("quantity", newQuantity);

        dbProducts.update(data)
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

    public void getBuyOrders(GetOrdersCallBack callBack){
        DocumentReference dbUser = db.collection("users").document(currentUser.getUid());
        db.runTransaction(new Transaction.Function<ArrayList<Order>>() {
            @Override
            public ArrayList<Order> apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                ArrayList<Order> result = new ArrayList<>();

                ArrayList<DocumentReference> orderRefs = (ArrayList<DocumentReference>) transaction.get(dbUser).get("buyOrderRef");
                if(orderRefs != null){
                    for(DocumentReference ref : orderRefs) {
                        DocumentSnapshot snapshot = transaction.get(ref);
                        if(snapshot.exists()){
                            Order order = snapshot.toObject(Order.class);
                            if(order != null){
                                result.add(order);
                            }
                        }
                    }
                }

                ArrayList<DocumentReference> newOrderRef = new ArrayList<>();
                for(Order order : result){
                    newOrderRef.add(db.collection("orders").document(order.getId()));
                }
                transaction.update(dbUser, "buyOrderRef", newOrderRef);
                return result;
            }
        }).addOnCompleteListener(new OnCompleteListener<ArrayList<Order>>() {
            @Override
            public void onComplete(@NonNull Task<ArrayList<Order>> task) {
                if(task.isSuccessful()){
                    callBack.onSuccess(task.getResult());
                }else{
                    task.getException().printStackTrace();
                    callBack.onFailure(task.getException());
                }
            }
        });
    }


    public void getBuyOrdersRef(getOrdersRefCallBack callBack){
        DocumentReference dbUser = db.collection("users").document(currentUser.getUid());
        dbUser.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    ArrayList<DocumentReference> orderRefList = (ArrayList<DocumentReference>) task.getResult().get("buyOrderRef");
                    callBack.onSuccess(orderRefList);
                } else {
                    callBack.onFailure(task.getException());
                }
            }
        });
    }

    public void getAllOrders(GetOrdersCallBack callBack){
        CollectionReference dbOrders = db.collection("orders");
        dbOrders.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                ArrayList<Order> orders = new ArrayList<>();
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot snapshot : task.getResult()){
                        Order order = snapshot.toObject(Order.class);
                        orders.add(order);
                    }
                    callBack.onSuccess(orders);
                }else {
                    callBack.onFailure(task.getException());
                }
            }
        });
    }

    public void getSellOrders(GetOrdersCallBack callBack){;
        DocumentReference dbUser = db.collection("users").document(currentUser.getUid());
        db.runTransaction(new Transaction.Function<ArrayList<Order>>() {
            @Override
            public ArrayList<Order> apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                ArrayList<Order> result = new ArrayList<>();

                ArrayList<DocumentReference> orderRefs = (ArrayList<DocumentReference>) transaction.get(dbUser).get("sellOrderRef");
                if(orderRefs != null){
                    for(DocumentReference ref : orderRefs) {
                        DocumentSnapshot snapshot = transaction.get(ref);
                        if(snapshot.exists()){
                            Order order = snapshot.toObject(Order.class);
                            if(order != null){
                                result.add(order);
                            }
                        }
                    }
                }

                ArrayList<DocumentReference> newOrderRef = new ArrayList<>();
                for(Order order : result){
                    newOrderRef.add(db.collection("orders").document(order.getId()));
                }
                transaction.update(dbUser, "sellOrderRef", newOrderRef);
                return result;
            }
        }).addOnCompleteListener(new OnCompleteListener<ArrayList<Order>>() {
            @Override
            public void onComplete(@NonNull Task<ArrayList<Order>> task) {
                if(task.isSuccessful()){
                    callBack.onSuccess(task.getResult());
                }else{
                    task.getException().printStackTrace();
                    callBack.onFailure(task.getException());
                }
            }
        });
    }

    public void getOrderAndProductsById(String id, GetOrderAndProductsCallBack callBack){
        DocumentReference dbOrder = db.collection("orders").document(id);
        CollectionReference dbProduct = db.collection("products");

        dbOrder.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    Order order = task.getResult().toObject(Order.class);

                    db.runTransaction(new Transaction.Function<ArrayList<Product>>() {
                        @Override
                        public ArrayList<Product> apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                            ArrayList<Product> products = new ArrayList<>();

                            for (String id : order.getProductIDs()){
                                DocumentSnapshot snapshot = transaction.get(dbProduct.document(id));
                                products.add(snapshot.toObject(Product.class));
                            }
                            return products;
                        }
                    }). addOnCompleteListener(new OnCompleteListener<ArrayList<Product>>() {
                        @Override
                        public void onComplete(@NonNull Task<ArrayList<Product>> task) {
                            if(task.isSuccessful()){
                                callBack.onSuccess(order, task.getResult());
                            }else { callBack.onFailure(task.getException());}
                        }
                    });
                }else {callBack.onFailure(task.getException());}
            }
        });
    }

    public void updateOrderStatus(String id, String status, AddOrdersCallBack callBack){
        DocumentReference dbProducts = db.collection("orders").document(id);
        dbProducts.update("status", status).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    callBack.onSuccess();
                }else {callBack.onFailure(task.getException());}
            }
        });
    }

    public void getEligibleVoucher(int totalPrice, GetVouchersCallBack callBack){
        CollectionReference dbVoucher = db.collection("vouchers");
        ArrayList<Voucher> vouchers = new ArrayList<>();
        dbVoucher.whereEqualTo("disabled", false)
                .whereLessThanOrEqualTo("minOrderValue", totalPrice)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            try{
                                for(QueryDocumentSnapshot snapshot : task.getResult()){
                                    Voucher voucher = snapshot.toObject(Voucher.class);
                                    if(!voucher.getUsedUsers().contains(currentUser.getUid())){
                                        vouchers.add(voucher);
                                    }
                                }
                                callBack.onSuccess(vouchers);
                            }catch (Exception e){
                                e.printStackTrace();
                                callBack.onFailure(e);
                            }
                        }else {callBack.onFailure(task.getException());}
                    }
                });
    }
    public void disableVoucher(String voucherId, disableVoucherCallBack callBack){
        DocumentReference dbVoucher = db.collection("vouchers").document(voucherId);

        Map<String, Object> update = new HashMap<>();
        update.put("disabled", true);

        dbVoucher.update(update).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    callBack.onSuccess();
                }else{callBack.onFailure(task.getException());}
            }
        });

    }
    public void disableProduct(String productId, disableProductCallBack callBack){
        DocumentReference dbProduct = db.collection("products").document(productId);

        Map<String, Object> update = new HashMap<>();
        update.put("disabled", true);

        dbProduct.update(update).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    callBack.onSuccess();
                }else{callBack.onFailure(task.getException());}
            }
        });

    }

    public void addOrders(Map<Product, Integer> cart, String address, Voucher voucher, AddOrdersCallBack callBack){
        CollectionReference dbOrders = db.collection("orders");
        CollectionReference dbUsers = db.collection("users");
        CollectionReference dbProducts = db.collection("products");

        if(address == null){
            callBack.onFailure(new MissingAddressException("Address is missing, please add one in your profile"));
            return;
        }else{
            if(address.equals("") || address.equals("There is not any shipping information, Let's add one!")){
                callBack.onFailure(new MissingAddressException("Address is missing, please add one in your profile"));
                return;
            }
        }

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
                    if(realQuant == null){
                        realQuant = 0L;
                    }
                    if(realQuant < entry.getValue()){
                        throw new OutOfStockException("Run out of stock for " + snapshot.getString("productName"), FirebaseFirestoreException.Code.ABORTED);
                    }

                    Long price = snapshot.getLong("productPrice");
                    if(price == null){
                        price = 0L;
                    }
                    netTotal += price * entry.getValue();
                    if(!sellerRefs.contains(entry.getKey().getSellerId())){
                        sellerRefs.add(entry.getKey().getSellerId());
                    }
                }
                int discountAmountTotal = 0;
                if(voucher != null){
                    discountAmountTotal = (int) (netTotal * voucher.getDiscountPercent() / 100);
                    if(discountAmountTotal > voucher.getMaxDiscount()){
                        discountAmountTotal = voucher.getMaxDiscount();
                        float newDiscountPercent = Math.round(((1-((float)(netTotal-discountAmountTotal)/netTotal)) * 100) * 10) / 10f;
                        voucher.setDiscountPercent(newDiscountPercent);
                    }
                }
                Log.d("Discount Amount", String.valueOf(discountAmountTotal));
                int finalNetTotal = netTotal - discountAmountTotal;


                // Check if user has sufficient fund
                Long currentBalance = transaction.get(dbUsers.document(currentUser.getUid())).getLong("balance");
                if(currentBalance == null){
                    currentBalance = 0L;
                }
                if(currentBalance < finalNetTotal){
                    throw new InsufficientBalanceException("Insufficient Balance", FirebaseFirestoreException.Code.ABORTED);
                }

                Log.d("Final Net Total", String.valueOf(finalNetTotal));
                transaction.update(dbUsers.document(currentUser.getUid()), "balance", FieldValue.increment(-finalNetTotal));
                if(voucher != null){
                    transaction.update(db.collection("vouchers").document(voucher.getVoucherId()), "usedUsers", FieldValue.arrayUnion(currentUser.getUid()));
                }


                int adminShare = 0;

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

                    int sellerShare = total * SELLER_SHARE/100;
                    adminShare += total * ADMIN_SHARE/100;
                    Log.d("Seller Share Total", String.valueOf(sellerShare));
                    Order order = new Order(id, productIDs, amount, total, address,
                            voucher, dbUsers.document(currentUser.getUid()), seller);

                    transaction.set(dbOrders.document(id), order);
                    transaction.update(seller, "balance", FieldValue.increment(sellerShare));
                    transaction.update(dbUsers.document(currentUser.getUid()), "buyOrderRef", FieldValue.arrayUnion(dbOrders.document(id)));
                    transaction.update(seller, "sellOrderRef", FieldValue.arrayUnion(dbOrders.document(id)));
                }
                int finalAdminShare = adminShare - discountAmountTotal;
                Log.d("Admin Share Total", String.valueOf(finalAdminShare));
                transaction.update(dbUsers.document(ADMIN_ID),"balance", FieldValue.increment(finalAdminShare));
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

    public void getChatWithOfAUser(GetChatWithOfAUserCallBack callBack){
        CollectionReference dbUserChatWith = db.collection("users")
                .document(currentUser.getUid())
                .collection("chatWith");
        dbUserChatWith.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    List<DocumentSnapshot> documents = task.getResult().getDocuments();
                    ArrayList<DocumentReference> chatRefList = new ArrayList<>();

                    for(DocumentSnapshot document: documents){
                        chatRefList.add(document.getDocumentReference(document.getId()));
                    }
                    callBack.onSuccess(chatRefList);
                } else {
                    callBack.onFailure(task.getException());
                }
            }
        });
    }

    public void getDataForChatAdapter(ArrayList<DocumentReference> chatRefList, GetDataForChatAdapterCallBack callBack){
        CollectionReference userRef = db.collection("users");

        db.runTransaction(new Transaction.Function<Map<User, Message>>() {

            @NonNull
            @Override
            public Map<User, Message> apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                Map<User, Message> data = new HashMap<>();
                int index = 0;
                for(DocumentReference chatRef: chatRefList){
                    DocumentSnapshot documentSnapshot = transaction.get(chatRef);

                    String m1Id = Objects.requireNonNull(documentSnapshot.get("m1")).toString();
                    String m2Id = Objects.requireNonNull(documentSnapshot.get("m2")).toString();
                    String otherId = (currentUser.getUid().equals(m1Id))? m2Id : m1Id;

                    Message msg;
                    if(documentSnapshot.get("lastMsg") != null){
                        DocumentReference msgRef = (DocumentReference) documentSnapshot.get("lastMsg");
                        msg = transaction.get(msgRef).toObject(Message.class);
                    } else {
                        continue;
                    }
                    User user = transaction.get(userRef.document(otherId)).toObject(User.class);
                    data.put(user, msg);
                }

                return data;
            }
        }).addOnSuccessListener(new OnSuccessListener<Map<User, Message>>() {
            @Override
            public void onSuccess(Map<User, Message> userMessageMap) {
                callBack.onSuccess(userMessageMap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callBack.onFailure(e);
            }
        });
    }

    public void addVoucher(Voucher voucher, AddVoucherCallBack callBack){
        DocumentReference voucherRef = db.collection("vouchers")
                .document(voucher.getVoucherId());

        voucherRef.set(voucher).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    callBack.onSuccess();
                } else {
                    callBack.onFailure(task.getException());
                }
            }
        });
    }

    // Callback interfaces
    public interface AddOrdersCallBack{
        void onSuccess();
        void onFailure(Exception e);
    }

    public interface getOrdersRefCallBack {
        void onSuccess(ArrayList<DocumentReference> result);
        void onFailure(Exception e);
    }

    public interface GetOrdersCallBack {
        void onSuccess(ArrayList<Order> result);
        void onFailure(Exception e);
    }

    public interface GetOrderAndProductsCallBack {
        void onSuccess(Order result, ArrayList<Product> products);
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

    public interface GetAllUserCallBack {
        void onSuccess(ArrayList<User> result);
        void onFailure(Exception e);
    }

    public interface GetAllVoucherCallBack {
        void onSuccess(ArrayList<Voucher> result);
        void onFailure(Exception e);
    }

    public interface AddVoucherDataCallBack {
        void onSuccess();
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

    public interface updateProductCallBack{
        void onSuccess();
        void onFailure(Exception e);
    }

    public interface GetChatWithOfAUserCallBack {
        void onSuccess(ArrayList<DocumentReference> chatRefList);
        void onFailure(Exception e);
    }

    public interface GetDataForChatAdapterCallBack {
        void onSuccess(Map<User, Message> data);
        void onFailure(Exception e);
    }

    public interface AddVoucherCallBack {
        void onSuccess();
        void onFailure(Exception e);
    }

    public interface getAllProductForAdminCallBack{
        void onSuccess(ArrayList<Product> result);
        void onFailure(Exception e);
    }

    public interface GetVouchersCallBack{
        void onSuccess(ArrayList<Voucher> result);
        void onFailure(Exception e);
    }

    public interface disableVoucherCallBack{
        void onSuccess();
        void onFailure(Exception e);
    }
    public interface disableProductCallBack{
        void onSuccess();
        void onFailure(Exception e);
    }


}

class NoUserInDatabaseException extends Exception{
    public NoUserInDatabaseException(String messageError){
        super(messageError);
    }
}

class MissingAddressException extends Exception{
    public MissingAddressException(String messageError){
        super(messageError);
    }
}

class InsufficientBalanceException extends FirebaseFirestoreException{
    public InsufficientBalanceException(String messageError, Code code){ super(messageError, code); }
}

class OutOfStockException extends FirebaseFirestoreException{
    public OutOfStockException(String messageError, Code code){ super(messageError, code); }
}



