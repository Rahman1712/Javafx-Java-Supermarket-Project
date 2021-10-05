
package com.ar.productlist;

import com.ar.alertmaker.AlertMaker;
import com.ar.database.DatabaseSectionMain;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


public class ProductEditController implements Initializable {

    @FXML
    private AnchorPane mainAnchor;
    @FXML
    private JFXTextField barcodeText;
    @FXML
    private JFXTextField nameText;
    @FXML
    private JFXTextField quantityText;
    @FXML
    private JFXTextField mrpText;
    @FXML
    private JFXTextField discountText;
    @FXML
    private JFXTextField priceText;
    @FXML
    private ImageView imageView;

    DatabaseSectionMain handler;
    @FXML
    private JFXButton saveButton;
    DecimalFormat deciformat = new DecimalFormat("0.00");
    String productFirstName="";
    UpdateOrNot uOn = new UpdateOrNot(0);
    @FXML
    private JFXButton refreshButton;
    @FXML
    private JFXButton closeBarIcon;
    @FXML
    private JFXButton editBarIcon;
    @FXML
    private JFXButton closeNamIcon;
    @FXML
    private JFXButton editNamIcon;
    @FXML
    private JFXButton closePriceIcon;
    @FXML
    private JFXButton editPriceIcon;
    @FXML
    private Label labelHeading;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            handler = DatabaseSectionMain.getInstance();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(ProductEditController.class.getName()).log(Level.SEVERE, null, ex);
        }
        priceText.setEditable(false);
        closeBarIcon.setVisible(false);
        closeNamIcon.setVisible(false);
        closePriceIcon.setVisible(false);
        barcodeText.setEditable(false);
        nameText.setEditable(false);
        textFieldClearProp();
         
    }    

    @FXML
    private void saveButtonAction(ActionEvent event) {
        if(barcodeText.getText().isEmpty() || nameText.getText().isEmpty() || mrpText.getText().isEmpty() || discountText.getText().isEmpty()
                || quantityText.getText().isEmpty() || priceText.getText().isEmpty() || "".equals(barcodeText.getText()) ||
                "".equals(nameText.getText()) || "".equals(mrpText.getText()) || "".equals(discountText.getText()) || 
                "".equals(priceText.getText()) || "".equals(quantityText.getText())){
            AlertMaker.showErrorMessage("Please fill all fields", nameText);
            return;
        }
        int quantity = Integer.parseInt(quantityText.getText());
        Float mrpProd =Float.parseFloat(mrpText.getText());
        Float discProd = Float.parseFloat(discountText.getText());
        Float priceProdTaking = Float.parseFloat(priceText.getText());
        String priceToChange = deciformat.format(priceProdTaking);
        Float priceProd = Float.parseFloat(priceToChange);
        Boolean result = handler.updateQuantityOfProductsUsingFxml(barcodeText.getText(), nameText.getText(), quantity, mrpProd, discProd, priceProd,productFirstName);
        System.out.println(productFirstName);
        if(result){
//            AlertMaker.showControFxDialogMessage("PRODUCT UPDATE", "product "+productFirstName+" updated successfully", Pos.CENTER);
           uOn.setUpdateStatus(1);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                Logger.getLogger(ProductEditController.class.getName()).log(Level.SEVERE, null, ex);
            }
            ((Stage)mainAnchor.getScene().getWindow()).close();
        }
        else{
            AlertMaker.showErrorControFxDialogMessage("PRODUCT UPDATE", "Product updation Failed", Pos.CENTER);
        }
    }

    @FXML
    private void cancelButtonAction(ActionEvent event) {
        ((Stage)mainAnchor.getScene().getWindow()).close();
        uOn.setUpdateStatus(0);
    }
    private void textFieldClearProp(){
        mrpText.textProperty().addListener(new ChangeListener<String>(){
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
             priceText.setText("");
            }
            
        });
        discountText.textProperty().addListener(new ChangeListener<String>(){
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
             priceText.setText("");
            }
            
        });
    }

    @FXML
    private void barcodeAction(ActionEvent event) {
        nameText.requestFocus();
    }

    @FXML
    private void pnameAction(ActionEvent event) {
        quantityText.requestFocus();
    }

    @FXML
    private void quantityAction(ActionEvent event) {
        mrpText.requestFocus();
    }

    @FXML
    private void mrpAction(ActionEvent event) {
        discountText.requestFocus();
    }

    @FXML
    private void discountAction(ActionEvent event) {
        if(mrpText.getText().isEmpty() || "".equals(mrpText.getText()) || 
                 discountText.getText().isEmpty() || "".equals(discountText.getText())){
            return;
        }
        Float prodMrp = Float.parseFloat(mrpText.getText());
        Float prodDisc = Float.parseFloat(discountText.getText());
        Float prodPrice;
        if(prodDisc == 0 || prodDisc == 0.0 || prodDisc == 00)
              {
                prodPrice = prodMrp;
              }else
              {
                prodPrice = prodMrp * (1 - prodDisc/100);
                String newProdPrice = deciformat.format(prodPrice); //// ee 2 vari first string leku maati shesham float leku maatunnu
                prodPrice = Float.parseFloat(newProdPrice);////////////
              }
        priceText.setText(prodPrice.toString());
        saveButton.requestFocus();
        
    }

    @FXML
    private void priceAction(ActionEvent event) {
        saveButton.requestFocus();
    }

    
    public void valueTakingToFxml(String bar,String nam,String qu,String mr,String ds,String pr){
        barcodeText.setText(bar);
        nameText.setText(nam);
        quantityText.setText(qu);
        mrpText.setText(mr);
        discountText.setText(ds);
        priceText.setText(pr);
        productFirstName = nam;
    }
    public void valueTakingToFxmlForViewingOnly(String bar,String nam,String qu,String mr,String ds,String pr){
        barcodeText.setText(bar);
        nameText.setText(nam);
        quantityText.setText(qu);
        mrpText.setText(mr);
        discountText.setText(ds);
        priceText.setText(pr);
        saveButton.setVisible(false);
        refreshButton.setVisible(false);
        barcodeText.setEditable(false);
        nameText.setEditable(false);
        quantityText.setEditable(false);
        mrpText.setEditable(false);
        discountText.setEditable(false);
        priceText.setEditable(false);
        editBarIcon.setVisible(false);
        editNamIcon.setVisible(false);
        editPriceIcon.setVisible(false);
        labelHeading.setText("PRODUCT DETAILS");
    }

    @FXML
    private void refreshButtonAction(ActionEvent event) {
        discountAction(null);
    }
    public int  StatusValue(){
        return uOn.getUpdateStatus();
    }
