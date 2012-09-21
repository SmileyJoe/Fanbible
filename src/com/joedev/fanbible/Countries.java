package com.joedev.fanbible;

public class Countries {
	private int id;
	private String code;
	private String country;
	
	public void set_id(int id){
		this.id = id;
	}
	
	public void set_code(String code){
		this.code = code;
	}
	
	public void set_country(String country){
		this.country = country;
	}
	
	public int get_id(){
		return this.id;
	}
	
	public String get_code(){
		return this.code;
	}
	
	public String get_country(){
		return this.country;
	}
}
