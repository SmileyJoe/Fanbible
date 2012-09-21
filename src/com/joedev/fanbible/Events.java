package com.joedev.fanbible;

import java.io.Serializable;
import java.util.ArrayList;

public class Events implements Serializable {
	private String title;
	private int id;
	private String dateTime;
	private String dateSmart;
	private int specialId;
	private String url;
	private ArrayList<String> places = new ArrayList<String>();
	private ArrayList<String> artists = new ArrayList<String>();
	private String location;
	private String fee;
	private String dateShortMonth;
	private String dateDay;
	
	/* **********
	 * SETTERS
	 ***********/
	public void set_title(String title){
		this.title = title;
	}
	
	public void set_dateTime(String dateTime){
		this.dateTime = dateTime;
	}
	
	public void set_dateSmart(String dateSmart){
		this.dateSmart = dateSmart;
	}
	
	public void set_url(String url){
		this.url = url;
	}
	
	public void set_location(String location){
		this.location = location;
	}
	
	public void set_id(int id){
		this.id = id;
	}
	
	public void set_specialId(int specialId){
		this.specialId = specialId;
	}
	
	public void set_place(String place){
		this.places.add(place);
	}
	
	public void set_artist(String artist){
		this.artists.add(artist);
	}
	
	public void set_fee(String fee){
		this.fee = fee;
	}
	
	public void set_dateShortMonth(String date){
		this.dateShortMonth = date;
	}
	
	public void set_dateDay(String date){
		this.dateDay = date;
	}
	
	/* *****
	 * GETS
	 ******/
	
	public String get_title(){
		return this.title;
	}
	
	public String get_dateTime(){
		return this.dateTime;
	}
	
	public String get_dateSmart(){
		return this.dateSmart;
	}
	
	public String get_url(){
		return this.url;
	}
	
	public String get_location(){
		return this.location;
	}
	
	public int get_id(){
		return this.id;
	}
	
	public int get_specialId(){
		return this.specialId;
	}
	
	public ArrayList<String> get_places(){
		return this.places;
	}
	
	public ArrayList<String> get_artists(){
		return this.artists;
	}
	
	public String get_fee(){
		return this.fee;
	}
	
	public String get_dateShortMonth(){
		return this.dateShortMonth;
	}
	
	public String get_dateDay(){
		return this.dateDay;
	}
	
}
