
package com.ar.passwordreseter;

import com.ar.alertmaker.AlertMaker;
import com.ar.database.DatabaseSectionMain;
import com.ar.util.PassWordSaver;
import com.ar.util.PasswordSavingModel;
import com.google.gson.Gson;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.apache.commons.codec.digest.DigestUtils;
import org.controlsfx.control.Notifications;


public class PasswordReseterController implements Initializable {
    DatabaseSectionMain handler;

    @FXML
    private AnchorPane mainAnchorPane;
    @FXML
    private AnchorPane innerAnchorPane;
    @FXML
    private JFXTextField billerIdTextfield;
    @FXML
    private JFXButton searchButton;
    @FXML
    private ImageView imageViewBiller;
    @FXML
    private Label labelBillerStatus;
    @FXML
    private Label labelStaffName;
    @FXML
    private Label labelStaffMobile;
    @FXML
    private Label labelStaffEmail;
    @FXML
    private Label labelPasswordChange;
    @FXML
    private Label labellStaffID;

    AnchorPane anchor = new AnchorPane();
    AnchorPane forgetanchor = new AnchorPane();
    AnchorPane finisheranchor = new AnchorPane();
    
    int otp ;
    boolean booleanAadharstatusForOk = false;
    
    PassWordSaver passSaver = new PassWordSaver();
    public static final String CONFIG_FILE = "bwuap.lnp";
    FileReader file;
    List<PasswordSavingModel> newList=new ArrayList<>();
   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            handler=DatabaseSectionMain.getInstance();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(PasswordReseterController.class.getName()).log(Level.SEVERE, null, ex);
        }
        labelBillerstatusClearAction();
        colorColr();
        changePasswordAction();
        
    }    

    @FXML
    private void billerTextfieldSearchAction(ActionEvent event) {
        searchButtonAction(null);
    }
    
    @FXML
    private void clearAllButtonAction(ActionEvent event) {
        labellStaffID.setText("");
        labelStaffName.setText("");
        labelStaffMobile.setText("");
        labelStaffEmail.setText("");
        labelBillerStatus.setText("");
        labelBillerStatus.setStyle("-fx-background-color:null;");
        billerIdTextfield.clear();
    }

    @FXML
    private void searchButtonAction(ActionEvent event) {
        clearLabelsAction();
        String checkId = "select count(*) from staffs where staffsection='BILLER' and staffid='"+billerIdTextfield.getText()+"'";
        String query ="select  * from staffs where staffsection='BILLER' and staffid='"+billerIdTextfield.getText()+"'";
        Boolean checkResult = handler.billerUserNameAndPasswordCheck(checkId);//method nte peru nokanda working onnu thanne.ithu upayogikan kaaranam puthiya oru methodum koodi undaakandallo ennu karuthiyittanu 
        if(checkResult){
            System.out.println("biller with id: "+billerIdTextfield.getText()+" exists");
            labelBillerStatus.setText("biller with id: "+billerIdTextfield.getText()+" exists");
            labelBillerStatus.setStyle("-fx-text-fill:green; -fx-background-color:lightblue;");
        }
        else{
            System.out.println("biller doesnot exist with id: "+billerIdTextfield.getText());
            labelBillerStatus.setText("biller doesnot exist with id: "+billerIdTextfield.getText());
            labelBillerStatus.setStyle("-fx-text-fill:derive(red,-50%); -fx-background-color:lightblue;");
            return;
        }
        ResultSet rs = handler.resultexecQuery(query);
        try {
            while(rs.next()){
                labellStaffID.setText(rs.getString("staffid"));
                labelStaffName.setText(rs.getString("staffname"));
                labelStaffMobile.setText(rs.getString("staffmobile"));
                labelStaffEmail.setText(rs.getString("staffemail"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(PasswordReseterController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void cancelButtonAction(ActionEvent event) {
        ((Stage)mainAnchorPane.getScene().getWindow()).close();
        
    }
    
    private void labelBillerstatusClearAction(){
        billerIdTextfield.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                labelBillerStatus.setText("");
                labelBillerStatus.setStyle("-fx-background-color:null;");
            }
        });
    }
    private void clearLabelsAction(){
        labellStaffID.setText("");
        labelStaffName.setText("");
        labelStaffMobile.setText("");
        labelStaffEmail.setText("");
    }
     private BackgroundFill colorColr(){
    Stop[] stops= new Stop[]{ new Stop(0, Color.BLUE), new Stop(1, Color.RED)};//range 0 to 1
    Stop[] stops2= new Stop[]{ new Stop(0.007662835249042145, Color.valueOf("#2a1359")),
        new Stop(0.8722860791826307,Color.valueOf("#5cd6e5")),new Stop(0, Color.valueOf("#5cd6e5")),
       new Stop(1.0,Color.valueOf("#2a1359"))};//range 0 to 1
    LinearGradient igColor = new LinearGradient(0, 0, 1, 0, true, CycleMethod.REFLECT, stops);
// centerX="0.6962963104248044" centerY="0.30793650036766407"
//cycleMethod="REPEAT" focusAngle="4.72" focusDistance="0.4883722260940908" radius="1.0"
//RadialGradient values: (focusangle,focusdistance,centerx,centery,radius,proportional(boolean),cyclemethod,stops)

    RadialGradient radColor = new RadialGradient(4.72, 0.4883722260940908, 0.6962963104248044,0.30793650036766407, 1.0, true, CycleMethod.REPEAT, stops2);
    
    BackgroundFill bgfill=new BackgroundFill(igColor, CornerRadii.EMPTY, Insets.EMPTY);
    BackgroundFill bgfill2=new BackgroundFill(radColor, CornerRadii.EMPTY, Insets.EMPTY);
    mainAnchorPane.setBackground(new Background(bgfill2));
       return bgfill2;
      }
     
      private void changePasswordAction(){
        labelPasswordChange.setOnMouseClicked(event->{
//            System.out.println("hai");
            
            Scene scene1 = new Scene(anchor,550,350);
            anchor.setStyle("-fx-background-color:transparent;");
            AnchorPane.setBottomAnchor(anchor, 0.0);
            AnchorPane.setTopAnchor(anchor, 0.0);
            AnchorPane.setRightAnchor(anchor, 0.0);
            AnchorPane.setLeftAnchor(anchor, 0.0);
            
            Label labelHead = new Label("PASSWORD CHANGIGNG");
            AnchorPane.setTopAnchor(labelHead, 0.0);
            AnchorPane.setLeftAnchor(labelHead, 30.0);
            AnchorPane.setRightAnchor(labelHead, 30.0);
            labelHead.setAlignment(Pos.CENTER);
            labelHead.setNodeOrientation(NodeOrientation.INHERIT);
            labelHead.setTextAlignment(TextAlignment.CENTER);
            labelHead.setLayoutX(110);
            labelHead.setLayoutY(5);
            labelHead.setUnderline(true);
            labelHead.setTextFill(Color.DARKBLUE);
            labelHead.setFont(new Font("System", 24));
            labelHead.setStyle(" -fx-font-weight:bold;");
            labelHead.setBackground(new Background(colorColr()));
            
            Label labelPasswordStatus = new Label();
            labelPasswordStatus.setAlignment(Pos.CENTER);
            labelPasswordStatus.setNodeOrientation(NodeOrientation.INHERIT);
            labelPasswordStatus.setTextAlignment(TextAlignment.CENTER);
            labelPasswordStatus.setLayoutX(55);
            labelPasswordStatus.setLayoutY(45);
            labelPasswordStatus.setFont(new Font(12));
            labelPasswordStatus.setStyle(" -fx-font-weight:bold; -fx-text-fill:derive(red,-50%);");
            
            Label labelOldPassword = new Label("Old Password");
            labelOldPassword.setAlignment(Pos.CENTER);
            labelOldPassword.setNodeOrientation(NodeOrientation.INHERIT);
            labelOldPassword.setTextAlignment(TextAlignment.CENTER);
            labelOldPassword.setLayoutX(55);
            labelOldPassword.setLayoutY(75);
            labelOldPassword.setFont(new Font(14));
            labelOldPassword.setStyle(" -fx-font-weight:bold;");
            
            Label labelnewPassword = new Label("New Password");
            labelnewPassword.setAlignment(Pos.CENTER);
            labelnewPassword.setNodeOrientation(NodeOrientation.INHERIT);
            labelnewPassword.setTextAlignment(TextAlignment.CENTER);
            labelnewPassword.setLayoutX(55);
            labelnewPassword.setLayoutY(130);
            labelnewPassword.setFont(new Font(14));
            labelnewPassword.setStyle(" -fx-font-weight:bold;");
            
            Label labelnewConfirmPassword = new Label("Confirm Password");
            labelnewConfirmPassword.setAlignment(Pos.CENTER);
            labelnewConfirmPassword.setNodeOrientation(NodeOrientation.INHERIT);
            labelnewConfirmPassword.setTextAlignment(TextAlignment.CENTER);
            labelnewConfirmPassword.setLayoutX(55);
            labelnewConfirmPassword.setLayoutY(185);
            labelnewConfirmPassword.setFont(new Font(14));
            labelnewConfirmPassword.setStyle(" -fx-font-weight:bold;");
            
                        
            Label labelsemicol1 = new Label(":");
            labelsemicol1.setAlignment(Pos.CENTER);
            labelsemicol1.setNodeOrientation(NodeOrientation.INHERIT);
            labelsemicol1.setTextAlignment(TextAlignment.CENTER);
            labelsemicol1.setLayoutX(180);
            labelsemicol1.setLayoutY(75);
            labelsemicol1.setFont(new Font(14));
            labelsemicol1.setStyle(" -fx-font-weight:bold;");
            
            Label labelsemicol2 = new Label(":");
            labelsemicol2.setAlignment(Pos.CENTER);
            labelsemicol2.setNodeOrientation(NodeOrientation.INHERIT);
            labelsemicol2.setTextAlignment(TextAlignment.CENTER);
            labelsemicol2.setLayoutX(180);
            labelsemicol2.setLayoutY(130);
            labelsemicol2.setFont(new Font(14));
            labelsemicol2.setStyle(" -fx-font-weight:bold;");
            
            Label labelsemicol3 = new Label(":");
            labelsemicol3.setAlignment(Pos.CENTER);
            labelsemicol3.setNodeOrientation(NodeOrientation.INHERIT);
            labelsemicol3.setTextAlignment(TextAlignment.CENTER);
            labelsemicol3.setLayoutX(180);
            labelsemicol3.setLayoutY(185);
            labelsemicol3.setFont(new Font(14));
            labelsemicol3.setStyle(" -fx-font-weight:bold;");
            
                        
            TextField oldPassText = new TextField();
            oldPassText.setPromptText("Enter old password");
            oldPassText.setPrefSize(250, 35);
            oldPassText.setLayoutX(185);
            oldPassText.setLayoutY(65);
            oldPassText.setVisible(false);
            oldPassText.setStyle("-fx-text-fill:darkblue; -fx-font-weight:bold;");
            oldPassText.setFont(new Font(14));
            AnchorPane.setLeftAnchor(oldPassText, 185.0);
            AnchorPane.setRightAnchor(oldPassText, 200.0);
            
            PasswordField oldPassPass = new PasswordField();
            oldPassPass.setPromptText("Enter old password");
            oldPassPass.setPrefSize(250, 35);
            oldPassPass.setLayoutX(185);
            oldPassPass.setLayoutY(65);
            oldPassPass.setStyle("-fx-font-weight:bold;");
            oldPassPass.setFont(new Font(14));
            AnchorPane.setLeftAnchor(oldPassPass, 185.0);
            AnchorPane.setRightAnchor(oldPassPass, 200.0);
            
            TextField newPassText = new TextField();
            newPassText.setPromptText("Enter new password");
            newPassText.setPrefSize(250, 35);
            newPassText.setLayoutX(185);
            newPassText.setLayoutY(120);
            newPassText.setVisible(false);
            newPassText.setStyle("-fx-text-fill:darkblue; -fx-font-weight:bold;");
            newPassText.setFont(new Font(14));
            AnchorPane.setLeftAnchor(newPassText, 185.0);
            AnchorPane.setRightAnchor(newPassText, 200.0);
            
            PasswordField newPassPass = new PasswordField();
            newPassPass.setPromptText("Enter new password");
            newPassPass.setPrefSize(250, 35);
            newPassPass.setLayoutX(185);
            newPassPass.setLayoutY(120);
            newPassPass.setStyle(" -fx-font-weight:bold;");
            newPassPass.setFont(new Font(14));
            AnchorPane.setLeftAnchor(newPassPass, 185.0);
            AnchorPane.setRightAnchor(newPassPass, 200.0);
            
            TextField newConfirmPassText = new TextField();
            newConfirmPassText.setPromptText("Confirm new password");
            newConfirmPassText.setPrefSize(250, 35);
            newConfirmPassText.setLayoutX(185);
            newConfirmPassText.setLayoutY(175);
            newConfirmPassText.setVisible(false);
            newConfirmPassText.setStyle("-fx-text-fill:darkblue; -fx-font-weight:bold;");
            newConfirmPassText.setFont(new Font(14));
            AnchorPane.setLeftAnchor(newConfirmPassText, 185.0);
            AnchorPane.setRightAnchor(newConfirmPassText, 200.0);
            
            PasswordField newConfirmPassPass = new PasswordField();
            newConfirmPassPass.setPromptText("Confirm new password");
            newConfirmPassPass.setPrefSize(250, 35);
            newConfirmPassPass.setLayoutX(185);
            newConfirmPassPass.setLayoutY(175);
            newConfirmPassPass.setStyle("-fx-font-weight:bold;");
            newConfirmPassPass.setFont(new Font(14));
            AnchorPane.setLeftAnchor(newConfirmPassPass, 185.0);
            AnchorPane.setRightAnchor(newConfirmPassPass, 200.0);
            
            oldPassPass.textProperty().bindBidirectional(oldPassText.textProperty());
            newPassPass.textProperty().bindBidirectional(newPassText.textProperty());
            newConfirmPassPass.textProperty().bindBidirectional(newConfirmPassText.textProperty());
            
            CheckBox check = new CheckBox("show password");
            check.setLayoutX(185);
            check.setLayoutY(215);
            check.setOnAction(checkevent->{
                if(check.isSelected()){
                    check.setText("hide password");
                    oldPassPass.setVisible(false);
                    newPassPass.setVisible(false);
                    newConfirmPassPass.setVisible(false);
                    oldPassText.setVisible(true);
                    newPassText.setVisible(true);
                    newConfirmPassText.setVisible(true);
                }
                else{
                    check.setText("show password");
                    oldPassPass.setVisible(true);
                    newPassPass.setVisible(true);
                    newConfirmPassPass.setVisible(true);
                    oldPassText.setVisible(false);
                    newPassText.setVisible(false);
                    newConfirmPassText.setVisible(false);
                }
            });
            
            Label labelForgotPassword = new Label("forgot password?.");
            labelForgotPassword.setAlignment(Pos.CENTER);
            labelForgotPassword.setNodeOrientation(NodeOrientation.INHERIT);
            labelForgotPassword.setTextAlignment(TextAlignment.CENTER);
            labelForgotPassword.setLayoutX(55);
            labelForgotPassword.setLayoutY(250);
//            labelForgotPassword.setFont(new Font(11));
            labelForgotPassword.getStyleClass().add("labelpass");
            
            
             HBox hbox = new HBox();
            hbox.setLayoutX(95);
            hbox.setLayoutY(300);
            hbox.setAlignment(Pos.CENTER);
            AnchorPane.setRightAnchor(hbox, 0.0);
            AnchorPane.setLeftAnchor(hbox, 0.0);
            
            Button back= new Button("BACK");
            back.setPrefSize(120, 40);
            HBox.setMargin(back, new Insets(0, 10, 0, 0));
            
            Button ok= new Button("OK");
            ok.setPrefSize(120, 40);
            HBox.setMargin(ok, new Insets(0, 0, 0, 10));
            
//            ==================textfield , password field action kal
            oldPassPass.setOnAction(pfevent1 -> {
                newPassPass.requestFocus();
            });
            oldPassText.setOnAction(tfevent1 ->{
                newPassText.requestFocus();
            });
            newPassPass.setOnAction(pfevent2 -> {
                newConfirmPassPass.requestFocus();
            });
            newPassText.setOnAction(tfevent2 ->{
                newConfirmPassText.requestFocus();
            });
            
//      ===============================================      
            
            back.setOnMouseClicked(eventb1 ->{
                labelPasswordStatus.setText("");
                oldPassText.clear();
                newPassText.clear();
                newConfirmPassText.clear();
                check.setText("");
                mainAnchorPane.getChildren().set(0, innerAnchorPane);
                innerAnchorPane.toFront();
            });
            
            ok.setOnMouseClicked(eventb1 ->{
                if("".equals(oldPassText.getText()) || oldPassText.getText().isEmpty() || newPassText.getText().isEmpty() || 
                       "".equals(newPassText.getText()) || newConfirmPassText.getText().isEmpty() || 
                        "".equals(newConfirmPassText.getText())){
                    AlertMaker.showErrorMessage("Please fill all fields", ok);
                    return;
                }
                String passwordOldTosend = DigestUtils.sha1Hex(oldPassText.getText());
                String query = "select count(*) from staffs where staffid='"+labellStaffID.getText()+"' and password='"+passwordOldTosend+"'";
                Boolean result = handler.billerUserNameAndPasswordCheck(query);
                if(!result){
                    labelPasswordStatus.setText("old password doesnot match");
                    
                }
                else{
                    if(newConfirmPassText.getText().equals(newPassText.getText())){
                        String passwordNewtoUpdate = DigestUtils.sha1Hex(newPassText.getText());
                        String updateQuery = "update staffs set password='"+passwordNewtoUpdate+"' where staffid='"+labellStaffID.getText()+"'";
                        Boolean updateResult = handler.updatePasswordQuery(updateQuery);
                        if(updateResult){
                            AlertMaker.showSimpleAlertWithNode("PASSWORD UPDATE", "password successfully updated", ok);
                            labelPasswordStatus.setText("Password updated");
                            
       //////////////////============================////////////////////////===================================
             newList.clear();//clear the list first other wise each and every time existing list + new item oru problem undaakunadu kaaanam
             checkingSection();  //checking the config.txt file if there taking the value then retreive the data to list
             int countno = editNewPassword();
             int newListSize = newList.size();
             if(countno == newListSize){
             System.out.println("element not in array or config file");
               }
              else{
                 PasswordSavingModel passmodel = newList.get(countno);
                 passmodel.password.bind(newPassText.textProperty());
                  onSaveMoveMent(); //saving data ie , retriving data from list(newly updated(edited)) and save to file
                }
              Notifications notificationBuilderResend = Notifications.create()
                     .title("PASSWORD UPDATE")
                     .text("Password Updated Succesfully")
                     .graphic(null)
                     .hideAfter(Duration.seconds(5))
                     .position(Pos.CENTER)
                      .onAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        System.out.println("Clicked password updated notification");
                    }
                });
                notificationBuilderResend.darkStyle();
                notificationBuilderResend.showInformation();
                ////////////////=======================//////////////////////////////////////===================/////////////////////
                        }
                        else{
                            AlertMaker.showErrorMessage("Error occured in updation", ok);
                        }
                    }
                    else{
                        AlertMaker.showErrorMessage("new password and confirm password doesnot match", ok);
                    }
                }
            });
            
            oldPassText.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                labelPasswordStatus.setText("");
            }
        });
