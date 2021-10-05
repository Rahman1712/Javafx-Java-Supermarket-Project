
package com.ar.proceedsales;

import com.ar.addsales.Sales;
import com.ar.alertmaker.AlertMaker;
import com.ar.database.DatabaseSectionMain;

import java.io.File;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

public class ProceedSalesController implements Initializable {
	DatabaseSectionMain handler;
	ArrayList<String> salesIDList = new ArrayList<>();
	ArrayList<SalesPrintJava> salesPrintList = new ArrayList<>();
	String salesIDnew;
	int inum;

	@FXML
	private TableView<PrintItems> printTable;
	@FXML
	private TableColumn<PrintItems, Integer> colSerialNo;
	@FXML
	private TableColumn<PrintItems, String> colBarcode;
	@FXML
	private TableColumn<PrintItems, String> colName;
	@FXML
	private TableColumn<PrintItems, Float> colMrp;
	@FXML
	private TableColumn<PrintItems, Float> colDiscount;
	@FXML
	private TableColumn<PrintItems, Integer> colQuantity;
	@FXML
	private TableColumn<PrintItems, Float> colTotalPrice;
	@FXML
	private TableColumn<PrintItems, Float> colPrice;
	@FXML
	private Label billerName;
	@FXML
	private Label invoiceNo;
	@FXML
	private Label customerName;
	@FXML
	private Label customerID;
	ObservableList<PrintItems> piList = FXCollections.observableArrayList();
	@FXML
	private TextField totalAmount;
	@FXML
	private TextField cashRecieved;
	@FXML
	private TextField cashBalance;
	@FXML
	private Label amountWord;

	PrintOrBack pob = new PrintOrBack(0);
	@FXML
	private AnchorPane mainAnchorpane;

