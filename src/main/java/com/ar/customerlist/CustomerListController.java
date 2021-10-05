
package com.ar.customerlist;

import com.ar.database.DatabaseSectionMain;
import com.jfoenix.controls.JFXButton;
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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public class CustomerListController implements Initializable {
	DatabaseSectionMain handler;
	@FXML
	private RadioButton radioName;
	@FXML
	private RadioButton radioPlace;
	@FXML
	private TextField textfieldSearch;
	@FXML
	private RadioButton radioID;
	@FXML
	private TableView<Customer> tableCustomer;
	@FXML
	private TableColumn<Customer, String> colCusID;
	@FXML
	private TableColumn<Customer, String> colCusMob;
	@FXML
	private TableColumn<Customer, String> colCusName;
	@FXML
	private TableColumn<Customer, String> colCusPlace;

	ObservableList<Customer> cusList = FXCollections.observableArrayList();

	ToggleGroup tg = new ToggleGroup();
	@FXML
	private Pane pane;
	@FXML
	private RadioButton radioMob;
	@FXML
	private AnchorPane mainAnchorPane;
	@FXML
	private JFXButton buttonAll1;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		try {
			handler = DatabaseSectionMain.getInstance();
		} catch (ClassNotFoundException | SQLException ex) {
			Logger.getLogger(CustomerListController.class.getName()).log(Level.SEVERE, null, ex);
		}
		radioButtonGrouping();
		initcol();
		loadData();
		tableViewSearchByTextfild();
	}

	private void initcol() {
		colCusID.setCellValueFactory(new PropertyValueFactory<>("cusid"));
		colCusMob.setCellValueFactory(new PropertyValueFactory<>("cusmob"));
		colCusName.setCellValueFactory(new PropertyValueFactory<>("cusname"));
		colCusPlace.setCellValueFactory(new PropertyValueFactory<>("cusplace"));
	}

	private void loadData() {
		String query = "select * from CUSTOMER";
		ResultSet rs = handler.resultexecQuery(query);
		int count = 0;
		int j = 0;
		try {
			while (rs.next()) {
				String cusnamex = rs.getString("cusname");
				String cusidx = rs.getString("cusid");
				String cusmobx = rs.getString("cusmob");
				String cusplacex = rs.getString("cusplace");
				count = count + 1;
				if ("cus00".equals(cusidx)) {
					j = count;
				}
				cusList.add(new Customer(cusidx, cusnamex, cusmobx, cusplacex));
			}
		} catch (SQLException ex) {
			Logger.getLogger(CustomerListController.class.getName()).log(Level.SEVERE, null, ex);
		}
		tableCustomer.setItems(cusList);
		if (j == 0) {
			System.out.println("cus00 not found");
		} else {
			System.out.println("cus00 at position : " + j);
			cusList.remove(j - 1);
		}
	}

	@FXML
	private void refreshCustomerAction(ActionEvent event) {
		allCustomerAction(null);
		cusList.clear();
		loadData();
		tableCustomer.refresh();
	}

	@FXML
	private void allCustomerAction(ActionEvent event) {
		radioID.setSelected(false);
		radioName.setSelected(false);
		radioPlace.setSelected(false);
		radioMob.setSelected(false);
		textfieldSearch.setText("");
		tableCustomer.setItems(cusList);
		tableCustomer.refresh();
	}

	private boolean searchFindsCustomer(Customer customer, String searchText) {
		if (radioName.isSelected()) {
			return (customer.getCusname().toLowerCase().contains(searchText.toLowerCase()));
		}
		if (radioPlace.isSelected()) {
			return (customer.getCusplace().toLowerCase().contains(searchText.toLowerCase()));
		}
		if (radioID.isSelected()) {
			return (customer.getCusid().toLowerCase().contains(searchText.toLowerCase()));
		}
		if (radioMob.isSelected()) {
			return (customer.getCusmob().toLowerCase().contains(searchText.toLowerCase()));
		}

		return ((customer.getCusname().toLowerCase().contains(searchText.toLowerCase())))
				|| ((customer.getCusplace().toLowerCase().contains(searchText.toLowerCase())))
				|| ((customer.getCusmob().toLowerCase().contains(searchText.toLowerCase())))
				|| ((customer.getCusid().toLowerCase().contains(searchText.toLowerCase())));
	}

	private ObservableList<Customer> filterList(List<Customer> list, String searchText) {
		List<Customer> filteredList = new ArrayList<>();
		for (Customer customer : list) {
			if (searchFindsCustomer(customer, searchText))
				filteredList.add(customer);
		}
		return FXCollections.observableArrayList(filteredList);
	}

	private void tableViewSearchByTextfild() {
		textfieldSearch.textProperty().addListener(((observable, oldValue, newValue) -> {
			tableCustomer.setItems(filterList(cusList, newValue));
		}));
	}
	private void radioButtonGrouping() {
		radioID.setToggleGroup(tg);
		radioName.setToggleGroup(tg);
		radioPlace.setToggleGroup(tg);
		radioMob.setToggleGroup(tg);
	}
}

