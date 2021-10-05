
package com.ar.util;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public final class PasswordSavingModel {
	public StringProperty username = new SimpleStringProperty();
	public StringProperty password = new SimpleStringProperty();
	public StringProperty billerid = new SimpleStringProperty();
	public StringProperty billername = new SimpleStringProperty();
	public StringProperty mobile = new SimpleStringProperty();

	public PassWor toData() {
		return new PassWor(username.get(), password.get(), billerid.get(), billername.get(), mobile.get());
	}

	public static class PassWor {
		private String username;
		private String password;
		private String billerid;
		private String billername;
		private String mobile;

		public PassWor() {
		}

		public PassWor(String username, String password, String billerid, String billername, String mobile) {
			this.username = username;
			this.password = password;
			this.billerid = billerid;
			this.billername = billername;
			this.mobile = mobile;
		}

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public String getBillerid() {
			return billerid;
		}

		public void setBillerid(String billerid) {
			this.billerid = billerid;
		}

		public String getBillername() {
			return billername;
		}

		public void setBillername(String billername) {
			this.billername = billername;
		}

		public String getMobile() {
			return mobile;
		}

		public void setMobile(String mobile) {
			this.mobile = mobile;
		}

		public PasswordSavingModel toModel() {
			PasswordSavingModel model = new PasswordSavingModel();
			model.username.set(username);
			model.password.set(password);
			model.billerid.set(billerid);
			model.billername.set(billername);
			model.mobile.set(mobile);
			return model;
		}
	}
}
