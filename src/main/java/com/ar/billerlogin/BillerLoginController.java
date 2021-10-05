
package com.ar.billerlogin;

import com.ar.database.BillerDatabaseSection;
import com.ar.database.DatabaseSectionMain;
import com.ar.databasefilesettings.DatabaseFileSetController;
import com.ar.salesbillprint.SalesPrintController;
import com.ar.settings.RegisterController;
import com.ar.settings.RegisterDerbyDatabase;
import com.ar.util.SuperMarketUtils;
import java.io.FileNotFoundException;
import java.io.FileReader;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.commons.codec.digest.DigestUtils;

public class BillerLoginController implements Initializable {
	DatabaseSectionMain handler;
	BillerDatabaseSection derbyhandler;
	RegisterDerbyDatabase regKeyHandler;
	String namex;

	@FXML
	private TextField userName;
	@FXML
	private TextField passWord;
	@FXML
	private Label labelStatus;
	@FXML
	private PasswordField passPassWord;
	@FXML
	private CheckBox checkBoxPass;
	@FXML
	private Button loginButton;

	public static final String DB_FILE = "Dbsettings.txt";

	@Override
	public void initialize(URL url, ResourceBundle rb) {

		///////////////// ================file database data saved
		///////////////// checking(DbSettings.txt)======================///////////////////////
		try {
			Boolean fileResult = fileDBExistsChecking();
			if (!fileResult) {
				System.out.println("closing the application");
				((Stage) loginButton.getScene().getWindow()).close();
			} else {
				System.out.println("LETS GOOOOOOOO.....");
			}
		} catch (IOException ex) {
			Logger.getLogger(BillerLoginController.class.getName()).log(Level.SEVERE, null, ex);
		}

		///////////////// ======================================/////////////////////////////////////////////
		try {
			derbyhandler = BillerDatabaseSection.getInstance();
			handler = DatabaseSectionMain.getInstance();
			regKeyHandler = RegisterDerbyDatabase.getInstance();
		} catch (ClassNotFoundException | SQLException ex) {
			Logger.getLogger(BillerLoginController.class.getName()).log(Level.SEVERE, null, ex);
		}
		passWord.textProperty().bindBidirectional(passPassWord.textProperty());
		reTypingPassword();
	}

	@FXML
	private void usernameAction(ActionEvent event) {
		if (checkBoxPass.isSelected()) {
			passWord.requestFocus();
		} else {
			passPassWord.requestFocus();
		}
	}

	@FXML
	private void passwordAction(ActionEvent event) {
		loginButton.requestFocus();
		loginAction(null);
	}

	@FXML
	private void cancelAction(ActionEvent event) {
		((Stage) userName.getScene().getWindow()).close();
	}

	@FXML
	private void checkBoxAction(ActionEvent event) {
		if (checkBoxPass.isSelected()) {
			checkBoxPass.setText("hide password");
			passPassWord.setVisible(false);
			passWord.setVisible(true);
		} else {
			checkBoxPass.setText("show password");
			passPassWord.setVisible(true);
			passWord.setVisible(false);
		}
	}

