package com.joedev.fanbible;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;




public class StateRowAdapter extends ArrayAdapter<States> {
	private Context context;
    private ArrayList<States> states;
    private static final String TAG = "fanbible";

    public StateRowAdapter(Context context, ArrayList<States> states) {
    	super(context, R.layout.location_row, states);
        this.context = context;
        this.states = states;
        Log.v(TAG, "initial size: " + Integer.toString(states.size()));
    }
    
	@Override
	public int getCount() {
		return this.states.size();
	}

	@Override
	public States getItem(int position) {
		return this.states.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup viewGroup) {
		States state = this.states.get(position);
		if (convertView == null) {
			 LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	         convertView = inflater.inflate(R.layout.location_row, null);
        }
		
		TextView tv_location_title = (TextView) convertView.findViewById(R.id.tv_location_title);
		tv_location_title.setText(state.get_state());
		
		return convertView;
	}

}
