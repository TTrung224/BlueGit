package com.example.bluegit.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.bluegit.R;
import com.example.bluegit.model.Voucher;
import androidx.annotation.NonNull;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;


public class AdminVoucherAdapter extends RecyclerView.Adapter<AdminVoucherAdapter.ViewHolder> {

    private ArrayList<Voucher> vouchers;
    private LayoutInflater layoutInflater;

    public AdminVoucherAdapter( ArrayList<Voucher> vouchers, Context context) {
        this.vouchers = vouchers;
        this.layoutInflater = LayoutInflater.from(context);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView voucherID;
        public TextView voucherName;
        public TextView discountPercent;
        public TextView minOrder;
        public TextView maxDiscount;

    public ViewHolder(@NonNull View voucherView) {
        super(voucherView);

        voucherID = voucherView.findViewById(R.id.voucherID);
        voucherName = voucherView.findViewById(R.id.voucherName);
        discountPercent = voucherView.findViewById(R.id.discount);
        minOrder = voucherView.findViewById(R.id.minOrderVoucher);
        maxDiscount = voucherView.findViewById(R.id.maxDiscount);
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
        holder.voucherID.setText(vouchers.get(position).getVoucherId());
        holder.voucherName.setText(vouchers.get(position).getVoucherName());
        holder.discountPercent.setText(vouchers.get(position).getDiscountPercent());

        NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        String minOrder = nf.format(vouchers.get(position).getMinOrderValue());
        holder.minOrder.setText(minOrder);

        String maxDiscount = nf.format(vouchers.get(position).getMaxDiscount());
        holder.maxDiscount.setText(maxDiscount);
    }

    @Override
    public int getItemCount() {
        return vouchers.size();
    }
}
