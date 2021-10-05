
package com.ar.login;

import com.ar.alertmaker.AlertMaker;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.apache.commons.codec.digest.DigestUtils;


public class LoginPassChangerController implements Initializable {

    @FXML
    private AnchorPane mainAnchor;
    @FXML
    private FontAwesomeIconView fontIcon;
    @FXML
    private JFXButton okButton;
    @FXML
    private JFXTextField oldPassTextField;
    @FXML
    private JFXTextField newPassTextField;
    @FXML
    private JFXTextField newPassConfirmTextField;
    @FXML
    private JFXPasswordField newPassPassfield;
    @FXML
    private JFXPasswordField newPassConfPassField;
    @FXML
    private JFXCheckBox checkBox;
    @FXML
    private JFXPasswordField oldPassPassfield;
    @FXML
    private Label labelStatusEight;
    @FXML
    private Label labelPassMatch;
    @FXML
    private JFXTextField newUserTextfield;
    @FXML
    private Label labelStatusCurrentPassword;
    @FXML
    private Label labelUsernameStatus;

  
    @Override
    public void initialize(URL url, ResourceBundle rb) {
      newPassTextField.setVisible(false);
      newPassConfirmTextField.setVisible(false);
      oldPassTextField.setVisible(false);
      oldPassTextField.textProperty().bindBidirectional(oldPassPassfield.textProperty());
      newPassTextField.textProperty().bindBidirectional(newPassPassfield.textProperty());
      newPassConfirmTextField.textProperty().bindBidirectional(newPassConfPassField.textProperty());
      textfieldChangeLabelClear();
    }    

    

    @FXML
    private void cancelButtonAction(ActionEvent event) {
        ((Stage)mainAnchor.getScene().getWindow()).close();
    }

    @FXML
    private void oldPassTextFieldAction(ActionEvent event) {
        newUserTextfield.requestFocus();
    }
     @FXML
    private void oldPassPassfieldAction(ActionEvent event) {
        newUserTextfield.requestFocus();
    }
    @FXML
    private void newUserTextfieldAction(ActionEvent event) {
        if(newUserTextfield.getText().length()<5){
            labelUsernameStatus.setText("username must be atleast 5 character");
            return;
        }
        if(checkBox.isSelected()){
            newPassTextField.requestFocus();
        }else{
            newPassPassfield.requestFocus();
        }
    }

    @FXML
    private void newPassTextFieldAction(ActionEvent event) {
        if(newPassTextField.getText().length()<8){
            labelStatusEight.setText("password must be atleast 8 character");
            return;
        }
          newPassConfirmTextField.requestFocus();      
    }

    @FXML
    private void newPassConfirmTextFieldAction(ActionEvent event) {
        okButton.requestFocus();
        okButtonAction(null);
    }

    @FXML
    private void newPassPassfieldAction(ActionEvent event) {
         if(newPassPassfield.getText().length()<8){
            labelStatusEight.setText("password must be atleast 8 character");
            return;
        }
        newPassConfPassField.requestFocus();
    }

    @FXML
    private void newPassConfPassFieldAction(ActionEvent event) {
        okButton.requestFocus();
        okButtonAction(null);
    }

    @FXML
    private void checkBoxAction(ActionEvent event) {
        if(checkBox.isSelected()){
            oldPassTextField.setVisible(true);
            oldPassPassfield.setVisible(false);
            newPassTextField.setVisible(true);
            newPassPassfield.setVisible(false);
            newPassConfirmTextField.setVisible(true);
            newPassConfPassField.setVisible(false);
            checkBox.setText("hide password");
        }
        else{
            oldPassTextField.setVisible(false);
            oldPassPassfield.setVisible(true);
            newPassTextField.setVisible(false);
            newPassPassfield.setVisible(true);
            newPassConfirmTextField.setVisible(false);
            newPassConfPassField.setVisible(true);
            checkBox.setText("show password");
        }
    }

   
    
//    private void textValidatorProp(JFXTextField tf){
//        RequiredFieldValidator validator = new RequiredFieldValidator();
//        tf.getValidators().add(validator);
//        validator.setMessage("password must be eight digits");
//        tf.focusedProperty().addListener(new ChangeListener<Boolean>() {
//            @Override
//            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
//              
////                 if(!newValue){
////                     tf.validate();
////                 }
//if(tf.getText().length()<8){
//    tf.validate();
//}
//            }
//        });
//    }

    private void textfieldChangeLabelClear() {
       newPassTextField.textProperty().addListener(new ChangeListener<String>() {
           @Override
           public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
              labelStatusEight.setText("");
              labelPassMatch.setText("");
              newPassConfirmTextField.setText("");
           }
       });
       newPassConfirmTextField.textProperty().addListener(new ChangeListener<String>() {
           @Override
           public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
//              labelStatusEight.setText("");
              labelPassMatch.setText("");
              
           }
       });
       newUserTextfield.textProperty().addListener(new ChangeListener<String>() {
           @Override
           public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

              labelUsernameStatus.setText("");
              
           }
       });
       oldPassTextField.textProperty().addListener(new ChangeListener<String>() {
           @Override
           public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
               labelStatusCurrentPassword.setText("");
               fontIcon.getStyleClass().setAll("glyph-icon-norm");
              
           }
       });
    }
    @FXML
    private void okButtonAction(ActionEvent event) {
        String oldPass = oldPassTextField.getText();
        String secureOldPass = DigestUtils.sha1Hex(oldPass);
        String newUsernam = newUserTextfield.getText();
        String secureNewUsernam = DigestUtils.sha1Hex(newUsernam);
        String newPass = newPassTextField.getText();
        String newConPass = newPassConfirmTextField.getText();
        String securenewPass = DigestUtils.sha1Hex(newPass);
        LoginSave logs = LoginSave.getLogerData();
        if(newPass.isEmpty() || "".equals(newPass) || oldPass.isEmpty() || "".equals(oldPass) ||
                "".equals(newUsernam) || newUsernam.isEmpty()){
            AlertMaker.showErrorMessage("Empty columns,fill columns", mainAnchor);
            return;
        }
        if(newUsernam.length()<5){
            labelUsernameStatus.setText("username must be atleast 5 character");
            return;
        }
        if(!newPass.equals(newConPass) ){
            AlertMaker.showErrorMessage("new Password and confirm password not equal",mainAnchor);
            labelPassMatch.setText("passwords not match");
             if(newPass.length() < 8)
            {labelStatusEight.setText("password must be atleast 8 character");}
            return;
        }
        System.out.println("equals");
        if(secureOldPass.equals(logs.getPassword())){
            System.out.println("EQUALS");
            logs.setUsername(secureNewUsernam);
            logs.setPassword(securenewPass);
            LoginSave.writeNewLoginUserAndPassword(logs);
        }
        else{
            AlertMaker.showErrorMessage("current password not Matching", mainAnchor);
            labelStatusCurrentPassword.setText("current password not matching");
            fontIcon.getStyleClass().setAll("glyph-icon-error");
        }
    }

    
}
