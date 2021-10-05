
package com.ar.billerlogin;


import com.ar.util.SuperMarketUtils;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class BillerLoginLoader extends Application{
      @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("BillerLogin.fxml"));
        //Scene scene = new Scene(root);
        //scene.setFill(Color.TRANSPARENT);
        Scene scene = new Scene(root,Color.TRANSPARENT);
        SuperMarketUtils.setStageIcon(stage);
        stage.setTitle("BILLER LOGIN");
        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.show();
    }

    
    public static void main(String[] args) {
        launch(args);
    }
}
