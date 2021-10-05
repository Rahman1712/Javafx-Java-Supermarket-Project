
package com.ar.workerlist;


public class Workers {
    String worID;
    String worName;
    String worSection;
    String worMob;
    String worEmail;
    String worIDProof;
    String worAddress;
    

    public Workers(String worID, String worName, String worSection, String worMob, String worEmail, String worIDProof, String worAddress) {
        this.worID = worID;
        this.worName = worName;
        this.worSection = worSection;
        this.worMob = worMob;
        this.worEmail=worEmail;
        this.worIDProof = worIDProof;
        this.worAddress = worAddress;
        
    }

   
    public String getWorID() {
        return worID;
    }

    public void setWorID(String worID) {
        this.worID = worID;
    }

    public String getWorName() {
        return worName;
    }

    public void setWorName(String worName) {
        this.worName = worName;
    }

    public String getWorSection() {
        return worSection;
    }

    public void setWorSection(String worSection) {
        this.worSection = worSection;
    }

    public String getWorEmail() {
        return worEmail;
    }

    public void setWorEmail(String worEmail) {
        this.worEmail = worEmail;
    }
    
    

    public String getWorMob() {
        return worMob;
    }

    public void setWorMob(String worMob) {
        this.worMob = worMob;
    }

    public String getWorIDProof() {
        return worIDProof;
    }

    public void setWorIDProof(String worIDProof) {
        this.worIDProof = worIDProof;
    }

    public String getWorAddress() {
        return worAddress;
    }

    public void setWorAddress(String worAddress) {
        this.worAddress = worAddress;
    }
    
    
}
