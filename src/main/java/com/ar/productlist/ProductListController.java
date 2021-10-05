
package com.ar.productlist;

import com.ar.alertmaker.AlertMaker;
import com.ar.database.DatabaseSectionMain;
import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javafx.util.converter.IntegerStringConverter;
import org.controlsfx.control.Notifications;

public class ProductListController implements Initializable {
	DatabaseSectionMain handler;

	@FXML
	private AnchorPane secAnchorPane;
	@FXML
	private SplitPane splitPane;
	@FXML
	private AnchorPane firstAnchorPaneInSplit;
	@FXML
	private JFXButton allButton;
	@FXML
	private Pane pane;
	@FXML
	private RadioButton radioName;
	@FXML
	private RadioButton radioBarcode;
	@FXML
	private TextField textfieldSearch;
	@FXML
	private AnchorPane secondAnchorPaneInSplit;
	@FXML
	private TableView<Products> tableViewProducts;
	@FXML
	private TableColumn<Products, String> colBarcode;
	@FXML
	private TableColumn<Products, String> colProductName;
	@FXML
	private TableColumn<Products, Integer> colProductQuantity;
	@FXML
	private TableColumn<Products, Float> colMRP;
	@FXML
	private TableColumn<Products, Float> colDiscount;
	@FXML
	private TableColumn<Products, Float> colPrice;

