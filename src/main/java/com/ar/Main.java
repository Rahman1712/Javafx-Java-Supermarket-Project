package com.ar;

import com.ar.billerlogin.BillerLoginLoader;
import com.ar.login.LoginLoader;

public class Main {

	public static void main(String[] args) {
		
		//First comment   BillerLoginLoader.main(args); then run ADMIN section
		//Then create Billers from  Admin panel
		//Then comment LoginLoader.main(args); then uncomment and Run  BillerLoginLoader.main(args);
		
		
		//FOR ADMIN 
		//default username = admin
		//default password = admin
		LoginLoader.main(args); 

		//FOR BILLER
		//first create biller in admin section then type biller's username and password
		BillerLoginLoader.main(args); 
	}
}
