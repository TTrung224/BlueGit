package com.example.bluegit;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bluegit.adapters.ProductManageAdapter;
import com.example.bluegit.adapters.RecyclerViewOnClickListener;
import com.example.bluegit.model.Product;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class ProductManageActivity extends AppCompatActivity {
    private String uId;
    Intent navIntent;

    FireStoreManager fireStoreManager;
    TextView emptyMessage;
    TextView editFormQuantity;
    TextView editFormId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_manage);
        fireStoreManager = new FireStoreManager(this, FirebaseAuth.getInstance().getCurrentUser());
        emptyMessage = findViewById(R.id.empty_message);

        editFormQuantity = findViewById(R.id.editFormProductQuantity);
        editFormId = findViewById(R.id.editFormProductId);

        navIntent = new Intent(this, MainActivity.class);
    }

    @Override
    protected void onStart() {
        super.onStart();
        RecyclerView productDisplay = findViewById(R.id.product_manager_recyclerview);
        fireStoreManager.getUserProducts(new FireStoreManager.GetProductsCallBack() {
            @Override
            public void onSuccess(ArrayList<Product> result) {
                if(result.size() > 0){
                    emptyMessage.setVisibility(View.GONE);
                    ProductManageAdapter adapter = new ProductManageAdapter(result,
                            ProductManageActivity.this, new RecyclerViewOnClickListener(){
                        @Override
                        public void onItemClick(int position) {
                            Product product = result.get(position);
                            findViewById(R.id.editForm).setVisibility(View.VISIBLE);

                            TextView editFormName = findViewById(R.id.editFormProductName);
                            editFormName.setText(product.getProductName());

                            editFormId.setText(product.getProductId());
                            editFormQuantity.setText(Integer.toString(product.getQuantity()));
                    }});

                    productDisplay.setAdapter(adapter);
                    productDisplay.setLayoutManager(
                            new LinearLayoutManager(ProductManageActivity.this));
                }
            }

            @Override
            public void onFailure(Exception e) {
                Log.d("GetUserProductException", e.getLocalizedMessage());
            }
        });
    }

    public void goBack(View view) {
        setResult(RESULT_OK);
        finish();
    }

    public void refresh(View view) {
        this.recreate();
    }

    public void toOrders(View view) {
        navIntent.putExtra("navTo", MainActivity.NAV_TO_ORDERS);
        setResult(RESULT_OK, navIntent);
        finish();
        overridePendingTransition(0, 0);
    }

    public void toHome(View view) {
        setResult(RESULT_OK);
        finish();
        overridePendingTransition(0, 0);
    }

    public void toCart(View view) {
        navIntent.putExtra("navTo", MainActivity.NAV_TO_CART);
        setResult(RESULT_OK, navIntent);
        finish();
        overridePendingTransition(0, 0);
    }

    public void toAccount(View view) {
        navIntent.putExtra("navTo", MainActivity.NAV_TO_ACCOUNT);
        setResult(RESULT_OK, navIntent);
        finish();
        overridePendingTransition(0, 0);
    }

    public void addProduct(View view) {
        Intent intent = new Intent(ProductManageActivity.this, AddProductActivity.class);
        startActivity(intent);
    }

    public void viewSellOrders(View view) {
        Intent intent = new Intent(ProductManageActivity.this, OrdersSellActivity.class);
        startActivity(intent);
    }

    public void closeForm(View view) {
        findViewById(R.id.editForm).setVisibility(View.GONE);
    }

    public void disableProduct(View view) {
        String productId = editFormId.getText().toString();
        findViewById(R.id.editForm).setVisibility(View.GONE);

        AlertDialog.Builder builder = new AlertDialog.Builder(ProductManageActivity.this);
        builder.create();
        builder.setTitle("DISABLE CONFIRMATION")
                .setMessage("Are you sure you want to disable this product? " +
                        "When you accept you cannot undo it.")
                .setPositiveButton("DISABLE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        fireStoreManager.disableProduct(productId,
                                new FireStoreManager.disableProductCallBack() {
                            @Override
                            public void onSuccess() {
                                Toast.makeText(ProductManageActivity.this,
                                        "Disabled successfully", Toast.LENGTH_SHORT).show();
                                recreate();
                            }

                            @Override
                            public void onFailure(Exception e) {
                                Log.d("Product Deleted ", e.getLocalizedMessage());
                                Toast.makeText(ProductManageActivity.this,
                                        "Fail to disable product, please try again",
                                        Toast.LENGTH_SHORT).show();
                                recreate();
                            }
                        });
                    }
                }).setNegativeButton("CANCEL", null).show();
    }

    public void updateProduct(View view) {
        String newQuantity = editFormQuantity.getText().toString();
        String productId = editFormId.getText().toString();


        if(newQuantity.equals("")){
            editFormQuantity.setError("Please enter quantity.");
            editFormQuantity.requestFocus();
        } else {
            fireStoreManager.updateProductQuantityById(productId, Integer.parseInt(newQuantity),
                    new FireStoreManager.updateProductCallBack() {
                @Override
                public void onSuccess() {
                    Toast.makeText(ProductManageActivity.this,
                            "Update successfully", Toast.LENGTH_SHORT).show();
                    findViewById(R.id.editForm).setVisibility(View.GONE);
                    recreate();
                }

                @Override
                public void onFailure(Exception e) {
                    Toast.makeText(ProductManageActivity.this,
                            "Update fail, please try again", Toast.LENGTH_SHORT).show();
                    findViewById(R.id.editForm).setVisibility(View.GONE);
                    recreate();
                }
            });
        }
    }
}