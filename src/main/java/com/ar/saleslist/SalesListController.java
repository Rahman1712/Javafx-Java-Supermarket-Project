
package com.ar.saleslist;

import com.ar.alertmaker.AlertMaker;
import com.ar.database.DatabaseSectionMain;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTimePicker;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

public class SalesListController implements Initializable {
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
	private RadioMenuItem radioMenuCustomerName;
	@FXML
	private RadioMenuItem radioMenuInvoiceNo;
	@FXML
	private RadioMenuItem radioMenuCustomerId;
	@FXML
	private RadioMenuItem radioMenuBiller;
	@FXML
	private TextField textfieldSearch;
	@FXML
	private Button buttonForHideSort;
	@FXML
	private Button buttonForShowSort;
	@FXML
	private AnchorPane secondAnchorPaneInSplit;
	@FXML
	private TableView<SalesArea> tableViewSales;
	@FXML
	private TableColumn<SalesArea, String> colInvoiceId;
	@FXML
	private TableColumn<SalesArea, String> colCustomerId;
	@FXML
	private TableColumn<SalesArea, String> colCustomerName;
	@FXML
	private TableColumn<SalesArea, Float> colSalesTotal;
	@FXML
	private TableColumn<SalesArea, Integer> colNoOfItems;
	@FXML
	private TableColumn<SalesArea, Float> colBillPaid;
	@FXML
	private TableColumn<SalesArea, Float> colBalance;
	@FXML
	private TableColumn<SalesArea, String> colBiller;
	@FXML
	private TableColumn<SalesArea, String> colSalesTime;

