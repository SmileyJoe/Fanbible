package com.joedev.fanbible;

import java.util.ArrayList;

public class Locations {
	public ArrayList<Countries> countries = new ArrayList<Countries>();
	
	public void set_country(Countries country){
		this.countries.add(country);
	}
}
