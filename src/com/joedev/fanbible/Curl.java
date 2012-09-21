package com.joedev.fanbible;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class Curl {
	private static final String TAG = "fanbible";
	private String contents;
	
	public boolean get(String url){
		InputStream response = getInputStreamFromUrl(url);
		
		try {
			this.contents = convertStreamToString(response);
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	public ArrayList<Countries> countries_decode(){
		ArrayList<Countries> countries = new ArrayList<Countries>();
		JSONObject contentsObj = null;
		JSONObject countriesObj = null;
		JSONObject countryObj = null;
		
		try {
			contentsObj = new JSONObject(this.contents);
			countriesObj = contentsObj.getJSONObject("country");
			JSONArray names = countriesObj.names();
			
			for(int i = 1; i <= names.length(); i++){
				Countries country = new Countries();
				countryObj = countriesObj.getJSONObject(Integer.toString(i));
				country.set_id(Integer.parseInt(countryObj.getString("country_id")));
				country.set_code(countryObj.getString("country_code"));
				country.set_country(countryObj.getString("country_country"));
				countries.add(country);
			}
		} catch (JSONException e) {
			Log.v(TAG, e.toString());
		}
		
		return countries;
	}
	
	public ArrayList<States> states_decode(){
		ArrayList<States> states = new ArrayList<States>();
		JSONObject contentsObj = null;
		JSONObject statesObj = null;
		JSONObject stateObj = null;
		
		try {
			contentsObj = new JSONObject(this.contents);
			statesObj = contentsObj.getJSONObject("state");
			JSONArray names = statesObj.names();
			
			for(int i = 1; i <= names.length(); i++){
				States state = new States();
				stateObj = statesObj.getJSONObject(Integer.toString(i));
				state.set_id(Integer.parseInt(stateObj.getString("state_id")));
				state.set_state(stateObj.getString("state_state"));
				states.add(state);
			}
		} catch (JSONException e) {
			Log.v(TAG, e.toString());
		}
		
		return states;
	}
	
	public ArrayList<Cities> cities_decode(){
		ArrayList<Cities> cities = new ArrayList<Cities>();
		JSONObject contentsObj = null;
		JSONObject citiesObj = null;
		JSONObject cityObj = null;
		
		try {
			contentsObj = new JSONObject(this.contents);
			citiesObj = contentsObj.getJSONObject("city");
			JSONArray names = citiesObj.names();
			
			for(int i = 1; i <= names.length(); i++){
				Cities city = new Cities();
				cityObj = citiesObj.getJSONObject(Integer.toString(i));
				city.set_id(Integer.parseInt(cityObj.getString("city_id")));
				city.set_city(cityObj.getString("city_city"));
				cities.add(city);
			}
		} catch (JSONException e) {
			Log.v(TAG, e.toString());
		}
		
		return cities;
	}
	
	public int country_data_decode(){
		JSONObject contentsObj = null;
		JSONObject countriesObj = null;
		JSONObject countryObj = null;
		int countryId = 0;
		
		try {
			contentsObj = new JSONObject(this.contents);
			countriesObj = contentsObj.getJSONObject("country");
			JSONArray names = countriesObj.names();
			
			for(int i = 1; i <= names.length(); i++){
				countryObj = countriesObj.getJSONObject(Integer.toString(i));
				countryId = Integer.parseInt(countryObj.getString("country_id"));
			}
		} catch (JSONException e) {
			Log.v(TAG, e.toString());
		}
		
		return countryId;
	}
	
	public ArrayList<Events> events_decode(){
		JSONObject contentsObj = null;
		JSONObject eventsObj = null;
		JSONObject eventObj = null;
		JSONObject placesObj = null;
		JSONObject artistsObj = null;
		ArrayList<Events> eventsList = new ArrayList<Events>();
		
		try {
			Log.v("fanbible", this.contents);
			contentsObj = new JSONObject(this.contents);
			Log.v("fanbible", contentsObj.toString());
			eventsObj = contentsObj.getJSONObject("events");
			JSONArray names = eventsObj.names();

			for(int i = 1; i <= names.length(); i++){
				Events event = new Events();
				eventObj = eventsObj.getJSONObject(Integer.toString(i));
				placesObj = eventObj.getJSONObject("places");
				JSONArray placesNames = placesObj.names();
				
				for(int j = 0; j < placesNames.length(); j++){
					event.set_place(placesObj.getString(placesNames.getString(j)));
				}
				
				artistsObj = eventObj.getJSONObject("artists");
				JSONArray artistsNames = artistsObj.names();
				
				for(int j = 0; j < artistsNames.length(); j++){
					event.set_artist(artistsObj.getString(artistsNames.getString(j)));
				}
				
				Log.v("fanbible", eventObj.getString("event.id"));
				event.set_id(Integer.parseInt(eventObj.getString("event.id")));
				event.set_dateSmart(eventObj.getString("event.date.smart"));
				event.set_dateTime(eventObj.getString("event.datetime"));
				event.set_location(eventObj.getString("this.location"));
				event.set_specialId(Integer.parseInt(eventObj.getString("event.special.id")));
				event.set_title(eventObj.getString("event.title"));
				event.set_url(eventObj.getString("event.url"));
				event.set_fee(eventObj.getString("event.fee"));
				event.set_dateShortMonth(eventObj.getString("event.date.month"));
				event.set_dateDay(eventObj.getString("event.date.day"));
				
				eventsList.add(event);
			}
		} catch (JSONException e) {
			Log.v(TAG, e.toString());
		}
		return eventsList;
	}
		
	public static InputStream getInputStreamFromUrl(String url) {
		InputStream content = null;
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse response = httpclient.execute(new HttpGet(url));
			content = response.getEntity().getContent();
		} catch (Exception e) {
			Log.v(TAG, e.toString());
		}
		return content;
	}
	  
	public static String convertStreamToString(InputStream is) throws Exception {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;
		
		while ((line = reader.readLine()) != null) {
			sb.append(line);
		}
		
		is.close();
		
		return sb.toString();
	}
}