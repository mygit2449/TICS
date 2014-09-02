package com.meldit.tca;

import java.util.Vector;

import com.meldit.tca.db.DbAdapter;

public class Utilities {

	public static boolean signin;
	public static boolean exit;
	public static String first_name;
	public static String last_name;
	public static String uname;
	public static String pword;
	public static String email;
	public static String contact;
	public static boolean flag = true;
	public static Vector <SetterAndGetters>TripsVector ;
	public static DbAdapter dbAdapter;

	
	public static String availble_trips_url =
			//"http://192.168.1.200:8080/TICS/services/servicemanagement/getpossibleServices?sourcename=";
			"http://dev.meldit.com:8080/TICS/services/servicemanagement/getpossibleServices?sourcename=";
	public static String forgetpassword_url = 
			//"http://192.168.1.200:8080/TICS/services/consumermanagement/consumerForgotPassword?userName=";
			"http://dev.meldit.com:8080/TICS/services/consumermanagement/consumerForgotPassword?userName=";
	public static String consumer_login_url = 
			//"http://192.168.1.200:8080/TICS/services/consumermanagement/consumerLogin";
			"http://dev.meldit.com:8080/TICS/services/consumermanagement/consumerLogin";
	public static String registration_url = 
			//"http://192.168.1.200:8080/TICS/services/consumermanagement/consumerCreate";
			"http://dev.meldit.com:8080/TICS/services/consumermanagement/consumerCreate";
}
