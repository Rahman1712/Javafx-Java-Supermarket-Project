package com.ar.salesbillprint;

import com.ar.addsales.MainSalesUIController;
import com.ar.alertmaker.AlertMaker;
import com.ar.database.BillerDatabaseSection;
import com.ar.database.DatabaseSectionMain;
import com.ar.util.SuperMarketUtils;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class SalesPrintController implements Initializable {
	DatabaseSectionMain handler;
	BillerDatabaseSection derbyhandler;
	String bnamexxx;
	String billerUserName;

	Button but;
	@FXML
	private TabPane tabPane;
	@FXML
	private Tab initialTab;
	@FXML
	private AnchorPane initialAnchorPane;
	@FXML
	private HBox hboxInitial;
	@FXML
	private AnchorPane mainAP;
	@FXML
	private AnchorPane downAnchorPane;
//    MainSalesUIController con = new MainSalesUIController();

	@FXML
	private Label billerNameLabel;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		try {
			derbyhandler = BillerDatabaseSection.getInstance();
			handler = DatabaseSectionMain.getInstance();
		} catch (ClassNotFoundException | SQLException ex) {
			Logger.getLogger(SalesPrintController.class.getName()).log(Level.SEVERE, null, ex);
		}
		String query = "select billerusername from BILLERNAME where bno=1";
		ResultSet rs = derbyhandler.resultexecQuery(query);
		try {
			while (rs.next()) {
				bnamexxx = rs.getString("billerusername");
				System.out.println("billername in SalesPrintController :" + bnamexxx);
			}
		} catch (SQLException ex) {
			Logger.getLogger(SalesPrintController.class.getName()).log(Level.SEVERE, null, ex);
		}
		billerNameLabel.setText(bnamexxx);
		try {
			tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.SELECTED_TAB);
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ar/addsales/MainSalesUI.fxml"));
			but = new Button("Cancel");
			but.setPrefHeight(40.0);
			but.setPrefWidth(135.0);
//            initialAnchorPane=FXMLLoader.load(getClass().getResource("/com/ar/addsales/MainSalesUI.fxml"));
			initialAnchorPane = loader.load();
			MainSalesUIController controller = (MainSalesUIController) loader.getController();
			controller.inflateUIBiller(billerNameLabel.getText());
			initialAnchorPane.getChildren().add(but);
			AnchorPane.setBottomAnchor(but, 30.0);
			AnchorPane.setRightAnchor(but, 300.0);
			initialTab.setContent(initialAnchorPane);

		} catch (IOException ex) {
			System.out.println("cant load mainsalesui fxml file");
			Logger.getLogger(SalesPrintController.class.getName()).log(Level.SEVERE, null, ex);
		}
		closeButtonCancelAction(but);

	}

	@FXML
	private void newButtonAction(ActionEvent event) {

		try {
			int inum;
			// aadyam last le tab ne st leku edukkunnu
			Tab t = tabPane.getSelectionModel().getSelectedItem();
			if (t == null) {
				System.out.println("NO tabssssss");
				inum = 1;
			} else {
				t = tabPane.getTabs().get(tabPane.getTabs().size() - 1);
				String lastTabName = t.getText();
				System.out.println("last tab:" + lastTabName);
				String[] arr = lastTabName.split("invoice");
				String ltabname = "";
				for (String s : arr) {
					System.out.println(s);
					ltabname = s;
				}
				inum = Integer.parseInt(ltabname);
				inum = inum + 1;
			}
			Tab tab = new Tab("invoice" + inum, new Label("new tab"));
			tabPane.getTabs().add(tab);

//            FXMLLoader loader=new FXMLLoader();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ar/addsales/MainSalesUI.fxml"));

			StackPane stackPane = new StackPane();
//            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/com/ar/addsales/MainSalesUI.fxml"));
			AnchorPane anchorPane = loader.load();
			MainSalesUIController controller = (MainSalesUIController) loader.getController();
			controller.inflateUIBiller(billerNameLabel.getText());

			but = new Button("Cancel");
			but.setPrefHeight(40.0);
			but.setPrefWidth(135.0);
			anchorPane.getChildren().add(but);
			AnchorPane.setBottomAnchor(but, 30.0);
			AnchorPane.setRightAnchor(but, 300.0);
			stackPane.getChildren().add(anchorPane);
			tab.setContent(stackPane);

			Tab selecttab = tabPane.getSelectionModel().getSelectedItem();
			String str = selecttab.getText();
			String i = selecttab.getId();
			System.out.println("tab name:" + str + "\n tab id:" + i);
			// tab focus to newly created
			tabPane.getSelectionModel().select(tab);
			// no of tabs
			int size = tabPane.getTabs().size();
			System.out.println("size===>" + size);

		} catch (IOException ex) {
			Logger.getLogger(SalesPrintController.class.getName()).log(Level.SEVERE, null, ex);
		}
		closeButtonCancelAction(but);

	}

	@FXML
	private void closeButtonAction(ActionEvent event) {
		Tab st = tabPane.getSelectionModel().getSelectedItem();
		if (st == null) {
			System.out.println("no tab selected");
		} else {
			// removing all tabs
//        st.getTabPane().getTabs().clear();
			// removing selected tab
			st.getTabPane().getTabs().remove(st);
			// set tab closable or not
//       st.setClosable(false);

		}
	}

	private void closeButtonCancelAction(Button btn) {
		btn.setOnMousePressed(e -> {
			Tab st = tabPane.getSelectionModel().getSelectedItem();
			st.getTabPane().getTabs().remove(st);
		});
	}

	public void billerNameTaking(String busernamex,String bnamex) {
		billerUserName = busernamex;
		billerNameLabel.setText(bnamex);
	}

	private String bill() {
		String str = billerNameLabel.getText();
		return str;
	}

	@FXML
	private void menuItemBillerAction(ActionEvent event) throws IOException {
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("BillerDetails.fxml"));
		FXMLLoader loader = new FXMLLoader(getClass().getResource("BillerDetailsWithTextFields.fxml"));
		Parent root = loader.load();
		Stage stage = new Stage();
		stage.setScene(new Scene(root));
		SuperMarketUtils.setStageIcon(stage);
		stage.setTitle("BILLER DETAILS");
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.initStyle(StageStyle.UTILITY);
//        BillerDetailsController controller = (BillerDetailsController)loader.getController();
		BillerDetailsWithTextFieldsController controller = (BillerDetailsWithTextFieldsController) loader
				.getController();
		/*
		String query = "select * from STAFFS where staffsection='BILLER' and username ='" + billerUserName
				+ "'";
		ResultSet rs = handler.resultexecQuery(query);
		String bidx = "";
		String namx = "";
		String mobbx = "";
		String emlx = "";
		try {
			while (rs.next()) {
				bidx = rs.getString("staffid");
				namx = rs.getString("staffname");
				mobbx = rs.getString("staffmobile");
				emlx = rs.getString("staffemail");
			}
		} catch (SQLException ex) {
			Logger.getLogger(SalesPrintController.class.getName()).log(Level.SEVERE, null, ex);
		}
		*/
