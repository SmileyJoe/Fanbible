package com.joedev.fanbible;

import android.content.Context;
import android.content.SharedPreferences;

public class Preference {
	private static String PREFS_NAME;
	private Context context;
	
	public Preference(String prefsName, Context cont){
		PREFS_NAME = prefsName;
		this.context = cont;
	}
	
	public void save_preference(String key, int value){
		this.save_preference(key, Integer.toString(value));
    }
	
	public void save_preference(String key, String value){
		SharedPreferences prefs = this.context.getSharedPreferences(PREFS_NAME, 0);
    	SharedPreferences.Editor prefsEditor = prefs.edit();
    	prefsEditor.putString(key, value);
    	prefsEditor.commit();
    }
    
    public String get_preference(String key){
    	SharedPreferences prefs = this.context.getSharedPreferences(PREFS_NAME, 0);
    	return prefs.getString(key, "");
    }
    
    public void clear_pref(String key){
    	SharedPreferences prefs = this.context.getSharedPreferences(PREFS_NAME, 0);
    	SharedPreferences.Editor prefsEditor = prefs.edit();
    	prefsEditor.clear();
    	prefsEditor.commit();
    }
}