	ObservableList<SalesArea> salesList = FXCollections.observableArrayList();
	@FXML
	private RadioMenuItem radioMenuItemDefault;
	@FXML
	private Label labelRadioStatus;
	@FXML
	private TableColumn<String, Timestamp> colSalesTimeTimeStamp;
	int statusno = 0;
	@FXML
	private AnchorPane anchorPaneSearching;
	@FXML
	private AnchorPane anchorPaneSorting;
	@FXML
	private RadioButton radioChooseTwoDates;
	@FXML
	private RadioButton radioChooseOneDate;
	@FXML
	private JFXDatePicker fromDatePicker;
	@FXML
	private JFXTimePicker fromTimePicker;
	@FXML
	private JFXDatePicker toDatePicker;
	@FXML
	private JFXTimePicker toTimePicker;
	@FXML
	private JFXButton okButtonSorting;
	@FXML
	private JFXDatePicker datePickerOnly;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		try {
			handler = DatabaseSectionMain.getInstance();
		} catch (ClassNotFoundException | SQLException ex) {
			Logger.getLogger(SalesListController.class.getName()).log(Level.SEVERE, null, ex);
		}
		initcol();
		loadData();
		ToggleGroup tg = new ToggleGroup();
		radioMenuBiller.setToggleGroup(tg);
		radioMenuCustomerId.setToggleGroup(tg);
		radioMenuCustomerName.setToggleGroup(tg);
		radioMenuInvoiceNo.setToggleGroup(tg);
		radioMenuItemDefault.setToggleGroup(tg);
		ToggleGroup sorttg = new ToggleGroup();
		radioChooseOneDate.setToggleGroup(sorttg);
		radioChooseTwoDates.setToggleGroup(sorttg);
		tableViewSearchByTextfild();
		radioMenuItemDefault.setSelected(true);
		colSalesTimeTimeStamp.setVisible(false);
		anchorPaneSorting.setVisible(false);
		datePickerOnly.setDisable(true);
		fromDatePicker.setDisable(true);
		fromTimePicker.setDisable(true);
		toDatePicker.setDisable(true);
		toTimePicker.setDisable(true);
	}

	private void initcol() {
		colInvoiceId.setCellValueFactory(new PropertyValueFactory<>("salesId"));
		colCustomerId.setCellValueFactory(new PropertyValueFactory<>("cusId"));
		colCustomerName.setCellValueFactory(new PropertyValueFactory<>("cusName"));
		colSalesTotal.setCellValueFactory(new PropertyValueFactory<>("salesTotal"));
		colNoOfItems.setCellValueFactory(new PropertyValueFactory<>("salesItemno"));
		colBillPaid.setCellValueFactory(new PropertyValueFactory<>("billPaid"));
		colBalance.setCellValueFactory(new PropertyValueFactory<>("billBalance"));
		colBiller.setCellValueFactory(new PropertyValueFactory<>("billerName"));
		colSalesTime.setCellValueFactory(new PropertyValueFactory<>("salesTimeString"));
		colSalesTimeTimeStamp.setCellValueFactory(new PropertyValueFactory<>("salesTimeStamp"));
	}

	private void loadData() {
		String query = "select * from SALES";
		ResultSet rs = handler.resultexecQuery(query);
		try {
			while (rs.next()) {
				String invoiceIdx = rs.getString("salesid");
				String cusidx = rs.getString("cusid");
				String cusnamex = rs.getString("cusname");
				float salestotalx = rs.getFloat("salestotal");
				int itemnox = rs.getInt("salesitemno");
				float billpaidx = rs.getFloat("salesbillpaid");
				float billbalancex = rs.getFloat("salesbalance");
				String billerx = rs.getString("salesbiller");
				Timestamp salesTimex = rs.getTimestamp("salestime");
				Date salesDatex = new Date(salesTimex.getTime());
				SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm a");
				String salesTimeStringx = formatter.format(salesDatex);
				salesList.add(new SalesArea(invoiceIdx, cusidx, cusnamex, salestotalx, itemnox, billpaidx, billbalancex,
						billerx, salesDatex, salesTimex, salesTimeStringx));

			}
		} catch (SQLException ex) {
			Logger.getLogger(SalesListController.class.getName()).log(Level.SEVERE, null, ex);
		}

		tableViewSales.setItems(salesList);
	}

	@FXML
	private void refreshButtonAction(ActionEvent event) {
		allButtonAction(null);
		salesList.clear();
		loadData();
		tableViewSales.refresh();
	}

	@FXML
	private void allButtonAction(ActionEvent event) {
		buttonHideSortAction(null);
		radioMenuItemDefault.setSelected(true);
		labelRadioStatus.setText("search in all field");
	}

	@FXML
	private void buttonHideSortAction(ActionEvent event) {
		radioChooseOneDate.setSelected(false);
		radioChooseTwoDates.setSelected(false);
		datePickerOnly.setDisable(true);
		fromDatePicker.setDisable(true);
		fromTimePicker.setDisable(true);
		toDatePicker.setDisable(true);
		toTimePicker.setDisable(true);
		statusno = 0;
		buttonForHideSort.setVisible(false);
		buttonForShowSort.setVisible(true);
		anchorPaneSorting.setVisible(false);
		textfieldSearch.setText("");
		tableViewSales.setItems(salesList);
	}

	@FXML
	private void buttonSortShowAction(ActionEvent event) {
		anchorPaneSorting.setVisible(true);
		buttonForShowSort.setVisible(false);
		buttonForHideSort.setVisible(true);
	}

	// =============================//workersearch by
	// textfield//=============================//
	private boolean searchFindsProductsByTextfield(SalesArea sales, String searchText, String dateOnly, Date first,
			Date second) {
		if (statusno == 1) {
			if (radioMenuInvoiceNo.isSelected()) {
				return (sales.getSalesId().toLowerCase().contains(searchText.toLowerCase())
						&& sales.getSalesTimeString().toLowerCase().contains(dateOnly.toLowerCase()));
			} else if (radioMenuCustomerId.isSelected()) {
				return (sales.getCusId().toLowerCase().contains(searchText.toLowerCase())
						&& sales.getSalesTimeString().toLowerCase().contains(dateOnly.toLowerCase()));
			} else if (radioMenuCustomerName.isSelected()) {
				return (sales.getCusName().toLowerCase().contains(searchText.toLowerCase())
						&& sales.getSalesTimeString().toLowerCase().contains(dateOnly.toLowerCase()));
			} else if (radioMenuBiller.isSelected()) {
				return (sales.getBillerName().toLowerCase().contains(searchText.toLowerCase())
						&& sales.getSalesTimeString().toLowerCase().contains(dateOnly.toLowerCase()));
			} else if (radioMenuItemDefault.isSelected()) {
				return ((sales.getSalesId().toLowerCase().contains(searchText.toLowerCase())
						|| sales.getCusId().toLowerCase().contains(searchText.toLowerCase())
						|| sales.getCusName().toLowerCase().contains(searchText.toLowerCase())
						|| sales.getBillerName().toLowerCase().contains(searchText.toLowerCase()))
						&& sales.getSalesTimeString().toLowerCase().contains(dateOnly.toLowerCase()));
			}

		}
		if (statusno == 2) {
			if (radioMenuInvoiceNo.isSelected()) {
				return (sales.getSalesId().toLowerCase().contains(searchText.toLowerCase())
						&& (sales.getSalesTime().compareTo(first) >= 1)
						&& (second.compareTo(sales.getSalesTime()) >= 1));
			} else if (radioMenuCustomerId.isSelected()) {
				return (sales.getCusId().toLowerCase().contains(searchText.toLowerCase())
						&& (sales.getSalesTime().compareTo(first) >= 1)
						&& (second.compareTo(sales.getSalesTime()) >= 1));
			} else if (radioMenuCustomerName.isSelected()) {
				return (sales.getCusName().toLowerCase().contains(searchText.toLowerCase())
						&& (sales.getSalesTime().compareTo(first) >= 1)
						&& (second.compareTo(sales.getSalesTime()) >= 1));
			} else if (radioMenuBiller.isSelected()) {
				return (sales.getBillerName().toLowerCase().contains(searchText.toLowerCase())
						&& (sales.getSalesTime().compareTo(first) >= 1)
						&& (second.compareTo(sales.getSalesTime()) >= 1));
			} else {
				return ((sales.getSalesId().toLowerCase().contains(searchText.toLowerCase())
						|| sales.getCusId().toLowerCase().contains(searchText.toLowerCase())
						|| sales.getCusName().toLowerCase().contains(searchText.toLowerCase())
						|| sales.getBillerName().toLowerCase().contains(searchText.toLowerCase()))
						&& (sales.getSalesTime().compareTo(first) >= 1)
						&& (second.compareTo(sales.getSalesTime()) >= 1));
			}

		}
		if (radioMenuInvoiceNo.isSelected()) {
			return (sales.getSalesId().toLowerCase().contains(searchText.toLowerCase()));
		}
		if (radioMenuCustomerId.isSelected()) {
			return (sales.getCusId().toLowerCase().contains(searchText.toLowerCase()));
		}
		if (radioMenuCustomerName.isSelected()) {
			return (sales.getCusName().toLowerCase().contains(searchText.toLowerCase()));
		}
		if (radioMenuBiller.isSelected()) {
			return (sales.getBillerName().toLowerCase().contains(searchText.toLowerCase()));
		}
		return (sales.getSalesId().toLowerCase().contains(searchText.toLowerCase())
				|| sales.getCusId().toLowerCase().contains(searchText.toLowerCase())
				|| sales.getCusName().toLowerCase().contains(searchText.toLowerCase())
				|| sales.getBillerName().toLowerCase().contains(searchText.toLowerCase()));
	}

	private ObservableList<SalesArea> filterListByTextfield(List<SalesArea> list, String searchText, String dateOnly,
			Date first, Date second) {
		List<SalesArea> filteredList = new ArrayList<>();
		// use functional operation instead of for loop
//         for(SalesArea sales : list){
//            if(searchFindsProductsByTextfield(sales, searchText,dateOnly,first,second)) filteredList.add(sales);
//        }
		list.stream().filter((sales) -> (searchFindsProductsByTextfield(sales, searchText, dateOnly, first, second)))
				.forEachOrdered((sales) -> {
					filteredList.add(sales);
				});
		return FXCollections.observableArrayList(filteredList);
	}

	private void tableViewSearchByTextfild() {
		textfieldSearch.textProperty()
				.addListener(((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
					switch (statusno) {
					case 1:
						if (datePickerOnly.getValue() == null) {
							AlertMaker.showErrorMessage("Please select date", radioChooseOneDate);
							return;
						}
						LocalDate dateOnly = (datePickerOnly.getValue());
						String dateformatter = dateOnly.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
						tableViewSales.setItems(filterListByTextfield(salesList, newValue, dateformatter, null, null));
						break;
					case 2:
						if (fromDatePicker.getValue() == null || fromTimePicker.getValue() == null
								|| toDatePicker.getValue() == null || toTimePicker.getValue() == null) {
							AlertMaker.showErrorMessage("Please select date and time", radioChooseTwoDates);
							return;
						}
						LocalDate fromDate = fromDatePicker.getValue();
						LocalTime fromTime = fromTimePicker.getValue();
						String strFromDate = fromDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
						String strFromTime = fromTime.format(DateTimeFormatter.ofPattern("HH:mm a"));
						String fromDateTimeJoin = strFromDate + " " + strFromTime;
						Date dateTimeFrom = null;
						try {
							dateTimeFrom = new SimpleDateFormat("dd/MM/yyyy HH:mm a").parse(fromDateTimeJoin);
						} catch (ParseException ex) {
							Logger.getLogger(SalesListController.class.getName()).log(Level.SEVERE, null, ex);
						}
						LocalDate toDate = toDatePicker.getValue();
						LocalTime toTime = toTimePicker.getValue();
						String strToDate = toDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
						String strToTime = toTime.format(DateTimeFormatter.ofPattern("HH:mm a"));
						String toDateTimeJoin = strToDate + " " + strToTime;
						Date dateTimeTo = null;
						try {
							dateTimeTo = new SimpleDateFormat("dd/MM/yyyy HH:mm a").parse(toDateTimeJoin);
						} catch (ParseException ex) {
							Logger.getLogger(SalesListController.class.getName()).log(Level.SEVERE, null, ex);
						}
						tableViewSales
								.setItems(filterListByTextfield(salesList, newValue, null, dateTimeFrom, dateTimeTo));
						break;
					default:
						tableViewSales.setItems(filterListByTextfield(salesList, newValue, null, null, null));
						break;
					}

				}));
	}
