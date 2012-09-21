package com.joedev.fanbible;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;




public class CityRowAdapter extends ArrayAdapter<Cities> {
	private Context context;
    private ArrayList<Cities> cities;
    private static final String TAG = "fanbible";

    public CityRowAdapter(Context context, ArrayList<Cities> cities) {
    	super(context, R.layout.location_row, cities);
        this.context = context;
        this.cities = cities;
    }
    
	@Override
	public int getCount() {
		return this.cities.size();
	}

	@Override
	public Cities getItem(int position) {
		return this.cities.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup viewGroup) {
		Cities city = this.cities.get(position);
		if (convertView == null) {
			 LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	         convertView = inflater.inflate(R.layout.location_row, null);
        }
		
		TextView tv_location_title = (TextView) convertView.findViewById(R.id.tv_location_title);
		tv_location_title.setText(city.get_city());
		
		return convertView;
	}

}
