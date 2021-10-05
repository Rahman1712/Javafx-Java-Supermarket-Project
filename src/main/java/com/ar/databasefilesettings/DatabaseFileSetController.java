
package com.ar.databasefilesettings;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class DatabaseFileSetController implements Initializable {

	@FXML
	private AnchorPane mainAnchorPane;
	@FXML
	private TextField textfieldUsername;
	@FXML
	private TextField textfieldPassword;
	@FXML
	private Button buttonTest;
	@FXML
	private Button buttonOk;
	@FXML
	private Button buttonCancel;
	@FXML
	private Label labelStatus;

	boolean isDbSetOrNot = false;

	private static final String MYSQL_URL = "com.mysql.jdbc.Driver";
	private static Connection conn = null;
	@FXML
	private TextField textfieldDatabaseName;
	@FXML
	private Label lbh;
	@FXML
	private Label labelTohide;

	DbSuccessOrFail dbSOrF = new DbSuccessOrFail(0);

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		labelStatus.setVisible(false);
		labelTohide.setVisible(false);
		DbSetFile.getLogerData();
		textfieldEditLabelNoVisible();
	}

	void createConnection() throws ClassNotFoundException, SQLException {
		String dbName = textfieldDatabaseName.getText();
		String username = textfieldUsername.getText();
		String password = textfieldPassword.getText();

		try {
			Class.forName(MYSQL_URL);

		} catch (ClassNotFoundException ex) {
			System.out.println("no driver");
			ex.printStackTrace();
			throw ex;
		}
		System.out.println("jdbc driver registered");
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost/" + dbName, username, password);
			System.out.println("success");
			labelStatus.setVisible(true);
			labelTohide.setVisible(false);
			labelStatus.setText("Connection Success...");
			labelStatus.setStyle("-fx-background-color:green;");
		} catch (SQLException ex) {
			System.out.println("failed connection");
			labelStatus.setVisible(true);
			labelTohide.setVisible(false);
			labelStatus.setText("Connection Failed...");
			labelStatus.setStyle("-fx-background-color:red;");
			ex.printStackTrace();
			throw ex;
		}
	}

	@FXML
	private void textfieldDatabaseNameAction(ActionEvent event) {
		textfieldUsername.requestFocus();
	}

	@FXML
	private void textfieldUsernameAction(ActionEvent event) {
		textfieldPassword.requestFocus();
	}

	@FXML
	private void textfieldPasswordAction(ActionEvent event) {
		buttonOk.requestFocus();
	}

	private void textfieldEditLabelNoVisible() {
		textfieldDatabaseName.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				labelStatus.setVisible(false);
			}
		});
		textfieldUsername.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				labelStatus.setVisible(false);
			}
		});
		textfieldPassword.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				labelStatus.setVisible(false);
			}
		});
	}

	@FXML
	private void buttonCancelAction(ActionEvent event) {
		((Stage) mainAnchorPane.getScene().getWindow()).close();
	}

	@FXML
	private void buttonTestAction(ActionEvent event) throws ClassNotFoundException, SQLException {
		createConnection();

	}

	@FXML
	private void buttonOkAction(ActionEvent event) throws ClassNotFoundException, SQLException {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Database settings");
		alert.setHeaderText(null);
		alert.setContentText("setting Database name ,Mysql username & Mysql password");
		alert.initOwner((Stage) mainAnchorPane.getScene().getWindow());// to top
		Optional<ButtonType> response = alert.showAndWait();
		if (ButtonType.OK == response.get()) {
			String dbName = textfieldDatabaseName.getText();
			String username = textfieldUsername.getText();
			String password = textfieldPassword.getText();

			try {
				Class.forName(MYSQL_URL);

			} catch (ClassNotFoundException ex) {
				System.out.println("no driver");
				ex.printStackTrace();
				throw ex;
			}
			System.out.println("jdbc driver registered");
			try {
				conn = DriverManager.getConnection("jdbc:mysql://localhost/" + dbName, username, password);
				System.out.println("success");
				labelStatus.setVisible(true);
				labelTohide.setVisible(false);
				labelStatus.setText("Connection Success...");
				labelStatus.setStyle("-fx-background-color:green;");

				dbSOrF.setDbStatus(1);

				DbSetFile dbset = DbSetFile.getLogerData();
				dbset.setDbDatabase(dbName);
				dbset.setDbUserName(username);
				dbset.setDbPassword(password);
				DbSetFile.writeNewLoginUserAndPassword(dbset);

			} catch (SQLException ex) {
				Alert alert2 = new Alert(Alert.AlertType.ERROR);
				alert2.setTitle("Not Updated");
				alert2.setContentText("Error in given values...");
				alert2.initOwner((Stage) mainAnchorPane.getScene().getWindow()); // making alert on top
				alert2.showAndWait();
				System.out.println("failed connection");
				labelStatus.setVisible(true);
				labelTohide.setVisible(false);
				labelStatus.setText("Connection Failed...");
				labelStatus.setStyle("-fx-background-color:red;");

				dbSOrF.setDbStatus(0);

				ex.printStackTrace();
				throw ex;
			}
		}
	}

	public void fxmlOpenComingFromDatabseSection(String dbnam, String unam, String pass) {
		labelTohide.setVisible(true);
		textfieldDatabaseName.setText(dbnam);
		textfieldUsername.setText(unam);
		textfieldPassword.setText(pass);
		buttonOk.setText("Update");

	}

	public void fxmlOpenComingFromLoginSection(String dbnam, String unam, String pass) {
		textfieldDatabaseName.setText(dbnam);
		textfieldUsername.setText(unam);
		textfieldPassword.setText(pass);
	}

	public int dbSetSuccessOrNotReturn() {
		int i = dbSOrF.getDbStatus();
		return i;
	}
}
