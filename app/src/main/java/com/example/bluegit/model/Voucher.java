package com.example.bluegit.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;

public class Voucher implements Serializable {
    private String voucherId;
    private String voucherName;
    private int discountPercent;
    private int minOrderValue;
    private int maxDiscount;
    private boolean disabled;
    private ArrayList<String> usedUsers;

    public Voucher() {
    }

    public Voucher(String voucherId, String voucherName, int discountPercent, int minOrderValue, int maxDiscount) {
        this.voucherId = voucherId;
        this.voucherName = voucherName;
        this.discountPercent = discountPercent;
        this.minOrderValue = minOrderValue;
        this.maxDiscount = maxDiscount;
        this.disabled = false;
        usedUsers = new ArrayList<>();
    }

    public ArrayList<String> getUsedUsers() {
        return usedUsers;
    }

    public void setUsedUsers(ArrayList<String> usedUsers) {
        this.usedUsers = usedUsers;
    }

    public String getVoucherId() {
        return voucherId;
    }

    public void setVoucherId(String voucherId) {
        this.voucherId = voucherId;
    }

    public String getVoucherName() {
        return voucherName;
    }

    public void setVoucherName(String voucherName) {
        this.voucherName = voucherName;
    }

    public int getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(int discountPercent) {
        this.discountPercent = discountPercent;
    }

    public int getMinOrderValue() {
        return minOrderValue;
    }

    public void setMinOrderValue(int minOrderValue) {
        this.minOrderValue = minOrderValue;
    }

    public int getMaxDiscount() {
        return maxDiscount;
    }

    public void setMaxDiscount(int maxDiscount) {
        this.maxDiscount = maxDiscount;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }
}
