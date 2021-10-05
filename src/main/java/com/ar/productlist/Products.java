
package com.ar.productlist;


public class Products {
    String prodBarcode;
    String prodName;
    int prodQuantity;
    float prodMRP;
    float prodDiscount;
    float prodPrice;

    public Products(String prodBarcode, String prodName, int prodQuantity, float prodMRP, float prodDiscount, float prodPrice ) {
        this.prodBarcode = prodBarcode;
        this.prodName = prodName;
        this.prodQuantity = prodQuantity;
        this.prodMRP = prodMRP;
        this.prodDiscount = prodDiscount;
        this.prodPrice = prodPrice;
      
    }

    public String getProdBarcode() {
        return prodBarcode;
    }

    public void setProdBarcode(String prodBarcode) {
        this.prodBarcode = prodBarcode;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public int getProdQuantity() {
        return prodQuantity;
    }

    public void setProdQuantity(int prodQuantity) {
        this.prodQuantity = prodQuantity;
    }

    public float getProdMRP() {
        return prodMRP;
    }

    public void setProdMRP(float prodMRP) {
        this.prodMRP = prodMRP;
    }

    public float getProdDiscount() {
        return prodDiscount;
    }

    public void setProdDiscount(float prodDiscount) {
        this.prodDiscount = prodDiscount;
    }

    public float getProdPrice() {
        return prodPrice;
    }

    public void setProdPrice(float prodPrice) {
        this.prodPrice = prodPrice;
    }
    
    
}
