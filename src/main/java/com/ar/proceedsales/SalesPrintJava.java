
package com.ar.proceedsales;

public class SalesPrintJava {
    private int sno;
    private String barcode;
    private String pname;
    private float mrp;
    private float disc;
    private float price;
    private int qnty;
    private float totprice;

    public SalesPrintJava(int sno, String barcode, String pname, float mrp, float disc, float price, int qnty, float totprice) {
        this.sno = sno;
        this.barcode = barcode;
        this.pname = pname;
        this.mrp = mrp;
        this.disc = disc;
        this.price = price;
        this.qnty = qnty;
        this.totprice = totprice;
    }

    

    public int getSno() {
        return sno;
    }

    public void setSno(int sno) {
        this.sno = sno;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public float getMrp() {
        return mrp;
    }

    public void setMrp(float mrp) {
        this.mrp = mrp;
    }

    public float getDisc() {
        return disc;
    }

    public void setDisc(float disc) {
        this.disc = disc;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
    

    public int getQnty() {
        return qnty;
    }

    public void setQnty(int qnty) {
        this.qnty = qnty;
    }

 
       public float getTotprice() {
        return totprice;
    }

    public void setTotprice(float totprice) {
        this.totprice = totprice;
    }
    
}
