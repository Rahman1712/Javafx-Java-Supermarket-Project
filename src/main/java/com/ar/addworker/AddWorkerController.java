
package com.ar.addworker;

import com.ar.alertmaker.AlertMaker;
import com.ar.database.DatabaseSectionMain;
import com.ar.util.PassWordSaver;
import com.ar.util.PasswordSavingModel;
import com.google.gson.Gson;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.apache.commons.codec.digest.DigestUtils;

public class AddWorkerController implements Initializable {
	DatabaseSectionMain handler;
	ObservableList<String> list = FXCollections.observableArrayList("BILLER", "SALES MAN", "MANAGER", "CLEANING");
	ArrayList<String> salesSectionList = new ArrayList<>();

	@FXML
	private ComboBox<String> sectionCombobox;
	@FXML
	private TextField workerName;
	@FXML
	private TextField workerEmail;
	@FXML
	private TextField workerMobile;
	@FXML
	private TextField workerAadhar;
	@FXML
	private TextArea workerAddress;
	@FXML
	private ImageView imageWorker;
	@FXML
	private TextField userName;
	@FXML
	private TextField password;
	@FXML
	private TextField confirmPassword;
	@FXML
	private Button addButton;
	@FXML
	private Label labelConfirm;
	@FXML
	private PasswordField passpassword;
	@FXML
	private PasswordField passConfirmPassword;
	@FXML
	private CheckBox checkBoxChannger;
	@FXML
	private AnchorPane mainAnchorPane;

	PassWordSaver passSaver = new PassWordSaver();
	public static final String CONFIG_FILE = "bwuap.lnp";
	FileReader file;
	List<PasswordSavingModel> newList = new ArrayList<>();

	@Override
	public void initialize(URL url, ResourceBundle rb) {

		try {
			handler = DatabaseSectionMain.getInstance();

		} catch (ClassNotFoundException | SQLException ex) {
			Logger.getLogger(AddWorkerController.class.getName()).log(Level.SEVERE, null, ex);
		}

		sectionCombobox.setItems(list);
		userName.setDisable(true);
		password.setDisable(true);
		confirmPassword.setDisable(true);
		passpassword.setDisable(true);
		passConfirmPassword.setDisable(true);
		reTypingPassword();
		password.textProperty().bindBidirectional(passpassword.textProperty());
		confirmPassword.textProperty().bindBidirectional(passConfirmPassword.textProperty());
		addButton.setDisable(true);
		password.setVisible(false);
		confirmPassword.setVisible(false);
	}

