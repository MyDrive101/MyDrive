package com.example.MyDrive;
	
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
	
public class PeerShowActivity extends Activity {
	
	final Context context = this;
	String ucenik = Global.ime;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.peer_show);
		
		TextView tv=(TextView)findViewById (R.id.peerInfo);
		
		try {
		    BufferedReader inputReader = new BufferedReader(new InputStreamReader(openFileInput(ucenik+".txt")));
		    String inputString;   
		    inputString = inputReader.readLine();
		    tv.append("Polaznik:" + '\n');
		    tv.append("  " + inputString + '\n');
		    inputString = inputReader.readLine();
		    tv.append("Polaznik dodan:" + '\n');
		    tv.append("  " + inputString + '\n');
		    inputString = inputReader.readLine();
		    tv.append("Položenih satova:" + '\n');
		    tv.append("  " + inputString + '\n');
    		inputString = inputReader.readLine();
			tv.append("Ponovljeni satovi:" + '\n');
    		tv.append(" " + inputString + '\n');
    		inputString = inputReader.readLine();
			tv.append("Ukupno satova:" + '\n');
    		tv.append("  " + inputString + '\n');
    		inputReader.close();
		} catch (IOException e) {
		    e.printStackTrace();
		}
		
		Button startDriveButton = (Button) findViewById(R.id.startDrive);
		startDriveButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity (new Intent(PeerShowActivity.this, TimerActivity.class));
            	finish();
			}
		});
		
		Button removeButton = (Button) findViewById(R.id.Obrisi);
		removeButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setMessage("Jeste li sigurni da želite obrisati pristupnika: " + ucenik).setPositiveButton("Da",
				  new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						//log.txt
						File oldFile = context.getFileStreamPath("Log.txt"); 
						try {
							BufferedReader inputReader = new BufferedReader(new InputStreamReader(openFileInput("Log.txt")));
							String inputString;
							while ((inputString = inputReader.readLine()) != null){
								if (!Pattern.matches(inputString, ucenik)) {
									try {
										FileOutputStream fos2 = openFileOutput("Logp.txt", Context.MODE_APPEND);		 
										fos2.write(inputString.toString().getBytes());
										fos2.write('\n');
										fos2.close();
									} catch (FileNotFoundException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
		
								}
							}					    
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}		    
				        context.getFileStreamPath("Log.txt").delete();
				        context.getFileStreamPath(ucenik + ".txt").delete();
				        context.getFileStreamPath("Logp.txt").renameTo(oldFile);
				        startActivity (new Intent(PeerShowActivity.this, ChoosePeerActivity.class));
		            	finish();
					}
				  })
				  .setNegativeButton("Ne",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						dialog.cancel();
					}
				  });
				builder.show();
			}	
		});
	}
	@Override
	public void onBackPressed(){
		startActivity (new Intent(PeerShowActivity.this, ChoosePeerActivity.class));
		finish();
	}
}
	