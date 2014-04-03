package com.example.MyDrive;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Chronometer.OnChronometerTickListener;
import android.widget.EditText;
import android.widget.Toast;

public class TimerActivity extends Activity implements LocationListener{
	  String ucenik = Global.ime;
	  private LocationManager locationManager;
	  private String provider;
	  int data_block = 100;
	  boolean necesRazbojnice=false;
	  double lat, lng;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timer);

        
        //deklaracija gumbi kao varijable
        final Button startGumb = (Button) findViewById(R.id.start);
        final Button greskaGumb = (Button) findViewById(R.id.greska);
        final Button stopGumb = (Button) findViewById(R.id.stop);
        final EditText greskaText = (EditText) findViewById(R.id.GreskaText);
        final Chronometer minute = (Chronometer) findViewById(R.id.minute);
        
        deleteFile("koordinate.txt");
        deleteFile("voznja.txt");
        deleteFile(ucenik);
        
        LocationManager service= (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean enabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER);
        
        //postavljanje varijabli (gumbi) kao vidljivih
        greskaText.setVisibility(View.GONE);
        startGumb.setVisibility(View.VISIBLE);
        greskaGumb.setVisibility(View.GONE);
        stopGumb.setVisibility(View.GONE);
        minute.setVisibility(View.GONE);
     // check if enabled and if not send user to the GSP settings
        // Better solution would be to display a dialog and suggesting to 
        // go to the settings
        if (!enabled) {
        //ak nije enablean GPS otvara settings
          Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
          startActivity(intent);
        } 
        
        // Get the location manager
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        // Define the criteria how to select the locatioin provider -> use
        // default
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        Location location = locationManager.getLastKnownLocation(provider);

        // Initialize the location fields
        if (location != null) {
          System.out.println("Provider " + provider + " has been selected.");
          onLocationChanged(location);
        } else {
          Toast.makeText(TimerActivity.this, "Lokacija nije dostupna.", Toast.LENGTH_LONG).show();
        }
        
        startGumb.setOnClickListener(new View.OnClickListener() {
			//reakcija na pritisak start gumba	
        	
			@Override
			public void onClick(View v) {
				InputMethodManager imm =(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		        imm.showSoftInput(greskaText, 0);
				// TODO Auto-generated method stub 
				necesRazbojnice=true;
				greskaText.setVisibility(View.VISIBLE);
				greskaGumb.setVisibility(View.VISIBLE);
				startGumb.setVisibility(View.GONE);
				stopGumb.setVisibility(View.VISIBLE);
				minute.setVisibility(View.VISIBLE);
				minute.setOnChronometerTickListener(new OnChronometerTickListener(){
			        @Override
			            public void onChronometerTick(Chronometer cArg) {
			            long time = SystemClock.elapsedRealtime() - cArg.getBase();
			            int h   = (int)(time/3600000);
			            int m = (int)(time - h*3600000)/60000;
			            int s= (int)(time - h*3600000- m*60000)/1000;
			            String mm = m < 10 ? "0"+m: m+"";
			            String ss = s < 10 ? "0"+s: s+"";
			            cArg.setText(h+":"+mm+":"+ss);
			        }
			    });
                minute.setBase(SystemClock.elapsedRealtime());
				minute.start();
		        
			}
		});
        
        greskaGumb.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(greskaText.getText().toString().length() == 0) {
					Toast.makeText(getApplicationContext(), "Broj greske nije upisan.", Toast.LENGTH_SHORT).show();
				}
				else if (Integer.parseInt(greskaText.getText().toString()) < 40 && Integer.parseInt(greskaText.getText().toString()) > 0){
				    Calendar a = Calendar.getInstance(TimeZone.getDefault());
				    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss",Locale.getDefault());
				    String strDate = sdf.format(a.getTime());
				try {
					FileOutputStream fou = openFileOutput("voznja.txt", MODE_APPEND);
					OutputStreamWriter osw = new OutputStreamWriter(fou);
					try {
						osw.write(greskaText.getText().toString());
						osw.write('\n');
						osw.write("" + lat);
						osw.write(" " + lng);
						osw.write('\n');
						osw.write(" " + strDate);
						osw.write('\n');
						osw.flush();
						osw.close();
						Toast.makeText(getApplicationContext(), "Greska broj "+greskaText.getText()+" je spremljena.", Toast.LENGTH_SHORT).show();
						greskaText.setText("");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
				e.printStackTrace();
				}
				}
				else{
					Toast.makeText(getApplicationContext(), "Upisali ste broj koji se ne koristi.", Toast.LENGTH_SHORT).show();
					greskaText.setText("");
				}
			}
		});
		
        
        stopGumb.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				minute.stop();
				greskaText.setVisibility(View.GONE);
				greskaGumb.setVisibility(View.GONE);
				stopGumb.setVisibility(View.GONE);
				startGumb.setVisibility(View.GONE);
				minute.setVisibility(View.GONE);
		        InputMethodManager imm =(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		        imm.hideSoftInputFromWindow(greskaText.getWindowToken(), 0);
	        		try {
						FileInputStream fis = openFileInput("voznja.txt");
						InputStreamReader isr = new InputStreamReader(fis);
						char[] data = new char[data_block];
						String final_data = "";
						int size;	
						String temp_data ="";
						int brznak=0;
							try {		
								while((size = isr.read(data)) > 0)
								{
									String read_data = String.copyValueOf(data, 0, size);
									temp_data += read_data;
									data = new char[data_block];
									brznak+=size;
								}
								int br=0;
								for(int i=0;i<brznak;i++){
									if(temp_data.charAt(i) == '\n'){
										br++;
										}
										if(br % 3 == 0 ){
								final_data += temp_data.charAt(i);
								}
							}
							try {
								FileOutputStream abc = openFileOutput(ucenik, MODE_APPEND);
								OutputStreamWriter kraj = new OutputStreamWriter(abc);
								try {
									kraj.write(final_data);
									kraj.write('\n');
									kraj.flush();
									kraj.close();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
							} catch (FileNotFoundException e) {
								// TODO Auto-generated catch block
							e.printStackTrace();
							}
							
							
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}	
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
						Intent lista = new Intent(TimerActivity.this, PrikazGreskiActivity.class);
						startActivity(lista);
						finish();
			}
		});
		
    }


    @Override
    protected void onResume() {
    super.onResume();
    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 1, this);
    }
    @Override
    protected void onPause() {
    super.onPause();
    locationManager.removeUpdates(this);
    }
    @Override
    public void onLocationChanged(Location location) {
	    //tu dobiva koordinate i ispisuje ih
	    if(necesRazbojnice==true){
	    lat = (double)(location.getLatitude());
	    lng = (double)(location.getLongitude());
	    Calendar c = Calendar.getInstance(TimeZone.getDefault());
	    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss",Locale.getDefault());
	    String Date = sdf.format(c.getTime());
	    try {
		    FileOutputStream fou = openFileOutput("koordinate.txt", MODE_APPEND);
		    OutputStreamWriter osw = new OutputStreamWriter(fou);
		    try {
			    osw.write("" + Date);
			    osw.write(" " + lat);
			    osw.write(" " + lng);
			    osw.write('\n');
			    osw.flush();
			    osw.close();
			    Toast.makeText(TimerActivity.this, "Koordinata spremljena", Toast.LENGTH_SHORT).show();
		    } catch (IOException e) {
			    // TODO Auto-generated catch block
			    e.printStackTrace();
		    }
		    } catch (FileNotFoundException e) {
		    	// TODO Auto-generated catch block
		    	e.printStackTrace();
		    }
	    }
    }
    @Override
    public void onProviderDisabled(String provider) {
	    // TODO Auto-generated method stub
	    Toast.makeText(this, "Disabled provider " + provider,
	    Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onProviderEnabled(String provider) {
	    // TODO Auto-generated method stub
	    Toast.makeText(this, "Enabled new provider " + provider,
	    Toast.LENGTH_SHORT).show();
    }
    @Override
	    public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
	    // TODO Auto-generated method stub
    }
	@Override
	public void onBackPressed(){
		if(!necesRazbojnice){
			startActivity (new Intent(TimerActivity.this, ChoosePeerActivity.class));
			finish();
		}
	}
		
}