	@FXML
	private void addWorkerAction(ActionEvent event) {
		String staffsection;
		String newStaffId = "";

		if (sectionCombobox.getValue() == null || "".equals(sectionCombobox.getValue())) {
			System.out.println("empty section");
			return;
		} else {

			staffsection = sectionCombobox.getValue();
			String q = "select staffid from staffs where staffsection='" + staffsection + "' order by stafftime";
			ResultSet rs = handler.resultexecQuery(q);
			try {
				while (rs.next()) {
					String section = rs.getString("staffid");
					System.out.println("staffid:" + section);
					salesSectionList.add(section);
				}
			} catch (SQLException ex) {
				Logger.getLogger(AddWorkerController.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		System.out.println("list  :" + salesSectionList);
		if (salesSectionList.isEmpty()) {

			switch (staffsection) {
			case "BILLER":
				newStaffId = "STFBLR01";
				break;
			case "SALES MAN":
				newStaffId = "STFSLM01";
				break;
			case "MANAGER":
				newStaffId = "STFMGR01";
				break;
			case "CLEANING":
				newStaffId = "STFCLN01";
				break;
			default:
				System.out.println("no case found");
			}
		} else {
			String str = salesSectionList.get(salesSectionList.size() - 1);
			String[] arr;
			String word = "";
			int num;
			switch (staffsection) {
			case "BILLER":
				arr = str.split("STFBLR0");
				for (String s : arr) {
					System.out.println("after split: " + s);
					word = s;
				}
				num = Integer.parseInt(word);
				num = num + 1;
				newStaffId = "STFBLR0" + num;
				break;
			case "SALES MAN":
				arr = str.split("STFSLM0");
				for (String s : arr) {
					System.out.println("after split: " + s);
					word = s;
				}
				num = Integer.parseInt(word);
				num = num + 1;
				newStaffId = "STFSLM0" + num;
				break;
			case "MANAGER":
				arr = str.split("STFMGR0");
				for (String s : arr) {
					System.out.println("after split: " + s);
					word = s;
				}
				num = Integer.parseInt(word);
				num = num + 1;
				newStaffId = "STFMGR0" + num;
				break;
			case "CLEANING":
				arr = str.split("STFCLN0");
				for (String s : arr) {
					System.out.println("after split: " + s);
					word = s;
				}
				num = Integer.parseInt(word);
				num = num + 1;
				newStaffId = "STFCLN0" + num;
				break;
			default:
				System.out.println("no case found on split");
			}
		}
		System.out.println("new id : " + newStaffId);

		Boolean flag;
		String query;
		if ("BILLER".equals(staffsection)) {
			flag = workerName.getText().isEmpty() || workerMobile.getText().isEmpty()
					|| workerAadhar.getText().isEmpty() || workerEmail.getText().isEmpty()
					|| workerAddress.getText().isEmpty() || userName.getText().isEmpty()
					|| password.getText().isEmpty();
			if (flag) {
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle(null);
				alert.setContentText("Fill all fields, check username,password entered");
				alert.initOwner((Stage) sectionCombobox.getScene().getWindow()); // making alert on top
				alert.showAndWait();
				salesSectionList.clear();// clearing list other wise when we click add button again old plus new value
											// to list it makes list bigger but working no problem
				return;
			}
			if (!password.getText().equals(confirmPassword.getText())) {
				AlertMaker.showErrorMessage("password doesnot match with confirm password", userName);
				salesSectionList.clear();
				return;
			}
			String userN = userName.getText();
			String passN = password.getText();
			String usercheck = "select count(*) from staffs where username='" + userN + "'";
			Boolean result = handler.billerUserNameAndPasswordCheck(usercheck);
			if (result) {
				System.out.println("username already exists");
				Alert alert2 = new Alert(Alert.AlertType.ERROR);
				alert2.setTitle(null);
				alert2.setContentText("Username alredy exists, Choose another username");
				alert2.initOwner((Stage) sectionCombobox.getScene().getWindow()); // making alert on top
				alert2.showAndWait();
				return;
			}
			String passwordSecure = DigestUtils.sha1Hex(password.getText());
			//////////////////////// ===================================
			newList.clear();// clear the list first other wise each and every time existing list it make
							// problem
			checkingSection(); // checking the config.txt file if there taking the value then retreive the data
								// to list
			onAddMoment(userN, passN, newStaffId, workerName.getText(), workerMobile.getText());// adding new username
																								// and password
			onSaveMoveMent(); // saving data ie , retriving data from list and save to file

			query = "insert into STAFFS(staffid,staffsection,staffname,staffmobile,staffaadhar,staffemail,staffaddress,staffstatus,username,password)"
					+ " values(" + "'" + newStaffId + "'," + "'" + staffsection + "'," + "'" + workerName.getText()
					+ "'," + "'" + workerMobile.getText() + "'," + "'" + workerAadhar.getText() + "'," + "'"
					+ workerEmail.getText() + "'," + "'" + workerAddress.getText() + "'," + true + "," + "'"
					+ userName.getText() + "'," + "'" + passwordSecure + "'" + ")";
		} else {
			flag = workerName.getText().isEmpty() || workerMobile.getText().isEmpty()
					|| workerAadhar.getText().isEmpty() || workerEmail.getText().isEmpty()
					|| workerAddress.getText().isEmpty();
			if (flag) {
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle(null);
				alert.setContentText("Fill all fields");
				alert.initOwner((Stage) sectionCombobox.getScene().getWindow()); // making alert on top
				alert.showAndWait();
				salesSectionList.clear();
				return;
			}
			query = "insert into STAFFS(staffid,staffsection,staffname,staffmobile,staffaadhar,staffemail,staffaddress) values("
					+ "'" + newStaffId + "'," + "'" + staffsection + "'," + "'" + workerName.getText() + "'," + "'"
					+ workerMobile.getText() + "'," + "'" + workerAadhar.getText() + "'," + "'" + workerEmail.getText()
					+ "'," + "'" + workerAddress.getText() + "'" + ")";
		}
		System.out.println("query  : " + query);
		Boolean result = handler.executeQuery(query);
		if (result) {
			System.out.println("Worker insertion success");
		} else {
			System.out.println("Failed worker insertion");
		}

		clearAction(null);

	}

	@FXML
	private void comboBoxSelectionAction(ActionEvent event) {
		if (sectionCombobox.getValue() == null || "".equals(sectionCombobox.getValue())) {
			System.out.println("no selection in combo box");
			addButton.setDisable(true);
		} else {
			if (sectionCombobox.getValue().equals("BILLER")) {
				userName.setDisable(false);
				password.setDisable(false);
				confirmPassword.setDisable(false);
				passpassword.setDisable(false);
				passConfirmPassword.setDisable(false);
				addButton.setDisable(false);

			} else {
				userName.setDisable(true);
				password.setDisable(true);
				confirmPassword.setDisable(true);
				passpassword.setDisable(true);
				passConfirmPassword.setDisable(true);
				addButton.setDisable(false);
			}
		}
	}

	@FXML
	private void nameFieldAction(ActionEvent event) {
		workerMobile.requestFocus();
	}

	@FXML
	private void mobileFieldAction(ActionEvent event) {
		workerAadhar.requestFocus();
	}

	@FXML
	private void aadharFieldAction(ActionEvent event) {
		workerEmail.requestFocus();
	}

	@FXML
	private void emailFieldAction(ActionEvent event) {
		workerAddress.requestFocus();
	}

	@FXML
	private void clearAction(ActionEvent event) {
		workerAadhar.clear();
		workerAddress.clear();
		workerEmail.clear();
		workerMobile.clear();
		workerName.clear();
		userName.clear();
		password.clear();
		confirmPassword.clear();
		passpassword.clear();
		passConfirmPassword.clear();
		sectionCombobox.setValue("");
		salesSectionList.clear();
		userName.setDisable(true);
		password.setDisable(true);
		confirmPassword.setDisable(true);
		passpassword.setDisable(true);
		passConfirmPassword.setDisable(true);
	}

	@FXML
	private void userNameFieldAction(ActionEvent event) {
		if (checkBoxChannger.isSelected()) {
			password.requestFocus();
		} else {
			passpassword.requestFocus();
		}
	}

	@FXML
	private void passwordFieldAction(ActionEvent event) {
		confirmPassword.requestFocus();
	}

	@FXML
	private void confirmationPasswordAction(ActionEvent event) {
		String pass = password.getText();
		String conPass = confirmPassword.getText();
		if (pass.equals(conPass)) {
			labelConfirm.setText("password  matched");
		} else {
			labelConfirm.setText("password doesnot match");
		}
	}

	private void reTypingPassword() {
		password.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				labelConfirm.setText(null);
				confirmPassword.clear();
			}
		});
		passConfirmPassword.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				labelConfirm.setText(null);
			}
		});
	}

	@FXML
	private void toggleVisiblePassword(ActionEvent event) {
		if (checkBoxChannger.isSelected()) {
			password.setVisible(true);
			confirmPassword.setVisible(true);
			passpassword.setVisible(false);
			passConfirmPassword.setVisible(false);
			checkBoxChannger.setText("hide password");
			return;
		}
		passpassword.setVisible(true);
		passConfirmPassword.setVisible(true);
		password.setVisible(false);
		confirmPassword.setVisible(false);
		checkBoxChannger.setText("show password");
	}

	@FXML
	private void passpasswordFieldAction(ActionEvent event) {
		passConfirmPassword.requestFocus();
	}

	private void checkingSection() {
		try {
			file = new FileReader(CONFIG_FILE);
			Boolean flag = file.ready();
			if (flag) {
				System.out.println("file exists");
				retrevingDataToList();

			} else {
				System.out.println("file not exists");
			}
		} catch (FileNotFoundException ex) {
			Logger.getLogger(AddWorkerController.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IOException ex) {
			Logger.getLogger(AddWorkerController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	private void retrevingDataToList() {
		Gson gson = new Gson();
		passSaver = gson.fromJson(file, PassWordSaver.class);
		List<PasswordSavingModel> modelList = passSaver.getPasswords().stream()
				.map(PasswordSavingModel.PassWor::toModel).collect(Collectors.toList());
		newList.addAll(modelList);
	}

	private void onAddMoment(String uname, String pword, String bilrid, String bilrname, String mb) {
		PasswordSavingModel passsec = new PasswordSavingModel();
		passsec.username.setValue(uname);
		passsec.password.setValue(pword);
		passsec.billerid.setValue(bilrid);
		passsec.billername.setValue(bilrname);
		passsec.mobile.setValue(mb);
		newList.add(passsec);
	}

	private void onSaveMoveMent() {
		List<PasswordSavingModel.PassWor> listElement = newList.stream().map(PasswordSavingModel::toData)
				.collect(Collectors.toList());
		Writer writer = null;
		try {
			Gson gson = new Gson();
			writer = new FileWriter(CONFIG_FILE);
			gson.toJson(new PassWordSaver(listElement), writer);
		} catch (IOException ex) {
			Logger.getLogger(AddWorkerController.class.getName()).log(Level.SEVERE, null, ex);
		} finally {
			try {
				writer.close();
			} catch (IOException ex) {
				Logger.getLogger(AddWorkerController.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}
}