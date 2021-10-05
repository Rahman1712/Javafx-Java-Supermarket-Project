package com.ar.allsection;

import com.ar.alertmaker.AlertMaker;
import com.jfoenix.controls.JFXToggleButton;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

public class ForCompanyController implements Initializable {

	@FXML
	private JFXToggleButton toggleButtonSwitch;
	@FXML
	private AnchorPane mainAnchorPane;
	@FXML
	private AnchorPane contentAnchorPaneInborderPane;

	boolean switchpopupstatus = false;

	@Override
	public void initialize(URL url, ResourceBundle rb) {

		try {
			AnchorPane popupScene;
			popupScene = (AnchorPane) FXMLLoader.load(getClass().getResource("/com/ar/allsection/AllSection.fxml"));
			AnchorPane.setBottomAnchor(popupScene, 0d);
			AnchorPane.setTopAnchor(popupScene, 0d);
			AnchorPane.setRightAnchor(popupScene, 0d);
			AnchorPane.setLeftAnchor(popupScene, 0d);
			contentAnchorPaneInborderPane.getChildren().setAll(popupScene);
		} catch (IOException ex) {
			Logger.getLogger(ForCompanyController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@FXML
	private void switchModeToggleAction(ActionEvent event) {
		Boolean flag = AlertMaker.showConfirmationMessage("Change to PopUp Mode", "CHANGE TO POPUP MODE",
				mainAnchorPane);
		if (flag) {
			if (switchpopupstatus) {
				makeFadeOut("/com/ar/allsection/AllSection.fxml");
				makeFadeIn();
				toggleButtonSwitch.setSelected(false);
				toggleButtonSwitch.setText("switch to popup mode");
				switchpopupstatus = false;
			} else {
				makeFadeOut("/com/ar/allsection/AllSectionPopUpMode.fxml");
				makeFadeIn();
				toggleButtonSwitch.setSelected(false);
				toggleButtonSwitch.setText("switch to slider mode");
				switchpopupstatus = true;
			}
		} else {
			toggleButtonSwitch.setSelected(false);
		}
	}

	private void makeFadeOut(String fxmlurl) {
		FadeTransition fadeTransition = new FadeTransition();
		fadeTransition.setDuration(Duration.millis(1000));
		fadeTransition.setNode(contentAnchorPaneInborderPane);
		fadeTransition.setFromValue(1);
		fadeTransition.setToValue(0);
		fadeTransition.setOnFinished(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				loadNextScreen(fxmlurl);
			}
		});
		fadeTransition.play();

	}

	private void makeFadeIn() {
		FadeTransition fadeTransition = new FadeTransition();
		fadeTransition.setDuration(Duration.millis(2000));
		fadeTransition.setNode(contentAnchorPaneInborderPane);
		fadeTransition.setFromValue(0);
		fadeTransition.setToValue(1);
		fadeTransition.play();
	}

	private void loadNextScreen(String fxmlurl) {
		try {
			AnchorPane popupScene = (AnchorPane) FXMLLoader.load(getClass().getResource(fxmlurl));
			AnchorPane.setBottomAnchor(popupScene, 0d);
			AnchorPane.setTopAnchor(popupScene, 0d);
			AnchorPane.setRightAnchor(popupScene, 0d);
			AnchorPane.setLeftAnchor(popupScene, 0d);
			contentAnchorPaneInborderPane.getChildren().setAll(popupScene);
		} catch (IOException ex) {
			Logger.getLogger(AllSectionController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
