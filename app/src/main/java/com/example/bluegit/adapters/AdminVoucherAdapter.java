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
import java.util.ArrayList;


public class AdminVoucherAdapter extends RecyclerView.Adapter<AdminVoucherAdapter.ViewHolder> {

    private RecyclerViewOnClickListener recyclerViewOnClickListener;
    private ArrayList<Voucher> vouchers;
    private LayoutInflater layoutInflater;

    public AdminVoucherAdapter(RecyclerViewOnClickListener recyclerViewOnClickListener, ArrayList<Voucher> vouchers, Context context, LayoutInflater layoutInflater) {
        this.recyclerViewOnClickListener = recyclerViewOnClickListener;
        this.vouchers = vouchers;
        this.layoutInflater = LayoutInflater.from(context);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView voucherID;
        public TextView voucherName;
        public TextView voucherDiscount;
        public TextView voucherQuantity;
        public TextView voucherExpireDate;

    public ViewHolder(@NonNull View voucherView,RecyclerViewOnClickListener recyclerViewOnClickListener) {
        super(voucherView);

        voucherID = voucherView.findViewById(R.id.voucherID);
        voucherName = voucherView.findViewById(R.id.voucherName);
        voucherDiscount = voucherView.findViewById(R.id.discount);
        voucherQuantity = voucherView.findViewById(R.id.quantityVoucher);
        voucherExpireDate = voucherView.findViewById(R.id.expireDate);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.layout_voucher_display, parent, false);
        return new AdminVoucherAdapter.ViewHolder(view, recyclerViewOnClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.voucherID.setText(vouchers.get(position).getVoucherId());
        holder.voucherName.setText(vouchers.get(position).getVoucherName());
//        holder.voucherDiscount.setText(vouchers.get(position).getVoucherDiscount());
//        holder.voucherQuantity.setText(vouchers.get(position).getVoucherQuantity());
//        holder.voucherExpireDate.setText(vouchers.get(position).getVoucherExpireDate().toString());
    }

    @Override
    public int getItemCount() {
        return vouchers.size();
    }
}
