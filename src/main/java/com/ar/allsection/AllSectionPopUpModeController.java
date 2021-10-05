
package com.ar.allsection;

import com.ar.util.SuperMarketUtils;
import com.jfoenix.controls.JFXDatePicker;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;


public class AllSectionPopUpModeController implements Initializable {

    @FXML
    private AnchorPane mainAnchorPane;
    @FXML
    private JFXDatePicker dateJfxPicker;

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        mainAnchorPane.setOpacity(0);
        makeFadeInTransition();
        dateJfxPicker.setValue(LocalDate.now());
    }    

    private void makeFadeInTransition() {
        FadeTransition fadeTransition = new FadeTransition();
        fadeTransition.setDuration(Duration.millis(1000));
        fadeTransition.setNode(mainAnchorPane);
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);
        fadeTransition.play();
    }

    @FXML
    private void addProductsButtonAction(ActionEvent event) throws IOException  {
        stageOpenByButtonClick("/com/ar/addproducts/Products.fxml","ADD PRODUCTS");
    }

    @FXML
    private void addWorkerButtonaction(ActionEvent event) throws IOException {
        stageOpenByButtonClick("/com/ar/addworker/AddWorker.fxml","ADD STAFFS");
    }

    @FXML
    private void viewProductsButtonAction(ActionEvent event) throws IOException {
        stageOpenByButtonClick("/com/ar/productlist/ProductList.fxml","PRODUCTS LIST");
    }

    @FXML
    private void viewStaffsButtonaction(ActionEvent event) throws IOException {
        stageOpenByButtonClick("/com/ar/workerlist/WorkerList.fxml","WORKER LIST");
    }

    @FXML
    private void viewCustomerButtonaction(ActionEvent event) throws IOException {
        stageOpenByButtonClick("/com/ar/customerlist/CustomerList.fxml","CUSTOMER LIST");
    }

    @FXML
    private void viewSalesButttonAction(ActionEvent event) throws IOException {
        stageOpenByButtonClick("/com/ar/saleslist/SalesList.fxml","SALES LIST");
    }
    @FXML
    private void viewSettingsButtonAction(ActionEvent event) throws IOException {
        stageOpenByButtonClick("/com/ar/settingssection/SettingsSection.fxml","SETTINGS");
    }
    
    private void stageOpenByButtonClick(String url,String title) throws IOException{
         Parent root = FXMLLoader.load(getClass().getResource(url));
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle(title);
        SuperMarketUtils.setStageIcon(stage);
//        stage.setResizable(false);  or   stage.resizableProperty().setValue(Boolean.FALSE);
//        stage.initModality(Modality.NONE);  ithil pudiyathu veendum turakkaam
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setAlwaysOnTop(true);
        stage.show();
    }

    
}
