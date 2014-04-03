package com.example.MyDrive;
	
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
	
	public class ChoosePeerActivity extends Activity {
	
	final Context context = this;	
	EditText editText;
	Button addButton;
	ListView listView;
	ArrayList<String> listItems;
	ArrayAdapter<String> adapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
	super.onCreate(savedInstanceState);
	setContentView(R.layout.choose_peer);
	
	
	addButton = (Button) findViewById(R.id.addItem);
	listView = (ListView) findViewById(R.id.peerView);
	listItems = new ArrayList<String>();
	adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listItems); 
	listView.setAdapter(adapter);
	
	try {
	    BufferedReader inputReader = new BufferedReader(new InputStreamReader(openFileInput("Log.txt")));
	    String inputString;
	    StringBuffer stringBuffer = new StringBuffer();                
	    while ((inputString = inputReader.readLine()) != null) {
	        stringBuffer.append(inputString + '\n');
	        listItems.add(inputString);
	    }
	} catch (IOException e) {
	    e.printStackTrace();
	}
	
	listView.setOnItemClickListener(new OnItemClickListener(){ 
		@Override
		public void onItemClick(AdapterView<?> a, View v,int position, long id) 
		{
				Global.ime = (String) ((TextView)v).getText();
				//Tu pokreni svoj activity	
				startActivity (new Intent(ChoosePeerActivity.this, PeerShowActivity.class));
				finish();
		}
	});
	
	addButton.setOnClickListener(new View.OnClickListener() {
		 
					@Override
					public void onClick(View v) {
		 
						// get prompts.xml view
						LayoutInflater li = LayoutInflater.from(context);
						View promptsView = li.inflate(R.layout.add_dialog, null);
		 
						AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
								context);
						// set prompts.xml to alertdialog builder
						alertDialogBuilder.setView(promptsView);
		 
						final EditText userInput = (EditText) promptsView
								.findViewById(R.id.editTextDialogUserInput);
		 
						// set dialog message
						alertDialogBuilder
							.setCancelable(false)
							.setPositiveButton("Dodaj",
							  new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,int id) {
								// get user input and set it to result
								// edit text
								if(userInput.getText().toString() == " "){
									Toast.makeText(getApplicationContext(), "Pogrešan upis! Pokušajte ponovo!", Toast.LENGTH_SHORT).show();
									dialog.cancel();
								} else {
									listItems.add(userInput.getText().toString());
									try {
										 
										 FileOutputStream fos1 = openFileOutput("Log.txt", Context.MODE_APPEND);
										 fos1.write(userInput.getText().toString().getBytes());
										 fos1.write('\n');
										 fos1.close();
									} catch (FileNotFoundException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
										
								try {
									Calendar c = Calendar.getInstance(); 
									int dan = c.get(Calendar.DAY_OF_MONTH);
									int mjesec = c.get(Calendar.MONTH) + 1;
									int godina = c.get(Calendar.YEAR);
									FileOutputStream fos = openFileOutput(userInput.getText().toString()+".txt", Context.MODE_PRIVATE);
									OutputStreamWriter osw = new OutputStreamWriter(fos);
										//Sadržaj datoteke (zaglavlje)
										osw.write(userInput.getText().toString() + '\n');
										osw.write(dan+"."+mjesec+"."+godina+"." + '\n');
										osw.write("0" + '\n');
										osw.write(" " + '\n');
										osw.write("0" + '\n');
										osw.write("==========");
										osw.close();
								} catch (FileNotFoundException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								/*
								* notifyDataSetChanged() notifies the attached observers that the underlying data has 
								* been changed and any View reflecting the data set should refresh itself
								*/
								adapter.notifyDataSetChanged();
									
							    }
								}
							  })
							.setNegativeButton("Odustani",new DialogInterface.OnClickListener() {
							    public void onClick(DialogInterface dialog,int id) {
								dialog.cancel();
							    }
							  });
						// create alert dialog
						AlertDialog alertDialog = alertDialogBuilder.create();
						// show it
						alertDialog.show();
					}		
	});
	}
	@Override
	public void onBackPressed(){
		startActivity (new Intent(ChoosePeerActivity.this, MainMenuActivity.class));
		finish();
	}
	}
		

	