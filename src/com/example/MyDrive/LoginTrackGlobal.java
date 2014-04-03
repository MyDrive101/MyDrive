package com.example.MyDrive;
public class LoginTrackGlobal {
	public static String korisnik;	
	public static String getLogin(){
	  return korisnik;
	}
	public static void setLogin(String Login){
		korisnik = Login;
	}
}