//           =======================labelforgot actions
            labelForgotPassword.setOnMouseClicked(labelforgetevent1->{
                forgetPasswordAction();
            });
//            labelForgotPassword.setOnMouseEntered(labelforgetevent2->{
//                labelForgotPassword.setStyle("-fx-text-fill:derive(red,-50%); -fx-underline:true; ");
//            });
//            labelForgotPassword.setOnMouseExited(labelforgetevent3->{
//                labelForgotPassword.setStyle("-fx-text-fill:black; -fx-font-weight:bold; -fx-underline:false; ");
//            });
//===================================
            
            hbox.getChildren().addAll(back,ok);
            
            anchor.getChildren().addAll(labelHead,labelPasswordStatus,labelOldPassword,labelnewPassword,labelnewConfirmPassword,hbox);
            anchor.getChildren().addAll(labelsemicol1,labelsemicol2,labelsemicol3);
            anchor.getChildren().addAll(oldPassText,oldPassPass);
            anchor.getChildren().addAll(newPassText,newPassPass);
            anchor.getChildren().addAll(newConfirmPassText,newConfirmPassPass);
            anchor.getChildren().add(check);
            anchor.getChildren().add(labelForgotPassword);
            
            mainAnchorPane.getChildren().set(0,anchor);
            anchor.toFront();
        });
    }
      private void forgetPasswordAction(){
//          System.out.println("hey hello");
            
            Scene scene2 = new Scene(forgetanchor,550,350);
            forgetanchor.setStyle("-fx-background-color:transparent;");
            AnchorPane.setBottomAnchor(forgetanchor, 0.0);
            AnchorPane.setTopAnchor(forgetanchor, 0.0);
            AnchorPane.setRightAnchor(forgetanchor, 0.0);
            AnchorPane.setLeftAnchor(forgetanchor, 0.0);
            
            Label labelHead = new Label("FORGOT PASSWORD");
            AnchorPane.setTopAnchor(labelHead, 0.0);
            AnchorPane.setLeftAnchor(labelHead, 30.0);
            AnchorPane.setRightAnchor(labelHead, 30.0);
            labelHead.setAlignment(Pos.CENTER);
            labelHead.setNodeOrientation(NodeOrientation.INHERIT);
            labelHead.setTextAlignment(TextAlignment.CENTER);
            labelHead.setLayoutX(110);
            labelHead.setLayoutY(5);
            labelHead.setUnderline(true);
            labelHead.setTextFill(Color.DARKBLUE);
            labelHead.setFont(new Font("System", 24));
            labelHead.setStyle(" -fx-font-weight:bold;");
            labelHead.setBackground(new Background(colorColr()));
            
            Label labelAadhar = new Label("Aadhar no. :");
            labelAadhar.setAlignment(Pos.CENTER);
            labelAadhar.setNodeOrientation(NodeOrientation.INHERIT);
            labelAadhar.setTextAlignment(TextAlignment.CENTER);
            labelAadhar.setLayoutX(20);
            labelAadhar.setLayoutY(75);
            labelAadhar.setFont(new Font(14));
            labelAadhar.setStyle(" -fx-font-weight:bold;");
            HBox.setMargin(labelAadhar, new Insets(0, 10, 0, 0));
            
            JFXTextField aadharText = new JFXTextField();
            aadharText.setPromptText("Enter aahar number");
            aadharText.setPrefSize(250, 35);
            aadharText.setLayoutX(60);
            aadharText.setLayoutY(75);
            aadharText.setStyle("-fx-text-fill:darkblue; -fx-font-weight:bold;");
            aadharText.setFont(new Font(14));
            aadharText.setLabelFloat(true);
            aadharText.setStyle("-fx-text-fill:darkblue;");
            aadharText.getStyleClass().add("searchaadharjfx-text-field");
            HBox.setMargin(aadharText, new Insets(0, 0, 0, 10));
            
            FontAwesomeIconView searchIcon = new FontAwesomeIconView(FontAwesomeIcon.SEARCH, String.valueOf(16));
            
            JFXButton aadharButton = new JFXButton("");
            aadharButton.setPrefSize(35, 35);
            aadharButton.getStyleClass().add("searchjfx-button");
            aadharButton.setStyle("-fx-background-radius:0 20 20 0;");
            aadharButton.setGraphic(searchIcon);
           
            
            HBox hboxAadhar = new HBox();
            hboxAadhar.setLayoutX(0);
            hboxAadhar.setLayoutY(100);
            hboxAadhar.setAlignment(Pos.CENTER);
            AnchorPane.setRightAnchor(hboxAadhar, 0.0);
            AnchorPane.setLeftAnchor(hboxAadhar, 0.0);
            
            Label labelAadharStatus = new Label("");
            labelAadharStatus.setAlignment(Pos.CENTER);
            labelAadharStatus.setNodeOrientation(NodeOrientation.INHERIT);
            labelAadharStatus.setTextAlignment(TextAlignment.CENTER);
//            labelAadharStatus.setLayoutX(215);
//            labelAadharStatus.setLayoutY(140);
            labelAadharStatus.setFont(new Font(12));
            labelAadharStatus.setStyle(" -fx-font-weight:bold;");
            HBox.setMargin(labelAadharStatus, new Insets(0, 0, 0, 110));
            
            HBox hboxAaaadhar = new HBox();
            hboxAaaadhar.setLayoutX(215);
            hboxAaaadhar.setLayoutY(140);
            hboxAaaadhar.setAlignment(Pos.CENTER);
            AnchorPane.setLeftAnchor(hboxAaaadhar, 0.0);
            AnchorPane.setRightAnchor(hboxAaaadhar, 0.0);
            
            JFXTextField otpText = new JFXTextField();
            otpText.setPromptText("Enter OTP");
            otpText.setPrefSize(250, 35);
            otpText.setStyle("-fx-text-fill:darkblue; -fx-font-weight:bold;");
            otpText.setFont(new Font(14));
            otpText.setLabelFloat(true);
            otpText.setStyle("-fx-text-fill:darkblue;");
            otpText.getStyleClass().add("searchaadharjfx-text-field");
            HBox.setMargin(otpText, new Insets(0, 0, 0, 100));
            
            FontAwesomeIconView otpIcon = new FontAwesomeIconView(FontAwesomeIcon.SEND_ALT, String.valueOf(16));
            
            JFXButton otpButton = new JFXButton("");
            otpButton.setPrefSize(35, 35);
            otpButton.getStyleClass().add("searchjfx-button");
            otpButton.setStyle("-fx-background-radius:0 20 20 0;");
            otpButton.setGraphic(otpIcon);
            otpButton.setTooltip(new Tooltip("reSend OTP"));
            
            
            HBox hboxOtp = new HBox();
            hboxOtp.setLayoutX(0);
            hboxOtp.setLayoutY(200);
            hboxOtp.setAlignment(Pos.CENTER);
            AnchorPane.setRightAnchor(hboxOtp, 0.0);
            AnchorPane.setLeftAnchor(hboxOtp, 0.0);
            
            HBox hbox = new HBox();
            hbox.setLayoutX(95);
            hbox.setLayoutY(300);
            hbox.setAlignment(Pos.CENTER);
            AnchorPane.setRightAnchor(hbox, 0.0);
            AnchorPane.setLeftAnchor(hbox, 0.0);
            
            Button back= new Button("BACK");
            back.setPrefSize(120, 40);
            HBox.setMargin(back, new Insets(0, 10, 0, 0));
            
            Button ok= new Button("OK");
            ok.setPrefSize(120, 40);
            HBox.setMargin(ok, new Insets(0, 0, 0, 10));
            
            EventHandler<ActionEvent> aadharEventx = new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                   if( aadharText.getText().isEmpty() || "".equals(aadharText.getText())){
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("EMPTY");
                        alert.setContentText("please fill Aaahar field");
                        alert.showAndWait();
                        System.out.println("null aadhar field");
                        return;
                    }
                    
                    System.out.println("haye hey");
          String query = "select count(*) from staffs where staffaadhar='"+aadharText.getText()+"' and "
                  + "staffid='"+labellStaffID.getText()+"'";
          Boolean aadharResult = handler.billerUserNameAndPasswordCheck(query);//method nte peru nokanda working onnu thanne.ithu upayogikan kaaranam puthiya oru methodum koodi undaakandallo ennu karuthiyittanu 
          if(aadharResult){
              labelAadharStatus.setText("aadhar no. is correct");
              labelAadharStatus.setStyle("-fx-text-fill:green; -fx-background-color:skyblue;");
              booleanAadharstatusForOk = true;
              
              //////////////////////otp making   and notification/////
              otp = (int)(Math.random()*1000000);
                System.out.println("OTP ="+otp);
//                Image img = new Image("/resorces/supermarketnew.png");
                Notifications notificationBuilder = Notifications.create()
                     .title("OTP Generator")
                     .text("OTP to Reset:"+otp)
//                     .graphic(new ImageView(img))
                     .graphic(null)
                     .hideAfter(Duration.seconds(15))
                     .position(Pos.TOP_RIGHT)
                      .onAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        System.out.println("Clicked otp notification");
                    }
                });
                notificationBuilder.darkStyle();
                notificationBuilder.showInformation();
             
                otpText.requestFocus();
          }
          else{
              labelAadharStatus.setText("aadhar no. is not match to previously saved aadhar");
              labelAadharStatus.setStyle("-fx-text-fill:derive(red,-50%); -fx-background-color:skyblue;");
              booleanAadharstatusForOk = false;
              
          }
                }
            };
            
            aadharText.setOnAction(aadharEventx);
            aadharButton.setOnAction(aadharEventx);
            
            otpButton.setOnAction(otpButtonEvent->{
                if(booleanAadharstatusForOk){
                    otp = (int)(Math.random()*1000000);
                System.out.println("OTP ="+otp);
//                Image img = new Image("/resorces/supermarketnew.png");
                Notifications notificationBuilderResend = Notifications.create()
                     .title("OTP Generator")
                     .text("OTP to Reset:"+otp)
//                     .graphic(new ImageView(img))
                     .graphic(null)
                     .hideAfter(Duration.seconds(15))
                     .position(Pos.TOP_RIGHT)
                      .onAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        System.out.println("Clicked otp notification");
                    }
                });
                notificationBuilderResend.darkStyle();
                notificationBuilderResend.showInformation();
                }
                else{
                    AlertMaker.showErrorMessage("Aadhar no. not validated", ok);
                }
            });
            
            back.setOnMouseClicked(eventb1 ->{
                labelAadharStatus.setText("");
                labelAadharStatus.setStyle("-fx-background-color:null;");
                aadharText.clear();
                otpText.clear();
                mainAnchorPane.getChildren().set(0, anchor);
                anchor.toFront();
                booleanAadharstatusForOk = false;
            });
            
            ok.setOnMouseClicked(eventb2 ->{
                if(otpText.getText().isEmpty() || "".equals(otpText.getText())){
                    AlertMaker.showErrorMessage("Empty otp\n Please type OTP", ok);
                    return;
                }
                if(booleanAadharstatusForOk){
              if(Integer.parseInt(otpText.getText()) == otp){
                  System.out.println("correct");
                  passwordResetFinisher();
              }
              else{
                  System.out.println("incorrect");
                  AlertMaker.showErrorMessage("OTP typed incorrect", ok);
              }
                }
                else{
                    System.out.println("booleanaddharstatus is false");
                    AlertMaker.showErrorMessage("Adhaar number does not correct,\n or Aaadhar doesnot exist \n or Empty aadhar textfield", ok);
                }
           });
            
            aadharText.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                labelAadharStatus.setText("");
                labelAadharStatus.setStyle("-fx-background-color:null;");
                booleanAadharstatusForOk=false;
            }
        });
            
            hboxAadhar.getChildren().addAll(labelAadhar,aadharText,aadharButton);
            hboxOtp.getChildren().addAll(otpText,otpButton);
            hbox.getChildren().addAll(back,ok);
            hboxAaaadhar.getChildren().add(labelAadharStatus);
            
