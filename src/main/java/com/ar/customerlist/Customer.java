
package com.ar.customerlist;

public class Customer {
    String cusid;
    String cusname;
    String cusmob;
    String cusplace;

    public Customer(String cusid, String cusname, String cusmob, String cusplace) {
        this.cusid = cusid;
        this.cusname = cusname;
        this.cusmob = cusmob;
        this.cusplace = cusplace;
    }

    public String getCusid() {
        return cusid;
    }

    public void setCusid(String cusid) {
        this.cusid = cusid;
    }

    public String getCusname() {
        return cusname;
    }

    public void setCusname(String cusname) {
        this.cusname = cusname;
    }

    public String getCusmob() {
        return cusmob;
    }

    public void setCusmob(String cusmob) {
        this.cusmob = cusmob;
    }

    public String getCusplace() {
        return cusplace;
    }

    public void setCusplace(String cusplace) {
        this.cusplace = cusplace;
    }
    
}
