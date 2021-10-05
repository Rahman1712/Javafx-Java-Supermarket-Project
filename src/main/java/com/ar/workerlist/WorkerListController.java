
package com.ar.workerlist;

import com.ar.database.DatabaseSectionMain;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
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
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

public class WorkerListController implements Initializable {
	DatabaseSectionMain handler;

	@FXML
	private JFXButton allButton;
	@FXML
	private JFXButton salesmanButton;
	@FXML
	private JFXButton billerButton;
	@FXML
	private JFXButton cleaningButton;
	@FXML
	private JFXButton managerButton;
	@FXML
	private TableView<Workers> tableViewDatas;
	@FXML
	private TableColumn<Workers, String> colID;
	@FXML
	private TableColumn<Workers, String> colName;
	@FXML
	private TableColumn<Workers, String> colMob;
	@FXML
	private TableColumn<Workers, String> colAadhar;
	@FXML
	private TableColumn<Workers, String> colAddress;
	@FXML
	private TableColumn<Workers, String> colSection;
	@FXML
	private TableColumn<Workers, String> colEmail;
	@FXML
	private AnchorPane mainAnchorPane;
	@FXML
	private AnchorPane secAnchorPane;
	@FXML
	private SplitPane splitPane;
	@FXML
	private AnchorPane firstAnchorPaneInSplit;
	@FXML
	private AnchorPane secondAnchorPaneInSplit;
	ObservableList<Workers> workerList = FXCollections.observableArrayList();
	@FXML
	private JFXTextField searchStaffTextField;

	int statusno = 0;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		try {
			handler = DatabaseSectionMain.getInstance();
		} catch (ClassNotFoundException | SQLException ex) {
			Logger.getLogger(WorkerListController.class.getName()).log(Level.SEVERE, null, ex);
		}

		initcol();
		loadData();
//        tableViewDatas.setFixedCellSize(Region.USE_COMPUTED_SIZE);
		tableViewSearchByTextfild();
	}

	private void initcol() {
		colID.setCellValueFactory(new PropertyValueFactory<>("worID"));
		colName.setCellValueFactory(new PropertyValueFactory<>("worName"));
		colSection.setCellValueFactory(new PropertyValueFactory<>("worSection"));
		colMob.setCellValueFactory(new PropertyValueFactory<>("worMob"));
		colEmail.setCellValueFactory(new PropertyValueFactory<>("worEmail"));
		colAadhar.setCellValueFactory(new PropertyValueFactory<>("worIDProof"));
		colAddress.setCellValueFactory(new PropertyValueFactory<>("worAddress"));
	}

	private void loadData() {
//         String query = "select * from STAFFS order by stafftime and staffsection";
		String query = "select * from STAFFS order by stafftime";
		ResultSet rs = handler.resultexecQuery(query);
		try {
			while (rs.next()) {
				String woridx = rs.getString("staffid");
				String wornamex = rs.getString("staffname");
				String worsectionx = rs.getString("staffsection");
				String wormobilex = rs.getString("staffmobile");
				String woremailx = rs.getString("staffemail");
				String woridproofx = rs.getString("staffaadhar");
				String woraddressx = rs.getString("staffaddress");
//                System.out.println("word address==>:"+woraddressx);
				String[] word = woraddressx.split("\n");
				String newworaddressx = "";
				for (String wordcutplus : word) {
					newworaddressx = newworaddressx + wordcutplus + " ";
				}
				System.out.println("new address without nextline:" + newworaddressx);
				workerList.add(
						new Workers(woridx, wornamex, worsectionx, wormobilex, woremailx, woridproofx, newworaddressx));
			}
		} catch (SQLException ex) {
			Logger.getLogger(WorkerListController.class.getName()).log(Level.SEVERE, null, ex);
		}
		tableViewDatas.setItems(workerList);
	}

	// =============================//workersearch by
	// textfield//=============================//
	private boolean searchFindsWorkersByTextfield(Workers workers, String searchText, String sectionText) {

		return (workers.getWorName().toLowerCase().contains(searchText.toLowerCase())
				&& workers.getWorSection().toLowerCase().contains(sectionText.toLowerCase()));
	}

	private ObservableList<Workers> filterListByTextfield(List<Workers> list, String searchText, String sectionText) {
		List<Workers> filteredList = new ArrayList<>();
		for (Workers workers : list) {
			if (searchFindsWorkersByTextfield(workers, searchText, sectionText))
				filteredList.add(workers);
		}
		return FXCollections.observableArrayList(filteredList);
	}

	private void tableViewSearchByTextfild() {
		searchStaffTextField.textProperty().addListener(((observable, oldValue, newValue) -> {
			switch (statusno) {
			case 1:
				tableViewDatas.setItems(filterListByTextfield(workerList, newValue, "SALES MAN"));
				break;
			case 2:
				tableViewDatas.setItems(filterListByTextfield(workerList, newValue, "BILLER"));
				break;
			case 3:
				tableViewDatas.setItems(filterListByTextfield(workerList, newValue, "CLEANING"));
				break;
			case 4:
				tableViewDatas.setItems(filterListByTextfield(workerList, newValue, "MANAGER"));
				break;
			default:
				tableViewDatas.setItems(filterListByTextfield(workerList, newValue, ""));
				break;
			}
		}));
	}
