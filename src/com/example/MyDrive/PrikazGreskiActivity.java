package com.example.MyDrive;

	
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class PrikazGreskiActivity extends Activity {
	final Context context = this;
	ListView listView;
	ArrayList<String> listItems;
	ArrayAdapter<String> adapter;
	int data_block=100;
	String ucenik = Global.ime;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prikaz_greski);
        listView = (ListView) findViewById(R.id.Greske);
        listItems = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listItems);  
        listView.setAdapter(adapter);
        
        Calendar c = Calendar.getInstance(); 
        int minuta = c.get(Calendar.MINUTE);
        int sat = c.get(Calendar.HOUR_OF_DAY);
		int dan = c.get(Calendar.DAY_OF_MONTH);
		int mjesec = c.get(Calendar.MONTH) + 1;
		int godina = c.get(Calendar.YEAR);
        
        try {
        	BufferedReader inputReader = new BufferedReader(new InputStreamReader(openFileInput("voznja.txt")));
        	String inputString;
        	try {
        		FileOutputStream pisi = openFileOutput(ucenik + ".txt", Context.MODE_APPEND);
        		OutputStreamWriter osw = new OutputStreamWriter(pisi);
        		osw.write(dan + "." + mjesec + "." + godina + ".");
        		osw.write(" " + sat + ":" + minuta + '\n');
          		while ((inputString = inputReader.readLine()) != null){
          			osw.write(inputString + '\n');
          		}
	          	osw.write("----------" + '\n');
	          	osw.close();
        	} catch (FileNotFoundException e1) {
        		    // TODO Auto-generated catch block
        		    e1.printStackTrace();
        	} catch (IOException e) {
        		    // TODO Auto-generated catch block
        		    e.printStackTrace();
        	}
        	inputReader.close();
        } catch (FileNotFoundException e) {
        	// TODO Auto-generated catch block
        	e.printStackTrace();
        }
        catch (IOException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
        }
       
       Button Prolaz_btn = (Button) findViewById(R.id.Prolaz);
       Prolaz_btn.setOnClickListener(new OnClickListener() {
           @Override
           public void onClick(View v) {
        	   	File oldFile = context.getFileStreamPath(ucenik + ".txt"); 
				try {
					BufferedReader inputReader = new BufferedReader(new InputStreamReader(openFileInput(ucenik + ".txt")));
					String inputString;   
					int a = 0, t;
					try {
		    			FileOutputStream pisi = openFileOutput(ucenik + "_temp.txt", Context.MODE_APPEND);	
		    			OutputStreamWriter osw = new OutputStreamWriter(pisi);
		    			while ((inputString = inputReader.readLine()) != null){
		    				a++;
		    				if (a == 3) {
		    					t = Integer.valueOf(inputString);
		    					t++;
		    					osw.write(String.valueOf(t));
		    					osw.write('\n');
		    				} else if (a == 5) {
		    					t = Integer.valueOf(inputString);
		    					t++;
		    					osw.write(String.valueOf(t));
		    					osw.write('\n');
		    				} else {
		    					osw.write(inputString);
		    					osw.write('\n');
		    				}
		    			}
		    			osw.close();
		    		} catch (FileNotFoundException e1) {
		    			// TODO Auto-generated catch block
		    			e1.printStackTrace();
		    		} catch (IOException e) {
		    			// TODO Auto-generated catch block
		    			e.printStackTrace();
		    		}					    
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		        context.getFileStreamPath(ucenik + ".txt").delete();
		        context.getFileStreamPath(ucenik + "_temp.txt").renameTo(oldFile);
			
		        startActivity (new Intent(PrikazGreskiActivity.this, ChoosePeerActivity.class));
		        finish();
           }
       });
       
       Button Pad_btn = (Button) findViewById(R.id.Pad);
       Pad_btn.setOnClickListener(new OnClickListener() {
           @Override
           public void onClick(View v) {
        	   File oldFile = context.getFileStreamPath(ucenik + ".txt"); 
				BufferedReader inputReader;
				try {
					inputReader = new BufferedReader(new InputStreamReader(openFileInput(ucenik + ".txt")));
					String inputString;   
					int a = 0, t;
					try {
		    			FileOutputStream pisi = openFileOutput(ucenik + "_temp.txt", Context.MODE_APPEND);	
		    			OutputStreamWriter osw = new OutputStreamWriter(pisi);
		    			while ((inputString = inputReader.readLine()) != null){
		    				a++;
		    				if (a == 3) {
		    					//Zadnji položeni sat
		    					t = Integer.valueOf(inputString);
		    					t++;
		    					osw.write(inputString + '\n');
		    					//Ponovljen sat:
		    					inputString = inputReader.readLine();
		    					osw.write(inputString);
		    					osw.write(String.valueOf(t) + ". , " + '\n');
		    					//Ukupni ++
		    					inputString = inputReader.readLine();
		    					t = Integer.valueOf(inputString);
		    					t++;
		    					osw.write(String.valueOf(t));
		    					osw.write('\n');
		    					continue;
		    				} else {
		    					osw.write(inputString);
		    					osw.write('\n');
		    				}
		    			}
		    			osw.close();
		    		} catch (FileNotFoundException e1) {
		    			// TODO Auto-generated catch block
		    			e1.printStackTrace();
		    		} catch (IOException e) {
		    			// TODO Auto-generated catch block
		    			e.printStackTrace();
		    		}					    
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		        context.getFileStreamPath(ucenik + ".txt").delete();
		        context.getFileStreamPath(ucenik + "_temp.txt").renameTo(oldFile);
		        
           		startActivity (new Intent(PrikazGreskiActivity.this, ChoosePeerActivity.class));
           		finish();
           }
       });
       
       try {
		FileInputStream fis = openFileInput(ucenik);
		InputStreamReader isr = new InputStreamReader(fis);
		char[] data = new char[data_block];
		int size;
		//MILEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE WTF JE OVO???
		try {		
			while((size = isr.read(data)) > 0)
			{
				String read_data = String.copyValueOf(data, 0, size);
				for (int i=0;i<size;i++){
				if (read_data.charAt(i) == '1'){
					if (read_data.charAt(i+1) == '1'){
						listItems.add("Jedanaesta");
						i++;
					}
					else if (read_data.charAt(i+1) == '2'){
						listItems.add("Dvanaesta");
						i++;
					}
					else if (read_data.charAt(i+1) == '3'){
						listItems.add("Trinaesta");
						i++;
					}
					else if (read_data.charAt(i+1) == '4'){
						listItems.add("Cetrnaesta");
						i++;
					}
					else if (read_data.charAt(i+1) == '5'){
						listItems.add("Petnaesta");
						i++;
					}
					else if (read_data.charAt(i+1) == '6'){
						listItems.add("Sesnaesta");
						i++;
					}
					else if (read_data.charAt(i+1) == '7'){
						listItems.add("Sedamnesta");
						i++;
					}
					else if (read_data.charAt(i+1) == '8'){
						listItems.add("Osmanesta");
						i++;
					}
					else if (read_data.charAt(i+1) == '9'){
						listItems.add("Devetnaesta");
						i++;
					}
					else if (read_data.charAt(i+1) == '0'){
						listItems.add("Deseta");
						i++;
					}
					else{
						listItems.add("Prva");
					}
				}
				else if (read_data.charAt(i) == '2'){
					if (read_data.charAt(i+1) == '1'){
						listItems.add("Dvadesetaprva");
						i++;
					}
					else if (read_data.charAt(i+1) == '2'){
						listItems.add("Dvadesetdruga");
						i++;
					}
					else if (read_data.charAt(i+1) == '3'){
						listItems.add("Dvadesettreca");
						i++;
					}
					else if (read_data.charAt(i+1) == '4'){
						listItems.add("Dvadesetcetvrta");
						i++;
					}
					else if (read_data.charAt(i+1) == '5'){
						listItems.add("Dvadesetpeta");
						i++;
					}
					else if (read_data.charAt(i+1) == '6'){
						listItems.add("Dvadesetsesta");
						i++;
					}
					else if (read_data.charAt(i+1) == '7'){
						listItems.add("Dvadesetsedma");
						i++;
					}
					else if (read_data.charAt(i+1) == '8'){
						listItems.add("Dvadesetosma");
						i++;
					}
					else if (read_data.charAt(i+1) == '9'){
						listItems.add("Dvadesetdeveta");
						i++;
					}
					else if (read_data.charAt(i+1) == '0'){
						listItems.add("Dvadeseta");
						i++;
					}
					else{
						listItems.add("Druga");
					}
				}
				else if (read_data.charAt(i) == '3'){
					if (read_data.charAt(i+1) == '1'){
						listItems.add("Tridesetaprva");
						i++;
					}
					else if (read_data.charAt(i+1) == '2'){
						listItems.add("Tridesetdruga");
						i++;
					}
					else if (read_data.charAt(i+1) == '3'){
						listItems.add("Tridesettreca");
						i++;
					}
					else if (read_data.charAt(i+1) == '4'){
						listItems.add("Tridesetcetvrta");
						i++;
					}
					else if (read_data.charAt(i+1) == '5'){
						listItems.add("Tridesetpeta");
						i++;
					}
					else if (read_data.charAt(i+1) == '6'){
						listItems.add("Tridesetsesta");
						i++;
					}
					else if (read_data.charAt(i+1) == '7'){
						listItems.add("Tridesetsedma");
						i++;
					}
					else if (read_data.charAt(i+1) == '8'){
						listItems.add("Tridesetosma");
						i++;
					}
					else if (read_data.charAt(i+1) == '9'){
						listItems.add("Tridesetdeveta");
						i++;
					}
					else if (read_data.charAt(i+1) == '0'){
						listItems.add("Trideseta");
						i++;
					}
					else{
						listItems.add("Treca");
					}
				}
				else if (read_data.charAt(i) == '4'){
					listItems.add("Cetvrta");
				}
				else if (read_data.charAt(i) == '5'){
					listItems.add("Peta");
				}
				else if (read_data.charAt(i) == '6'){
					listItems.add("Sesta");
				}
				else if (read_data.charAt(i) == '7'){
					listItems.add("Sedma");
				}
				else if (read_data.charAt(i) == '8'){
					listItems.add("Osma");
				}
				else if (read_data.charAt(i) == '9'){
					listItems.add("Deveta");
				}
			}
			}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }  
    @Override
	public void onBackPressed(){}
}