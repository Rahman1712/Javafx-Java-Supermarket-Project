
package com.ar.saleslist;

import java.sql.Timestamp;
import java.util.Date;


public class SalesArea {
    String salesId;
    String cusId;
    String cusName;
    float salesTotal;
    int salesItemno;
    float billPaid;
    float billBalance;
    String billerName;
    Date salesTime;
    Timestamp salesTimeStamp;
    String salesTimeString;

    

    public SalesArea(String salesId, String cusId, String cusName, float salesTotal, int salesItemno, float billPaid, float billBalance, String billerName, Date salesTime, Timestamp salesTimeStamp, String salesTimeString) {
        this.salesId = salesId;
        this.cusId = cusId;
        this.cusName = cusName;
        this.salesTotal = salesTotal;
        this.salesItemno = salesItemno;
        this.billPaid = billPaid;
        this.billBalance = billBalance;
        this.billerName = billerName;
        this.salesTime = salesTime;
        this.salesTimeStamp = salesTimeStamp;
        this.salesTimeString = salesTimeString;
    }

    

    public String getSalesId() {
        return salesId;
    }

    public void setSalesId(String salesId) {
        this.salesId = salesId;
    }

    public String getCusId() {
        return cusId;
    }

    public void setCusId(String cusId) {
        this.cusId = cusId;
    }

    public String getCusName() {
        return cusName;
    }

    public void setCusName(String cusName) {
        this.cusName = cusName;
    }

    public float getSalesTotal() {
        return salesTotal;
    }

    public void setSalesTotal(float salesTotal) {
        this.salesTotal = salesTotal;
    }

    public int getSalesItemno() {
        return salesItemno;
    }

    public void setSalesItemno(int salesItemno) {
        this.salesItemno = salesItemno;
    }

    public float getBillPaid() {
        return billPaid;
    }

    public void setBillPaid(float billPaid) {
        this.billPaid = billPaid;
    }

    public float getBillBalance() {
        return billBalance;
    }

    public void setBillBalance(float billBalance) {
        this.billBalance = billBalance;
    }

    public String getBillerName() {
        return billerName;
    }

    public void setBillerName(String billerName) {
        this.billerName = billerName;
    }

    public Date getSalesTime() {
        return salesTime;
    }

    public void setSalesTime(Date salesTime) {
        this.salesTime = salesTime;
    }

    public Timestamp getSalesTimeStamp() {
        return salesTimeStamp;
    }

    public void setSalesTimeStamp(Timestamp salesTimeStamp) {
        this.salesTimeStamp = salesTimeStamp;
    }

    public String getSalesTimeString() {
        return salesTimeString;
    }

    public void setSalesTimeString(String salesTimeString) {
        this.salesTimeString = salesTimeString;
    }
    

 
    
}
