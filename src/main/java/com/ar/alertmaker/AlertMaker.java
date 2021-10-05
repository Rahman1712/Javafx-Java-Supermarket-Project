
package com.ar.alertmaker;

import java.util.Optional;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;


public class AlertMaker {
    
    
    public static void showErrorMessage(String content,Node node){
         Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(null);
            alert.setContentText(content);
            alert.initOwner((Stage)node.getScene().getWindow()); //making alert on top
            alert.showAndWait();
    }
    public static void showSimpleErrorMessage(String title,String content){
         Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(null);
            alert.setContentText(content);
           alert.showAndWait();
    }
    public static void showSimpleAlert(String title,String content){
         Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(title);
            alert.setContentText(content);
            alert.showAndWait();
    }
    public static void showSimpleAlertWithNode(String title,String content,Node node){
         Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(title);
            alert.setContentText(content);
            alert.initOwner((Stage)node.getScene().getWindow());
            alert.showAndWait();
    }
    public static boolean showConfirmationMessage(String content ,String title,Node node){
        Alert alert =new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.initOwner((Stage)node.getScene().getWindow());//to top
        Optional<ButtonType> response = alert.showAndWait();
        return ButtonType.OK==response.get();
    }
    public static void showControFxDialogMessage(String title,String content,Pos position){
        Notifications notificationBuilderResendError = Notifications.create()
                     .title(title)
                     .text(content)
                     .graphic(null)
                     .hideAfter(Duration.seconds(2))
                     .position(position)
                      .onAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        System.out.println("Clicked quantity updated notification");
                    }
                });
                notificationBuilderResendError.darkStyle();
                notificationBuilderResendError.showInformation();
    }
    public static void showErrorControFxDialogMessage(String title,String content,Pos position){
        Notifications notificationBuilderResendError = Notifications.create()
                     .title(title)
                     .text(content)
                     .graphic(null)
                     .hideAfter(Duration.seconds(5))
                     .position(position)
                      .onAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        System.out.println("Clicked quantity updated notification");
                    }
                });
                notificationBuilderResendError.darkStyle();
                notificationBuilderResendError.showError();
    }
            
}
