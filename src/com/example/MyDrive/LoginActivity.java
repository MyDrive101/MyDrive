package com.example.MyDrive;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity 
{
	Button btnSignIn,btnSignUp;
	LoginDataBaseAdapter loginDataBaseAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	     super.onCreate(savedInstanceState);
	     setContentView(R.layout.main);
	     
	     LocationManager service= (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	     boolean enabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER);
	     if (getIntent().getBooleanExtra("EXIT", false)) {
	    	 	if (enabled) {
	    	          Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
	    	          startActivity(intent);
	    	    } 
	    	 	finish();
	    	    System.exit(0);
	    	}
	     
	     Button izlaz_btn = (Button) findViewById(R.id.Izlaz0);
	     izlaz_btn.setOnClickListener(new OnClickListener() {
	            @Override
	            public void onClick(View v) {
	         			final Dialog dialog = new Dialog(LoginActivity.this);
	         			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	         			dialog.setContentView(R.layout.exit_dialog);
	         		    dialog.setTitle("Upit_tekst");
	         	       		    
	         			Button btnDa=(Button)dialog.findViewById(R.id.btnDa);
	         			btnDa.setOnClickListener(new View.OnClickListener() {
	         				public void onClick(View v) {
	         		    	 	finish();
	         		    	    System.exit(0);
	         				}
	         			});
	         			Button btnNe=(Button)dialog.findViewById(R.id.btnNe);
	         			btnNe.setOnClickListener(new View.OnClickListener() {
	         				public void onClick(View v) {
	         					dialog.dismiss();
	         				}
	         			});
	         			dialog.show();
	            }
	        });
	     
	     // create a instance of SQLite Database
	     loginDataBaseAdapter=new LoginDataBaseAdapter(this);
	     loginDataBaseAdapter=loginDataBaseAdapter.open();
	     
	     // Get The Refference Of Buttons
	     btnSignIn=(Button)findViewById(R.id.buttonSignIN);
	     btnSignUp=(Button)findViewById(R.id.buttonSignUP);
			
	    // Set OnClick Listener on SignUp button 
	    btnSignUp.setOnClickListener(new View.OnClickListener() {
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			/// Create Intent for SignUpActivity  and Start The Activity
			startActivity (new Intent (LoginActivity.this, SignUPActivity.class));
			}
		});
	}
	// Method to handleClick Event of Sign In Button
	public void signIn(View V)
	   {
			boolean a = false;
			try {
				InputStream inputStream = openFileInput("autoLog.txt");
				InputStreamReader read = new InputStreamReader(openFileInput("autoLog.txt"));
				BufferedReader bufferedReader = new BufferedReader(read);
				StringBuilder stringBuilder = new StringBuilder();
				String userName = "";
				userName = bufferedReader.readLine();
				LoginTrackGlobal.setLogin(userName);
				a = true;
			
			} catch (FileNotFoundException e){
				a = false;
				Log.e("Exception", "File open failed: " + e.toString());
			} catch (IOException e) {
				a = false;
				Log.e("Exception", "File write failed: " + e.toString());
			}
			if (a == true){
				startActivity (new Intent(LoginActivity.this, MainMenuActivity.class));
				finish();
			} else {
			final Dialog dialog = new Dialog(LoginActivity.this);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.login_dialog);
		    dialog.setTitle("Prijava");
	
		    // get the Refferences of views
		    final  EditText editTextUserName=(EditText)dialog.findViewById(R.id.editTextUserNameToLogin);
		    final  EditText editTextPassword=(EditText)dialog.findViewById(R.id.editTextPasswordToLogin);
		    
		    final CheckBox autoLog = (CheckBox)dialog.findViewById(R.id.autoLog);
		    
			Button btnSignIn=(Button)dialog.findViewById(R.id.buttonSignIn);
			btnSignIn.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {					
					// get The User name and Password
					String userName=editTextUserName.getText().toString();
					LoginTrackGlobal.setLogin(userName);
					String password=editTextPassword.getText().toString();
					
					// fetch the Password form database for respective user name
					String storedPassword=loginDataBaseAdapter.getSinlgeEntry(userName);
					
					// check if the Stored password matches with  Password entered by user
					if(password.equals(storedPassword))
					{
						if(autoLog.isChecked()){
							try {
								OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput("autoLog.txt", Context.MODE_PRIVATE));
						        outputStreamWriter.write(userName);
						        outputStreamWriter.write('\n');
						        outputStreamWriter.write(password);
						        outputStreamWriter.close();
							} catch (IOException e) {
						        Log.e("Exception", "File write failed: " + e.toString());
						    } 
						}
						
						Toast.makeText(LoginActivity.this, "Uspješno ste prijavljeni!", Toast.LENGTH_SHORT).show();
						dialog.dismiss();
			    	 	finish();
						startActivity (new Intent(LoginActivity.this, MainMenuActivity.class));
					}
					else
					{
						Toast.makeText(LoginActivity.this, "Korisnièko ime i lozinka se ne podudaraju!", Toast.LENGTH_LONG).show();
					}
				}
			});			
			dialog.show();
			}
		}
		
	@Override
	public void onBackPressed(){}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	    // Close The Database
		loginDataBaseAdapter.close();
	}
}
