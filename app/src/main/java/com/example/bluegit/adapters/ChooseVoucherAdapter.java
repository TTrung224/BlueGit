package com.example.bluegit.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bluegit.R;
import com.example.bluegit.model.Voucher;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ChooseVoucherAdapter extends RecyclerView.Adapter<ChooseVoucherAdapter.ViewHolder> {

    private RecyclerViewOnClickListener recyclerViewOnClickListener;
    public final ArrayList<Voucher> vouchers;
    private final LayoutInflater inflater;

    public ChooseVoucherAdapter(ArrayList<Voucher> vouchers, Context context, RecyclerViewOnClickListener recyclerViewOnClickListener) {
        this.vouchers = vouchers;
        this.inflater = LayoutInflater.from(context);
        this.recyclerViewOnClickListener = recyclerViewOnClickListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView voucherName;
        TextView voucherDiscount;
        TextView voucherMaxDisc;

        public ViewHolder(@NonNull View itemView, RecyclerViewOnClickListener recyclerViewOnClickListener) {
            super(itemView);
            voucherName = itemView.findViewById(R.id.voucherName);
            voucherDiscount = itemView.findViewById(R.id.voucherDiscount);
            voucherMaxDisc = itemView.findViewById(R.id.voucherMaxDisc);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (recyclerViewOnClickListener != null) {
                        int pos = getAdapterPosition();
                        if(pos != RecyclerView.NO_POSITION){
                            recyclerViewOnClickListener.onItemClick(pos);
                        }
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public ChooseVoucherAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.layout_voucher_select, parent, false);
        return new ChooseVoucherAdapter.ViewHolder(view, recyclerViewOnClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ChooseVoucherAdapter.ViewHolder holder, int position) {
        holder.voucherName.setText(vouchers.get(position).getVoucherName());
        String discountVal = String.valueOf(vouchers.get(position).getDiscountPercent());
        NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        String maxDiscText = "Max. deducted amount: " + nf.format(vouchers.get(position).getMaxDiscount());

        holder.voucherMaxDisc.setText(maxDiscText);
        String discountMinOrder = nf.format(vouchers.get(position).getMinOrderValue());

        String disText = "-" + discountVal + "% off ";
        if(vouchers.get(position).getMinOrderValue() > 0){
            disText += " for orders above " + discountMinOrder;
        }
        holder.voucherDiscount.setText(disText);
    }

    @Override
    public int getItemCount() {
        return vouchers.size();
    }
}