	private void reTypingPassword() {
		userName.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				labelStatus.setText(null);
				passWord.clear();
			}
		});
		passWord.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				labelStatus.setText(null);

			}
		});
	}

	@FXML
	private void loginAction(ActionEvent event) {
		Boolean flag = userName.getText().isEmpty() || passWord.getText().isEmpty();
		if (flag) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle(null);
			alert.setContentText("Enter username and password");
			alert.initOwner((Stage) userName.getScene().getWindow()); // making alert on top
			alert.showAndWait();
			return;
		}

		String user = userName.getText();
		String pass = passWord.getText();
		String passChangetoSecureVer = DigestUtils.sha1Hex(pass);
		String query = "select count(*) from staffs where username='" + user + "' and password='"
				+ passChangetoSecureVer + "' and staffsection='BILLER'";
		Boolean result = handler.billerUserNameAndPasswordCheck(query);
		if (result) {
			System.out.println("username and password correct");
			labelStatus.setText("success");
			String takeBillerName = "select staffname from staffs where username='" + user + "' and password='"
					+ passChangetoSecureVer + "'";
			ResultSet rs = handler.resultexecQuery(takeBillerName);
			try {
				while (rs.next()) {
					namex = rs.getString("staffname");
					System.out.println("Staff name(biller name) :" + namex);
				}
			} catch (SQLException ex) {
				Logger.getLogger(BillerLoginController.class.getName()).log(Level.SEVERE, null, ex);
			}
			String billerQuery = "update BILLERNAME set billerusername='" + namex + "' where bno=1";
			if (derbyhandler.executeQuery(billerQuery)) {
				System.out.println("billerusername updated");
			} else {
				System.out.println("failed billeruser name updated");
			}
			//////////////// ========ISFirstTme Check==================/////////////////
			Boolean isfirst = true;
			String firtTimeQuery = "select isfirsttime from REGISTERTABLE where registerid='SUPERMARKETID'";
			ResultSet firstSet = regKeyHandler.resultexecQuery(firtTimeQuery);
			try {
				while (firstSet.next()) {
					isfirst = firstSet.getBoolean("isfirsttime");
				}
			} catch (SQLException ex) {
				Logger.getLogger(BillerLoginController.class.getName()).log(Level.SEVERE, null, ex);
			}
			if (isfirst) {
				try {
					FXMLLoader loaderS = new FXMLLoader(
							getClass().getResource("/com/ar/salesbillprint/SalesPrint.fxml"));
					Parent parentS = loaderS.load();
					Stage stageS = new Stage(StageStyle.DECORATED);
					SalesPrintController controller = (SalesPrintController) loaderS.getController();
					controller.billerNameTaking(user,namex);
					stageS.setTitle("BILLING SECTION");
					SuperMarketUtils.setStageIcon(stageS);
					stageS.setScene(new Scene(parentS));
					stageS.show();
					((Stage) userName.getScene().getWindow()).close();
				} catch (IOException ex) {
					Logger.getLogger(BillerLoginController.class.getName()).log(Level.SEVERE, null, ex);
				}
				((Stage) loginButton.getScene().getWindow()).close();
				String queryToc = "update REGISTERTABLE set isfirsttime=false where registerid='SUPERMARKETID'";// setting
																												// isfirsttime=false
																												// after
																												// first
																												// open
				regKeyHandler.executeQuery(queryToc);
				return;
			}

			////// =================REGISTER KEY CHECK===============///////////////////

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
				Logger.getLogger(BillerLoginController.class.getName()).log(Level.SEVERE, null, ex);
			}
			//////////// =======if aanenkil salesprint leku
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
						((Stage) loginButton.getScene().getWindow()).close();
					} catch (IOException ex) {
						Logger.getLogger(BillerLoginController.class.getName()).log(Level.SEVERE, null, ex);
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
								((Stage) loginButton.getScene().getWindow()).close();
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
									((Stage) loginButton.getScene().getWindow()).close();
								} catch (IOException ex) {
									Logger.getLogger(BillerLoginController.class.getName()).log(Level.SEVERE, null, ex);
								}
							}
						} catch (IOException ex) {
							Logger.getLogger(BillerLoginController.class.getName()).log(Level.SEVERE, null, ex);
						}
					}
					/////////////////// days baki 8 nu mukalil aanenkil

					try {
						FXMLLoader loaderSales = new FXMLLoader(
								getClass().getResource("/com/ar/salesbillprint/SalesPrint.fxml"));
						Parent parent2 = loaderSales.load();
						Stage stage2 = new Stage(StageStyle.DECORATED);
						SalesPrintController controller = (SalesPrintController) loaderSales.getController();
						controller.billerNameTaking(user,namex);
						stage2.setTitle("BILLING SECTION");
						SuperMarketUtils.setStageIcon(stage2);
						stage2.setScene(new Scene(parent2));
						stage2.show();
						((Stage) userName.getScene().getWindow()).close();
					} catch (IOException ex) {
						Logger.getLogger(BillerLoginController.class.getName()).log(Level.SEVERE, null, ex);
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
									getClass().getResource("/com/ar/salesbillprint/SalesPrint.fxml"));
							Parent parent2 = loaderSales.load();
							Stage stage2 = new Stage(StageStyle.DECORATED);
							SalesPrintController controller = (SalesPrintController) loaderSales.getController();
							controller.billerNameTaking(user,namex);
							stage2.setTitle("BILLING SECTION");
							SuperMarketUtils.setStageIcon(stage2);
							stage2.setScene(new Scene(parent2));
							stage2.show();
							((Stage) userName.getScene().getWindow()).close();
						} catch (IOException ex) {
							Logger.getLogger(BillerLoginController.class.getName()).log(Level.SEVERE, null, ex);
						}
					} else {
						((Stage) loginButton.getScene().getWindow()).close();
					}

				} catch (IOException ex) {
					Logger.getLogger(BillerLoginController.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
		} else {
			labelStatus.setText("username or password incorrect");
		}
	}

	private boolean fileDBExistsChecking() throws IOException {
		try {
			FileReader fr = new FileReader(DB_FILE);
			System.out.println("file exists");
			return true;
		} catch (FileNotFoundException ex) {
//            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
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
