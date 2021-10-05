

package com.ar.settings;

import com.google.gson.Gson;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.codec.digest.DigestUtils;


public class Preferences {
    String keyActivation;
    public static final String CONFIG_FILE="config.sak";

    public Preferences() {
        keyActivation = DigestUtils.sha1Hex("AR12d0r017km6YsM");
    }

    public String getKeyActivation() {
        return keyActivation;
    }

    public void setKeyActivation(String keyActivation) {
        this.keyActivation = keyActivation;
    }
    
    public static void initConfig(){
        Writer writer = null;
        try {
            
            Preferences preferences = new Preferences();
            Gson gson = new Gson();
            writer = new FileWriter(CONFIG_FILE);
            gson.toJson(preferences,writer);
        } catch (IOException ex) {
            Logger.getLogger(Preferences.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally{
            try {
                writer.close();
            } catch (IOException ex) {
                Logger.getLogger(Preferences.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    public static Preferences getPreferences(){
        Gson gson =new Gson();
        Preferences preferences = new Preferences();
        try {
            preferences = gson.fromJson(new FileReader(CONFIG_FILE), Preferences.class);
        } catch (FileNotFoundException ex) {
            initConfig();
            Logger.getLogger(Preferences.class.getName()).log(Level.SEVERE, null, ex);
        }
        return preferences;
    }
    public static Preferences getPreferencesFromFileChooser(String pathfile){
        Gson gson =new Gson();
        Preferences preferences = new Preferences();
        try {
            preferences = gson.fromJson(new FileReader(pathfile), Preferences.class);
        } catch (FileNotFoundException ex) {
//            initConfig();
            Logger.getLogger(Preferences.class.getName()).log(Level.SEVERE, null, ex);
        }
        return preferences;
    }
}