	ObservableList<Products> productList = FXCollections.observableArrayList();
	@FXML
	private ContextMenu contextMenu;
	@FXML
	private MenuItem menuItemEdit;
	@FXML
	private MenuItem menuItemView;
	@FXML
	private MenuItem menuItemDelete;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		try {
			handler = DatabaseSectionMain.getInstance();
		} catch (ClassNotFoundException | SQLException ex) {
			Logger.getLogger(ProductListController.class.getName()).log(Level.SEVERE, null, ex);
		}
		initcol();
		loadData();
		ToggleGroup tg = new ToggleGroup();
		radioBarcode.setToggleGroup(tg);
		radioName.setToggleGroup(tg);
		tableViewSearchByTextfild();
		editableCols();
		stylingCssInside();
	}

	private void initcol() {
		colBarcode.setCellValueFactory(new PropertyValueFactory<>("prodBarcode"));
		colProductName.setCellValueFactory(new PropertyValueFactory<>("prodName"));
		colProductQuantity.setCellValueFactory(new PropertyValueFactory<>("prodQuantity"));
		colMRP.setCellValueFactory(new PropertyValueFactory<>("prodMRP"));
		colDiscount.setCellValueFactory(new PropertyValueFactory<>("prodDiscount"));
		colPrice.setCellValueFactory(new PropertyValueFactory<>("prodPrice"));

		tableViewProducts.setEditable(true);
	}

	private void loadData() {
		String query = "select * from PRODUCTS";
		ResultSet rs = handler.resultexecQuery(query);
		try {
			while (rs.next()) {
				String prbarcodex = rs.getString("prbarcode");
				String prnamex = rs.getString("prname");
				int prquandx = rs.getInt("prquantity");
				float prmrpx = rs.getFloat("prmrp");
				float prdiscx = rs.getFloat("prdiscount");
				float prpricex = rs.getFloat("prprice");
				productList.add(new Products(prbarcodex, prnamex, prquandx, prmrpx, prdiscx, prpricex));
			}
		} catch (SQLException ex) {
			Logger.getLogger(ProductListController.class.getName()).log(Level.SEVERE, null, ex);
		}

		tableViewProducts.setItems(productList);
	}

	@FXML
	private void refreshButtonAction(ActionEvent event) {
		clearAction();
		productList.clear();
		loadData();
//        tableViewProducts.setItems(productList);
		tableViewProducts.refresh();
	}

	@FXML
	private void allButtonAction(ActionEvent event) {
		clearAction();
		tableViewProducts.setItems(productList);
		tableViewProducts.refresh();
	}

	// =============================//workersearch by
	// textfield//=============================//
	private boolean searchFindsProductsByTextfield(Products products, String searchText) {
		if (radioName.isSelected()) {
			return (products.getProdName().toLowerCase().contains(searchText.toLowerCase()));
		}
		if (radioBarcode.isSelected()) {
			return (products.getProdBarcode().toLowerCase().contains(searchText.toLowerCase()));
		}
		return (products.getProdName().toLowerCase().contains(searchText.toLowerCase())
				|| products.getProdBarcode().toLowerCase().contains(searchText.toLowerCase()));
	}

	private ObservableList<Products> filterListByTextfield(List<Products> list, String searchText) {
		List<Products> filteredList = new ArrayList<>();
		for (Products products : list) {
			if (searchFindsProductsByTextfield(products, searchText))
				filteredList.add(products);
		}
		return FXCollections.observableArrayList(filteredList);
	}

	private void tableViewSearchByTextfild() {
		textfieldSearch.textProperty().addListener(((observable, oldValue, newValue) -> {
			tableViewProducts.setItems(filterListByTextfield(productList, newValue));

		}));
	}

	private void clearAction() {
		textfieldSearch.setText("");
		radioBarcode.setSelected(false);
		radioName.setSelected(false);

	}

	private void editableCols() {
		colProductQuantity.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
		colProductQuantity.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Products, Integer>>() {
			@Override
			public void handle(TableColumn.CellEditEvent<Products, Integer> event) {
				Products prods = event.getRowValue();
				prods.setProdQuantity(event.getNewValue());
				Boolean result = handler.updateQuantityOfProducts(prods);
				if (result) {
					Notifications notificationBuilderResend = Notifications.create().title("Quantity UPDATE")
							.text("Product Quantity Updated Succesfully").graphic(null).hideAfter(Duration.seconds(2))
							.position(Pos.CENTER).onAction(new EventHandler<ActionEvent>() {
								@Override
								public void handle(ActionEvent event) {
									System.out.println("Clicked quantity updated notification");
								}
							});
					notificationBuilderResend.darkStyle();
					notificationBuilderResend.showInformation();
				} else {
					Notifications notificationBuilderResendError = Notifications.create().title("Quantity UPDATE")
							.text("Product Quantity Updated Failed").graphic(null).hideAfter(Duration.seconds(5))
							.position(Pos.CENTER).onAction(new EventHandler<ActionEvent>() {
								@Override
								public void handle(ActionEvent event) {
									System.out.println("Clicked quantity updated notification");
								}
							});
					notificationBuilderResendError.darkStyle();
					notificationBuilderResendError.showError();
				}
			}

		});
	}

	@FXML
	private void editProductMenuItemAction(ActionEvent event) throws IOException {
		Products selectedprod = tableViewProducts.getSelectionModel().getSelectedItem();
		String prodNameBeforeEdit = selectedprod.getProdName();
		
		if (selectedprod == null) {
			AlertMaker.showErrorMessage("No Item selected", tableViewProducts);
			return;
		}
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("ProductEdit.fxml"));
		Parent root = loader.load();
		Stage stage = new Stage();
		ProductEditController controller = (ProductEditController) loader.getController();
		controller.valueTakingToFxml(selectedprod.getProdBarcode(), selectedprod.getProdName(),
				String.valueOf(selectedprod.getProdQuantity()), String.valueOf(selectedprod.getProdMRP()),
				String.valueOf(selectedprod.getProdDiscount()), String.valueOf(selectedprod.getProdPrice()));

		stage.setScene(new Scene(root));
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.initStyle(StageStyle.UTILITY);
		stage.setTitle("PRODUCT UPDATE");
		stage.setAlwaysOnTop(true);
		stage.showAndWait();

		int updateOrBackOrCloseStatus = controller.StatusValue();
		if (updateOrBackOrCloseStatus == 1) {
			AlertMaker.showControFxDialogMessage("PRODUCT UPDATE",
					"product " + prodNameBeforeEdit + " updated successfully", Pos.CENTER);
			refreshButtonAction(null);
			System.out.println("updateOrBackOrCloseStatus nte value: " + updateOrBackOrCloseStatus);
		} else {
			System.out.println("updateOrBackOrCloseStatus nte value: " + updateOrBackOrCloseStatus);
		}

	}

	@FXML
	private void viewProductMenuItemAction(ActionEvent event) throws IOException {
		Products selectedprod = tableViewProducts.getSelectionModel().getSelectedItem();
		if (selectedprod == null) {
			AlertMaker.showErrorMessage("No Item selected", tableViewProducts);
			return;
		}
		FXMLLoader loader = new FXMLLoader(getClass().getResource("ProductEdit.fxml"));
		Parent root = loader.load();
		Stage stage = new Stage();
		ProductEditController controller = (ProductEditController) loader.getController();
		controller.valueTakingToFxmlForViewingOnly(selectedprod.getProdBarcode(), selectedprod.getProdName(),
				String.valueOf(selectedprod.getProdQuantity()), String.valueOf(selectedprod.getProdMRP()),
				String.valueOf(selectedprod.getProdDiscount()), String.valueOf(selectedprod.getProdPrice()));

		stage.setScene(new Scene(root));
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.initStyle(StageStyle.UTILITY);
		stage.setTitle("PRODUCT DETAILS");
		stage.setAlwaysOnTop(true);
		stage.show();
	}

	@FXML
	private void deleteProductMenuItemAction(ActionEvent event) {
		Products selectedprod = tableViewProducts.getSelectionModel().getSelectedItem();
		if (selectedprod == null) {
			AlertMaker.showErrorMessage("No Item selected", tableViewProducts);
			return;
		}
		Boolean flag = AlertMaker.showConfirmationMessage(
				"Are you sure delete  product '" + selectedprod.getProdName().toUpperCase() + "'", "DELETE PRODUCT",
				tableViewProducts);
		if (flag) {
			Boolean result = handler.DeleteProductsFromTable(selectedprod);
			if (result) {
				AlertMaker.showControFxDialogMessage("PRODUCT DELETED",
						"Product " + selectedprod.getProdName() + " deleted from table", Pos.CENTER);
				tableViewProducts.getItems().remove(selectedprod);
			} else {
				AlertMaker.showErrorControFxDialogMessage("PRODUCT DELETED", "Product  deletion Failed", Pos.CENTER);
			}
		}
	}

	private void stylingCssInside() {
		contextMenu.setStyle(
				"-fx-background-color: linear-gradient(to bottom,darkgrey, #93f9b944); -fx-font-weight:bold;");
	}

}
