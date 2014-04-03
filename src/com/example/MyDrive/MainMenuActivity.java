package com.example.MyDrive;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

public class MainMenuActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);
    	
    	Button Voznja_btn = (Button) findViewById(R.id.Voznja);
        Voznja_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	startActivity (new Intent(MainMenuActivity.this, ChoosePeerActivity.class));
            	finish();
            }
        });

        Button postavke_btn = (Button) findViewById(R.id.Postavke);
        postavke_btn.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		startActivity (new Intent(MainMenuActivity.this, PostavkeActivity.class));
        		finish();
        		
        	}
        });
        Button izlaz_btn = (Button) findViewById(R.id.Izlaz);
        izlaz_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
         			final Dialog dialog = new Dialog(MainMenuActivity.this);
         			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
         			dialog.setContentView(R.layout.exit_dialog);
         		    dialog.setTitle("Upit_tekst");
         	       		    
         			Button btnDa=(Button)dialog.findViewById(R.id.btnDa);
         			btnDa.setOnClickListener(new View.OnClickListener() {
         				public void onClick(View v) {
         					Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
         					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
         					intent.putExtra("EXIT", true);
         					startActivity(intent); 
         					finish();
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
    }
	@Override
	public void onBackPressed(){/*Overriding the back button*/}
}