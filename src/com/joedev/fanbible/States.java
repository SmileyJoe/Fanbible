package com.joedev.fanbible;

public class States {
	private int id;
	private String state;
	
	public void set_id(int id){
		this.id = id;
	}
	
	public void set_state(String state){
		this.state = state;
	}
	
	public int get_id(){
		return this.id;
	}
	
	public String get_state(){
		return this.state;
	}
}