//=============================================================//

	@FXML
	private void radioMenuCustomerNameAction(ActionEvent event) {
		labelRadioStatus.setText("search customer name");
		textfieldSearch.setText("");
	}

	@FXML
	private void radioMenuItemDefaultAction(ActionEvent event) {
		labelRadioStatus.setText("search in all field");
		textfieldSearch.setText("");
	}

	@FXML
	private void radioMenuInvoiceNoAction(ActionEvent event) {
		labelRadioStatus.setText("search invoice no.");
		textfieldSearch.setText("");
	}

	@FXML
	private void radioMenuCustomerIdAction(ActionEvent event) {
		labelRadioStatus.setText("search customer id");
		textfieldSearch.setText("");
	}

	@FXML
	private void radioMenuBillerAction(ActionEvent event) {
		labelRadioStatus.setText("search biller name");
		textfieldSearch.setText("");
	}

	private boolean searchFindsWorkers(SalesArea sales, Date first, Date second, String dateOnly) {
		if (statusno == 1) {
			return (sales.getSalesTimeString().toLowerCase().contains(dateOnly.toLowerCase()));
		}
		if (statusno == 2) {
			return ((sales.getSalesTime().compareTo(first) >= 1) && (second.compareTo(sales.getSalesTime()) >= 1));
		}
		return (sales.getSalesId().toLowerCase().contains(""));///////////////////// check if any bug in this
																///////////////////// ///////////////
	}

	private ObservableList<SalesArea> filterList(List<SalesArea> list, Date first, Date second, String dateOnly) {
		List<SalesArea> filteredList = new ArrayList<>();
		list.stream().filter((sales) -> (searchFindsWorkers(sales, first, second, dateOnly)))
				.forEachOrdered((sales) -> {
					filteredList.add(sales);
				});
		return FXCollections.observableArrayList(filteredList);
	}

	@FXML
	private void okButtonSortingAction(ActionEvent event) throws ParseException {

		if (statusno == 1) {
			System.out.println(datePickerOnly.getValue());
			if (datePickerOnly.getValue() == null) {
				AlertMaker.showErrorMessage("Please select date", radioChooseOneDate);
				return;
			}
			LocalDate dateOnly = (datePickerOnly.getValue());
			String dateformatter = dateOnly.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
			tableViewSales.setItems(filterList(salesList, null, null, dateformatter));
		} else if (statusno == 2) {
			if (fromDatePicker.getValue() == null || fromTimePicker.getValue() == null
					|| toDatePicker.getValue() == null || toTimePicker.getValue() == null) {
				AlertMaker.showErrorMessage("Please select date and time", radioChooseTwoDates);
				return;
			}
			LocalDate fromDate = fromDatePicker.getValue();
			LocalTime fromTime = fromTimePicker.getValue();
			String strFromDate = fromDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
			String strFromTime = fromTime.format(DateTimeFormatter.ofPattern("HH:mm a"));
			String fromDateTimeJoin = strFromDate + " " + strFromTime;
			System.out.println("date ======>" + strFromDate);
			System.out.println("Time ======>" + strFromTime);
			System.out.println("dateTime ======>" + fromDateTimeJoin);
			Date dateTimeFrom = new SimpleDateFormat("dd/MM/yyyy HH:mm a").parse(fromDateTimeJoin);
			System.out.println("date after parse:" + dateTimeFrom);

			LocalDate toDate = toDatePicker.getValue();
			LocalTime toTime = toTimePicker.getValue();
			String strToDate = toDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
			String strToTime = toTime.format(DateTimeFormatter.ofPattern("HH:mm a"));
			String toDateTimeJoin = strToDate + " " + strToTime;
			System.out.println("date ======>" + strToDate);
			System.out.println("Time ======>" + strToTime);
			System.out.println("dateTime ======>" + fromDateTimeJoin);
			Date dateTimeTo = new SimpleDateFormat("dd/MM/yyyy HH:mm a").parse(toDateTimeJoin);
			System.out.println("date after parse:" + dateTimeTo);
			tableViewSales.setItems(filterList(salesList, dateTimeFrom, dateTimeTo, null));
		}
	}

	@FXML
	private void radioChooseOneDateAction(ActionEvent event) {
		datePickerOnly.setDisable(false);
		fromDatePicker.setDisable(true);
		fromTimePicker.setDisable(true);
		toDatePicker.setDisable(true);
		toTimePicker.setDisable(true);
		statusno = 1;
	}

	@FXML
	private void radioChooseTwoDatesAction(ActionEvent event) {
		datePickerOnly.setDisable(true);
		fromDatePicker.setDisable(false);
		fromTimePicker.setDisable(false);
		toDatePicker.setDisable(false);
		toTimePicker.setDisable(false);
		statusno = 2;
	}
}
