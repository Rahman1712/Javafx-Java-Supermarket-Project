
package com.ar.login;

import com.ar.databasefilesettings.DatabaseFileSetController;
import com.ar.settings.RegisterController;
import com.ar.settings.RegisterDerbyDatabase;
import com.ar.util.SuperMarketUtils;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.commons.codec.digest.DigestUtils;

public class LoginController implements Initializable {
	RegisterDerbyDatabase regKeyHandler;

	@FXML
	private AnchorPane mainAnchor;
	@FXML
	private JFXButton loginButton;
	@FXML
	private JFXTextField userTextfield;
	@FXML
	private JFXTextField passTextfield;
	@FXML
	private JFXPasswordField passPassField;
	@FXML
	private JFXCheckBox checkBox;

	LoginSave logsav;
	@FXML
	private FontAwesomeIconView userIcon;
	@FXML
	private FontAwesomeIconView passIcon;
	@FXML
	private Circle circleOne;
	@FXML
	private Circle circleTwo;

	public static final String DB_FILE = "Dbsettings.txt"; 

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		userTextfield.requestFocus();
		try {
			regKeyHandler = RegisterDerbyDatabase.getInstance();
		} catch (ClassNotFoundException | SQLException ex) {
			Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
		}
		logsav = LoginSave.getLogerData();
		passTextfield.setVisible(false);
		passTextfield.textProperty().bindBidirectional(passPassField.textProperty());
		textFieldChangeClear();
	}

	@FXML
	private void loginButtonAction(ActionEvent event) {
		String uname = DigestUtils.sha1Hex(userTextfield.getText());
		String pass = DigestUtils.sha1Hex(passTextfield.getText());

		if (uname.equals(logsav.getUsername()) && pass.equals(logsav.getPassword())) {
			System.out.println("correct");
			userTextfield.setStyle("-jfx-unfocus-color:green; -jfx-focus-color:green; -fx-text-fill:green;");
			passTextfield.setStyle("-jfx-unfocus-color:green; -jfx-focus-color:green; -fx-text-fill:green;");
			passPassField.setStyle("-jfx-unfocus-color:green; -jfx-focus-color:green; -fx-text-fill:green;");
			circleOne.setStyle("-fx-fill:linear-gradient(green,white,green);");
			circleTwo.setStyle("-fx-fill:linear-gradient(green,white,green);");

			///////////////// ================file database data saved
			///////////////// checking(DbSettings.txt)======================///////////////////////
			try {
				Boolean fileResult = fileDBExistsChecking();
				if (!fileResult) {
					System.out.println("returning from the function");
					return;
				} else {
					System.out.println("LETS GOOOOOOOO.....");
				}
			} catch (IOException ex) {
				Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
			}

			///////////////// ======================================/////////////////////////////////////////////

			//////////////// ========ISFirstTme Check==================/////////////////
			Boolean isfirst = true;
			String firtTimeQuery = "select isfirsttime from REGISTERTABLE where registerid='SUPERMARKETID'";
			ResultSet firstSet = regKeyHandler.resultexecQuery(firtTimeQuery);
			try {
				while (firstSet.next()) {
					isfirst = firstSet.getBoolean("isfirsttime");
				}
			} catch (SQLException ex) {
				Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
			}
			if (isfirst) {
				try {
					FXMLLoader loaderS = new FXMLLoader(getClass().getResource("/com/ar/allsection/ForCompany.fxml"));
					Parent parentS = loaderS.load();
					Stage stageS = new Stage(StageStyle.DECORATED);
					stageS.setTitle("COMPANY SECTION");
					SuperMarketUtils.setStageIcon(stageS);
					stageS.setScene(new Scene(parentS));
					stageS.show();
					((Stage) mainAnchor.getScene().getWindow()).close();
				} catch (IOException ex) {
					Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
				}
				((Stage) loginButton.getScene().getWindow()).close();
				String queryToc = "update REGISTERTABLE set isfirsttime=false where registerid='SUPERMARKETID'";
				// setting isfirsttime=false after first open
				regKeyHandler.executeQuery(queryToc);
				return;
			}
			Boolean bool = false;
			Boolean fullBool = false;
			Boolean thirtyBool = false;
			Timestamp times = new Timestamp(System.currentTimeMillis());
			Timestamp timesset = new Timestamp(System.currentTimeMillis());
			String regQuery = "select * from REGISTERTABLE where registerid='SUPERMARKETID'";
			ResultSet regRs = regKeyHandler.resultexecQuery(regQuery);
			try {
				while (regRs.next()) {
					bool = regRs.getBoolean("registerstatus");
					fullBool = regRs.getBoolean("isfreemode");
					thirtyBool = regRs.getBoolean("isthirtydaysmode");
					times = regRs.getTimestamp("registertime");
					timesset = regRs.getTimestamp("registersettime");
				}
			} catch (SQLException ex) {
				Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
			}
			//////////// =======if aanenkil forcompany leku
			if (bool) {

				if (fullBool) {
					try {
						FXMLLoader loader = new FXMLLoader(
								getClass().getResource("/com/ar/allsection/ForCompany.fxml"));
						Parent parent = loader.load();
						Stage stage = new Stage(StageStyle.DECORATED);
						stage.setTitle("COMPANY SECTION");
						SuperMarketUtils.setStageIcon(stage);
						stage.setScene(new Scene(parent));
						stage.show();
						((Stage) mainAnchor.getScene().getWindow()).close();
					} catch (IOException ex) {
						Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
					}
				}
				if (thirtyBool) {
					Long timeFromSetting = System.currentTimeMillis() - timesset.getTime();
					Long daysFromSetting = TimeUnit.DAYS.convert(timeFromSetting, TimeUnit.MILLISECONDS);
					int daysOnPc = Integer.parseInt(daysFromSetting.toString());
					System.out.println("days on Pc :  " + daysOnPc);
					int daysBaki = 30 - daysOnPc;
					System.out.println("days baki  :  " + daysBaki);
					if (daysBaki < 8) {
						try {
							FXMLLoader loader = new FXMLLoader(
									getClass().getResource("/com/ar/settings/Register.fxml"));
							Parent parent = loader.load();
							Stage stage = new Stage(StageStyle.DECORATED);
							RegisterController controllerRegThirty = (RegisterController) loader.getController();
							controllerRegThirty.registerStatusTakingFromThirtyDaysMode(daysBaki);
							stage.setTitle("REGISTER SECTION");
							SuperMarketUtils.setStageIcon(stage);
							stage.initModality(Modality.APPLICATION_MODAL);
							stage.setScene(new Scene(parent));
							stage.showAndWait();
							if (controllerRegThirty.registerIntThirtyStatusSetterValueTaker() == 0) {
								((Stage) mainAnchor.getScene().getWindow()).close();
							} else {
								try {
									FXMLLoader loaderSales = new FXMLLoader(
											getClass().getResource("/com/ar/allsection/ForCompany.fxml"));
									Parent parent2 = loaderSales.load();
									Stage stage2 = new Stage(StageStyle.DECORATED);
									stage.setTitle("COMPANY SECTION");
									SuperMarketUtils.setStageIcon(stage2);
									stage2.setScene(new Scene(parent2));
									stage2.show();
									((Stage) mainAnchor.getScene().getWindow()).close();
								} catch (IOException ex) {
									Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
								}
							}
						} catch (IOException ex) {
							Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
						}
					}
					////////////////// days baki 8 nu mukalil aanenkil//////////////////////
					try {
						FXMLLoader loaderSales = new FXMLLoader(
								getClass().getResource("/com/ar/allsection/ForCompany.fxml"));
						Parent parent2 = loaderSales.load();
						Stage stage2 = new Stage(StageStyle.DECORATED);
						stage2.setTitle("COMPANY SECTION");
						SuperMarketUtils.setStageIcon(stage2);
						stage2.setScene(new Scene(parent2));
						stage2.show();
						((Stage) mainAnchor.getScene().getWindow()).close();
					} catch (IOException ex) {
						Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
					}

				}
			} else {
				try {
					FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ar/settings/Register.fxml"));
					Parent parent = loader.load();
					Stage stage = new Stage(StageStyle.DECORATED);
					RegisterController controllerReg = (RegisterController) loader.getController();
					controllerReg.registerStatusTaking(times, timesset);
					stage.setTitle("REGISTER SECTION");
					SuperMarketUtils.setStageIcon(stage);
					stage.initModality(Modality.APPLICATION_MODAL);
					stage.setScene(new Scene(parent));
					stage.showAndWait();
					//////////////////// ==return varumbol
					if (controllerReg.registerIntSetterValueTaker() == 1) {
						try {
							FXMLLoader loaderSales = new FXMLLoader(
									getClass().getResource("/com/ar/allsection/ForCompany.fxml"));
							Parent parent2 = loaderSales.load();
							Stage stage2 = new Stage(StageStyle.DECORATED);
							stage2.setTitle("COMPANY SECTION");
							SuperMarketUtils.setStageIcon(stage2);
							stage2.setScene(new Scene(parent2));
							stage2.show();
							((Stage) mainAnchor.getScene().getWindow()).close();
						} catch (IOException ex) {
							Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
						}
					} else {
						((Stage) loginButton.getScene().getWindow()).close();
					}

				} catch (IOException ex) {
					Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
		} else {
			System.out.println("incorrect");
			userTextfield.setStyle("-jfx-unfocus-color:red; -jfx-focus-color:red; -fx-text-fill:red;");
			passTextfield.setStyle("-jfx-unfocus-color:red; -jfx-focus-color:red; -fx-text-fill:red;");
			passPassField.setStyle("-jfx-unfocus-color:red; -jfx-focus-color:red; -fx-text-fill:red;");
			circleOne.setStyle("-fx-fill:linear-gradient(red,white,red);");
			circleTwo.setStyle("-fx-fill:linear-gradient(red,white,red);");
		}
	}

	@FXML
	private void cancelButtonAction(ActionEvent event) {
		((Stage) mainAnchor.getScene().getWindow()).close();
	}

	@FXML
	private void passwordFieldAction(ActionEvent event) {
		loginButtonAction(null);
		loginButton.requestFocus();
	}

	@FXML
	private void checkBoxAction(ActionEvent event) {
		if (checkBox.isSelected()) {
			passTextfield.setVisible(true);
			passPassField.setVisible(false);
			checkBox.setText("hide password");
		} else {
			passTextfield.setVisible(false);
			passPassField.setVisible(true);
			checkBox.setText("show password");

		}
	}

	@FXML
	private void userTextfieldAction(ActionEvent event) {
		if (checkBox.isSelected()) {
			passTextfield.requestFocus();
		} else {
			passPassField.requestFocus();
		}
	}

	private void textFieldChangeClear() {
		userTextfield.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				userTextfield
						.setStyle("-jfx-unfocus-color:royalblue; -jfx-focus-color:violet; -fx-text-fill:darkblue;");
				passTextfield
						.setStyle("-jfx-unfocus-color:royalblue; -jfx-focus-color:violet; -fx-text-fill:darkblue;");
				passPassField
						.setStyle("-jfx-unfocus-color:royalblue; -jfx-focus-color:violet; -fx-text-fill:darkblue;");
				passTextfield.setText("");
				circleOne.setStyle(
						"-fx-fill:linear-gradient(to bottom,#1fb4ffc6,#5be4ff 46.104725415070236%,#3da3cc 92.18443470132057%,#3da3cc 100.0%);");
				circleTwo.setStyle(
						"-fx-fill:linear-gradient(to bottom,#1fb4ffc6,#5be4ff 46.104725415070236%,#3da3cc 92.18443470132057%,#3da3cc 100.0%);");
			}
		});
		passTextfield.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				userTextfield
						.setStyle("-jfx-unfocus-color:royalblue; -jfx-focus-color:violet; -fx-text-fill:darkblue;");
				passTextfield
						.setStyle("-jfx-unfocus-color:royalblue; -jfx-focus-color:violet; -fx-text-fill:darkblue;");
				passPassField
						.setStyle("-jfx-unfocus-color:royalblue; -jfx-focus-color:violet; -fx-text-fill:darkblue;");
				circleOne.setStyle(
						"-fx-fill:linear-gradient(to bottom,#1fb4ffc6,#5be4ff 46.104725415070236%,#3da3cc 92.18443470132057%,#3da3cc 100.0%);");
				circleTwo.setStyle(
						"-fx-fill:linear-gradient(to bottom,#1fb4ffc6,#5be4ff 46.104725415070236%,#3da3cc 92.18443470132057%,#3da3cc 100.0%);");
			}
		});
	}

	private boolean fileDBExistsChecking() throws IOException {
		File file = new File(DB_FILE);
		if (file.exists()) {
			System.out.println("file exists");
			return true;
		} else {
			System.out.println("file not exist");
			FXMLLoader loader = new FXMLLoader(
					getClass().getResource("/com/ar/databasefilesettings/DatabaseFileSet.fxml"));
			Parent parent = loader.load();
			Stage stage = new Stage(StageStyle.DECORATED);
			stage.setAlwaysOnTop(true); // making window on top
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setTitle("Database Settings");
			SuperMarketUtils.setStageIcon(stage);
			DatabaseFileSetController controller = (DatabaseFileSetController) loader.getController();
			controller.fxmlOpenComingFromLoginSection("database", "root", "root");
			stage.setScene(new Scene(parent));
			stage.showAndWait();
			if (controller.dbSetSuccessOrNotReturn() == 1) {
				System.out.println("continue to program");
				return true;
			} else {
				System.out.println("return from function");
				return false;
			}
		}
	}

}
