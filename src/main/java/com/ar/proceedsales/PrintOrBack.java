
package com.ar.proceedsales;


public class PrintOrBack {
    int status;

    public PrintOrBack(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
    
    public int valueSend(){
        int j;
        j=getStatus();
        return j;
    }
    
}
