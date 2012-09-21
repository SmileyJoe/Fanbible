package com.joedev.fanbible;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class Fanbible extends Activity implements OnClickListener {
	private Button btEventList;
	private Button btSelectLocation;
	private Preference locationPrefs = new Preference("locationPrefs", this);
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        if(this.locationPrefs.get_preference("city_id") == ""){
        	this.show_toast("Please select a location before you start");
        	Intent selectLocationActivity = new Intent(this, SelectLocation.class);
      		startActivity(selectLocationActivity);
        }
        //Intent selectLocationActivity = new Intent(this, SelectLocation.class);
  		//startActivity(selectLocationActivity);
  		
        this.initialize();
        
    }
	
	private void initialize(){
    	this.btEventList = (Button) findViewById(R.id.bt_event_list);
    	this.btEventList.setOnClickListener(this);
    	this.btSelectLocation = (Button) findViewById(R.id.bt_select_location);
    	this.btSelectLocation.setOnClickListener(this);
    }
	
	public void show_toast(String message){
		Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
		toast.show();
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
			case R.id.bt_event_list:
				Intent eventListActivity = new Intent(this, EventList.class);
		  		startActivity(eventListActivity);
				break;
			case R.id.bt_select_location:
				Intent selectLocationActivity = new Intent(this, SelectLocation.class);
		  		startActivity(selectLocationActivity);
				break;
		}
	}
	
	public void onBackPressed(){
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Are you sure you want to exit?")
		       .setCancelable(false)
		       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   finish();
		           }
		       })
		       .setNegativeButton("No", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		                dialog.cancel();
		           }
		       });
		AlertDialog alert = builder.create();
		alert.show();
    }
}

