package com.banksql;

import java.io.Serializable;

public class Customer implements Serializable{
    private String address, name ,NationalID, IssueDate, IssuePlace, phoneNumber;
private int CustomerID;

public String getAddress() {
    return address;
}

public void setAddress(String address) {
    this.address = address;
}

public String getName() {
    return name;
}

public void setName(String name) {
    this.name = name;
}

public String getNationalID() {
    return NationalID;
}

public void setNationalID(String NationalID) {
    this.NationalID = NationalID;
}

public String getIssueDate() {
    return IssueDate;
}

public void setIssueDate(String IssueDate) {
    this.IssueDate = IssueDate;
}

public String getIssuePlace() {
    return IssuePlace;
}

public void setIssuePlace(String IssuePlace) {
    this.IssuePlace = IssuePlace;
}

public String getPhoneNumber() {
    return phoneNumber;
}

public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
}

public int getCustomerID() {
    return CustomerID;
}

public void setCustomerID(int CustomerID) {
    this.CustomerID = CustomerID;
}

public Customer(String address, String name, String NationalID, String IssueDate, String IssuePlace, String phoneNumber, int CustomerID) {
    this.address = address;
    this.name = name;
    this.NationalID = NationalID;
    this.IssueDate = IssueDate;
    this.IssuePlace = IssuePlace;
    this.phoneNumber = phoneNumber;
    this.CustomerID = CustomerID;
}

public Customer() {
}

@Override
public String toString() {
    return "[CustomerID: "+this.getCustomerID()+" /Name: "+ this.getName()+" /NaID: "+this.getNationalID() +" /IDate: "+ this.getIssueDate()
            +" /IPlace "+ this.getIssuePlace()+" /Add: "+this.getAddress()+" /Phone: "+this.getPhoneNumber();
}
}
