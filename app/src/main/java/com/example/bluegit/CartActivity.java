package com.example.bluegit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bluegit.adapters.AdminProductAdapter;
import com.example.bluegit.adapters.CartAdapter;
import com.example.bluegit.model.Product;
import com.example.bluegit.model.User;
import com.example.bluegit.model.Voucher;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.checkerframework.checker.units.qual.A;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class CartActivity extends AppCompatActivity {

    FireStoreManager fireStoreManager;
    NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
    TextView emptyMessage;
    TextView orderTotal;
    Button orderBtn;
    ProgressBar orderProgress;
    Spinner shippingInfoSpin;
    Button voucherBtn;
    TextView voucherName;
    TextView voucherDiscount;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    Button itemListDisabler;
    Voucher voucher = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        fireStoreManager = new FireStoreManager(this, FirebaseAuth.getInstance().getCurrentUser());
        emptyMessage = findViewById(R.id.empty_message);
        orderTotal = findViewById(R.id.order_total);
        orderBtn = findViewById(R.id.order_button);
        orderProgress = findViewById(R.id.order_progress);
        shippingInfoSpin = findViewById(R.id.shippingInfo);
        voucherBtn = findViewById(R.id.addVoucherBtn);
        itemListDisabler = findViewById(R.id.itemListDisabler);
        voucherName = findViewById(R.id.voucherName);
        voucherDiscount = findViewById(R.id.voucherDiscount);

        fireStoreManager.getCurrentUser(new FireStoreManager.GetUserDataCallBack() {
            @Override
            public void onSuccess(User result) {
                List<String> addressList = result.getAddress();
                if(addressList == null){
                    addressList = new ArrayList<>();
                }
                if (addressList.isEmpty()) {
                    addressList.add("There is not any shipping information, Let's add one!");
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(CartActivity.this,
                            android.R.layout.simple_spinner_item,
                            addressList);

                    adapter.setDropDownViewResource(android.R.layout.simple_expandable_list_item_1);
                    shippingInfoSpin.setAdapter(adapter);
                } else {
                    addressList = result.getAddress();
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(CartActivity.this,
                            android.R.layout.simple_spinner_item,
                            addressList);

                    adapter.setDropDownViewResource(android.R.layout.simple_expandable_list_item_1);
                    shippingInfoSpin.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Exception e) {
                Log.d("Get User Exception", e.getMessage());
                finish();
            }
        });

        fireStoreManager.getCart(new FireStoreManager.GetCartCallBack() {
            @Override
            public void onSuccess(Map<String, Object> cartResult) {
                fireStoreManager.getProductsFromCart(cartResult, new FireStoreManager.GetProductsFromCartCallBack() {
                    @Override
                    public void onSuccess(Map<Product, Integer> result) {
                        if(!result.isEmpty()){
                            emptyMessage.setVisibility(View.GONE);
                        }

                        RecyclerView recyclerView = findViewById(R.id.itemList);
                        CartAdapter adapter = new CartAdapter(result, CartActivity.this, orderTotal);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(CartActivity.this));

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            recyclerView.setFocusable(View.NOT_FOCUSABLE);
                        }
                        voucherBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent i = new Intent(CartActivity.this, VoucherSelect.class);
                                i.putExtra("totalPrice", adapter.totalPrice);
                                startActivityForResult(i, 300);
                            }
                        });

                        orderBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(adapter.getItemCount() < 1){
                                    Toast.makeText(CartActivity.this, "No item in cart", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                orderProgress.setVisibility(View.VISIBLE);
                                v.setEnabled(false);
                                v.setAlpha(0.5f);

                                fireStoreManager.addOrders(adapter.cartMap, shippingInfoSpin.getSelectedItem().toString(), voucher, new FireStoreManager.AddOrdersCallBack() {
                                    @Override
                                    public void onSuccess() {
                                        fireStoreManager.emptyCart();
                                        Toast.makeText(CartActivity.this, "Successfully create a new order", Toast.LENGTH_SHORT).show();
                                        orderProgress.setVisibility(View.GONE);
                                        finish();
                                    }

                                    @Override
                                    public void onFailure(Exception e) {
                                        v.setEnabled(true);
                                        v.setAlpha(1f);
                                        orderProgress.setVisibility(View.GONE);
                                        if(e instanceof InsufficientBalanceException){
                                            Toast.makeText(CartActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }else if(e instanceof OutOfStockException){
                                            Toast.makeText(CartActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        } else if(e instanceof MissingAddressException){
                                            Toast.makeText(CartActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        } else{
                                            Toast.makeText(CartActivity.this, "Unable to create order", Toast.LENGTH_SHORT).show();
                                            Log.d("MakeOrderException", e.getMessage());
                                        }
                                    }
                                });
                            }
                        });
                    }
                    @Override
                    public void onFailure(Exception e) {
                        Log.d("DisplayCartEx", e.getLocalizedMessage());
                    }
                });
            }
            @Override
            public void onFailure(Exception e) {
                Log.d("DisplayCartEx", e.getLocalizedMessage());
                Toast.makeText(CartActivity.this, "Unable to load cart", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 300){
            if(resultCode == RESULT_OK){
                itemListDisabler.setVisibility(View.VISIBLE);
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                    voucher = data.getExtras().getSerializable("voucher", Voucher.class);
                    int newPrice = data.getIntExtra("newPrice", 0);

                    voucherName.setText(voucher.getVoucherName());
                    String tDisc = "-" + voucher.getDiscountPercent() + "%";
                    voucherDiscount.setText(tDisc);
                    NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
                    orderTotal.setText(nf.format(newPrice));
                    itemListDisabler.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(this, "Your Version does not support this feature", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void goBack(View view) {
        setResult(RESULT_OK);
        finish();
    }

    public void toBusiness(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("navTo", MainActivity.NAV_TO_BUSINESS);
        setResult(RESULT_OK, intent);
        finish();
        overridePendingTransition(0, 0);
    }

    public void toOrders(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("navTo", MainActivity.NAV_TO_ORDERS);
        setResult(RESULT_OK, intent);
        finish();
        overridePendingTransition(0, 0);
    }

    public void toHome(View view) {
        setResult(RESULT_OK);
        finish();
        overridePendingTransition(0, 0);
    }

    public void refresh(View view) {
        this.recreate();
    }

    public void toAccount(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("navTo", MainActivity.NAV_TO_ACCOUNT);
        setResult(RESULT_OK, intent);
        finish();
        overridePendingTransition(0, 0);
    }
}