package com.joedev.fanbible;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class SelectLocation extends Activity implements OnClickListener, OnItemClickListener {
	private String TAG = "fanbible";
	private Button btCountry;
	private Button btState;
	private Button btCity;
	private ListView lvCountry;
	private ListView lvState;
	private ListView lvCity;
	private Curl curl = new Curl();
	private ArrayList<Countries> countries = new ArrayList<Countries>();
	private ArrayList<States> states = new ArrayList<States>();
	private ArrayList<Cities> cities = new ArrayList<Cities>();
	private LocationRowAdapter countryAdapter;
	private StateRowAdapter stateAdapter;
	private CityRowAdapter cityAdapter;
	private ImageView ivLoaderImage;
	public  AnimationDrawable frameAnimation;
	public String countryCode;
	private int countryId;
	private Preference locationPrefs = new Preference("locationPrefs", this);
	private int stateId;
	private boolean mustReturn;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_location);
        this.initialize();
        
        if(this.locationPrefs.get_preference("city_id") == ""){
        	GetLocationsTask task = new GetLocationsTask(getApplicationContext());
            task.set_type(1);
    		task.execute(0);
        } else {
        	this.load_cities();
        }
        
        
		
		try{
			if((Boolean) getIntent().getSerializableExtra("return")){
	        	this.mustReturn = true;
	        }
		} catch(NullPointerException E){
			this.mustReturn = false;
		}
        
        this.populate_view();
    }
	
	public void load_cities(){
		this.countryCode = this.locationPrefs.get_preference("country_code");
		this.countryId = Integer.parseInt(this.locationPrefs.get_preference("country_id"));
		this.stateId = Integer.parseInt(this.locationPrefs.get_preference("state_id"));
		
		GetLocationsTask task = new GetLocationsTask(getApplicationContext());
		task = new GetLocationsTask(getApplicationContext());
		task.set_type(1);
		task.execute(0);
		
		task = new GetLocationsTask(getApplicationContext());
		task.set_country_code(this.locationPrefs.get_preference("country_code"));
		task.set_type(2);
		task.execute(0);
		
		task = new GetLocationsTask(getApplicationContext());
		task.set_country_code(this.locationPrefs.get_preference("country_code"));
    	task.set_county_id(Integer.parseInt(this.locationPrefs.get_preference("country_id")));
    	task.set_state_id(Integer.parseInt(this.locationPrefs.get_preference("state_id")));
        task.set_type(4);
		task.execute(0);
	}
	
	public void initialize(){
    	this.btCountry = (Button) findViewById(R.id.bt_country);
    	this.btCountry.setOnClickListener(this);
    	this.btState = (Button) findViewById(R.id.bt_state);
    	this.btState.setOnClickListener(this);
    	this.btCity = (Button) findViewById(R.id.bt_city);
    	this.btCity.setOnClickListener(this);
    	
    	this.lvCountry = (ListView)findViewById(R.id.lv_countries);
    	this.lvCountry.setOnItemClickListener(this);
    	this.lvState = (ListView)findViewById(R.id.lv_state);
    	this.lvState.setOnItemClickListener(this);
    	this.lvCity = (ListView)findViewById(R.id.lv_city);
    	this.lvCity.setOnItemClickListener(this);
    	
    	this.countryAdapter = new LocationRowAdapter(this, this.countries);
    	this.stateAdapter = new StateRowAdapter(this, this.states);
    	this.cityAdapter = new CityRowAdapter(this, this.cities);
    	
    	this.ivLoaderImage=(ImageView)findViewById(R.id.loader_image);
    	this.frameAnimation = (AnimationDrawable) this.ivLoaderImage.getBackground();
    	this.ivLoaderImage.post(new Starter());
	}
	
    public void populate_view(){
		int first = this.lvCountry.getFirstVisiblePosition();
		View top_child = lvCountry.getChildAt(0);
		int top;
		
		if(top_child == null){
			top = 0;
		} else {
			top = top_child.getTop();
		}
		this.countryAdapter = new LocationRowAdapter(this, this.countries);
		this.lvCountry.setAdapter(this.countryAdapter);
		
		this.stateAdapter = new StateRowAdapter(this, this.states);
		this.lvState.setAdapter(this.stateAdapter);
		
		this.cityAdapter = new CityRowAdapter(this, this.cities);
		this.lvCity.setAdapter(this.cityAdapter);
		
		/*View footerView = ((LayoutInflater)this.getSystemService(this.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.event_footer_view, null, false);
		eventList.addFooterView(footerView);*/
		this.lvCountry.setSelectionFromTop(first, top);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
			case R.id.bt_country:
			case R.id.bt_state:
			case R.id.bt_city:
				change_visibility(v);
				break;
		}
	}
	
	public void change_visibility(View v){
		switch(v.getId()){
			case R.id.bt_country:
				this.lvCountry.setVisibility(0);
				this.lvState.setVisibility(8);
				this.lvCity.setVisibility(8);
				break;
			case R.id.bt_state:
				this.lvCountry.setVisibility(8);
				this.lvState.setVisibility(0);
				this.lvCity.setVisibility(8);
				break;
			case R.id.bt_city:
				this.lvCountry.setVisibility(8);
				this.lvState.setVisibility(8);
				this.lvCity.setVisibility(0);
				break;
		}
	}
	
	public void show_toast(String message){
		Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
		toast.show();
	}

	@Override
	public void onItemClick(AdapterView<?> v, View arg1, int position, long arg3) {
		GetLocationsTask task = new GetLocationsTask(getApplicationContext());
		switch(v.getId()){
			case R.id.lv_countries:
				Log.v(TAG, this.countries.get(position).get_code());
				this.states.clear();
				
				this.countryCode = this.countries.get(position).get_code();
				this.countryId = this.countries.get(position).get_id();
				task.set_country_code(this.countryCode);
				
				//task.set_country_code(this.countryCode);
		        task.set_type(2);
				task.execute(0);
				//Log.v(TAG, "country Id:" + Integer.toString(task.get_country_id()));
				//this.btState.performClick();
				break;
			case R.id.lv_state:
				this.cities.clear();
				//task.set_country_code(this.countryCode);
		        task.set_type(4);
		        task.set_county_id(this.countryId);
		        task.set_state_id(this.states.get(position).get_id());
				task.execute(0);
				this.stateId = this.states.get(position).get_id();
				break;
			case R.id.lv_city:
				locationPrefs.save_preference("city_id", this.cities.get(position).get_id());
				locationPrefs.save_preference("city_name", this.cities.get(position).get_city());
				locationPrefs.save_preference("country_id", this.countryId);
				locationPrefs.save_preference("country_code", this.countryCode);
				locationPrefs.save_preference("state_id", this.stateId);
				
				if(this.mustReturn){
					Intent resultIntent = new Intent();
					resultIntent.putExtra("location_result", true);
					setResult(Activity.RESULT_OK, resultIntent);
				}
				
				finish();
				break;
		}
		
	}
	
	private class GetLocationsTask extends AsyncTask{
		private final String TAG = "fanbible";
		private boolean no_more = false;
		boolean success;
		private int countryId = 0;
		private int stateId = 0;
		private String countryCode = "";
		/*
		 * 1 countries
		 * 2 country data
		 * 3 states
		 * 4 cities
		 */
		private int type;
		
			public void set_type(int type){
				this.type = type;
			}
			
			public void set_country_code(String code){
				this.countryCode = code;
			}
			
			public void set_state_id(int id){
				this.stateId = id;
			}
			
			public int get_country_id(){
				return this.countryId;
			}
			
			public void set_county_id(int id){
				this.countryId = id;
			}
			
		    public GetLocationsTask(Context context){
		    	Log.v(TAG, "start");
		    }
		 
		    protected void onPreExecute(){
		    	SelectLocation.this.ivLoaderImage.setVisibility(View.VISIBLE);
		    	SelectLocation.this.lvCountry.setVisibility(View.GONE);
		    	SelectLocation.this.lvState.setVisibility(View.GONE);
		    	SelectLocation.this.lvCity.setVisibility(View.GONE);
		    	TextView tv_loading = (TextView) findViewById(R.id.tv_loading);
		    	tv_loading.setVisibility(View.VISIBLE);
		    	Log.v(TAG, "pre");
		    }
		    
		    @Override
		    protected void onPostExecute(Object updated)   {
		    	SelectLocation.this.ivLoaderImage.setVisibility(View.INVISIBLE);
		    	Log.v(TAG, "post size:" + Integer.toString(SelectLocation.this.countries.size()));
		    	Log.v(TAG, "post");
		    }
		    
		    @Override
		    protected void onProgressUpdate(Object... params){
				Log.v(TAG, "progressUpdate");
				TextView tv_loading = (TextView) findViewById(R.id.tv_loading);
				if(this.success){
					switch(this.type){
						case 1:
							tv_loading.setVisibility(View.GONE);
							if(!SelectLocation.this.mustReturn){
								SelectLocation.this.lvCountry.setVisibility(View.VISIBLE);
							}
							SelectLocation.this.countryAdapter.notifyDataSetChanged();
							break;
						case 2:
							//tv_loading.setVisibility(View.GONE);
							GetLocationsTask task = new GetLocationsTask(getApplicationContext());
							//task.set_country_code(this.countryCode);
					        task.set_type(3);
					        task.set_county_id(this.countryId);
							task.execute(0);
							SelectLocation.this.countryId = this.countryId;
							/*SelectLocation.this.lvCountry.setVisibility(View.VISIBLE);
							SelectLocation.this.countryAdapter.notifyDataSetChanged();*/
							break;
						case 3:
							tv_loading.setVisibility(View.GONE);
							if(!SelectLocation.this.mustReturn){
								SelectLocation.this.lvState.setVisibility(View.VISIBLE);
							}
							SelectLocation.this.stateAdapter.notifyDataSetChanged();
							//SelectLocation.this.countryAdapter.notifyDataSetChanged();
							break;
						case 4:
							tv_loading.setVisibility(View.GONE);
							SelectLocation.this.lvCity.setVisibility(View.VISIBLE);
							SelectLocation.this.cityAdapter.notifyDataSetChanged();
							break;
					}
					
					Log.v(TAG, "notify");
				} else {
					tv_loading.setText("An error occured.");
					
					SelectLocation.this.show_toast("An error occured please try again later");
				}
				
			}

			@Override
			protected Object doInBackground(Object... params) {
				String url = "";
				Log.v(TAG, "do");
				ArrayList<Countries> newCountries = new ArrayList<Countries>();
				ArrayList<States> newStates = new ArrayList<States>();
				ArrayList<Cities> newCities = new ArrayList<Cities>();
				
				switch(this.type){
					case 1:
						url = "http://www.fanbible.com/api/locations.php?type=country&countryCode";
						break;
					case 2:
						url = "http://www.fanbible.com/api/locations.php?type=country&country_code=" + this.countryCode;
						break;
					case 3:
						url = "http://www.fanbible.com/api/locations.php?type=state&country_id=" + this.countryId;
						break;
					case 4:
						url = "http://www.fanbible.com/api/locations.php?type=city&country_id=" + this.countryId + "&state_id=" + this.stateId;
						break;
				}
				this.success = curl.get(url);
				
				
				if(success){
					switch(this.type){
						case 1:
							newCountries = curl.countries_decode();
							
							for(int i = 0; i < newCountries.size(); i++){
								SelectLocation.this.countries.add(newCountries.get(i));
							}
							break;
						case 2:
							this.countryId = curl.country_data_decode();
							Log.v(TAG, "country Id:" + Integer.toString(this.countryId));
							break;
						case 3:
							newStates = curl.states_decode();
							
							for(int i = 0; i < newStates.size(); i++){
								SelectLocation.this.states.add(newStates.get(i));
							}
							break;
						case 4:
							newCities = curl.cities_decode();
							
							for(int i = 0; i < newCities.size(); i++){
								SelectLocation.this.cities.add(newCities.get(i));
							}
							break;
					}

					
					publishProgress();
					Log.v(TAG, "finished2");
					switch(this.type){
						case 1:
							return newCountries;
						case 2:
							return this.countryId;
						case 3:
							return null;
						case 4:
							return newCities;
					}
				} else {
					Log.v(TAG, "Something went wrong");
					publishProgress();
				}
				
				return null;
			}
			
	}
	
	class Starter implements Runnable {

        public void run() {
        	frameAnimation.start();    
        }
        

    }

	
}