//        controller.fetchingData(bidx,namx, mobbx, emlx); ////////////ithinu pakaram taayattethu upayogikunnu/////////
		controller.takingOnlyCustomerUsername(billerUserName); /// ithu upayogikkunnu
		stage.show();

	}

	@FXML
	private void menuItemLogoutAction(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ar/billerlogin/BillerLogin.fxml"));
			Parent parent = loader.load();
			Stage stage = new Stage(StageStyle.DECORATED);
			stage.setScene(new Scene(parent));
			stage.setTitle("BILLER LOGIN");
			stage.show();
			((Stage) tabPane.getScene().getWindow()).close();
		} catch (IOException ex) {
			Logger.getLogger(SalesPrintController.class.getName()).log(Level.SEVERE, null, ex);
		}

	}

	@FXML
	private void menuItemCloseApplication(ActionEvent event) {
		Boolean flag = AlertMaker.showConfirmationMessage("Are you sure close from Application", "CLOSING", tabPane);
		if (flag) {
			((Stage) tabPane.getScene().getWindow()).close();
		}
	}

	@FXML
	private void menuItemFullScreenAction(ActionEvent event) {
		Stage stage = ((Stage) tabPane.getScene().getWindow());
		stage.setFullScreen(!stage.isFullScreen());
	}

}
