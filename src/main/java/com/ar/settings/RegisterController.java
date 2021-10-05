
package com.ar.settings;

import com.ar.alertmaker.AlertMaker;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import java.io.File;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.apache.commons.codec.digest.DigestUtils;
import org.controlsfx.control.Notifications;

public class RegisterController implements Initializable {

	RegisterDerbyDatabase dbhandler;
	RegisterSetter rgs = new RegisterSetter(0);
	RegisterSetterForThirtyDays rgsThirty = new RegisterSetterForThirtyDays(1);

	@FXML
	private AnchorPane mainAnchor;
	@FXML
	private TextArea textAreaContent;
	@FXML
	private AnchorPane registerModeAnchor;
	@FXML
	private JFXTextField regiKeyTFOne;
	@FXML
	private JFXTextField regiKeyTFTwo;
	@FXML
	private JFXTextField regiKeyTFThree;
	@FXML
	private JFXTextField regiKeyTFFour;
	@FXML
	private AnchorPane fileModeAnchor;
	@FXML
	private JFXTextField fileChooseTextField;
	@FXML
	private JFXButton fileChooseButton;
	@FXML
	private JFXRadioButton radioFileMode;
	@FXML
	private JFXRadioButton radioRegisterMode;
	@FXML
	private Label labelGetKey;
	@FXML
	private JFXButton registerButton;
	@FXML
	private Label labelstatusRegister;
	@FXML
	private JFXButton continueButton;
	@FXML
	private Label labellValidInvalid;
	@FXML
	private JFXButton okButton;
	@FXML
	private JFXButton cancelButton;
	@FXML
	private Label labelFileInputPassStatus;
	@FXML
	private Label labelRegisterTime;

	FileChooser fc = new FileChooser();
	File selectedFile;
	JFXTextField tf = new JFXTextField();

	Timestamp todayWithTime = new Timestamp(System.currentTimeMillis());

	List<LockerModel> lockList = new ArrayList<>();

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		try {
			dbhandler = RegisterDerbyDatabase.getInstance();
		} catch (ClassNotFoundException | SQLException ex) {
			Logger.getLogger(RegisterController.class.getName()).log(Level.SEVERE, null, ex);
		}
		Preferences.initConfig();
		fileModeAnchor.setDisable(true);
		registerModeAnchor.setDisable(true);
		ToggleGroup tg = new ToggleGroup();
		radioFileMode.setToggleGroup(tg);
		radioRegisterMode.setToggleGroup(tg);
		TextfieldChangeClearLabel();
		okButton.setVisible(false);
		textAreaContent.setEditable(false);
		fileChooseTextField.setEditable(false);

		textfieldMaxdigitMode(regiKeyTFOne, 4);
		textfieldMaxdigitMode(regiKeyTFTwo, 4);
		textfieldMaxdigitMode(regiKeyTFThree, 4);
		textfieldMaxdigitMode(regiKeyTFFour, 4);

		continueButton.setVisible(false);

