
package com.ar.addproducts;

import com.ar.alertmaker.AlertMaker;
import com.ar.database.DatabaseSectionMain;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.stage.Stage;

public class ProductsController implements Initializable {

	DatabaseSectionMain handler;
	@FXML
	private TextField prodbarcode;
	@FXML
	private TextField prodname;
	@FXML
	private TextField prodquand;
	@FXML
	private TextField prodmrp;
	@FXML
	private TextField prodprice;
	@FXML
	private TextField proddisc;
	@FXML
	private Button buttonADD;

	String productBarcode;
	String productName;
	int productQuantity;
	float productMRP;
	float productDiscount;
	float productPrice;
	@FXML
	private AnchorPane mainAnchorPane;

	DecimalFormat deciformat = new DecimalFormat("0.00");
	@FXML
	private ComboBox<String> comboBox;
	@FXML
	private HBox labelGreen;
	ObservableList<String> prnameList = FXCollections.observableArrayList();

	boolean isUpdateOrNot = false;
	@FXML
	private Label labelNumber;
	@FXML
	private Label labelQuantity;
	@FXML
	private Label labelHeading;

	int existingQuand = 0;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		try {
			handler = DatabaseSectionMain.getInstance();
		} catch (ClassNotFoundException | SQLException ex) {
			Logger.getLogger(ProductsController.class.getName()).log(Level.SEVERE, null, ex);
		}
		mrpOrQuandChangePriceTextfieldClear();
		comboBox.setDisable(true);
		labelGreen.setVisible(false);
		labelQuantity.setVisible(false);
		barcodeTypeSearchDB();
		comboBoxTooltip("");
		labelNumber.setVisible(false);
	}

	@FXML
	private void actionProdBC(ActionEvent event) {
		prodname.requestFocus();
	}

	@FXML
	private void actionProdN(ActionEvent event) {
		prodquand.requestFocus();
	}

	@FXML
	private void actionProdQ(ActionEvent event) {
		prodmrp.requestFocus();
	}

	@FXML
	private void actionProdMRP(ActionEvent event) {
		proddisc.requestFocus();

	}

	@FXML
	private void actionProdDisc(ActionEvent event) {

		productMRP = Float.parseFloat(prodmrp.getText());
		if (proddisc.getText().isEmpty()) {
			productPrice = productMRP;
		} else {
			productDiscount = Float.parseFloat(proddisc.getText());
			if (productDiscount == 0 || productDiscount == 0.0 || productDiscount == 00) {
				productPrice = productMRP;
			} else {
				productPrice = productMRP * (1 - productDiscount / 100);
				String newProdPrice = deciformat.format(productPrice); //// ee 2 vari first string leku maati shesham
																		//// float leku maatunnu
				productPrice = Float.parseFloat(newProdPrice);////////////
			}
		}

		String stringProdPrice = deciformat.format(productPrice);
		prodprice.setText(stringProdPrice);
		prodprice.setEditable(false);
		buttonADD.requestFocus();
	}

	@FXML
	private void productAddAction(ActionEvent event) {

		Boolean flag = prodbarcode.getText().isEmpty() || prodmrp.getText().isEmpty() || prodquand.getText().isEmpty()
				|| prodprice.getText().isEmpty() || prodname.getText().isEmpty() || proddisc.getText().isEmpty()
				|| "".equals(prodprice.getText());
		if (flag) {
			AlertMaker.showErrorMessage("Fill all fields", prodname);
			return;
		}

		productBarcode = prodbarcode.getText();
		productName = prodname.getText();
		productQuantity = Integer.parseInt(prodquand.getText());

		if (isUpdateOrNot) {
			int productQuantityForUpd = existingQuand + productQuantity;
			float productMRPForUpd = Float.parseFloat(prodmrp.getText());
			float productDiscountForUpd = Float.parseFloat(proddisc.getText());
			float productPriceForUpd = Float.parseFloat(prodprice.getText());

			Boolean result = handler.updateQuantityOfProductsUsingFxml(productBarcode, productName,
					productQuantityForUpd, productMRPForUpd, productDiscountForUpd, productPriceForUpd, productName);
			if (result) {
				AlertMaker.showControFxDialogMessage("PRODUCT UPDATION", "product updated successfully", Pos.CENTER);
			} else {
				AlertMaker.showErrorControFxDialogMessage("PRODUCT UPDATION", "Failed product updation", Pos.CENTER);
			}

		} else {
			String query = "INSERT INTO PRODUCTS VALUES(" + "'" + productBarcode + "'," + "'" + productName + "',"
					+ productQuantity + "," + productMRP + "," + productDiscount + "," + productPrice + ")";
			System.out.println(query);

			Boolean result = handler.executeQuery(query);
			if (result) {
				AlertMaker.showControFxDialogMessage("PRODUCT ADDING",
						"product '" + productName.toUpperCase() + "' added Successfully", Pos.CENTER);
			} else {
				AlertMaker.showErrorMessage("Error Occured in insertion \n item didn't insert", prodname);
				return;
			}
		}
		textFieldClear();

	}

	private void textFieldClear() {
		prodbarcode.clear();
		prodname.clear();
		prodmrp.clear();
		prodquand.clear();
		proddisc.clear();
		prodprice.clear();
		prodname.setVisible(true);
		labelGreen.setVisible(false);
		labelQuantity.setVisible(false);
		prnameList.clear();
		comboBox.setItems(prnameList);
		comboBox.setDisable(true);
		isUpdateOrNot = false;
		buttonADD.setText("ADD");
		labelHeading.setText("PRODUCT ADDING");
		prodbarcode.requestFocus();
		comboBoxTooltip("");
		labelNumber.setVisible(false);
		labelNumber.setText("0");
		prodname.setEditable(true);
	}

	@FXML
	private void closeScreenAction(ActionEvent event) {
		((Stage) prodbarcode.getScene().getWindow()).close();
	}

	private void mrpOrQuandChangePriceTextfieldClear() {
		prodmrp.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				prodprice.setText("");
			}
		});
		proddisc.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				prodprice.setText("");
			}
		});
	}

	@FXML
	private void clearButtonAction(ActionEvent event) {
		textFieldClear();
	}

	@FXML
	private void comboBoxAction(ActionEvent event) {
		String comboValue = comboBox.getValue();
		System.out.println("value name : " + comboValue);
		if (comboValue == null || "".equals(comboValue)) {
			System.out.println("null ie,combo value : " + comboValue);
			return;
		}

		prodname.setEditable(false);
		labelGreen.setVisible(true);
		labelQuantity.setVisible(true);
		buttonADD.setText("UPDATE");
		labelHeading.setText("PRODUCT UPDATING");
		comboBoxTooltip(comboBox.getValue());

		String query = "select * from PRODUCTS where PRBARCODE='" + prodbarcode.getText() + "' and PRNAME='"
				+ comboValue + "'";
		ResultSet rs = handler.resultexecQuery(query);
		try {
			while (rs.next()) {
				prodname.setText(rs.getString("prname"));
				prodmrp.setText(String.valueOf(rs.getFloat("prmrp")));
				proddisc.setText(String.valueOf(rs.getFloat("prdiscount")));
				prodprice.setText(String.valueOf(rs.getFloat("prprice")));
				existingQuand = rs.getInt("prquantity");
				isUpdateOrNot = true;/////////////////////////////////////// ithu ivideku maati///////////
			}
		} catch (SQLException ex) {
			Logger.getLogger(ProductsController.class.getName()).log(Level.SEVERE, null, ex);
		}
		prodquand.setText("0");
		prodquand.requestFocus();
	}

	private void barcodeTypeSearchDB() {
		prodbarcode.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

				prnameList.clear();
				isUpdateOrNot = false;
				prodname.setEditable(true);
				prodname.clear();
				prodmrp.clear();
				prodquand.clear();
				proddisc.clear();
				prodprice.clear();
				buttonADD.setText("ADD");
				labelHeading.setText("PRODUCT ADDING");
				comboBoxTooltip("");
				labelGreen.setVisible(false);
				labelQuantity.setVisible(false);

				String queryItemCount = "select count(*) from PRODUCTS where PRBARCODE='" + newValue + "'";
				int rscount = handler.productItemsCountForCombobox(queryItemCount);
				System.out.println("count :" + rscount);
				if (rscount > 0) {
					comboBox.setDisable(false);
					labelNumber.setVisible(true);
					labelNumber.setText(String.valueOf(rscount));
					comboBox.setStyle("-fx-background-color: linear-gradient(to bottom,#1dbbdd44,#ff1f1f52, #93f9b944);");
				} else {
					comboBox.setDisable(true);
					labelNumber.setVisible(false);
					labelNumber.setText(String.valueOf(rscount));
					comboBox.setStyle("-fx-background-color: linear-gradient(to bottom,#1dbbdd44, #93f9b944);");
				}
				String query = "select * from PRODUCTS where PRBARCODE='" + newValue + "'";
				ResultSet rs = handler.resultexecQuery(query);
				try {
					while (rs.next()) {
						System.out.println("pbarcode : " + rs.getString("prbarcode"));
						System.out.println("prname : " + rs.getString("prname"));
						System.out.println("prprice : " + rs.getFloat("prprice"));
						prnameList.add(rs.getString("prname"));
						comboBox.setItems(prnameList);
					}
				} catch (SQLException ex) {
					Logger.getLogger(ProductsController.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
		});
	}

	private void comboBoxTooltip(String str) {
		if (!(comboBox.getValue() == null || "".equals(comboBox.getValue()))) {
			comboBox.setTooltip(new Tooltip(str));
		} else {
			comboBox.setTooltip(new Tooltip("not selected"));
		}
	}
}
