
package com.ar.settingssection;

import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;


public class SettingsSectionController implements Initializable {

    @FXML
    private AnchorPane mainAnchor;
    @FXML
    private SplitPane splitPane;
    @FXML
    private JFXButton buttonAdminPasswordChange;
    @FXML
    private JFXButton buttonBillerPasswordChange;
    @FXML
    private JFXButton buttonRegistration;
    @FXML
    private AnchorPane innersecondAnchor;

   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       try {
                AnchorPane anchor =FXMLLoader.load(getClass().getResource("/com/ar/login/LoginPassChanger.fxml"));
                innersecondAnchor.getChildren().setAll(anchor);
                AnchorPane.setBottomAnchor(anchor, 0d);
                AnchorPane.setTopAnchor(anchor, 0d);
                AnchorPane.setRightAnchor(anchor, 0d);
                AnchorPane.setLeftAnchor(anchor, 0d);
            } catch (IOException ex) {
                Logger.getLogger(SettingsSectionController.class.getName()).log(Level.SEVERE, null, ex);
            }
       buttonAdminPasswordChange.requestFocus();
    }    

    @FXML
    private void buttonAdminPasswordChangeAction(ActionEvent event) {
        try {
                AnchorPane anchor =FXMLLoader.load(getClass().getResource("/com/ar/login/LoginPassChanger.fxml"));
                innersecondAnchor.getChildren().setAll(anchor);
                AnchorPane.setBottomAnchor(anchor, 0d);
                AnchorPane.setTopAnchor(anchor, 0d);
                AnchorPane.setRightAnchor(anchor, 0d);
                AnchorPane.setLeftAnchor(anchor, 0d);
            } catch (IOException ex) {
                Logger.getLogger(SettingsSectionController.class.getName()).log(Level.SEVERE, null, ex);
            }
    }

    @FXML
    private void buttonBillerPasswordChangeAction(ActionEvent event) {
        try {
                AnchorPane anchor =FXMLLoader.load(getClass().getResource("/com/ar/passwordreseter/PasswordReseter.fxml"));
                innersecondAnchor.getChildren().setAll(anchor);
                AnchorPane.setBottomAnchor(anchor, 0d);
                AnchorPane.setTopAnchor(anchor, 0d);
                AnchorPane.setRightAnchor(anchor, 0d);
                AnchorPane.setLeftAnchor(anchor, 0d);
            } catch (IOException ex) {
                Logger.getLogger(SettingsSectionController.class.getName()).log(Level.SEVERE, null, ex);
            }
    }

    @FXML
    private void buttonRegistrationAction(ActionEvent event) {
        try {
                AnchorPane anchor =FXMLLoader.load(getClass().getResource("/com/ar/settings/Register.fxml"));
                innersecondAnchor.getChildren().setAll(anchor);
                AnchorPane.setBottomAnchor(anchor, 0d);
                AnchorPane.setTopAnchor(anchor, 0d);
                AnchorPane.setRightAnchor(anchor, 0d);
                AnchorPane.setLeftAnchor(anchor, 0d);
            } catch (IOException ex) {
                Logger.getLogger(SettingsSectionController.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
    
}
