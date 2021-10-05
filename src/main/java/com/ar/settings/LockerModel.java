
package com.ar.settings;

import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.apache.commons.codec.digest.DigestUtils;

public class LockerModel {
	public StringProperty keyCode = new SimpleStringProperty();
	public StringProperty keyActivation = new SimpleStringProperty();
	private static List<LockerModel> newList = new ArrayList<>();

	public Locker toData() {
		return new Locker(keyCode.get(), keyActivation.get());
	}

	@Override
	public String toString() {
		return keyCode.get() + " " + keyActivation.get();
	}

	public static class Locker {
		private String keyCode;
		private String keyActivation;

		public Locker() {
		}

		public Locker(String keyCode, String keyActivation) {
			this.keyCode = keyCode;
			this.keyActivation = keyActivation;
		}

		public String getKeyCode() {
			return keyCode;
		}

		public void setKeyCode(String keyCode) {
			this.keyCode = keyCode;
		}

		public String getKeyActivation() {
			return keyActivation;
		}

		public void setKeyActivation(String keyActivation) {
			this.keyActivation = keyActivation;
		}

		public LockerModel toModel() {
			LockerModel lcmodel = new LockerModel();
			lcmodel.keyCode.setValue(keyCode);
			lcmodel.keyActivation.setValue(keyActivation);
			return lcmodel;
		}
	}

	public static void initListAKConfig() {
		String kc1 = DigestUtils.sha1Hex("30DAYSMODE");
		String kc2 = DigestUtils.sha1Hex("FREEMODE");
		String ak1 = DigestUtils.sha1Hex("abcd1234ABCD1234");
		String ak2 = DigestUtils.sha1Hex("AR12d0r017km6YsM");
		onAddMoment(kc1, ak1);
		onAddMoment(kc2, ak2);
	}

	public static List<LockerModel> retrevingDataToList() {
		System.out.println(newList);
		return newList;
	}

	private static void onAddMoment(String keycd, String actKey) {
		LockerModel lockr = new LockerModel();
		lockr.keyCode.setValue(keycd);
		lockr.keyActivation.setValue(actKey);
		newList.add(lockr);
	}
}