//=============================================================//

//=================================//workerselection by button//======================================================//
	private boolean searchFindsWorkers(Workers workers, String searchText) {
		return (workers.getWorSection().toLowerCase().contains(searchText.toLowerCase()));
	}

	private ObservableList<Workers> filterList(List<Workers> list, String searchText) {
		List<Workers> filteredList = new ArrayList<>();
		for (Workers workers : list) {
			if (searchFindsWorkers(workers, searchText))
				filteredList.add(workers);
		}
		return FXCollections.observableArrayList(filteredList);
	}

	@FXML
	private void allButtonAction(ActionEvent event) {
		searchStaffTextField.setText("");
		statusno = 0;
		tableViewDatas.setItems(workerList);
		colSection.setVisible(true);
		buttonBorder(allButton);
	}

	@FXML
	private void salesManButtonaction(ActionEvent event) {
		searchStaffTextField.setText("");
		String sectionText = "SALES MAN";
		tableViewDatas.setItems(filterList(workerList, sectionText));
		colSection.setVisible(false);
		buttonBorder(salesmanButton);
		statusno = 1;
	}

	@FXML
	private void billerButtonAction(ActionEvent event) {
		searchStaffTextField.setText("");
		String sectionText = "BILLER";
		tableViewDatas.setItems(filterList(workerList, sectionText));
		colSection.setVisible(false);
		buttonBorder(billerButton);
		statusno = 2;
	}

	@FXML
	private void cleaningButtonAction(ActionEvent event) {
		searchStaffTextField.setText("");
		String sectionText = "CLEANING";
		tableViewDatas.setItems(filterList(workerList, sectionText));
		colSection.setVisible(false);
		buttonBorder(cleaningButton);
		statusno = 3;
	}

	@FXML
	private void managerButtonaction(ActionEvent event) {
		searchStaffTextField.setText("");
		String sectionText = "MANAGER";
		tableViewDatas.setItems(filterList(workerList, sectionText));
		colSection.setVisible(false);
		buttonBorder(managerButton);
		statusno = 4;
	}

	private void buttonBorder(JFXButton but) {

		ArrayList<JFXButton> buttons = new ArrayList<>();
		buttons.add(allButton);
		buttons.add(billerButton);
		buttons.add(salesmanButton);
		buttons.add(cleaningButton);
		buttons.add(managerButton);
		buttons.forEach((bn) -> {
			if (but == bn) {
				bn.setStyle(
						"-fx-border-color:white; -fx-background-color:linear-gradient(to right,#1dbbdd44, #93f9b944);");
			} else {
				bn.setStyle("-fx-border-color:null; -fx-background-color:transparent;");
			}
		});
	}

	@FXML
	private void refreshButtonAction(ActionEvent event) {
		allButtonAction(null);
		workerList.clear();
		loadData();
		tableViewDatas.refresh();
	}
}
