package com.example.MyDrive;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class PostavkeActivity extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.settings_screen);
	    
	    String Login = LoginTrackGlobal.getLogin();
	    ((TextView)findViewById (R.id.korisnik)).setText (Login);
	    
	    Button odjavi_btn = (Button) findViewById(R.id.Odjavi);
        odjavi_btn.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		LoginTrackGlobal.setLogin("");
        		deleteFile("autoLog.txt");
        		startActivity (new Intent(PostavkeActivity.this, LoginActivity.class));
        		finish();
        		}
        });      
	}
	
	@Override
	public void onBackPressed(){
		startActivity (new Intent(PostavkeActivity.this, MainMenuActivity.class));
		finish();
	}
	
	public void onDestroy(){
		super.onDestroy();
	}
}
