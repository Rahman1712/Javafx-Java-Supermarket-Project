
package com.ar.proceedsales;


public class PrintItems {
    int srNo;
    String barcode;
    String name;
    float mrp;
    float discount;
    float price;
    int quantity;
    float total;

    public PrintItems(int srNo, String barcode, String name, float mrp, float discount, float price, int quantity, float total) {
        this.srNo = srNo;
        this.barcode = barcode;
        this.name = name;
        this.mrp = mrp;
        this.discount = discount;
        this.price = price;
        this.quantity = quantity;
        this.total = total;
    }

  

    public int getSrNo() {
        return srNo;
    }

    public void setSrNo(int srNo) {
        this.srNo = srNo;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getMrp() {
        return mrp;
    }

    public void setMrp(float mrp) {
        this.mrp = mrp;
    }

    public float getDiscount() {
        return discount;
    }

    public void setDiscount(float discount) {
        this.discount = discount;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
    

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }
    
}
