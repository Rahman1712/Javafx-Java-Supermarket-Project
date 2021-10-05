
package com.ar.util;

import java.util.List;


public class PassWordSaver {
     private List<PasswordSavingModel.PassWor> passwords;

    public PassWordSaver() {
    }

    public PassWordSaver(List<PasswordSavingModel.PassWor> passwords) {
        this.passwords = passwords;
    }

    public List<PasswordSavingModel.PassWor> getPasswords() {
        return passwords;
    }

    public void setPasswords(List<PasswordSavingModel.PassWor> passwords) {
        this.passwords = passwords;
    }
     

}