//            forgetanchor.getChildren().addAll(labelHead,labelAadharStatus,hbox,hboxAadhar,hboxOtp);
            forgetanchor.getChildren().addAll(labelHead,hboxAaaadhar,hbox,hboxAadhar,hboxOtp);

            
            mainAnchorPane.getChildren().set(0,forgetanchor);
            forgetanchor.toFront();
      }

      private void passwordResetFinisher(){
          System.out.println("halllooo");
          Scene scene3 = new Scene(finisheranchor,550,350);
            finisheranchor.setStyle("-fx-background-color:transparent;");
            AnchorPane.setBottomAnchor(finisheranchor, 0.0);
            AnchorPane.setTopAnchor(finisheranchor, 0.0);
            AnchorPane.setRightAnchor(finisheranchor, 0.0);
            AnchorPane.setLeftAnchor(finisheranchor, 0.0);
            
            Label labelHead = new Label("PASSWORD RENEWING");
            AnchorPane.setTopAnchor(labelHead, 0.0);
            AnchorPane.setLeftAnchor(labelHead, 30.0);
            AnchorPane.setRightAnchor(labelHead, 30.0);
            labelHead.setAlignment(Pos.CENTER);
            labelHead.setNodeOrientation(NodeOrientation.INHERIT);
            labelHead.setTextAlignment(TextAlignment.CENTER);
            labelHead.setLayoutX(110);
            labelHead.setLayoutY(5);
            labelHead.setUnderline(true);
            labelHead.setTextFill(Color.DARKBLUE);
            labelHead.setFont(new Font("System", 24));
            labelHead.setStyle(" -fx-font-weight:bold;");
            labelHead.setBackground(new Background(colorColr()));
            
            Label labelPasswordEqual = new Label();
            labelPasswordEqual.setAlignment(Pos.CENTER);
            labelPasswordEqual.setNodeOrientation(NodeOrientation.INHERIT);
            labelPasswordEqual.setTextAlignment(TextAlignment.CENTER);
            labelPasswordEqual.setLayoutX(55);
            labelPasswordEqual.setLayoutY(45);
            labelPasswordEqual.setFont(new Font(12));
            labelPasswordEqual.setStyle(" -fx-font-weight:bold; -fx-text-fill:derive(red,-50%);");
            
                        
            Label labelRenewPassword = new Label("Enter new Password");
            labelRenewPassword.setAlignment(Pos.CENTER);
            labelRenewPassword.setNodeOrientation(NodeOrientation.INHERIT);
            labelRenewPassword.setTextAlignment(TextAlignment.CENTER);
            labelRenewPassword.setLayoutX(55);
            labelRenewPassword.setLayoutY(130);
            labelRenewPassword.setFont(new Font(14));
            labelRenewPassword.setStyle(" -fx-font-weight:bold;");
            
            Label labelRenewConfirmPassword = new Label("Confirm Password");
            labelRenewConfirmPassword.setAlignment(Pos.CENTER);
            labelRenewConfirmPassword.setNodeOrientation(NodeOrientation.INHERIT);
            labelRenewConfirmPassword.setTextAlignment(TextAlignment.CENTER);
            labelRenewConfirmPassword.setLayoutX(55);
            labelRenewConfirmPassword.setLayoutY(185);
            labelRenewConfirmPassword.setFont(new Font(14));
            labelRenewConfirmPassword.setStyle(" -fx-font-weight:bold;");
            
                        
            Label labelsemicolum1 = new Label(":");
            labelsemicolum1.setAlignment(Pos.CENTER);
            labelsemicolum1.setNodeOrientation(NodeOrientation.INHERIT);
            labelsemicolum1.setTextAlignment(TextAlignment.CENTER);
            labelsemicolum1.setLayoutX(190);
            labelsemicolum1.setLayoutY(130);
            labelsemicolum1.setFont(new Font(14));
            labelsemicolum1.setStyle(" -fx-font-weight:bold;");
            
            Label labelsemicolum2 = new Label(":");
            labelsemicolum2.setAlignment(Pos.CENTER);
            labelsemicolum2.setNodeOrientation(NodeOrientation.INHERIT);
            labelsemicolum2.setTextAlignment(TextAlignment.CENTER);
            labelsemicolum2.setLayoutX(190);
            labelsemicolum2.setLayoutY(185);
            labelsemicolum2.setFont(new Font(14));
            labelsemicolum2.setStyle(" -fx-font-weight:bold;");
            
                        
            TextField newRenewPassText = new TextField();
            newRenewPassText.setPromptText("Enter new password");
            newRenewPassText.setPrefSize(250, 35);
            newRenewPassText.setLayoutX(195);
            newRenewPassText.setLayoutY(120);
            newRenewPassText.setVisible(false);
            newRenewPassText.setStyle("-fx-text-fill:darkblue; -fx-font-weight:bold;");
            newRenewPassText.setFont(new Font(14));
            AnchorPane.setLeftAnchor(newRenewPassText, 195.0);
            AnchorPane.setRightAnchor(newRenewPassText, 200.0);
            
            PasswordField newRenewPassPass = new PasswordField();
            newRenewPassPass.setPromptText("Enter new password");
            newRenewPassPass.setPrefSize(250, 35);
            newRenewPassPass.setLayoutX(195);
            newRenewPassPass.setLayoutY(120);
            newRenewPassPass.setStyle(" -fx-font-weight:bold;");
            newRenewPassPass.setFont(new Font(14));
            AnchorPane.setLeftAnchor(newRenewPassPass, 195.0);
            AnchorPane.setRightAnchor(newRenewPassPass, 200.0);
            
            TextField newRenewConfirmPassText = new TextField();
            newRenewConfirmPassText.setPromptText("Confirm new password");
            newRenewConfirmPassText.setPrefSize(250, 35);
            newRenewConfirmPassText.setLayoutX(195);
            newRenewConfirmPassText.setLayoutY(175);
            newRenewConfirmPassText.setVisible(false);
            newRenewConfirmPassText.setStyle("-fx-text-fill:darkblue; -fx-font-weight:bold;");
            newRenewConfirmPassText.setFont(new Font(14));
            AnchorPane.setLeftAnchor(newRenewConfirmPassText, 195.0);
            AnchorPane.setRightAnchor(newRenewConfirmPassText, 200.0);
            
            PasswordField newRenewConfirmPassPass = new PasswordField();
            newRenewConfirmPassPass.setPromptText("Confirm new password");
            newRenewConfirmPassPass.setPrefSize(250, 35);
            newRenewConfirmPassPass.setLayoutX(195);
            newRenewConfirmPassPass.setLayoutY(175);
            newRenewConfirmPassPass.setStyle("-fx-font-weight:bold;");
            newRenewConfirmPassPass.setFont(new Font(14));
            AnchorPane.setLeftAnchor(newRenewConfirmPassPass, 195.0);
            AnchorPane.setRightAnchor(newRenewConfirmPassPass, 200.0);
            
            
            newRenewPassPass.textProperty().bindBidirectional(newRenewPassText.textProperty());
            newRenewConfirmPassPass.textProperty().bindBidirectional(newRenewConfirmPassText.textProperty());
            
            CheckBox checkRenew = new CheckBox("show password");
            checkRenew.setLayoutX(185);
            checkRenew.setLayoutY(215);
            checkRenew.setOnAction(checkevent->{
                if(checkRenew.isSelected()){
                    checkRenew.setText("hide password");
                    newRenewPassPass.setVisible(false);
                    newRenewConfirmPassPass.setVisible(false);
                    newRenewPassText.setVisible(true);
                    newRenewConfirmPassText.setVisible(true);
                }
                else{
                    checkRenew.setText("show password");
                    newRenewPassPass.setVisible(true);
                    newRenewConfirmPassPass.setVisible(true);
                    newRenewPassText.setVisible(false);
                    newRenewConfirmPassText.setVisible(false);
                }
            });
            
            HBox hboxButton = new HBox();
            hboxButton.setLayoutX(95);
            hboxButton.setLayoutY(300);
            hboxButton.setAlignment(Pos.CENTER);
            AnchorPane.setRightAnchor(hboxButton, 0.0);
            AnchorPane.setLeftAnchor(hboxButton, 0.0);
            
            Button back= new Button("BACK");
            back.setPrefSize(120, 40);
            HBox.setMargin(back, new Insets(0, 10, 0, 0));
            
            Button ok= new Button("OK");
            ok.setPrefSize(120, 40);
            HBox.setMargin(ok, new Insets(0, 0, 0, 10));
            
//            ====================textfield,passwordfield request focus=======///////////

            newRenewPassPass.setOnAction(pfevent01 -> {
                newRenewConfirmPassPass.requestFocus();
            });
            newRenewPassText.setOnAction(tfevent01 ->{
                newRenewConfirmPassText.requestFocus();
            });
            newRenewConfirmPassText.setOnAction(tfevent02->{
                if(newRenewPassText.getText().equals(newRenewConfirmPassText.getText())){
                    labelPasswordEqual.setText("password matched");
                    labelPasswordEqual.setStyle("-fx-background-color:skyblue; -fx-text-fill:green;");
                    ok.requestFocus();
                }
                else{
                    labelPasswordEqual.setText("new password and confirm password doesn't match");
                    labelPasswordEqual.setStyle("-fx-background-color:skyblue; -fx-text-fill:derive(red,-50%);");
                    ok.requestFocus();
                }
            });
            newRenewConfirmPassPass.setOnAction(pfevent02->{
                if(newRenewPassPass.getText().equals(newRenewConfirmPassPass.getText())){
                    labelPasswordEqual.setText("password matched");
                    labelPasswordEqual.setStyle("-fx-background-color:skyblue; -fx-text-fill:green;");
                    ok.requestFocus();
                }
                else{
                    labelPasswordEqual.setText("new password and confirm password doesn't match");
                    labelPasswordEqual.setStyle("-fx-background-color:skyblue; -fx-text-fill:derive(red,-50%);");
                    ok.requestFocus();
                }
            });
//            =============================textfield edit label clear==========
            newRenewPassText.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                labelPasswordEqual.setText("");
                newRenewConfirmPassText.setText("");
                labelPasswordEqual.setStyle("-fx-background-color:null;");
                booleanAadharstatusForOk=false;
            }
        });
            newRenewConfirmPassText.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                labelPasswordEqual.setText("");
                labelPasswordEqual.setStyle("-fx-background-color:null;");
                booleanAadharstatusForOk=false;
            }
        });
