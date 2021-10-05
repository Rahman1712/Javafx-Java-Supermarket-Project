
package com.ar.maker;

import com.ar.alertmaker.AlertMaker;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.codec.digest.DigestUtils;

public class SecurePasswordMakerController implements Initializable {

	@FXML
	private AnchorPane mainAnchor;
	@FXML
	private TextField textFieldPass;
	@FXML
	private TextArea textAreaReg;
	@FXML
	private JFXButton createButton;
	@FXML
	private JFXButton saveButton;

	String text;
	String textToSec;
	@FXML
	private JFXCheckBox checkBox;
	@FXML
	private TextField pathTextField;
	@FXML
	private JFXButton pathButton;

	FileChooser fc = new FileChooser();
	File selectedFile;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		textAreaReg.setEditable(false);
		checkBox.setVisible(false);
		pathButton.setVisible(false);
		pathTextField.setVisible(false);
	}

	@FXML
	private void textfieldPassAction(ActionEvent event) {
		createButtonAction(null);
		createButton.requestFocus();
	}

	@FXML
	private void createButtonAction(ActionEvent event) {
		text = textFieldPass.getText();
		textToSec = DigestUtils.sha1Hex(text);
		textAreaReg.setText("secure password for password : " + text + "\n \n");
		textAreaReg.setText(textAreaReg.getText() + "{\"keyActivation\":\"" + textToSec + "\"}");
	}

	@FXML
	private void cancelButtonAction(ActionEvent event) {
		((Stage) mainAnchor.getScene().getWindow()).close();
	}

	@FXML
	private void saveButtonAction(ActionEvent event) {
		try {
			try (FileWriter newFile = new FileWriter(text + ".ipl")) {
				newFile.write("{\"keyActivation\":\"" + textToSec + "\"}");
				AlertMaker.showSimpleAlertWithNode("File Creation", "file '" + text + ".ipl' created succussfully",
						createButton);
			}

		} catch (IOException ex) {
			System.out.println("Error occured");
			AlertMaker.showErrorMessage("File creation failed", createButton);
			Logger.getLogger(SecurePasswordMakerController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@FXML
	private void pathButtonAction(ActionEvent event) {
		fc.setTitle("Choose File");
		Stage stage = (Stage) mainAnchor.getScene().getWindow();
		selectedFile = fc.showOpenDialog(stage);

	}

}