//    public String oldNameOfProduct(){
//        return productFirstName;
//    }

    @FXML
    private void closeBarIconAction(ActionEvent event) {
        editBarIcon.setVisible(true);
        closeBarIcon.setVisible(false);
        barcodeText.setEditable(false);
      
    }

    @FXML
    private void editBarIconAction(ActionEvent event) {
        editBarIcon.setVisible(false);
        closeBarIcon.setVisible(true);
        barcodeText.setEditable(true);
        barcodeText.requestFocus();
    }

    @FXML
    private void closeNamIconAction(ActionEvent event) {
        editNamIcon.setVisible(true);
        closeNamIcon.setVisible(false);
        nameText.setEditable(false);
    }

    @FXML
    private void editNamIconAction(ActionEvent event) {
        editNamIcon.setVisible(false);
        closeNamIcon.setVisible(true);
        nameText.setEditable(true);
        nameText.requestFocus();
    }

    @FXML
    private void closePriceIconAction(ActionEvent event) {
        closePriceIcon.setVisible(false);
        editPriceIcon.setVisible(true);
        priceText.setEditable(false);
    }

    @FXML
    private void editPriceIconAction(ActionEvent event) {
        editPriceIcon.setVisible(false);
        closePriceIcon.setVisible(true);
        priceText.setEditable(true);
        priceText.requestFocus();
    }
    @FXML
    private void editPriceIconMouseEnteredAction() {
        final Tooltip tooltip = new Tooltip("Editing(manual) may be occures different value\n compare to in-built calculation");
        tooltip.setStyle("-fx-background-color:linear-gradient(to bottom,#1dbbdd44,yellow,lightyellow,yellow, #93f9b944); -fx-text-fill:derive(red,-30%);");
        Image img = new Image(getClass().getResourceAsStream("tooltipimage.png"));
        ImageView imView = new ImageView(img);
        imView.setFitHeight(20.0);
        imView.setFitWidth(20.0);
        imView.setStyle("-fx-border-color:red;");
        tooltip.setGraphic(imView);
        editPriceIcon.setTooltip(tooltip);
        editPriceIcon.setStyle("-fx-background-color:linear-gradient(to bottom,#1dbbdd44,yellow, #93f9b944);");
    }

    @FXML
    private void editPriceIconMouseExitedAction(MouseEvent event) {
        editPriceIcon.setStyle("-fx-background-color:transparent;");
    }


}
