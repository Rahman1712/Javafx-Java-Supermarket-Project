
package com.ar.addsales;

import com.ar.database.DatabaseSectionMain;
import com.ar.numbertoword.ConvertNumToWord;
import com.ar.proceedsales.ProceedSalesController;
import com.ar.salesbillprint.SalesPrintController;
import com.ar.util.SuperMarketUtils;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.converter.IntegerStringConverter;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

public class MainSalesUIController implements Initializable {
	DatabaseSectionMain handler;
	SalesPrintController slp = new SalesPrintController();
	String barcode;
	ArrayList<String> listPrNames = new ArrayList<>();
	ArrayList<String> cusList = new ArrayList<>();
	private AutoCompletionBinding<String> autocom;
	private AutoCompletionBinding<String> autocustom;
	Boolean isNewCustomer = false;
	int inum;
	String cusnewidx;
//    final QuantityNumber quantNum = new QuantityNumber();

	SalesPrintController spc;

	@FXML
	private TextField salBarcode;
	@FXML
	private TextField salName;
	@FXML
	private TextField salQuant;
	@FXML
	private TextField salMRP;
	@FXML
	private TextField salDisc;
	@FXML
	private TextField salPrice;
	@FXML
	private TextField custMob;
	@FXML
	private TextField custPlace;
	@FXML
	private TextField custName;
	@FXML
	private Label custID;
	@FXML
	private Label custDescription;
	@FXML
	private Label custStatus;
	@FXML
	private TableView<Sales> salesTable;
//    @FXML
//    private TableColumn<Sales, Integer> colProSerialNo;
	@FXML
	private TableColumn<Sales, String> colProBarCode;
	@FXML
	private TableColumn<Sales, String> colProName;
	@FXML
	private TableColumn<Sales, Float> colProMRP;
	@FXML
	private TableColumn<Sales, Integer> colProQuantity;
	@FXML
	private TableColumn<Sales, Float> colProDiscount;
	@FXML
	private TableColumn<Sales, Float> colProPrice;
	@FXML
	private TableColumn<Sales, Float> colProTotal;

	ObservableList<Sales> salesList = FXCollections.observableArrayList();
	SalesTotal slt = new SalesTotal(0);

	@FXML
	private BorderPane borderPaneMain;
	@FXML
	public Button cancelButtonToHide;
	@FXML
	private Label amountWord;
	@FXML
	private Label totalAmount;
	private final SimpleStringProperty text = new SimpleStringProperty("rs");
	ConvertNumToWord convert = new ConvertNumToWord();
	@FXML
	private Label billerbiller;
	@FXML
	private AnchorPane mainAnchorPaneofScreen;
	@FXML
	private Button proceedButton;

	float productPriceOnly = 0;
	DecimalFormat deciformat = new DecimalFormat("0.00");

	@Override
	public void initialize(URL url, ResourceBundle rb) {

		try {
			handler = DatabaseSectionMain.getInstance();
		} catch (ClassNotFoundException | SQLException ex) {
			Logger.getLogger(MainSalesUIController.class.getName()).log(Level.SEVERE, null, ex);
		}
		salQuant.setEditable(false);
		salName.setEditable(false);
		cancelButtonToHide.setDisable(true);
		quantityTextFieldUpDownAction();
		initcol();
		editableCols();
		deleteCols();
		bindingPriceval();
	}

	private void initcol() {
		// int proSerNo;String proBar;String proNam;Float proMR;int proQu;Float
		// proDs;Float proPr;
//        colProSerialNo.setCellValueFactory(new PropertyValueFactory<>("proSerNo"));
		colProBarCode.setCellValueFactory(new PropertyValueFactory<>("proBar"));
		colProName.setCellValueFactory(new PropertyValueFactory<>("proNam"));
		colProMRP.setCellValueFactory(new PropertyValueFactory<>("proMR"));
		colProQuantity.setCellValueFactory(new PropertyValueFactory<>("proQu"));
		colProDiscount.setCellValueFactory(new PropertyValueFactory<>("proDs"));
		colProPrice.setCellValueFactory(new PropertyValueFactory<>("proPr"));
		colProTotal.setCellValueFactory(new PropertyValueFactory<>("proTotPr"));

		salesTable.setEditable(true);
		colProQuantity.setEditable(true);
	}

