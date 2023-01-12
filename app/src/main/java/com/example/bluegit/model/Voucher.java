package com.example.bluegit.model;

import java.sql.Timestamp;

public class Voucher {
    private String voucherId;
    private String voucherName;
    private int voucherDiscount;
    private int voucherQuantity;
    private Timestamp voucherExpireDate;

    public Voucher(){}

    public Voucher(String voucherId, String voucherName, int voucherDiscount, int voucherQuantity, Timestamp voucherExpireDate) {
        this.voucherId = voucherId;
        this.voucherName = voucherName;
        this.voucherDiscount = voucherDiscount;
        this.voucherQuantity = voucherQuantity;
        this.voucherExpireDate = voucherExpireDate;
    }

    public String getVoucherId() {
        return voucherId;
    }

    public String getVoucherName() {
        return voucherName;
    }

    public int getVoucherDiscount() {
        return voucherDiscount;
    }

    public int getVoucherQuantity() {
        return voucherQuantity;
    }

    public Timestamp getVoucherExpireDate() {
        return voucherExpireDate;
    }

    public void setVoucherId(String voucherId) {
        this.voucherId = voucherId;
    }

    public void setVoucherName(String voucherName) {
        this.voucherName = voucherName;
    }

    public void setVoucherDiscount(int voucherDiscount) {
        this.voucherDiscount = voucherDiscount;
    }

    public void setVoucherQuantity(int voucherQuantity) {
        this.voucherQuantity = voucherQuantity;
    }

    public void setVoucherExpireDate(Timestamp voucherExpireDate) {
        this.voucherExpireDate = voucherExpireDate;
    }
}
