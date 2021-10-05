
package com.ar.maker;

import com.ar.util.SuperMarketUtils;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SecurePasswordMakerLoader extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("SecurePasswordMaker.fxml"));
		stage.setTitle("Register Key Generator");
		stage.setScene(new Scene(root));
		SuperMarketUtils.setStageIcon(stage);
		stage.show();

	}

	public static void main(String[] args) {
		launch(args);
	}
}
