
package com.ar.salesbillprint;

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
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.apache.commons.codec.digest.DigestUtils;
import org.controlsfx.control.Notifications;

public class BillerDetailsWithTextFieldsController implements Initializable {
	DatabaseSectionMain handler;

	@FXML
	private AnchorPane mainAnchorPane;
	@FXML
	private AnchorPane insideAnchorPane;
	@FXML
	private ImageView imageView;
	@FXML
	private VBox vboxContainer;
	@FXML
	private Label changePasswordLabel;
	@FXML
	private TextField tfStaffID;
	@FXML
	private TextField tfStaffName;
	@FXML
	private TextField tfStaffMobile;
	@FXML
	private TextField tfStaffEmail;

	PassWordSaver passSaver = new PassWordSaver();
	public static final String CONFIG_FILE = "bwuap.sak"; // SAK////////change from//"bwuap.lnp"
	FileReader file;
	List<PasswordSavingModel> newList = new ArrayList<>();

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		try {
			handler = DatabaseSectionMain.getInstance();
		} catch (ClassNotFoundException | SQLException ex) {
			Logger.getLogger(BillerDetailsWithTextFieldsController.class.getName()).log(Level.SEVERE, null, ex);
		}
		changePasswordAction();
	}

	public void fetchingData(String bid, String name, String mob, String email) {
		tfStaffID.setText(bid);
		tfStaffName.setText(name);
		tfStaffMobile.setText(mob);
		tfStaffEmail.setText(email);

	}

	public void takingOnlyCustomerUsername(String usernam) {
		String query = "SELECT * FROM STAFFS WHERE STAFFSECTION='BILLER' AND USERNAME ='" + usernam + "'";
		ResultSet rs = handler.resultexecQuery(query);
		String bidx;
		String namx;
		String mobbx;
		String emlx;
		try {
			while (rs.next()) {
				bidx = rs.getString("staffid");
				namx = rs.getString("staffname");
				mobbx = rs.getString("staffmobile");
				emlx = rs.getString("staffemail");
				System.out.println("staffid :" + bidx + "\nstaffname:" + namx + "\nmobile:" + mobbx + "\nemail:" + emlx);
				tfStaffID.setText(bidx);
				tfStaffName.setText(namx);
				tfStaffMobile.setText(mobbx);
				tfStaffEmail.setText(emlx);
			}
		} catch (SQLException ex) {
			Logger.getLogger(SalesPrintController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@FXML
	private void okButtonAction(ActionEvent event) {
		((Stage) mainAnchorPane.getScene().getWindow()).close();
	}

	private void changePasswordAction() {
		changePasswordLabel.setOnMouseClicked(event -> {
//            System.out.println("hai");
			AnchorPane anchor = new AnchorPane();
			Scene scene = new Scene(anchor, 550, 350);
			anchor.setStyle("-fx-background-color:transparent;");
			AnchorPane.setBottomAnchor(anchor, 0.0);
			AnchorPane.setTopAnchor(anchor, 0.0);
			AnchorPane.setRightAnchor(anchor, 0.0);
			AnchorPane.setLeftAnchor(anchor, 0.0);

			Label labelHead = new Label("PASSWORD CHANGIGNG");
			AnchorPane.setTopAnchor(labelHead, 0.0);
			AnchorPane.setLeftAnchor(labelHead, 30.0);
			AnchorPane.setRightAnchor(labelHead, 30.0);
			labelHead.setAlignment(Pos.CENTER);
			labelHead.setNodeOrientation(NodeOrientation.INHERIT);
			labelHead.setTextAlignment(TextAlignment.CENTER);
			labelHead.setLayoutX(110);
			labelHead.setLayoutY(5);
			labelHead.setUnderline(true);
			labelHead.setTextFill(Color.DARKBLUE);
			labelHead.setFont(new Font("System", 24));
			labelHead.setStyle(" -fx-font-weight:bold;");
			labelHead.setBackground(new Background(colorColr()));

			Label labelPasswordStatus = new Label();
			labelPasswordStatus.setAlignment(Pos.CENTER);
			labelPasswordStatus.setNodeOrientation(NodeOrientation.INHERIT);
			labelPasswordStatus.setTextAlignment(TextAlignment.CENTER);
			labelPasswordStatus.setLayoutX(55);
			labelPasswordStatus.setLayoutY(45);
			labelPasswordStatus.setFont(new Font(12));
			labelPasswordStatus.setStyle(" -fx-font-weight:bold; -fx-text-fill:derive(red,-50%);");

			Label labelOldPassword = new Label("Old Password");
			labelOldPassword.setAlignment(Pos.CENTER);
			labelOldPassword.setNodeOrientation(NodeOrientation.INHERIT);
			labelOldPassword.setTextAlignment(TextAlignment.CENTER);
			labelOldPassword.setLayoutX(55);
			labelOldPassword.setLayoutY(75);
			labelOldPassword.setFont(new Font(14));
			labelOldPassword.setStyle(" -fx-font-weight:bold;");

			Label labelnewPassword = new Label("New Password");
			labelnewPassword.setAlignment(Pos.CENTER);
			labelnewPassword.setNodeOrientation(NodeOrientation.INHERIT);
			labelnewPassword.setTextAlignment(TextAlignment.CENTER);
			labelnewPassword.setLayoutX(55);
			labelnewPassword.setLayoutY(130);
			labelnewPassword.setFont(new Font(14));
			labelnewPassword.setStyle(" -fx-font-weight:bold;");

			Label labelnewConfirmPassword = new Label("Confirm Password");
			labelnewConfirmPassword.setAlignment(Pos.CENTER);
			labelnewConfirmPassword.setNodeOrientation(NodeOrientation.INHERIT);
			labelnewConfirmPassword.setTextAlignment(TextAlignment.CENTER);
			labelnewConfirmPassword.setLayoutX(55);
			labelnewConfirmPassword.setLayoutY(185);
			labelnewConfirmPassword.setFont(new Font(14));
			labelnewConfirmPassword.setStyle(" -fx-font-weight:bold;");

			Label labelsemicol1 = new Label(":");
			labelsemicol1.setAlignment(Pos.CENTER);
			labelsemicol1.setNodeOrientation(NodeOrientation.INHERIT);
			labelsemicol1.setTextAlignment(TextAlignment.CENTER);
			labelsemicol1.setLayoutX(180);
			labelsemicol1.setLayoutY(75);
			labelsemicol1.setFont(new Font(14));
			labelsemicol1.setStyle(" -fx-font-weight:bold;");

			Label labelsemicol2 = new Label(":");
			labelsemicol2.setAlignment(Pos.CENTER);
			labelsemicol2.setNodeOrientation(NodeOrientation.INHERIT);
			labelsemicol2.setTextAlignment(TextAlignment.CENTER);
			labelsemicol2.setLayoutX(180);
			labelsemicol2.setLayoutY(130);
			labelsemicol2.setFont(new Font(14));
			labelsemicol2.setStyle(" -fx-font-weight:bold;");

			Label labelsemicol3 = new Label(":");
			labelsemicol3.setAlignment(Pos.CENTER);
			labelsemicol3.setNodeOrientation(NodeOrientation.INHERIT);
			labelsemicol3.setTextAlignment(TextAlignment.CENTER);
			labelsemicol3.setLayoutX(180);
			labelsemicol3.setLayoutY(185);
			labelsemicol3.setFont(new Font(14));
			labelsemicol3.setStyle(" -fx-font-weight:bold;");

			TextField oldPassText = new TextField();
			oldPassText.setPromptText("Enter old password");
			oldPassText.setPrefSize(400, 35);
			oldPassText.setLayoutX(185);
			oldPassText.setLayoutY(65);
			oldPassText.setVisible(false);
			oldPassText.setStyle("-fx-text-fill:darkblue; -fx-font-weight:bold;");
			oldPassText.setFont(new Font(14));

			PasswordField oldPassPass = new PasswordField();
			oldPassPass.setPromptText("Enter old password");
			oldPassPass.setPrefSize(400, 35);
			oldPassPass.setLayoutX(185);
			oldPassPass.setLayoutY(65);
			oldPassPass.setStyle("-fx-font-weight:bold;");
			oldPassPass.setFont(new Font(14));

			TextField newPassText = new TextField();
			newPassText.setPromptText("Enter new password");
			newPassText.setPrefSize(400, 35);
			newPassText.setLayoutX(185);
			newPassText.setLayoutY(120);
			newPassText.setVisible(false);
			newPassText.setStyle("-fx-text-fill:darkblue; -fx-font-weight:bold;");
			newPassText.setFont(new Font(14));

			PasswordField newPassPass = new PasswordField();
			newPassPass.setPromptText("Enter new password");
			newPassPass.setPrefSize(400, 35);
			newPassPass.setLayoutX(185);
			newPassPass.setLayoutY(120);
			newPassPass.setStyle(" -fx-font-weight:bold;");
			newPassPass.setFont(new Font(14));

			TextField newConfirmPassText = new TextField();
			newConfirmPassText.setPromptText("Confirm new password");
			newConfirmPassText.setPrefSize(400, 35);
			newConfirmPassText.setLayoutX(185);
			newConfirmPassText.setLayoutY(175);
			newConfirmPassText.setVisible(false);
			newConfirmPassText.setStyle("-fx-text-fill:darkblue; -fx-font-weight:bold;");
			newConfirmPassText.setFont(new Font(14));

			PasswordField newConfirmPassPass = new PasswordField();
			newConfirmPassPass.setPromptText("Confirm new password");
			newConfirmPassPass.setPrefSize(400, 35);
			newConfirmPassPass.setLayoutX(185);
			newConfirmPassPass.setLayoutY(175);
			newConfirmPassPass.setStyle("-fx-font-weight:bold;");
			newConfirmPassPass.setFont(new Font(14));

			oldPassPass.textProperty().bindBidirectional(oldPassText.textProperty());
			newPassPass.textProperty().bindBidirectional(newPassText.textProperty());
			newConfirmPassPass.textProperty().bindBidirectional(newConfirmPassText.textProperty());

			CheckBox check = new CheckBox("show password");
			check.setLayoutX(185);
			check.setLayoutY(215);
			check.setOnAction(checkevent -> {
				if (check.isSelected()) {
					check.setText("hide password");
					oldPassPass.setVisible(false);
					newPassPass.setVisible(false);
					newConfirmPassPass.setVisible(false);
					oldPassText.setVisible(true);
					newPassText.setVisible(true);
					newConfirmPassText.setVisible(true);
				} else {
					check.setText("show password");
					oldPassPass.setVisible(true);
					newPassPass.setVisible(true);
					newConfirmPassPass.setVisible(true);
					oldPassText.setVisible(false);
					newPassText.setVisible(false);
					newConfirmPassText.setVisible(false);
				}
			});

			HBox hbox = new HBox();
			hbox.setLayoutX(95);
			hbox.setLayoutY(260);
			hbox.setAlignment(Pos.CENTER);
			AnchorPane.setRightAnchor(hbox, 0.0);
			AnchorPane.setLeftAnchor(hbox, 0.0);

			Button back = new Button("BACK");
			back.setPrefSize(120, 40);
			HBox.setMargin(back, new Insets(0, 10, 0, 0));

			Button ok = new Button("OK");
			ok.setPrefSize(120, 40);
			HBox.setMargin(ok, new Insets(0, 0, 0, 10));

//            ==================textfield , password field action kal
			oldPassPass.setOnAction(pfevent1 -> {
				newPassPass.requestFocus();
			});
			oldPassText.setOnAction(tfevent1 -> {
				newPassText.requestFocus();
			});
			newPassPass.setOnAction(pfevent2 -> {
				newConfirmPassPass.requestFocus();
			});
			newPassText.setOnAction(tfevent2 -> {
				newConfirmPassText.requestFocus();
			});

//      ===============================================      

			back.setOnMouseClicked(eventb1 -> {
				mainAnchorPane.getChildren().set(0, insideAnchorPane);
				insideAnchorPane.toFront();
			});

			ok.setOnMouseClicked(eventb1 -> {
				if ("".equals(oldPassText.getText()) || oldPassText.getText().isEmpty()
						|| newPassText.getText().isEmpty() || "".equals(newPassText.getText())
						|| newConfirmPassText.getText().isEmpty() || "".equals(newConfirmPassText.getText())) {
					AlertMaker.showErrorMessage("Please fill all fields", ok);
					return;
				}
				String passwordOldTosend = DigestUtils.sha1Hex(oldPassText.getText());
				String query = "select count(*) from staffs where staffid='" + tfStaffID.getText() + "' and password='"
						+ passwordOldTosend + "'";
				Boolean result = handler.billerUserNameAndPasswordCheck(query);
				if (!result) {
					labelPasswordStatus.setText("old password doesnot match");

				} else {
					if (newConfirmPassText.getText().equals(newPassText.getText())) {
						String passwordNewtoUpdate = DigestUtils.sha1Hex(newPassText.getText());
						String updateQuery = "update staffs set password='" + passwordNewtoUpdate + "' where staffid='"
								+ tfStaffID.getText() + "'";
						Boolean updateResult = handler.updatePasswordQuery(updateQuery);
						if (updateResult) {
//                            AlertMaker.showSimpleAlertWithNode("PASSWORD UPDATE", "password successfully updated", ok);
							labelPasswordStatus.setText("Password updated");

							//////////////////////// ===================================
							newList.clear();// clear the list first other wise each and every time existing list + new
											// item oru problem undaakunadu kaaanam
							checkingSection(); // checking the config.txt file if there taking the value then retreive
												// the data to list
							int countno = editNewPassword();
							int newListSize = newList.size();
							if (countno == newListSize) {
								System.out.println("element not in array or config file");
							} else {
								PasswordSavingModel passmodel = newList.get(countno);
								passmodel.password.bind(newPassText.textProperty());
								onSaveMoveMent(); // saving data ie , retriving data from list(newly updated(edited))
													// and save to file
							}

							Notifications notificationBuilderResend = Notifications.create().title("PASSWORD UPDATE")
									.text("Password Updated Succesfully").graphic(null).hideAfter(Duration.seconds(5))
									.position(Pos.CENTER).onAction(new EventHandler<ActionEvent>() {
										@Override
										public void handle(ActionEvent event) {
											System.out.println("Clicked password updated notification");
										}
									});
							notificationBuilderResend.darkStyle();
							notificationBuilderResend.showInformation();

						} else {
							AlertMaker.showErrorMessage("Error occured in updation", ok);
						}
					} else {
						AlertMaker.showErrorMessage("new password and confirm password doesnot match", ok);
					}
				}
			});

			oldPassText.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					labelPasswordStatus.setText("");
				}
			});

			hbox.getChildren().addAll(back, ok);

			anchor.getChildren().addAll(labelHead, labelPasswordStatus, labelOldPassword, labelnewPassword,
					labelnewConfirmPassword, hbox);
			anchor.getChildren().addAll(labelsemicol1, labelsemicol2, labelsemicol3);
			anchor.getChildren().addAll(oldPassText, oldPassPass);
			anchor.getChildren().addAll(newPassText, newPassPass);
			anchor.getChildren().addAll(newConfirmPassText, newConfirmPassPass);
			anchor.getChildren().add(check);

			mainAnchorPane.getChildren().set(0, anchor);
			anchor.toFront();
		});
	}

	private BackgroundFill colorColr() {
		Stop[] stops = new Stop[] { new Stop(0, Color.BLUE), new Stop(1, Color.RED) };// range 0 to 1
		Stop[] stops2 = new Stop[] { new Stop(0.007662835249042145, Color.valueOf("#2a1359")),
				new Stop(0.8722860791826307, Color.valueOf("#5cd6e5")), new Stop(0, Color.valueOf("#5cd6e5")),
				new Stop(1.0, Color.valueOf("#2a1359")) };// range 0 to 1
		LinearGradient igColor = new LinearGradient(0, 0, 1, 0, true, CycleMethod.REFLECT, stops);
// centerX="0.6962963104248044" centerY="0.30793650036766407"
//cycleMethod="REPEAT" focusAngle="4.72" focusDistance="0.4883722260940908" radius="1.0"
//RadialGradient values: (focusangle,focusdistance,centerx,centery,radius,proportional(boolean),cyclemethod,stops)

		RadialGradient radColor = new RadialGradient(4.72, 0.4883722260940908, 0.6962963104248044, 0.30793650036766407,
				1.0, true, CycleMethod.REPEAT, stops2);

		BackgroundFill bgfill = new BackgroundFill(igColor, CornerRadii.EMPTY, Insets.EMPTY);
		BackgroundFill bgfill2 = new BackgroundFill(radColor, CornerRadii.EMPTY, Insets.EMPTY);
//    splitPane.setBackground(new Background(bgfill2));
//    mainAnchorPane.setBackground(new Background(bgfill2));
		return bgfill2;
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
			Logger.getLogger(BillerDetailsWithTextFieldsController.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IOException ex) {
			Logger.getLogger(BillerDetailsWithTextFieldsController.class.getName()).log(Level.SEVERE, null, ex);
		}

	}

	private void retrevingDataToList() {
		Gson gson = new Gson();
		passSaver = gson.fromJson(file, PassWordSaver.class);
		List<PasswordSavingModel> modelList = passSaver.getPasswords().stream()
				.map(PasswordSavingModel.PassWor::toModel).collect(Collectors.toList());
		newList.addAll(modelList);
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
			Logger.getLogger(BillerDetailsWithTextFieldsController.class.getName()).log(Level.SEVERE, null, ex);
		} finally {
			try {
				writer.close();
			} catch (IOException ex) {
				System.out.println("EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE");
				Logger.getLogger(BillerDetailsWithTextFieldsController.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	private int editNewPassword() {
		int count = 0;
		for (PasswordSavingModel ps : newList) {
			if (ps.toData().getBillerid().contains(tfStaffID.getText()))
				break;
			count = count + 1;
		}
		System.out.println("count of billerid:" + count);
		return count;
	}

}
