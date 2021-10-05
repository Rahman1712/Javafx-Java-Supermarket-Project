
package com.ar.database;

import com.ar.databasefilesettings.DatabaseFileSetController;
import com.ar.databasefilesettings.DbSetFile;
import com.ar.proceedsales.PrintItems;
import com.ar.productlist.Products;
import com.ar.util.SuperMarketUtils;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.swing.JOptionPane;

public final class DatabaseSectionMain {
	public static DatabaseSectionMain handler;

	private static final String MYSQL_URL = "com.mysql.cj.jdbc.Driver";
	private static Connection conn = null;
	private static Statement stmt = null;

	public static DatabaseSectionMain getInstance() throws ClassNotFoundException, SQLException {
		if (handler == null) {
			handler = new DatabaseSectionMain();
		}
		return handler;
	}

	private DatabaseSectionMain() throws ClassNotFoundException, SQLException {
		createConnection();
		setupProducts();
		setupCustomer();
		setupSales();
		setupStaffs();
	}

	void createConnection() throws ClassNotFoundException, SQLException {
		DbSetFile dbsets = DbSetFile.getLogerData();
		String dbname = dbsets.getDbDatabase();
		String dbUsername = dbsets.getDbUserName();
		String dbPassword = dbsets.getDbPassword();
		try {
			Class.forName(MYSQL_URL);

		} catch (ClassNotFoundException ex) {
			System.out.println("no driver");
			ex.printStackTrace();
			throw ex;
		}
		System.out.println("jdbc driver registered");
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost/" + dbname, dbUsername, dbPassword);

		} catch (SQLException ex) {
			System.out.println("failed connection");
			try {
				FXMLLoader loader = new FXMLLoader(
						getClass().getResource("/com/ar/databasefilesettings/DatabaseFileSet.fxml"));
				Parent parent = loader.load();
				Stage stage = new Stage(StageStyle.DECORATED);
				stage.setAlwaysOnTop(true); // making window on top
				stage.initModality(Modality.APPLICATION_MODAL);
				stage.setTitle("Database Error(Set to correct connection)");
				SuperMarketUtils.setStageIcon(stage);
				DatabaseFileSetController controller = (DatabaseFileSetController) loader.getController();
				controller.fxmlOpenComingFromDatabseSection(dbname, dbUsername, dbPassword);
				stage.setScene(new Scene(parent));
				stage.showAndWait();
				System.out.println("Closing all window");
				System.exit(0);////////////////////// =========closing all============////////////////////

			} catch (IOException ex1) {
				Logger.getLogger(DatabaseSectionMain.class.getName()).log(Level.SEVERE, null, ex1);
			}

			ex.printStackTrace();
			throw ex;
		}
	}

	public ResultSet resultexecQuery(String q) {
		ResultSet r;
		try {
			stmt = conn.createStatement();
			r = stmt.executeQuery(q);
		} catch (SQLException ex) {
			Logger.getLogger(DatabaseSectionMain.class.getName()).log(Level.SEVERE, null, ex);

			System.out.println("exeption at resultset query");
			return null;
		} finally {

		}
		return r;
	}

	void setupProducts() {
		String TABLE_NAME = "PRODUCTS";
		String TABLE_SCHEMA_NAME = "SUPERMARKET";
		try {
			stmt = conn.createStatement();
			DatabaseMetaData dbm = conn.getMetaData();
			ResultSet tables = dbm.getTables(null, TABLE_SCHEMA_NAME.toUpperCase(), TABLE_NAME.toUpperCase(), null);
			if (tables.next()) {
				System.out.println("Table " + TABLE_NAME + " already exists Ready lets go");
			} else {
				stmt.execute("CREATE TABLE " + TABLE_NAME + "(" + "prbarcode varchar(20) not null,\n"
						+ "prname varchar(200) primary key not null,\n" + "prquantity int not null,\n"
						+ "prmrp float not null,\n" + "prdiscount float not null,\n" + "prprice float not null" + ")");

				System.out.println("Successfully created " + TABLE_NAME);
			}

		} catch (SQLException ex) {
			System.out.println("Setup DataBase-----!");
			Logger.getLogger(DatabaseSectionMain.class.getName()).log(Level.SEVERE, null, ex);
		} finally {
		}
	}

	void setupCustomer() {
		String TABLE_NAME = "CUSTOMER";
		String TABLE_SCHEMA_NAME = "SUPERMARKET";
		try {
			stmt = conn.createStatement();
			DatabaseMetaData dbm = conn.getMetaData();
			ResultSet tables = dbm.getTables(null, TABLE_SCHEMA_NAME.toUpperCase(), TABLE_NAME.toUpperCase(), null);
			if (tables.next()) {
				System.out.println("Table " + TABLE_NAME + " already exists Ready lets go");
			} else {
				stmt.execute("CREATE TABLE " + TABLE_NAME + "(" + "cusid varchar(20) primary key not null,\n"
						+ "cusname varchar(200) not null,\n" + "cusplace varchar(200) not null,\n"
						+ "cusmob varchar(20) not null,\n" + "custime timestamp default CURRENT_TIMESTAMP" + ")");

				System.out.println("Successfully created " + TABLE_NAME);
			}

		} catch (SQLException ex) {
			System.out.println("Setup DataBase-----!");
			Logger.getLogger(DatabaseSectionMain.class.getName()).log(Level.SEVERE, null, ex);
		} finally {
		}
		String checkItem = "SELECT COUNT(*) FROM CUSTOMER WHERE CUSID='cus00'";
		Boolean result = isCustomerAlreadyExist(checkItem);
		if (result) {
			System.out.println("cus00 alredy exists dont create new one !");
		} else {
			String queryFirstInsertion = "INSERT INTO CUSTOMER(cusid,cusname,cusplace,cusmob) VALUES('cus00','null','null','null')";
			if (executeQuery(queryFirstInsertion)) {
				System.out.println("create cus00 successfully");
			} else {
				System.out.println("failed cus00 creation");
			}
		}
	}

	void setupSales() {
		String TABLE_NAME = "SALES";
		String TABLE_SCHEMA_NAME = "SUPERMARKET";
		try {
			stmt = conn.createStatement();
			DatabaseMetaData dbm = conn.getMetaData();
			ResultSet tables = dbm.getTables(null, TABLE_SCHEMA_NAME.toUpperCase(), TABLE_NAME.toUpperCase(), null);
			if (tables.next()) {
				System.out.println("Table " + TABLE_NAME + " already exists Ready lets go");
			} else {
				stmt.execute("CREATE TABLE " + TABLE_NAME + "(" + "salesid varchar(20) primary key not null,\n"
						+ "cusid varchar(20) not null,\n" + "cusname varchar(20) not null,\n"
						+ "salestotal float not null,\n" + "salesitemno int not null,\n"
						+ "salesbillpaid float not null,\n" + "salesbalance float not null,\n"
						+ "salesbiller varchar(200) not null,\n" + "salestime timestamp default CURRENT_TIMESTAMP,\n"
						+ "FOREIGN KEY (cusid) REFERENCES CUSTOMER(cusid)" + ")");

				System.out.println("Successfully created " + TABLE_NAME);
			}

		} catch (SQLException ex) {
			System.out.println("Setup DataBase-----!");
			Logger.getLogger(DatabaseSectionMain.class.getName()).log(Level.SEVERE, null, ex);
		} finally {
		}
	}

	public boolean executeQuery(String query) {
		try {
			stmt = conn.createStatement();
			stmt.execute(query);
			return true;
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(null,
					"Error occuring in insertion Check it \n " + "* may be empty column \n "
							+ "* may be item/name already existing in TABLE \n" + "   (primarykey Violation)",
					"ERROR OCCURED !" + "message\n " + ex.getMessage(), JOptionPane.ERROR_MESSAGE);
			System.out.println("Error in insertion");
			Logger.getLogger(DatabaseSectionMain.class.getName()).log(Level.SEVERE, null, ex);
			return false;
		} finally {
		}
	}

	public boolean updateProducts(PrintItems items) {
		try {
			String q = "select prquantity from products where prname='" + items.getName() + "'";
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(q);
			int qnty = 0;
			while (rs.next()) {
				qnty = rs.getInt("prquantity");
			}
			String update = "UPDATE PRODUCTS SET PRQUANTITY=? WHERE PRNAME=?";
			PreparedStatement statement = conn.prepareStatement(update);
			statement.setInt(1, qnty - items.getQuantity());
			statement.setString(2, items.getName());

			int res = statement.executeUpdate();
			return (res > 0);

		} catch (SQLException ex) {
			Logger.getLogger(DatabaseSectionMain.class.getName()).log(Level.SEVERE, null, ex);
		}
		return false;
	}

	private boolean isCustomerAlreadyExist(String q) {
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(q);
			if (rs.next()) {
				int count = rs.getInt(1);
				System.out.println("Count   :" + count);
				return (count > 0);
			}
		} catch (SQLException ex) {
			Logger.getLogger(DatabaseSectionMain.class.getName()).log(Level.SEVERE, null, ex);
		}
		return false;
	}

	public boolean isSalesIdAlreadyExist(String q) {
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(q);
			if (rs.next()) {
				int count = rs.getInt(1);
				System.out.println("Count   :" + count);
				return (count > 0);
			}
		} catch (SQLException ex) {
			Logger.getLogger(DatabaseSectionMain.class.getName()).log(Level.SEVERE, null, ex);
		}
		return false;
	}

	void setupStaffs() {
		String TABLE_NAME = "STAFFS";
		String TABLE_SCHEMA_NAME = "SUPERMARKET";
		try {
			stmt = conn.createStatement();
			DatabaseMetaData dbm = conn.getMetaData();
			ResultSet tables = dbm.getTables(null, TABLE_SCHEMA_NAME.toUpperCase(), TABLE_NAME.toUpperCase(), null);
			if (tables.next()) {
				System.out.println("Table " + TABLE_NAME + " already exists Ready lets go");
			} else {
				stmt.execute("CREATE TABLE " + TABLE_NAME + "(" + "staffid varchar(20) primary key not null,\n"
						+ "staffsection varchar(100) not null,\n" + "staffname varchar(200) not null,\n"
						+ "staffmobile varchar(20) not null,\n" + "staffaadhar varchar(30) not null,\n"
						+ "staffemail varchar(200) not null,\n" + "staffaddress varchar(200) not null,\n"
						+ "staffstatus boolean default false,\n" + "username varchar(200),\n"
						+ "password varchar(200),\n" + "stafftime timestamp default CURRENT_TIMESTAMP" + ")");

				System.out.println("Successfully created " + TABLE_NAME);
			}

		} catch (SQLException ex) {
			System.out.println("Setup DataBase-----!");
			Logger.getLogger(DatabaseSectionMain.class.getName()).log(Level.SEVERE, null, ex);
		} finally {
		}
	}

	public boolean billerUserNameAndPasswordCheck(String q) {
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(q);
			if (rs.next()) {
				int count = rs.getInt(1);
				System.out.println("Count   :" + count);
				return (count > 0);
			}
		} catch (SQLException ex) {
			Logger.getLogger(DatabaseSectionMain.class.getName()).log(Level.SEVERE, null, ex);
		}
		return false;
	}

	private boolean isBillerPasswordAlreadyExist(String q) {
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(q);
			if (rs.next()) {
				int count = rs.getInt(1);
				System.out.println("Count   :" + count);
				return (count > 0);
			}
		} catch (SQLException ex) {
			Logger.getLogger(DatabaseSectionMain.class.getName()).log(Level.SEVERE, null, ex);
		}
		return false;
	}

	public boolean updatePasswordQuery(String query) {
		try {
			stmt = conn.createStatement();
			stmt.executeUpdate(query);
			return true;
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(null, "Error occuring in updation Check it \n " + "* may be empty column \n "
					+ "* may be CaseSensitive in password \n" + "   (password changing/correption occured in database)",
					"ERROR OCCURED !" + "message\n " + ex.getMessage(), JOptionPane.ERROR_MESSAGE);
			System.out.println("Error in insertion");
			Logger.getLogger(DatabaseSectionMain.class.getName()).log(Level.SEVERE, null, ex);
			return false;
		} finally {
		}
	}

	public boolean updateQuantityOfProducts(Products product) {
		try {
			stmt = conn.createStatement();
			String updateProd = "UPDATE PRODUCTS SET PRQUANTITY=? WHERE PRNAME=?";
			PreparedStatement statement = conn.prepareStatement(updateProd);
			statement.setInt(1, product.getProdQuantity());
			statement.setString(2, product.getProdName());
			int res = statement.executeUpdate();
			return (res > 0);
		} catch (SQLException ex) {
			Logger.getLogger(DatabaseSectionMain.class.getName()).log(Level.SEVERE, null, ex);
		}
		return false;
	}

	public boolean updateQuantityOfProductsUsingFxml(String barcode, String pname, int quant, float mrp, float disc,
			float price, String pnamefirst) {
		try {
			stmt = conn.createStatement();
			String updateProd = "UPDATE PRODUCTS SET PRBARCODE='" + barcode + "' ,PRNAME='" + pname + "' ,PRQUANTITY="
					+ quant + "" + " ,PRMRP=" + mrp + " ,PRDISCOUNT=" + disc + " ,PRPRICE=" + price + "  WHERE PRNAME='"
					+ pnamefirst + "'";
			int res = stmt.executeUpdate(updateProd);
			return (res > 0);
		} catch (SQLException ex) {
			Logger.getLogger(DatabaseSectionMain.class.getName()).log(Level.SEVERE, null, ex);
		}
		return false;
	}

	public boolean DeleteProductsFromTable(Products product) {
		try {
			stmt = conn.createStatement();
			String updateProd = "DELETE FROM PRODUCTS WHERE PRNAME=?";
			PreparedStatement statement = conn.prepareStatement(updateProd);
			statement.setString(1, product.getProdName());
			int res = statement.executeUpdate();
			return (res > 0);
		} catch (SQLException ex) {
			Logger.getLogger(DatabaseSectionMain.class.getName()).log(Level.SEVERE, null, ex);
		}
		return false;
	}

	public int productItemsCountForCombobox(String q) {
		int count = 0;
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(q);
			if (rs.next()) {
				count = rs.getInt(1);
				System.out.println("Count   :" + count);

			}
		} catch (SQLException ex) {
			Logger.getLogger(DatabaseSectionMain.class.getName()).log(Level.SEVERE, null, ex);
		}
		return count;
	}
}
