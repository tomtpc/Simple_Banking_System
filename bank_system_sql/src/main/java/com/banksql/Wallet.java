package com.banksql;

import java.io.Serializable;

public class Wallet implements Serializable{
    private int CustomerID, status;
    private float amount;
    private String WalletNo, CreateDate;

    public int getCustomerID() {
        return CustomerID;
    }

    public void setCustomerID(int CustomerID) {
        this.CustomerID = CustomerID;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getWalletNo() {
        return WalletNo;
    }

    public void setWalletNo(String WalletNo) {
        this.WalletNo = WalletNo;
    }

    public String getCreateDate() {
        return CreateDate;
    }

    public void setCreateDate(String CreateDate) {
        this.CreateDate = CreateDate;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public Wallet(int CustomerID, int status, String WalletNo, String CreateDate, float amount) {
        this.CustomerID = CustomerID;
        this.status = status;
        this.WalletNo = WalletNo;
        this.CreateDate = CreateDate;
        this.amount = amount;
    }

    public Wallet() {
    }

    @Override
    public String toString() {
        return " /WalletNo: "+this.getWalletNo()+" /CreDate: "+this.getCreateDate()+" /Status: "+this.getStatus()+" /Amout: "+this.getAmount()+ " ]";
    }
}
