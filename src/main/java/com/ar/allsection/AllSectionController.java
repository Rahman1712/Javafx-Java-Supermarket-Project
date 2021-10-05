
package com.ar.allsection;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AllSectionController implements Initializable {

	@FXML
	private JFXHamburger hamburgerMenu;
	@FXML
	private AnchorPane anchorPaneContent;
	@FXML
	private JFXDrawer jfxDrawerOne;
	@FXML
	private AnchorPane anchorPaneDrawer;

	Button buttonAddProducts = new Button("ADD PRODUCTS");
	Button buttonAddWorker = new Button("ADD WORKER");
	Button buttonViewProducts = new Button("VIEW PRODUCTS");
	Button buttonViewWorkers = new Button("VIEW WORKERS");
	Button buttonViewCustomers = new Button("VIEW CUSTOMERS");
	Button buttonViewSales = new Button("VIEW SALES");
	Button buttonViewSettings = new Button("SETTINGS");
	@FXML
	private AnchorPane mainAnchorPane;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		try {
			AnchorPane anchor = FXMLLoader.load(getClass().getResource("/com/ar/addproducts/Products.fxml"));
			anchorPaneContent.getChildren().setAll(anchor);
			AnchorPane.setBottomAnchor(anchor, 0d);
			AnchorPane.setTopAnchor(anchor, 0d);
			AnchorPane.setRightAnchor(anchor, 0d);
			AnchorPane.setLeftAnchor(anchor, 0d);

		} catch (IOException ex) {
			Logger.getLogger(AllSectionController.class.getName()).log(Level.SEVERE, null, ex);
		}
		hamburgerAction();
		drawerButtonActions();
		buttonHoverEffect();
	}

	private void hamburgerAction() {
		VBox vboxSet = new VBox();
		vboxSet.setLayoutX(0);
		vboxSet.setLayoutY(0);
		vboxSet.setPrefHeight(435);
		vboxSet.setPrefWidth(200);

		buttonSizeSet(buttonAddProducts);
		buttonSizeSet(buttonAddWorker);
		buttonSizeSet(buttonViewProducts);
		buttonSizeSet(buttonViewWorkers);
		buttonSizeSet(buttonViewCustomers);
		buttonSizeSet(buttonViewSales);
		buttonSizeSet(buttonViewSettings);
		vboxSet.getChildren().addAll(buttonAddProducts, buttonAddWorker, buttonViewProducts, buttonViewWorkers,
				buttonViewCustomers, buttonViewSales, buttonViewSettings);
		jfxDrawerOne.setSidePane(vboxSet);

		HamburgerSlideCloseTransition task = new HamburgerSlideCloseTransition(hamburgerMenu);
		task.setRate(-1);
		hamburgerMenu.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<Event>() {
			@Override
			public void handle(Event event) {
				task.setRate(task.getRate() * -1);
				task.play();
				if (jfxDrawerOne.isClosed()) {
					jfxDrawerOne.open();
					jfxDrawerOne.setPrefWidth(200);
					anchorPaneDrawer.setPrefWidth(200);
				} else {
					jfxDrawerOne.close();
					anchorPaneDrawer.setPrefWidth(0);
				}
			}

		});
	}

	private void buttonSizeSet(Button but) {
		but.setPrefSize(200, 100);
		but.setStyle("-fx-background-color:linear-gradient(to bottom,#1dbbdd44, #93f9b944); "
				+ "-fx-border-color:linear-gradient(from 43.174604234241304% 4.444442022414435% to 45.079363868350075% 86.34920460837228%,\n"
				+ "        #a1a674,\n" + "       #a1a674  11.255941330594728%,\n"
				+ "       #8f8b39 56.55093415491567%,\n" + "       #8e8f60 95.36016642308857%,\n"
				+ "       #8e8f60 100%);");
	}

	private void drawerButtonActions() {
		buttonAddProducts.setOnMouseClicked(event -> {
			try {
				AnchorPane anchor = FXMLLoader.load(getClass().getResource("/com/ar/addproducts/Products.fxml"));
				anchorPaneContent.getChildren().setAll(anchor);
				AnchorPane.setBottomAnchor(anchor, 0d);
				AnchorPane.setTopAnchor(anchor, 0d);
				AnchorPane.setRightAnchor(anchor, 0d);
				AnchorPane.setLeftAnchor(anchor, 0d);

			} catch (IOException ex) {
				Logger.getLogger(AllSectionController.class.getName()).log(Level.SEVERE, null, ex);
			}
		});
		buttonAddWorker.setOnMouseClicked(event -> {
			try {
				AnchorPane anchor = FXMLLoader.load(getClass().getResource("/com/ar/addworker/AddWorker.fxml"));
				anchorPaneContent.getChildren().setAll(anchor);
				AnchorPane.setBottomAnchor(anchor, 0d);
				AnchorPane.setTopAnchor(anchor, 0d);
				AnchorPane.setRightAnchor(anchor, 0d);
				AnchorPane.setLeftAnchor(anchor, 0d);

			} catch (IOException ex) {
				Logger.getLogger(AllSectionController.class.getName()).log(Level.SEVERE, null, ex);
			}
		});
		buttonViewProducts.setOnMouseClicked(event -> {
			try {
				AnchorPane anchor = FXMLLoader.load(getClass().getResource("/com/ar/productlist/ProductList.fxml"));
				anchorPaneContent.getChildren().setAll(anchor);
				AnchorPane.setBottomAnchor(anchor, 0d);
				AnchorPane.setTopAnchor(anchor, 0d);
				AnchorPane.setRightAnchor(anchor, 0d);
				AnchorPane.setLeftAnchor(anchor, 0d);
			} catch (IOException ex) {
				Logger.getLogger(AllSectionController.class.getName()).log(Level.SEVERE, null, ex);
			}
		});
		buttonViewWorkers.setOnMouseClicked(event -> {
			try {
				AnchorPane anchor = FXMLLoader.load(getClass().getResource("/com/ar/workerlist/WorkerList.fxml"));
				anchorPaneContent.getChildren().setAll(anchor);
				AnchorPane.setBottomAnchor(anchor, 0d);
				AnchorPane.setTopAnchor(anchor, 0d);
				AnchorPane.setRightAnchor(anchor, 0d);
				AnchorPane.setLeftAnchor(anchor, 0d);
			} catch (IOException ex) {
				Logger.getLogger(AllSectionController.class.getName()).log(Level.SEVERE, null, ex);
			}
		});
		buttonViewCustomers.setOnMouseClicked(event -> {
			try {
				AnchorPane anchor = FXMLLoader.load(getClass().getResource("/com/ar/customerlist/CustomerList.fxml"));
				anchorPaneContent.getChildren().setAll(anchor);
				AnchorPane.setBottomAnchor(anchor, 0d);
				AnchorPane.setTopAnchor(anchor, 0d);
				AnchorPane.setRightAnchor(anchor, 0d);
				AnchorPane.setLeftAnchor(anchor, 0d);
			} catch (IOException ex) {
				Logger.getLogger(AllSectionController.class.getName()).log(Level.SEVERE, null, ex);
			}
		});
		buttonViewSales.setOnMouseClicked(event -> {
			try {
				AnchorPane anchor = FXMLLoader.load(getClass().getResource("/com/ar/saleslist/SalesList.fxml"));
				anchorPaneContent.getChildren().setAll(anchor);
				AnchorPane.setBottomAnchor(anchor, 0d);
				AnchorPane.setTopAnchor(anchor, 0d);
				AnchorPane.setRightAnchor(anchor, 0d);
				AnchorPane.setLeftAnchor(anchor, 0d);
			} catch (IOException ex) {
				Logger.getLogger(AllSectionController.class.getName()).log(Level.SEVERE, null, ex);
			}
		});
		buttonViewSettings.setOnMouseClicked(event -> {
			try {
				AnchorPane anchor = FXMLLoader
						.load(getClass().getResource("/com/ar/settingssection/SettingsSection.fxml"));
				anchorPaneContent.getChildren().setAll(anchor);
				AnchorPane.setBottomAnchor(anchor, 0d);
				AnchorPane.setTopAnchor(anchor, 0d);
				AnchorPane.setRightAnchor(anchor, 0d);
				AnchorPane.setLeftAnchor(anchor, 0d);
			} catch (IOException ex) {
				Logger.getLogger(AllSectionController.class.getName()).log(Level.SEVERE, null, ex);
			}
		});
	}

	private void loadNextScreen() {
		try {
			Parent popupScene;
			popupScene = (AnchorPane) FXMLLoader
					.load(getClass().getResource("/com/ar/allsection/AllSectionPopUpMode.fxml"));
			Scene newScene = new Scene(popupScene);
			Stage curStage = (Stage) mainAnchorPane.getScene().getWindow();
			curStage.setScene(newScene);
		} catch (IOException ex) {
			Logger.getLogger(AllSectionController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	private void buttonHoverEffect() {
		ArrayList<Button> buts = new ArrayList<>();
		buts.add(buttonAddWorker);
		buts.add(buttonAddProducts);
		buts.add(buttonViewProducts);
		buts.add(buttonViewCustomers);
		buts.add(buttonViewSales);
		buts.add(buttonViewWorkers);
		buts.add(buttonViewSettings);
		buts.forEach((b) -> {
			b.setOnMouseEntered(e -> b.setStyle("-fx-background-color:skyblue; -fx-border-color:white;"));
			b.setOnMouseExited(e -> b.setStyle(
					"-fx-background-color:linear-gradient(to bottom,#1dbbdd44, #93f9b944); -fx-border-color:linear-gradient(from 43.174604234241304% 4.444442022414435% to 45.079363868350075% 86.34920460837228%,\n"
							+ "        #a1a674,\n" + "       #a1a674  11.255941330594728%,\n"
							+ "       #8f8b39 56.55093415491567%,\n" + "       #8e8f60 95.36016642308857%,\n"
							+ "       #8e8f60 100%);"));
		});

	}
}
