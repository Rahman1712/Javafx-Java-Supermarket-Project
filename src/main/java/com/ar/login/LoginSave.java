
package com.ar.login;

import com.ar.alertmaker.AlertMaker;
import com.google.gson.Gson;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Pos;
import org.apache.commons.codec.digest.DigestUtils;


public final class LoginSave {
    String username;
    String password;
    public static final String USER_FILE = "logadm.sak"; /////SAK

    public LoginSave() {
        setUsername(DigestUtils.sha1Hex("admin"));
        setPassword(DigestUtils.sha1Hex("admin"));
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public static void initConfiger(){
        Writer writer = null;
        try {
            LoginSave logsav = new LoginSave();
            Gson gson = new Gson();
            writer = new FileWriter(USER_FILE);
            gson.toJson(logsav,writer);
        } catch (IOException ex) {
            Logger.getLogger(LoginSave.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally{
            try {
                writer.close();
            } catch (IOException ex) {
                Logger.getLogger(LoginSave.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    public static LoginSave getLogerData(){
        Gson gson = new Gson();
        LoginSave logsaves = new LoginSave();
        try {
            logsaves = gson.fromJson(new FileReader(USER_FILE), LoginSave.class);
        } catch (FileNotFoundException ex) {
            initConfiger();
            Logger.getLogger(LoginSave.class.getName()).log(Level.SEVERE, null, ex);
        }
        return logsaves;
    }
    public static void writeNewLoginUserAndPassword(LoginSave loggs){
        Writer writer = null;
        
        try {
            writer = new FileWriter(USER_FILE);
            Gson gson = new Gson();
            gson.toJson(loggs,writer);
            AlertMaker.showControFxDialogMessage("UPDATION", "Username & Password updated", Pos.CENTER);
        } catch (IOException ex) {
            AlertMaker.showErrorControFxDialogMessage("UPDATION Failed", "Username and password updation Failed!!", Pos.CENTER);
            Logger.getLogger(LoginSave.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally{
            try {
                writer.close();
            } catch (IOException ex) {
                Logger.getLogger(LoginSave.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
  }
    
}
