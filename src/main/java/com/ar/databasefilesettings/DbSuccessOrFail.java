
package com.ar.databasefilesettings;


public class DbSuccessOrFail {
    int dbStatus;

    public DbSuccessOrFail(int dbStatus) {
        this.dbStatus = dbStatus;
    }

    public int getDbStatus() {
        return dbStatus;
    }

    public void setDbStatus(int dbStatus) {
        this.dbStatus = dbStatus;
    }
    
}
