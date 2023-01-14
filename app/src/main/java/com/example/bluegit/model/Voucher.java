package com.example.bluegit.model;

import java.sql.Timestamp;

public class Voucher {
    private String voucherId;
    private String voucherName;
    private int discountPercent;
    private int minOrderValue;
    private int maxDiscount;
    private boolean disabled;

    public Voucher(){}

    public Voucher(String voucherId, String voucherName, int discountPercent, int minOrderValue, int maxDiscount) {
        this.voucherId = voucherId;
        this.voucherName = voucherName;
        this.discountPercent = discountPercent;
        this.minOrderValue = minOrderValue;
        this.maxDiscount = maxDiscount;
        this.disabled = false;
    }

    public String getVoucherId() {
        return voucherId;
    }

    public String getVoucherName() {
        return voucherName;
    }

    public int getDiscountPercent() {
        return discountPercent;
    }

    public int getMinOrderValue() {
        return minOrderValue;
    }

    public int getMaxDiscount() {
        return maxDiscount;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setVoucherId(String voucherId) {
        this.voucherId = voucherId;
    }

    public void setVoucherName(String voucherName) {
        this.voucherName = voucherName;
    }

    public void setDiscountPercent(int discountPercent) {
        this.discountPercent = discountPercent;
    }

    public void setMinOrderValue(int minOrderValue) {
        this.minOrderValue = minOrderValue;
    }

    public void setMaxDiscount(int maxDiscount) {
        this.maxDiscount = maxDiscount;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }
}
