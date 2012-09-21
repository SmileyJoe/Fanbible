package com.joedev.fanbible;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class EventRowAdapter extends BaseAdapter{
	private Context context;
    private ArrayList<Events> events;
    private String TAG = "fanbible";

    public EventRowAdapter(Context context, ArrayList<Events> events) {
        this.context = context;
        this.events = events;
        Log.v(TAG, "initial size:" + Integer.toString(events.size()));
    }
    
    public View getView(int position, View convertView, ViewGroup viewGroup) {
    	Events event = events.get(position);
    	ArrayList<String> tempArray = new ArrayList<String>();
    	Log.v("fanbible", "rowAdapter");
    	Log.v("fanbible", Integer.toString(position));
    	
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.event_row, null);
        }
        /*TextView tv_name = (TextView) convertView.findViewById(R.id.tv_player_row_name);
        tv_name.setText(Integer.toString(player.get_order() + 1) 
        				+ ". " + player.get_name()
        				+ " : " + Integer.toString(player.get_score()));
        //tv_name.setTypeface(tf);*/
        
        TextView tv_title = (TextView) convertView.findViewById(R.id.tv_event_title);
        tv_title.setText(event.get_title());
        
        TextView tv_date = (TextView) convertView.findViewById(R.id.tv_event_date);
        tv_date.setText("Date: " + event.get_dateTime());
        
        TextView tv_location = (TextView) convertView.findViewById(R.id.tv_event_location);
        tv_location.setText("Location: " + event.get_location());
        
        TextView tv_places = (TextView) convertView.findViewById(R.id.tv_event_places);
        tv_places.setText("Venue: " + arrayList_csv(event.get_places()));
        
        TextView tv_artists = (TextView) convertView.findViewById(R.id.tv_event_artists);
        tv_artists.setText("Artists: " + arrayList_csv(event.get_artists()));
        
        TextView tv_date_short_month = (TextView) convertView.findViewById(R.id.tv_date_short_month);
        tv_date_short_month.setText(event.get_dateShortMonth());
        
        TextView tv_date_day = (TextView) convertView.findViewById(R.id.tv_date_day);
        tv_date_day.setText(event.get_dateDay());
        
        return convertView;
    }
    
    public String arrayList_csv(ArrayList<String> tempArray){
    	boolean first = true;
    	String tempText = "";
    	
    	for(int i=0;i<tempArray.size();i++){
        	if(first){
        		first = false;
        		tempText += tempArray.get(i);
        	} else {
        		tempText += ", " + tempArray.get(i);
        	}
        }
    	
    	return tempText;
    }
    
	@Override
	public int getCount() {
		Log.v(TAG, "size: " + Integer.toString(this.events.size()));
		return this.events.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return this.events.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
}