		try {
			initialFetchingDataFromDB();
		} catch (SQLException ex) {
			Logger.getLogger(RegisterController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@FXML
	private void regiKeyTFOneAction(ActionEvent event) {
		regiKeyTFTwo.requestFocus();
	}

	@FXML
	private void regiKeyTFTwoAction(ActionEvent event) {
		regiKeyTFThree.requestFocus();
	}

	@FXML
	private void regiKeyTFThreeAction(ActionEvent event) {
		regiKeyTFFour.requestFocus();
	}

	@FXML
	private void regiKeyTFFourAction(ActionEvent event) {
		registerButton.requestFocus();
	}

	@FXML
	private void fileChooseButtonAction(ActionEvent event) {
		labelFileInputPassStatus.setText("");
		fileChooseTextField.setText("");
		fc.setTitle("Choose File");
		Stage stage = (Stage) mainAnchor.getScene().getWindow();
		selectedFile = fc.showOpenDialog(stage);
		fc.getExtensionFilters().addAll(new ExtensionFilter("Registry Key Files", "*.rgk"));
		if (selectedFile != null) {
			fileChooseTextField.setText(selectedFile.getAbsolutePath());
		}
	}

	@FXML
	private void radioFileModeAction(ActionEvent event) {
		fileModeAnchor.setDisable(false);
		registerModeAnchor.setDisable(true);
	}

	@FXML
	private void radioRegisterModeAction(ActionEvent event) {
		registerModeAnchor.setDisable(false);
		fileModeAnchor.setDisable(true);
	}

	@FXML
	private void cancelButtonAction(ActionEvent event) {
		rgs.setRegIntStatus(0);
		((Stage) mainAnchor.getScene().getWindow()).close();
	}

	@FXML
	private void continueButtonAction(ActionEvent event) throws SQLException {
		rgs.setRegIntStatus(1);
		((Stage) mainAnchor.getScene().getWindow()).close();
	}

	public void registerStatusTaking(Timestamp timeStmp, Timestamp timesetStmp) {
		continueButton.setVisible(true);
		Timestamp installTime = timeStmp;
		Long timeFromInstall = System.currentTimeMillis() - installTime.getTime();
		Long daysFromInstall = TimeUnit.DAYS.convert(timeFromInstall, TimeUnit.MILLISECONDS);
		int daysOnPc = Integer.parseInt(daysFromInstall.toString());
		System.out.println("days on Pc :  " + daysOnPc);
		int daysBaki = 5 - daysOnPc;

		System.out.println("days baki  :  " + daysBaki);
		labelstatusRegister.setText(daysBaki + " days left to expire !.");
		labelstatusRegister.setStyle("-fx-text-fill:red;");
		labelRegisterTime.setText("");////// registerTime label "" aaki vekkunnu
		if (daysBaki < 0) {
			continueButton.setVisible(false);
			labelstatusRegister.setText("Expired ! register software");
		}
	}

	public void registerStatusTakingFromThirtyDaysMode(int daysbaki) {
		continueButton.setVisible(true);
		labelstatusRegister.setText(daysbaki + " days left to expire !.");
		labelstatusRegister.setStyle("-fx-text-fill:red;");
		if (daysbaki < 0) {
			continueButton.setVisible(false);
			labelstatusRegister.setText("Expired ! register software");
			rgsThirty.setRegIntThirtyStatus(0);
		}
	}

	public int registerIntSetterValueTaker() {
		return (rgs.getRegIntStatus());
	}

	public int registerIntThirtyStatusSetterValueTaker() {
		return (rgsThirty.getRegIntThirtyStatus());
	}

	@FXML
	private void registerButtonAction(ActionEvent event) {
		//======================Register by importing file================================
		if (radioFileMode.isSelected()) {
			System.out.println("radio file");

			String queryCheckAlreadyFreeReg = "select * from REGISTERTABLE";
			ResultSet rsCk = dbhandler.resultexecQuery(queryCheckAlreadyFreeReg);
			Boolean boolCkStatus = false;
			try {
				while (rsCk.next()) {
					System.out.println("isfreemode : " + rsCk.getBoolean("isfreemode"));
					boolCkStatus = rsCk.getBoolean("isfreemode");
				}
			} catch (SQLException ex) {
				Logger.getLogger(RegisterController.class.getName()).log(Level.SEVERE, null, ex);
			}
			if (boolCkStatus) {
				System.out.println("Already registered free version");
				AlertMaker.showSimpleAlertWithNode("REGISTRATION", "Software already registered", okButton);
				return;
			}

			if (selectedFile == null) {
				System.out.println("no file");
			} else {
				System.out.println("file name : " + selectedFile.getName());
				Preferences outpref = Preferences.getPreferencesFromFileChooser(selectedFile.getAbsolutePath());
				String takingActivation = outpref.getKeyActivation();
				Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
				alert.initOwner((Stage) mainAnchor.getScene().getWindow());
				AnchorPane anchor = new AnchorPane();
				anchor.setPrefSize(300.0, 100.0);
				anchor.getChildren().add(tf);
				tf.setLabelFloat(true);
				tf.setPromptText("enter password");
				AnchorPane.setTopAnchor(tf, 20.0);
				AnchorPane.setLeftAnchor(tf, 20.0);
				AnchorPane.setRightAnchor(tf, 20.0);
				alert.getDialogPane().setContent(anchor);
				Optional<ButtonType> response = alert.showAndWait();
				if (ButtonType.OK == response.get()) {
					if (tf.getText().isEmpty() || "".equals(tf.getText())) {
						System.out.println("no password type");
						return;
					}
					System.out.println("tf value : " + tf.getText());
					String passType = tf.getText();
					String toShax = DigestUtils.sha1Hex(passType);
					if (toShax.equals(takingActivation)) {
						System.out.println("same");
						labelFileInputPassStatus.setText("correct password");
						labelFileInputPassStatus.setStyle("-fx-text-fill:green;");
						String queryUpdate = "update REGISTERTABLE set registerstatus=true , registersettime='"
								+ todayWithTime
								+ "',isthirtydaysmode=false,isfreemode=true where registerid='SUPERMARKETID'";// setting
																												// registerstaus=true
						Boolean flagResult = dbhandler.executeUpdateQuery(queryUpdate);
						if (flagResult) {
							Notifications notificationBuilderResendError = Notifications.create().title("REGISTRATION")
									.text("Software Registered Successfully").graphic(null)
									.hideAfter(Duration.seconds(3)).position(Pos.TOP_CENTER)
									.onAction(new EventHandler<ActionEvent>() {
										@Override
										public void handle(ActionEvent event) {
											System.out.println("Clicked quantity updated notification");
										}
									});
							notificationBuilderResendError.darkStyle();
							notificationBuilderResendError.showInformation();
							rgs.setRegIntStatus(1);
							labelRegisterTime.setText("Registered at :" + todayWithTime);
							textAreaContent.setText("Software registered , now you can use freely...");
							labelstatusRegister.setText("SuperMarket Software Registered");
							labelstatusRegister
									.setStyle("-fx-text-fill:green; -fx-font-weight:bold; -fx-underline:true;");
							okButton.setVisible(true);
							cancelButton.setVisible(false);
							continueButton.setVisible(false);
						} else {
							AlertMaker.showErrorControFxDialogMessage("REGISTRATION", "Registration Failed",
									Pos.CENTER);
						}
					} else {
						System.out.println("not same");
						labelFileInputPassStatus.setText("incorrect password");
						labelFileInputPassStatus.setStyle("-fx-text-fill:#ac0707;");
					}
				}
			}
		}
		//======================Register by Code Typing================================
		if (radioRegisterMode.isSelected()) {
			System.out.println("radio register");

			String queryCheckAlreadyFreeReg = "select * from REGISTERTABLE";
			ResultSet rsCk = dbhandler.resultexecQuery(queryCheckAlreadyFreeReg);
			Boolean boolCkStatus = false;
			try {
				while (rsCk.next()) {
					System.out.println("isfreemode : " + rsCk.getBoolean("isfreemode"));
					boolCkStatus = rsCk.getBoolean("isfreemode");
				}
			} catch (SQLException ex) {
				Logger.getLogger(RegisterController.class.getName()).log(Level.SEVERE, null, ex);
			}
			if (boolCkStatus) {
				System.out.println("Already registered free version");
				AlertMaker.showSimpleAlertWithNode("REGISTRATION", "Software already registered", okButton);
				return;
			}

			String kc1 = DigestUtils.sha1Hex("30DAYSMODE");
			String kc2 = DigestUtils.sha1Hex("FREEMODE");
			String joinString = regiKeyTFOne.getText() + regiKeyTFTwo.getText() + regiKeyTFThree.getText()
					+ regiKeyTFFour.getText();
			String shaxModOfJoin = DigestUtils.sha1Hex(joinString);
			LockerModel.initListAKConfig();
			lockList = LockerModel.retrevingDataToList();
			System.out.println("join string : " + joinString);
			String nameActivationKey = "";/////////////////////
			String nameKeyCode = "";////////////////////
			for (LockerModel lk : lockList) {
				if (lk.toData().getKeyActivation().equals(shaxModOfJoin)) {
					System.out.println("Joined " + joinString + "(" + shaxModOfJoin + ") nu same aayitu ulladhu "
							+ lk.toData().getKeyActivation());
					nameActivationKey = lk.toData().getKeyActivation();
					nameKeyCode = lk.toData().getKeyCode();
					break;
				}
			}
			if (!nameActivationKey.equals(shaxModOfJoin)) {
				System.out.println("not same");
				labellValidInvalid.setText("invalid key");
				labellValidInvalid.setStyle("-fx-text-fill:#c51b1b;");
				return;
			}

			if (nameKeyCode.equals(kc1)) {
				labellValidInvalid.setText("valid key");
				labellValidInvalid.setStyle("-fx-text-fill:green;");
				Boolean isfirst = true;
				String firtTimeQuery = "select isthirtydaysmodefirsttime from REGISTERTABLE where registerid='SUPERMARKETID'";
				ResultSet firstSet = dbhandler.resultexecQuery(firtTimeQuery);
				try {
					while (firstSet.next()) {
						isfirst = firstSet.getBoolean("isthirtydaysmodefirsttime");
					}
				} catch (SQLException ex) {
					Logger.getLogger(RegisterController.class.getName()).log(Level.SEVERE, null, ex);
				}
				if (isfirst) {
					System.out.println("first time for 30 days mode");
					String queryUpdate = "update REGISTERTABLE set registerstatus=true , registersettime='"
							+ todayWithTime
							+ "',isthirtydaysmode=true,isthirtydaysmodefirsttime=false where registerid='SUPERMARKETID'";// setting
																															// registerstaus=true
					Boolean flagResult = dbhandler.executeUpdateQuery(queryUpdate);
					if (flagResult) {
						Notifications notificationBuilderResendError = Notifications.create().title("REGISTRATION")
								.text("Software Registered Successfully").graphic(null).hideAfter(Duration.seconds(3))
								.position(Pos.TOP_CENTER).onAction(new EventHandler<ActionEvent>() {
									@Override
									public void handle(ActionEvent event) {
										System.out.println("Clicked quantity updated notification");
									}
								});
						notificationBuilderResendError.darkStyle();
						notificationBuilderResendError.showInformation();
						rgs.setRegIntStatus(1);
						labelRegisterTime.setText("Registered at :" + todayWithTime);
						textAreaContent
								.setText("Software registered for 30 days ,\n now you can use 30 days freely...");
						labelstatusRegister.setText("SuperMarket Software Registered");
						labelstatusRegister.setStyle("-fx-text-fill:green; -fx-font-weight:bold; -fx-underline:true;");
						okButton.setVisible(true);
						cancelButton.setVisible(false);
						continueButton.setVisible(false);
					}
				} else {
					AlertMaker.showErrorMessage("used this register key before \n cannot use again this register key.",
							okButton);
				}

			} else if (nameKeyCode.equals(kc2)) {
				labellValidInvalid.setText("valid key");
				labellValidInvalid.setStyle("-fx-text-fill:green;");
				String queryUpdate = "update REGISTERTABLE set registerstatus=true , registersettime='" + todayWithTime
						+ "',isthirtydaysmode=false,isfreemode=true where registerid='SUPERMARKETID'";// setting
																										// registerstaus=true
				Boolean flagResult = dbhandler.executeUpdateQuery(queryUpdate);
				if (flagResult) {
					Notifications notificationBuilderResendError = Notifications.create().title("REGISTRATION")
							.text("Software Registered Successfully").graphic(null).hideAfter(Duration.seconds(3))
							.position(Pos.TOP_CENTER).onAction(new EventHandler<ActionEvent>() {
								@Override
								public void handle(ActionEvent event) {
									System.out.println("Clicked quantity updated notification");
								}
							});
					notificationBuilderResendError.darkStyle();
					notificationBuilderResendError.showInformation();
					rgs.setRegIntStatus(1);
					labelRegisterTime.setText("Registered at :" + todayWithTime);
					textAreaContent.setText("Software registered , now you can use freely...");
					labelstatusRegister.setText("SuperMarket Software Registered");
					labelstatusRegister.setStyle("-fx-text-fill:green; -fx-font-weight:bold; -fx-underline:true;");
					okButton.setVisible(true);
					cancelButton.setVisible(false);
					continueButton.setVisible(false);
				}
			} else {
				System.out.println("key not join.Error in inside of 'code' oCCURED");
			}

		}
	}

	private void TextfieldChangeClearLabel() {
		regiKeyTFOne.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				labellValidInvalid.setText("");
			}
		});
		regiKeyTFTwo.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				labellValidInvalid.setText("");
			}
		});
		regiKeyTFThree.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				labellValidInvalid.setText("");
			}
		});
		regiKeyTFFour.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				labellValidInvalid.setText("");
			}
		});
	}

	@FXML
	private void okButtonAction(ActionEvent event) {
		((Stage) mainAnchor.getScene().getWindow()).close();
	}

	private void textfieldMaxdigitMode(final JFXTextField tf, final int maxLength) {
		tf.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (tf.getText().length() > maxLength) {
					String s = tf.getText().substring(0, maxLength);
					tf.setText(s);
				}
			}
		});
	}

	private void initialFetchingDataFromDB() throws SQLException {
		Boolean boolStatus = false;
		Boolean fullBoolStatus = false;
		Boolean thirtyBoolStatus = false;
		Timestamp installTime = new Timestamp(System.currentTimeMillis());
		Timestamp registerTime = new Timestamp(System.currentTimeMillis());
		String query = "select * from REGISTERTABLE";
		ResultSet rs = dbhandler.resultexecQuery(query);
		while (rs.next()) {
			System.out.println("id : " + rs.getString("registerid"));
			System.out.println("status : " + rs.getBoolean("registerstatus"));
			System.out.println("installTime : " + rs.getTimestamp("registertime"));
			System.out.println("isFirsttime : " + rs.getBoolean("isfirsttime"));
			System.out.println("isthirtydaysmode : " + rs.getBoolean("isthirtydaysmode"));
			System.out.println("RegTime : " + rs.getTimestamp("registersettime"));
			boolStatus = rs.getBoolean("registerstatus");
			fullBoolStatus = rs.getBoolean("isfreemode");
			thirtyBoolStatus = rs.getBoolean("isthirtydaysmode");
			installTime = rs.getTimestamp("registertime");
			registerTime = rs.getTimestamp("registersettime");
		}
		if (boolStatus) {
			if (fullBoolStatus) {
				labelRegisterTime.setText("Registered at :" + registerTime);
				textAreaContent.setText("Software registered , now you can use freely...");
				labelstatusRegister.setText("SuperMarket Software Registered");
				labelstatusRegister.setStyle("-fx-text-fill:green; -fx-font-weight:bold; -fx-underline:true;");
			}
			if (thirtyBoolStatus) {
				Long timeFromSetting = System.currentTimeMillis() - registerTime.getTime();
				Long daysFromSetting = TimeUnit.DAYS.convert(timeFromSetting, TimeUnit.MILLISECONDS);
				int daysOnPc = Integer.parseInt(daysFromSetting.toString());
				System.out.println("days on Pc :  " + daysOnPc);
				int daysBaki = 30 - daysOnPc;
				System.out.println("days baki  :  " + daysBaki);
				labelstatusRegister.setText(daysBaki + " days left to expire !!.");
				labelstatusRegister.setStyle("-fx-text-fill:orange;");
				labelRegisterTime.setText("Registered at :" + registerTime);
				textAreaContent.setText(
						"Software registered for 30 days ,\n  you can use " + daysBaki + " days only for free...");

				if (daysBaki < 8) {
					labelstatusRegister.setText(daysBaki + " days left to expire !!.");
					labelstatusRegister.setStyle("-fx-text-fill:red;");
				}

				if (daysBaki < 0) {
					continueButton.setVisible(false);
					labelstatusRegister.setText("Expired ! register software");
					labelstatusRegister.setStyle("-fx-text-fill:red;");
					textAreaContent
							.setText("Software registered for 30 days ,\n  your registration period is expired..");
				}
			}
		} else {
			Long timeFromInstall = System.currentTimeMillis() - installTime.getTime();
			Long daysFromInstall = TimeUnit.DAYS.convert(timeFromInstall, TimeUnit.MILLISECONDS);
			int daysOnPc = Integer.parseInt(daysFromInstall.toString());
			System.out.println("days on Pc :  " + daysOnPc);
			int daysBaki = 5 - daysOnPc;

			System.out.println("days baki  :  " + daysBaki);
			labelstatusRegister.setText(daysBaki + " days left to expire !!.");
			labelstatusRegister.setStyle("-fx-text-fill:red;");
			labelRegisterTime.setText("");////// registerTime label "" aaki vekkunnu
			if (daysBaki < 0) {
				continueButton.setVisible(false);
				labelstatusRegister.setText("Expired ! register software");
			}
		}
	}
}