	private void quantityTextFieldUpDownAction() {

		salQuant.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode().equals(KeyCode.UP)) {
					if (salQuant.getText().isEmpty()) {
						System.out.println("null quantity");
					} else {
						int num = Integer.parseInt(salQuant.getText());
						num = num + 1;
						salQuant.setText(String.valueOf(num));
						quantityChangePriceChange();
					}
				}
				if (event.getCode().equals(KeyCode.DOWN)) {
					if (salQuant.getText().isEmpty()) {
						System.out.println("null quantity");
					} else {
						int num = Integer.parseInt(salQuant.getText());
						if (num == 1) {
							num = 1;
						} else {
							num = num - 1;
						}
						salQuant.setText(String.valueOf(num));
						quantityChangePriceChange();
					}
				}
			}
		});
	}

	private void quantityChangePriceChange() {
		if (!salQuant.getText().isEmpty() || salQuant == null) {
			String[] word = salDisc.getText().split(" %");
			String arr = "";
			for (String s : word) {
				arr = s;
			}
			Float discount = Float.parseFloat(arr);
			Float value;
			if (discount == 0.0) {
				value = Float.parseFloat(salMRP.getText()) * Integer.parseInt(salQuant.getText());
			} else {
				value = Float.parseFloat(salMRP.getText()) * discount * Integer.parseInt(salQuant.getText());
			}
			String textval = value.toString();
			salPrice.setText(textval);
		}
	}

	@FXML
	private void barcodeTextfieldAction(ActionEvent event) throws SQLException {
		barcode = salBarcode.getText();
		String query1 = "select count(*) from PRODUCTS where PRBARCODE='" + barcode + "'";
		String query2 = "select * from products where PRBARCODE='" + barcode + "'";

		ResultSet rs1 = handler.resultexecQuery(query1);
		ResultSet rs2 = handler.resultexecQuery(query2);
		int count = 0;
		try {

			if (rs1.next()) {
				count = rs1.getInt(1);
			}
			System.out.println("count:" + count);

		} catch (SQLException ex) {
			System.out.println("error in barcode enter");
			Logger.getLogger(MainSalesUIController.class.getName()).log(Level.SEVERE, null, ex);

		}
		System.out.println("count out try:" + count);
		if (count == 1) {
			while (rs2.next()) {
				salName.setText(rs2.getString("prname"));
				Float salesMRP = rs2.getFloat("prmrp");
				salMRP.setText(salesMRP.toString());
				Float salesDisc = rs2.getFloat("prdiscount");
				salDisc.setText(salesDisc.toString() + " %");

				Float salesPriceTaked = rs2.getFloat("prprice");
				String corectedProdPrice = deciformat.format(salesPriceTaked);
				Float salesPrice = Float.parseFloat(corectedProdPrice);
				salPrice.setText(salesPrice.toString());

//                productPriceOnly=rs2.getFloat("prprice");///////////////////////assigning discount kayichitulla price 
				productPriceOnly = Float.parseFloat(corectedProdPrice);/////////////////////// assigning discount
																		/////////////////////// kayichitulla price
				System.out.println("============" + productPriceOnly);
				salQuant.setText("1");
				salQuant.setEditable(true);
				salQuant.requestFocus();

			}

		}
		if (count > 1) {

			while (rs2.next()) {
				String namex = (rs2.getString("prname"));
				listPrNames.add(namex);
				autocom = TextFields.bindAutoCompletion(salName, listPrNames);
				salName.setEditable(true);
				salName.requestFocus();
			}

		}
		System.out.println("list valus:" + listPrNames);
//               System.out.println("size 0f list----> "+listPrNames.size());
//               System.out.println("2nd element:"+listPrNames.get(1));
//                listPrNames.clear();
//                autocom.dispose();
	}

	@FXML
	private void nameTextfieldAction(ActionEvent event) {
		if (salName.getText().isEmpty()) {
			System.out.println("NO element null textfield");
		} else {
			try {
				String query = "select * from products where prbarcode='" + salBarcode.getText() + "' and prname='"
						+ salName.getText() + "'";
				ResultSet rs = handler.resultexecQuery(query);
				while (rs.next()) {
					salMRP.setText(String.valueOf(rs.getFloat("prmrp")));
					salDisc.setText(String.valueOf(rs.getFloat("prdiscount")));
					salQuant.setText("1");

//                salPrice.setText(String.valueOf(rs.getFloat("prprice")));===============
					Float salesPriceTaked = rs.getFloat("prprice");
					String corectedProdPrice = deciformat.format(salesPriceTaked);
					Float salesPrice = Float.parseFloat(corectedProdPrice);
					salPrice.setText(salesPrice.toString());

//                productPriceOnly = rs.getFloat("prprice");////////////////////price value taking discount kayichitulla if onniladikamm product with same barcode
					productPriceOnly = Float.parseFloat(corectedProdPrice);//////////////////// price value taking
																			//////////////////// discount kayichitulla
																			//////////////////// if onniladikamm product
																			//////////////////// with same barcode

					salQuant.setEditable(true);
					salQuant.requestFocus();
					listPrNames.clear();
					autocom.dispose();

				}
			} catch (SQLException ex) {
				Logger.getLogger(MainSalesUIController.class.getName()).log(Level.SEVERE, null, ex);
			}
		}

	}

	@FXML
	private void custMobTextfieldAction(ActionEvent event) throws SQLException {
		if (custMob.getText().isEmpty()) {
			System.out.println("null textfield customer id");
		} else {
			String querycus = "select count(*) from customer where cusmob='" + custMob.getText() + "'";
			int custResult = customerCountFunction(querycus);
			if (custResult == 1) {
				System.out.println("customer already exists");
				String queryFetchCustomer = "select *from customer where cusmob='" + custMob.getText() + "'";
				ResultSet equalsOne = handler.resultexecQuery(queryFetchCustomer);
				while (equalsOne.next()) {
					custName.setText(equalsOne.getString("cusname"));
					custPlace.setText(equalsOne.getString("cusplace"));
					custID.setText(equalsOne.getString("cusid"));
					custDescription.setText("existing customer");
					custStatus.setText("existing");
					custStatus.setStyle("-fx-background-color:derive(blue, -20%); -fx-text-fill:white;");
					custDescription.setStyle("-fx-background-color:skyblue; -fx-text-fill:black;");
					salBarcode.requestFocus();
				}
				isNewCustomer = false;
			}
			if (custResult > 1) {
				String query = "select cusname from customer where cusmob='" + custMob.getText() + "'";
				ResultSet rs = handler.resultexecQuery(query);
				while (rs.next()) {
					String custNamex = rs.getString("cusname");
//                System.out.println("cusname:"+custNamex);
					cusList.add(custNamex);
//                System.out.println("list customer:===>"+cusList);

				}
				autocustom = TextFields.bindAutoCompletion(custName, cusList);

				custDescription.setText("more than one customer having same number");
				custDescription.setStyle("-fx-background-color:skyblue; -fx-text-fill:black;");
				custStatus.setText("existing");
				custStatus.setStyle("-fx-background-color:blue; -fx-text-fill:white;");
				custName.requestFocus();
				isNewCustomer = false;
			}
			if (custResult == 0) {
				System.out.println("new customer");
				isNewCustomer = true;
				custStatus.setText("new");
				custDescription.setText("");
				custStatus.setStyle("-fx-background-color:green; -fx-text-fill:white;");
				custName.requestFocus();
			}

		}

	}

	@FXML
	private void custNameTextfieldAction(ActionEvent event) {
		if (isNewCustomer) {
			custPlace.requestFocus();
		} else {
			String qu = "select count(*) from CUSTOMER where CUSMOB='" + custMob.getText() + "' and CUSNAME='"
					+ custName.getText() + "'";
			Boolean custNameBool = customerNameFunction(qu);
			String query = "select * from CUSTOMER where CUSMOB='" + custMob.getText() + "' and CUSNAME='"
					+ custName.getText() + "'";
			ResultSet rs = handler.resultexecQuery(query);
			try {
				while (rs.next()) {
					custPlace.setText(rs.getString("cusplace"));
					custID.setText(rs.getString("cusid"));
					custDescription.setText("existing customer");

				}

			} catch (SQLException ex) {
				System.out.println("Error in textfield name to clear moving");
				custNameBool = false;
				Logger.getLogger(MainSalesUIController.class.getName()).log(Level.SEVERE, null, ex);
			}

			if (custNameBool) {
				salBarcode.requestFocus();
				cusList.clear();
			} else {
				custPlace.requestFocus();
			}
			autocustom.dispose();

		}

	}

	@FXML
	private void custPlaceTextfieldAction(ActionEvent event) {
		try {

			cusList.clear();
			String query = "select cusid from customer order by custime ASC";
			ResultSet rs = handler.resultexecQuery(query);
			while (rs.next()) {
				String custIDx = rs.getString("cusid");
				System.out.println("cuid:" + custIDx);
				cusList.add(custIDx);
				System.out.println("list customer:===>" + cusList);

			}
		} catch (SQLException ex) {
			Logger.getLogger(MainSalesUIController.class.getName()).log(Level.SEVERE, null, ex);
		}
		if (cusList.isEmpty()) {
			cusnewidx = "cus01";
		} else {
			String str;
			str = (cusList.get(cusList.size() - 1)).toUpperCase();
			if ("CUS00".equals(str)) {
				if (cusList.size() > 1) {
					str = (cusList.get(cusList.size() - 2)).toUpperCase();
				}
				if (cusList.size() == 1) {
					str = "CUS00";
				}
			}
			String[] arr = str.split("CUS0");
			String ss = "";
			for (String s : arr) {
				System.out.println(s);
				ss = s;
			}
			try {
				inum = Integer.parseInt(ss);
				inum = inum + 1;
				System.out.println("inum is : " + inum);
			} catch (NumberFormatException ex) {
				System.out.println("Execption in splitting");
				System.out.println("ERROR" + ex);
			}

			cusnewidx = "cus0" + inum;
		}
		// INSERTION

		String query = "insert into customer(cusid,cusname,cusplace,cusmob) values(" + "'" + cusnewidx + "'," + "'"
				+ custName.getText() + "'," + "'" + custPlace.getText() + "'," + "'" + custMob.getText() + "'" + ")";

		System.out.println(query);
		if (handler.executeQuery(query)) {
			System.out.println("Customer insertion success");
			custID.setText(cusnewidx);
			custDescription.setText("success insertion .newly added customer");
			custDescription.setStyle("-fx-background-color:skyblue; -fx-text-fill:black;");
		} else {
			System.out.println("Error occurred during insertion");
		}
		cusList.clear();
		salBarcode.requestFocus();

	}

	private boolean customerNameFunction(String query) {

		try {
			ResultSet rs = handler.resultexecQuery(query);
			if (rs.next()) {
				int count = rs.getInt(1);
				System.out.println("Count of nameFunction :" + count);
				return (count > 0);
			}
		} catch (SQLException ex) {
			Logger.getLogger(MainSalesUIController.class.getName()).log(Level.SEVERE, null, ex);
		}
		return false;
	}

	private int customerCountFunction(String query) {
		int count;
		try {
			ResultSet rs = handler.resultexecQuery(query);
			if (rs.next()) {
				count = rs.getInt(1);
				System.out.println("count of cusid=======> " + count);
				return count;
			}

		} catch (SQLException ex) {
			Logger.getLogger(MainSalesUIController.class.getName()).log(Level.SEVERE, null, ex);
		}
		return 0;
	}

	@FXML
	public void clearAction(ActionEvent event) {
		custID.setText("CUSID");
		custDescription.setText("");
		custMob.clear();
		custName.clear();
		custPlace.clear();
		cusList.clear();
		isNewCustomer = false;
		custStatus.setText("");
		custStatus.setStyle("-fx-background-color:transparent; -fx-text-fill:black;");
		custDescription.setStyle("-fx-background-color:transparent; -fx-text-fill:black;");
//        autocustom.dispose();
	}

	@FXML
	private void clearSalesFieldAction(ActionEvent event) {
		salBarcode.clear();
		salMRP.clear();
		salDisc.clear();
		salName.clear();
		salPrice.clear();
		salQuant.clear();
		salName.setEditable(false);
		salQuant.setEditable(false);
	}

	@FXML
	private void cancelAction(ActionEvent event) {
		((Stage) salesTable.getScene().getWindow()).close();

	}

	private void editableCols() {

		colProQuantity.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
		colProQuantity.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Sales, Integer>>() {
			@Override
			public void handle(TableColumn.CellEditEvent<Sales, Integer> event) {

				Sales item = event.getRowValue();
				int i = item.getProQu();// payaya quantity i leku edukunnu
				item.setProQu(event.getNewValue());// pudhiya quantity set cheyyunnu
				item.setProTotPr(item.getProQu() * item.getProPr());// puthiya total set cheyyunnu (puthiya quad *
																	// discount kayichitulla price)
				slt.setSalesTot((slt.getSalesTot() - i * item.getProPr()));// SalesTotal.class le setter leku total ne
																			// set cheyyunnu ie aadyam payayathu maatam
																			// varutthi puthi yathu cherkuka
				// mukalil ullathinde baaki -->ie, payaya totalil ninu kurakuka (payaya quantity
				// * discount kayichitulla price)
				slt.setSalesTot(slt.getSalesTot() + item.getProQu() * item.getProPr());// puthiya quantity * discount
																						// kayichitulla price chertu
																						// kodukunnu setter il cherkunnu

//                totalAmount.setText(String.valueOf(slt.getSalesTot()));   ========MAATAM
				totalAmount.setText(deciformat.format(slt.getSalesTot()));

				bindingTotalAmountToWord();
				salesTable.refresh();
				System.out.println("old value quantity:" + i);
				System.out.println("new value quqntity:" + item.getProQu());
				System.out.println("new value tot price:" + item.getProTotPr());
				System.out.println("new grandtotal:" + slt.getSalesTot());
			}
		});
	}

	private void deleteCols() {
		salesTable.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				Sales sltditm = salesTable.getSelectionModel().getSelectedItem();
				if (sltditm != null) {
					if (event.getCode().equals(KeyCode.DELETE)) {
						float priceToDel = sltditm.getProPr();
						slt.setSalesTot(slt.getSalesTot() - priceToDel);

//                    totalAmount.setText(String.valueOf(slt.getSalesTot()));
						totalAmount.setText(deciformat.format(slt.getSalesTot()));

						salesList.remove(sltditm);
						bindingTotalAmountToWord();
						salesTable.refresh();
					}
				}
			}
		});
	}

	@FXML
	private void quantityTextfieldAction(ActionEvent event) {
		if (salBarcode.getText().isEmpty() || salName.getText().isEmpty() || salQuant.getText().isEmpty()) {
			System.out.println("Empty columns Found");
		} else {
//        int sno = 1;
			String br = salBarcode.getText();
			String pn = salName.getText();
			Float mr = Float.parseFloat(salMRP.getText());
			int qu = Integer.parseInt(salQuant.getText());
			String[] word = salDisc.getText().split(" %");
			String arr = "";
			for (String s : word) {
				arr = s;
			}
			Float ds = Float.parseFloat(arr);
//        Float pr = Float.parseFloat(salPrice.getText()); //issue when we adding quantity by typing

			Float pr;
			if (ds != 0 || ds != 0.0 || ds != 0.00) {
				pr = mr * qu * (1 - ds / 100);
			} else {
				pr = mr * qu;
			}
			//////// ==========MAATAM ===========////////
			Float salesPriceNew = pr;
			String corectedProdPrice = deciformat.format(salesPriceNew);
			Float salesPricePr = Float.parseFloat(corectedProdPrice);

//        salesList.add(new Sales(br, pn, mr, ds, qu,productPriceOnly, pr));
			salesList.add(new Sales(br, pn, mr, ds, qu, productPriceOnly, salesPricePr));

			salesTable.setItems(salesList);

//        slt.setSalesTot(slt.getSalesTot()+pr);
			slt.setSalesTot(slt.getSalesTot() + salesPricePr);

//        totalAmount.setText(String.valueOf(slt.getSalesTot()));
			totalAmount.setText(deciformat.format(slt.getSalesTot()));

			bindingTotalAmountToWord();
			clearSalesFieldAction(null);
			salBarcode.requestFocus();

		}
	}

	private void bindingTotalAmountToWord() {
		if (totalAmount.getText().isEmpty()) {
			System.out.println("empty totalamount label");
		} else {
			float num = Float.parseFloat(totalAmount.getText());
			long number = (long) num;
			String convertNumber = ConvertNumToWord.convertNumber(number);
			text.set(convertNumber);
			amountWord.textProperty().bind(text);
		}
	}

	@FXML
	private void bindingToPriceTextfieldFromQuantTextfield(InputMethodEvent event) {
//        salQuant.textProperty().addListener(new ChangeListener<String>(){
//            @Override
//            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
//                System.out.println("text changed to  "+newValue+"\n");
//            }
//            
//        });

	}

	private void bindingPriceval() {
		salQuant.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				System.out.println("textfield changed from :" + oldValue + " to:" + newValue);
				if (salQuant.getText().isEmpty() || salQuant.getText().equals("0")) {
					System.out.println("no quant , null quant");
				} else {
					if (!salDisc.getText().isEmpty() || !salMRP.getText().isEmpty()) {
						Float mr = Float.parseFloat(salMRP.getText());
						int qu = Integer.parseInt(salQuant.getText());
						String[] word = salDisc.getText().split(" %");
						String arr = "";
						for (String s : word) {
							arr = s;
						}
						Float ds = Float.parseFloat(arr);
						Float pr;
						if (ds != 0 || ds != 0.0 || ds != 0.00) {
							pr = mr * qu * (1 - ds / 100);
						} else {
							pr = mr * qu;
						}
						/////////////////////// ==========putukiyAthu======
						String corectedProdPrice = deciformat.format(pr);
						salPrice.setText(corectedProdPrice);
//                     salPrice.setText(pr.toString());
					}
				}
			}
		});
	}

	@FXML
	private void proceedAction(ActionEvent event) {
		ObservableList<Sales> s = salesList;
		String cusIdToSend;
		String cusnametoSend;
		// if cusid = CUSID then we have to take value is cus00 because other wise it
		// shows foreign key problem problem leads to stuck
		if (custID.getText().equals("CUSID")) {
			cusIdToSend = "cus00";
		} else {
			cusIdToSend = custID.getText();
		}
		if (custName.getText().isEmpty()) {
			cusnametoSend = "customer";
		} else {
			cusnametoSend = custName.getText();
		}
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ar/proceedsales/ProceedSales.fxml"));
			Parent parent = loader.load();
			Stage stage = new Stage(StageStyle.DECORATED);
			stage.setAlwaysOnTop(true); // making window on top
//            stage.setFocused(true);
			stage.initModality(Modality.APPLICATION_MODAL);// making window open then only acees this window (no
															// minimize button in this )
//            stage.requestFocus();

			stage.setTitle("PPRINT SALES");
			SuperMarketUtils.setStageIcon(stage);
			ProceedSalesController controller = (ProceedSalesController) loader.getController();
			controller.inflateUI(s);
			controller.transferTextFieldCustomerData(cusIdToSend, cusnametoSend);
			controller.transferAmountData(totalAmount.getText(), amountWord.getText());
			controller.billerData(billerbiller.getText());
			stage.setScene(new Scene(parent));
			stage.showAndWait();

			if (controller.printORbackStatusretun() == 1) {
				System.out.println("value :" + controller.printORbackStatusretun() + " printed");
				proceedButton.setDisable(true);
				proceedButton.getStyleClass().add("buttonprintsuccess");
				proceedButton.setText("PRINTED");

//                 ((Stage)billerbiller.getScene().getWindow()).close();
			} else {
				System.out.println("value :" + controller.printORbackStatusretun() + " backed");
			}

		} catch (IOException ex) {
			Logger.getLogger(MainSalesUIController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	

	public void inflateUIBiller(String bx) {
		billerbiller.setText(bx);
	}

}
