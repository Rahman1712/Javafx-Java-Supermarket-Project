
package com.ar.addsales;


public class Sales {
//    int proSerNo;
    String proBar;
    String proNam;
    Float proMR;
    Float proDs;
    int proQu;
    Float proPr;
    Float proTotPr;

    public Sales(String proBar, String proNam, Float proMR, Float proDs, int proQu, Float proPr, Float proTotPr) {
        this.proBar = proBar;
        this.proNam = proNam;
        this.proMR = proMR;
        this.proDs = proDs;
        this.proQu = proQu;
        this.proPr = proPr;
        this.proTotPr = proTotPr;
    }

//    public Sales(String proBar, String proNam, Float proMR, int proQu, Float proDs, Float proPr) {
////        this.proSerNo = proSerNo;
//        this.proBar = proBar;
//        this.proNam = proNam;
//        this.proMR = proMR;
//        this.proQu = proQu;
//        this.proDs = proDs;
//        this.proPr = proPr;
//    }

//    public int getProSerNo() {
//        return proSerNo;
//    }
//
//    public void setProSerNo(int proSerNo) {
//        this.proSerNo = proSerNo;
//    }

    public String getProBar() {
        return proBar;
    }

    public void setProBar(String proBar) {
        this.proBar = proBar;
    }

    public String getProNam() {
        return proNam;
    }

    public void setProNam(String proNam) {
        this.proNam = proNam;
    }

    public Float getProMR() {
        return proMR;
    }

    public void setProMR(Float proMR) {
        this.proMR = proMR;
    }

    public int getProQu() {
        return proQu;
    }

    public void setProQu(int proQu) {
        this.proQu = proQu;
    }

    public Float getProDs() {
        return proDs;
    }

    public void setProDs(Float proDs) {
        this.proDs = proDs;
    }
    

    public Float getProPr() {
        return proPr;
    }

    public void setProPr(Float proPr) {
        this.proPr = proPr;
    }

    public Float getProTotPr() {
        return proTotPr;
    }

    public void setProTotPr(Float proTotPr) {
        this.proTotPr = proTotPr;
    }

   
    
}
