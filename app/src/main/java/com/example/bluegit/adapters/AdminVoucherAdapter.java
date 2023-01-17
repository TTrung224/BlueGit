package com.example.bluegit.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bluegit.FireStoreManager;
import com.example.bluegit.R;
import com.example.bluegit.model.Voucher;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;


public class AdminVoucherAdapter extends RecyclerView.Adapter<AdminVoucherAdapter.ViewHolder> {
    private ArrayList<Voucher> vouchers;
    private LayoutInflater layoutInflater;
    private Context context;

    public AdminVoucherAdapter( ArrayList<Voucher> vouchers, Context context) {
        this.vouchers = vouchers;
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView voucherName;
        public TextView discountPercent;
        public TextView minOrder;
        public TextView maxDiscount;
        public Button disabaleBtn;

    public ViewHolder(@NonNull View voucherView) {
        super(voucherView);

        voucherName = voucherView.findViewById(R.id.voucherName);
        discountPercent = voucherView.findViewById(R.id.discount);
        minOrder = voucherView.findViewById(R.id.minOrderVoucher);
        maxDiscount = voucherView.findViewById(R.id.maxDiscount);
        disabaleBtn = voucherView.findViewById(R.id.disableBtn);

        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.layout_voucher_display, parent, false);
        return new AdminVoucherAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FireStoreManager fireStoreManager= new FireStoreManager(this.layoutInflater.getContext(), FirebaseAuth.getInstance().getCurrentUser());
        holder.voucherName.setText(vouchers.get(position).getVoucherName());

        String discountStr = String.valueOf(vouchers.get(position).getDiscountPercent());
        holder.discountPercent.setText(discountStr);

        NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        String minOrder = nf.format(vouchers.get(position).getMinOrderValue());
        holder.minOrder.setText(minOrder);

        String maxDiscount = nf.format(vouchers.get(position).getMaxDiscount());
        holder.maxDiscount.setText(maxDiscount);

        if(vouchers.get(holder.getAdapterPosition()).isDisabled()) {
            holder.disabaleBtn.setVisibility(View.GONE);
        }

        holder.disabaleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.create();
                builder.setTitle("DISABLE CONFIRMATION")
                        .setMessage("Are you sure you want to disable this voucher? When you accept you cannot undo it.")
                        .setPositiveButton("DISABLE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        fireStoreManager.disableVoucher(vouchers.get(holder.getAdapterPosition()).getVoucherId(), new FireStoreManager.disableVoucherCallBack(){
                            @Override
                            public void onSuccess(){
                                v.findViewById(R.id.disableBtn).setVisibility(View.GONE);
                            }

                            @Override
                            public void onFailure(Exception e) {
                                Log.d("DisableVoucher", e.getLocalizedMessage());
                            }
                        });
                    }
                }).setNegativeButton("CANCEL", null).show();
            }
        });

    }



    @Override
    public int getItemCount() {
        return vouchers.size();
    }
}