//            ================================////////////
            
            back.setOnMouseClicked(eventb1 ->{
                labelPasswordEqual.setText("");
                newRenewPassText.clear();
                newRenewConfirmPassText.clear();
                checkRenew.setText("");
                mainAnchorPane.getChildren().set(0, forgetanchor);
                forgetanchor.toFront();
            });
            ok.setOnAction(eventButtonok ->{
                Boolean flag = newRenewPassText.getText().isEmpty() || newRenewConfirmPassText.getText().isEmpty() ||
                        "".equals(newRenewPassText.getText()) || "".equals(newRenewConfirmPassText.getText());
                if(flag){
                    AlertMaker.showErrorMessage("Empty columns ,please fill all fields", ok);
                    return;
                }
                if(newRenewPassText.getText().equals(newRenewConfirmPassText.getText())){
                String passwordRenewtoUpdate = DigestUtils.sha1Hex(newRenewPassText.getText());
                String updateQuery = "update staffs set password='"+passwordRenewtoUpdate+"' where staffid='"+labellStaffID.getText()+"'";
                Boolean updateRenewResult = handler.updatePasswordQuery(updateQuery);
                if(updateRenewResult){
//                    AlertMaker.showSimpleAlertWithNode("PASSWORD UPDATE", "password successfully updated", ok);
                    
                    ////////////////////////===================================
          newList.clear();//clear the list first other wise each and every time existing list + new item oru problem undaakunadu kaaanam
          checkingSection();  //checking the config.txt file if there taking the value then retreive the data to list
          int countno = editNewPassword();
          int newListSize = newList.size();
           if(countno == newListSize){
            System.out.println("element not in array or config file");
               }
           else{
                 PasswordSavingModel passmodel = newList.get(countno);
                 passmodel.password.bind(newRenewPassText.textProperty());
                  onSaveMoveMent(); //saving data ie , retriving data from list(newly updated(edited)) and save to file
                }
         
                    
                    labelPasswordEqual.setText("password updated");
                    
                    Notifications notificationBuilderResend = Notifications.create()
                     .title("PASSWORD UPDATE")
                     .text("Password Updated Succesfully")
                     .graphic(null)
                     .hideAfter(Duration.seconds(5))
                     .position(Pos.CENTER)
                      .onAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        System.out.println("Clicked password updated notification");
                    }
                });
                notificationBuilderResend.darkStyle();
                notificationBuilderResend.showInformation();
               
//                    try {
//                        Thread.sleep(3000);                          
//                        ((Stage)mainAnchorPane.getScene().getWindow()).close();
//                    } catch (InterruptedException ex) {
//                        Logger.getLogger(PasswordReseterController.class.getName()).log(Level.SEVERE, null, ex);
//                    }
                }
                else{
                    AlertMaker.showErrorMessage("Error occured in updation", ok);
                }
                }
                else{
                     AlertMaker.showErrorMessage("new password and confirm password doesnot match", ok);
                }
            });
            
            hboxButton.getChildren().addAll(back,ok);
            
            finisheranchor.getChildren().addAll(labelHead,labelPasswordEqual,hboxButton,checkRenew);
            finisheranchor.getChildren().addAll(labelRenewPassword,labelRenewConfirmPassword,labelsemicolum1,labelsemicolum2);
            finisheranchor.getChildren().addAll(newRenewPassText,newRenewPassPass);
            finisheranchor.getChildren().addAll(newRenewConfirmPassText,newRenewConfirmPassPass);
                        
            mainAnchorPane.getChildren().set(0,finisheranchor);
            finisheranchor.toFront(); 
      }


       private void checkingSection(){
         
        try {
            file = new FileReader(CONFIG_FILE);
            Boolean flag = file.ready();
            if(flag){
                
                System.out.println("file exists");  
                retrevingDataToList();
               
        }
            else{System.out.println("file not exists");}
        } catch (FileNotFoundException ex) {
            Logger.getLogger(PasswordReseterController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PasswordReseterController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
              
    }
    private void retrevingDataToList(){
          Gson gson =new Gson();
          passSaver = gson.fromJson(file, PassWordSaver.class);
          List<PasswordSavingModel> modelList = passSaver.getPasswords().stream().map(PasswordSavingModel.PassWor::toModel).collect(Collectors.toList());
          newList.addAll(modelList);
    }
     private void onSaveMoveMent() {
        List<PasswordSavingModel.PassWor> listElement = newList.stream().map(PasswordSavingModel::toData).collect(Collectors.toList());
        Writer   writer=null;
        try {
            Gson gson =new Gson();
            writer = new FileWriter(CONFIG_FILE);
            gson.toJson(new PassWordSaver(listElement), writer);
                   } catch (IOException ex) {
            Logger.getLogger(PasswordReseterController.class.getName()).log(Level.SEVERE, null, ex);
        }
         finally{
            try {
                writer.close();
            } catch (IOException ex) {
                System.out.println("EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE");
                Logger.getLogger(PasswordReseterController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
   }
    private int editNewPassword(){
        int count = 0;
        for(PasswordSavingModel ps:newList){
            if(ps.toData().getBillerid().contains(labellStaffID.getText()))
                break;
            count=count+1;
        }
        System.out.println("count of billerid:"+count);
      return count;
    }
    
    
}


                