	String salesIdxx;
	DecimalFormat deciformat = new DecimalFormat("0.00");

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		try {
			handler = DatabaseSectionMain.getInstance();
		} catch (ClassNotFoundException | SQLException ex) {
			Logger.getLogger(ProceedSalesController.class.getName()).log(Level.SEVERE, null, ex);
		}
		initcol();
		totalAmount.setEditable(false);
		cashBalance.setEditable(false);
		textFieldClearByEditing();
	}

	private void initcol() {
		colSerialNo.setCellValueFactory(new PropertyValueFactory<>("srNo"));
		colBarcode.setCellValueFactory(new PropertyValueFactory<>("barcode"));
		colName.setCellValueFactory(new PropertyValueFactory<>("name"));
		colMrp.setCellValueFactory(new PropertyValueFactory<>("mrp"));
		colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
		colQuantity.setCellValueFactory(new PropertyValueFactory<>("discount"));
		colDiscount.setCellValueFactory(new PropertyValueFactory<>("quantity"));
		colTotalPrice.setCellValueFactory(new PropertyValueFactory<>("total"));

	}

	public void inflateUI(ObservableList<Sales> sales) {
		int i = 0;

		DecimalFormat df = new DecimalFormat("0.00");
		for (Sales sls : sales) {

			String br = sls.getProBar();
			String nam = sls.getProNam();
			Float mr = sls.getProMR();
			int qu = sls.getProQu();

			Float prctaking = sls.getProPr();
			String newprc = df.format(prctaking);
			Float prc = Float.parseFloat(newprc);

			Float dis = sls.getProDs();

			Float tottaking = sls.getProTotPr();
			String newtot = df.format(tottaking);
			Float tot = Float.parseFloat(newtot);

			i++;
			piList.add(new PrintItems(i, br, nam, mr, dis, prc, qu, tot));
			printTable.setItems(piList);

			// saving data to ArrayList(List) using newly created SalesPrintJava.class for
			// printing purpose
			salesPrintList.add(new SalesPrintJava(i, br, nam, mr, dis, prc, qu, tot));
		}

	}

	public void transferTextFieldCustomerData(String cusidx, String cusnamex) {
		customerID.setText(cusidx);
		customerName.setText(cusnamex);
	}

	public void transferAmountData(String totalx, String wordx) {
		totalAmount.setText(totalx);
		amountWord.setText(wordx);
	}

	public void billerData(String billerx) {
		billerName.setText(billerx);
	}

	@FXML
	private void printButtonAction(ActionEvent event) throws InterruptedException, JRException {

		if (piList.isEmpty()) {
			System.out.println("no items");
			AlertMaker.showErrorMessage("Empty Table", printTable);
			return;
		}

		if (cashRecieved.getText().isEmpty() || cashBalance.getText().isEmpty() || "".equals(cashRecieved.getText())
				|| "".equals(cashBalance.getText())) {
			AlertMaker.showErrorMessage("Pay Amount Empty cash Recieved Column", printTable);
			return;
		}
		Boolean flag = addToSales();
		if (flag) {
			for (PrintItems s : piList) {
				Boolean result = handler.updateProducts(s);
				if (result) {
					System.out.println("products table quantity updated");
				}
			}
			///////////////// printer pdf print/////////////
			printerActionOnPrintButtonAction();

			try {
				Thread.sleep(3000);
			} catch (InterruptedException ex) {
				Logger.getLogger(ProceedSalesController.class.getName()).log(Level.SEVERE, null, ex);
			}

			((Stage) printTable.getScene().getWindow()).close();

			pob.setStatus(1);

		} else {
			AlertMaker.showErrorMessage("Error Occured in Sale table insertion", printTable);
			System.out.println("failed");
		}
	}

	@FXML
	private void backButtonAction(ActionEvent event) {
		((Stage) printTable.getScene().getWindow()).close();
		pob.setStatus(0);
	}

	@FXML
	private void cashRecievedAction(ActionEvent event) throws NumberFormatException {
		if (!cashRecieved.getText().isEmpty()) {
			Float cashRecieve = Float.parseFloat(cashRecieved.getText());
			Float billTOPay = Float.parseFloat(totalAmount.getText());
			Float balTaking = cashRecieve - billTOPay;
			String stringBalancePrice = deciformat.format(balTaking);
			Float bal = Float.parseFloat(stringBalancePrice);
			cashBalance.setText(String.valueOf(bal));
		}
	}

	private String salesIdMaking() {
		String query = "select salesid from sales order by salestime";
		ResultSet rs = handler.resultexecQuery(query);
		try {
			while (rs.next()) {
				String salesIDx = rs.getString("salesid");
				salesIDList.add(salesIDx);
			}
		} catch (SQLException ex) {
			Logger.getLogger(ProceedSalesController.class.getName()).log(Level.SEVERE, null, ex);
		}
		if (salesIDList.isEmpty()) {
			SimpleDateFormat f = new SimpleDateFormat("ddMMyyyy");
			Date date = new Date();
			String s = f.format(date);
			System.out.println(f.format(date));
			salesIDnew = s + "01";
		} else {
			String str = (salesIDList.get(salesIDList.size() - 1)).toUpperCase();// taking last string
			String substringStartEightDigit = str.substring(0, 8);// taking 1st char to 8th char (range from 0 to nth)
			// example 17112020073 after substring value is 17112020
			System.out.println("substring string method from 0 to 8 char========>" + substringStartEightDigit);
			String substringafterninedigit = str.substring(9); // substring method to taking after 9 character range
																// start from 1
			// example 17112020073 after substring value is 73
			System.out.println("substring string method from 9th char=========>" + substringafterninedigit);
			SimpleDateFormat f = new SimpleDateFormat("ddMMyyyy");
			Date date = new Date();
			String todayDate = f.format(date);
			System.out.println("today date:" + todayDate);// today date
			System.out.println("string compare: last array list cut with today date==>"
					+ substringStartEightDigit.equals(todayDate));// boolean result
			if (substringStartEightDigit.equals(todayDate)) {
				try {
					inum = Integer.parseInt(substringafterninedigit);
					inum = inum + 1;
					System.out.println("new inum " + inum);
				} catch (NumberFormatException ex) {
					System.out.println("Execption in splitting");
					System.out.println("ERROR" + ex);
				}
				salesIDnew = todayDate + "0" + inum;
			} else {
				salesIDnew = todayDate + "01";
				System.out.println("new date start with 1");
			}

		}
		invoiceNo.setText(salesIDnew);
		return salesIDnew;

	}

	private boolean addToSales() {
		salesIdxx = salesIdMaking(); // invoice no. making
		// when the pc date makes error or happens screen date equals to previous date
		String queryCheckSalesId = "select count(*) from sales where salesid='" + salesIdxx + "'";
		Boolean res = handler.isSalesIdAlreadyExist(queryCheckSalesId);
		if (res) {
			System.out.println("sales id already exist making new id with id+duplic !");
			salesIdxx = salesIdxx + "DPLC";
		}
		System.out.println("sales id not already exist sales id is: " + salesIdxx);
		String cusidxx = customerID.getText();
		String cusnamexx = customerName.getText();
		Float salesTotalxx = Float.parseFloat(totalAmount.getText());
		int salesItemnoxx = piList.size();
		String salesBillerxx = billerName.getText();

		Float cashPaidxx = Float.parseFloat(cashRecieved.getText());
		Float cashBalancexx = Float.parseFloat(cashBalance.getText());

		String query = "INSERT INTO SALES"
				+ "(salesid,cusid,cusname,salestotal,salesitemno,salesbillpaid,salesbalance,salesbiller) VALUES(" + "'"
				+ salesIdxx + "'," + "'" + cusidxx + "'," + "'" + cusnamexx + "'," + salesTotalxx + "," + salesItemnoxx
				+ "," + cashPaidxx + "," + cashBalancexx + "," + "'" + salesBillerxx + "'" + ")";
		System.out.println("query  ==>:" + query);
		if (handler.executeQuery(query)) {
			System.out.println("succesfully sales inserted");
			return true;
		} else {
			System.out.println("failed sales insertion");
		}
		return false;
	}


	public int printORbackStatusretun() {
		int i = pob.getStatus();
		return i;
	}

	private void printerActionOnPrintButtonAction() throws JRException {
		SimpleDateFormat formatterToday = new SimpleDateFormat("dd/MM/yyyy HH:mm a");
		Date todayDate = new Date();
		String todayDateTime = formatterToday.format(todayDate);

		String invno = "SLINV" + salesIdxx;
		String amTowrd = amountWord.getText().toUpperCase() + " Rupees Only";

		JRBeanCollectionDataSource itemsJRbean = new JRBeanCollectionDataSource(salesPrintList);
		Map<String, Object> parameters = new HashMap<>();
//		String destpdfFile = "D:\\" + invno + ".pdf";
		File filePath = new File("InvoiceFiles");
		if(!filePath.exists()) {
			filePath.mkdir();
		}
		String destpdfFile = "InvoiceFiles\\" + invno + ".pdf";

		parameters.put("SuperMarketParam", itemsJRbean);
		parameters.put("InvoiceNo", invno);
		parameters.put("DateAndTime", todayDateTime);
		parameters.put("AmountInWords", amTowrd);
		parameters.put("Biller", billerName.getText());
		parameters.put("Customer", customerName.getText());
		parameters.put("ToPay", totalAmount.getText());
		parameters.put("PaidAmount", cashRecieved.getText());
		parameters.put("Balance", cashBalance.getText());

		JasperReport jr = JasperCompileManager.compileReport("src/main/resources/SuperMarketPrint.jrxml");
		JasperPrint jp = JasperFillManager.fillReport(jr, parameters, new JREmptyDataSource());

		JasperExportManager.exportReportToPdfFile(jp, destpdfFile);
		JasperViewer.viewReport(jp, false);
	}

	private void textFieldClearByEditing() {
		cashRecieved.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				cashBalance.setText("");
			}
		});
	}
}
