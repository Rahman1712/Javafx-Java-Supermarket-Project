
package com.ar.databasefilesettings;

import com.google.gson.Gson;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

public final class DbSetFile {
    String dbDatabase;
   String dbUserName;
   String dbPassword;
   
    public static final String DB_FILE = "Dbsettings.txt";

    public DbSetFile() {
        setDbDatabase("supermarketdb");
        setDbUserName("root");
        setDbPassword("arrahmankm");
    }

    public String getDbDatabase() {
        return dbDatabase;
    }

    public void setDbDatabase(String dbDatabase) {
        this.dbDatabase = dbDatabase;
    }
    

    public String getDbUserName() {
        return dbUserName;
    }

    public void setDbUserName(String dbUserName) {
        this.dbUserName = dbUserName;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }
    public static void initConfiger(){
        Writer writer = null;
        try {
            DbSetFile dbset = new DbSetFile();
            Gson gson = new Gson();
            writer = new FileWriter(DB_FILE);
            gson.toJson(dbset,writer);
        } catch (IOException ex) {
            Logger.getLogger(DbSetFile.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally{
            try {
                writer.close();
            } catch (IOException ex) {
                Logger.getLogger(DbSetFile.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    public static DbSetFile getLogerData(){
        Gson gson = new Gson();
        DbSetFile dbsets = new DbSetFile();
        try {
            dbsets = gson.fromJson(new FileReader(DB_FILE), DbSetFile.class);
        } catch (FileNotFoundException ex) {
            initConfiger();
            Logger.getLogger(DbSetFile.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dbsets;
    }
    public static void writeNewLoginUserAndPassword(DbSetFile dbsets){
        Writer writer = null;
        
        try {
            writer = new FileWriter(DB_FILE);
            Gson gson = new Gson();
            gson.toJson(dbsets,writer);
            Notifications notificationBuilderResendError = Notifications.create()
                     .title("UPDATION")
                     .text("Database,Username & Password updated")
                     .graphic(null)
                     .hideAfter(Duration.seconds(2))
                     .position( Pos.CENTER)
                      .onAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        System.out.println("Clicked quantity updated notification");
                    }
                });
                notificationBuilderResendError.darkStyle();
                notificationBuilderResendError.showInformation();
        } catch (IOException ex) {
             Notifications notificationBuilderResendError = Notifications.create()
                     .title("UPDATION Failed")
                     .text("Database,Username and password updation Failed!!")
                     .graphic(null)
                     .hideAfter(Duration.seconds(5))
                     .position(Pos.CENTER)
                      .onAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        System.out.println("Clicked quantity updated notification");
                    }
                });
                notificationBuilderResendError.darkStyle();
                notificationBuilderResendError.showError();
            Logger.getLogger(DbSetFile.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally{
            try {
                writer.close();
            } catch (IOException ex) {
                Logger.getLogger(DbSetFile.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
  }
}
