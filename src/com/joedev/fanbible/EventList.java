package com.joedev.fanbible;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class EventList extends Activity implements OnItemClickListener, OnItemSelectedListener, OnLongClickListener, OnClickListener, OnScrollListener {
    /** Called when the activity is first created. */
	private static final String TAG = "fanbible";
	private static final int LOCATION_ACTIVITY = 1;
	private ListView eventList;
	protected ArrayList<Events> eventsList;
	private Spinner spRegion;
	private Spinner spCal;
	private Button btRegion;
	private TextView tvHeaderText;
	private ArrayList<String> regions;
	private Curl curl = new Curl();
	private boolean loading = false;
	private EventRowAdapter eventAdapter;
	private int eventPage = 1;
	private ImageView ivLoaderImage;
	public  AnimationDrawable frameAnimation;
	private boolean show_more_events = true;
	private int selectedEvent = -1;
	private String[] calNames;
	private int[] calIds;
	private Preference locationPrefs = new Preference("locationPrefs", this);
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_list);
        this.initialize();
        
        GetEventsTask task = new GetEventsTask(getApplicationContext());
		task.execute(0);
        //curl.get("http://www.fanbible.com/api/events.php");
        //this.eventsList = curl.decode();
        //this.eventAdapter.notifyDataSetChanged();
        this.populate_view();
        this.populate_sp_region();
        int iTestCalendarID = ListSelectedCalendars();
	  	this.populate_sp_cal();
        //this.populate_sp_cal();
        
    }
    
    private boolean add_to_calender(int position){
    	 // will return the last found calendar with "Test" in the name
        int iTestCalendarID = ListSelectedCalendars();

        // change this when you know which calendar you want to use
        // If you create a new calendar, you may need to manually sync the
        // phone first

        //Uri newEvent = MakeNewCalendarEntry(iTestCalendarID, position);
        
        return true;
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
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
	  if (v.getId()==R.id.lv_events) {
	    AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
	    this.selectedEvent = info.position;
	    menu.setHeaderTitle(this.getString(R.string.event_row_menu_heading));
	    menu.add(Menu.NONE, 0, 0, this.getString(R.string.event_row_menu_option_1));
	    menu.add(Menu.NONE, 1, 1, this.getString(R.string.event_row_menu_option_2));
	    menu.add(Menu.NONE, 2, 2, this.getString(R.string.event_row_menu_option_3));
	  }
	}
    
    public void populate_sp_cal(){
        ArrayAdapter <CharSequence> adapter = new ArrayAdapter <CharSequence> (this, android.R.layout.simple_spinner_item );
    	adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	int length = this.calNames.length;
    	Log.v(TAG, Integer.toString(length));
    	
    	for(int i = 0; i < length-1; i++){
    		adapter.add(this.calNames[i]);
    		Log.v(TAG, this.calNames[i]);
    	}
    	
    	this.spCal.setAdapter(adapter);
	}
    
    public boolean onContextItemSelected(MenuItem item) {
  	  AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
  	  int menuItemIndex = item.getItemId();
  	  switch(menuItemIndex){
  	  	case 0:
  	  		Log.v(TAG, "Add to calender");
  	  		this.spCal.performClick();
  	  		break;
  	  	case 1:
  	  		this.open_link("www.fanbible.com/" + this.eventsList.get(this.selectedEvent).get_url());
  	  		break;
  	  	case 2:
  	  		this.share(this.getString(R.string.share_text) + this.eventsList.get(this.selectedEvent).get_url());
  	  		break;
  	  }
  	  return true;
  	}
    
    public void share(String Text){
    	Intent sendIntent = new Intent(android.content.Intent.ACTION_SEND);
    	sendIntent.setType("text/plain");
    	sendIntent.putExtra(Intent.EXTRA_TEXT, Text);
    	startActivity(Intent.createChooser(sendIntent,this.getString(R.string.share_heading_text)));
    }
    
    public void open_link(String link){
    	if (!link.startsWith("http://") && !link.startsWith("https://")){
    		link = "http://" + link;
    	}
    		
    	Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
    	startActivity(browserIntent);
    }
    
    private Uri MakeNewCalendarEntry(int calId, int position) {
        ContentValues event = new ContentValues();

        event.put("calendar_id", calId);
        event.put("title", this.eventsList.get(position).get_title());
        event.put("description", arrayList_csv(this.eventsList.get(position).get_artists()));
        event.put("eventLocation", arrayList_csv(this.eventsList.get(position).get_places()));

        long startTime = System.currentTimeMillis() + 1000 * 60 * 60;
        long endTime = System.currentTimeMillis() + 1000 * 60 * 60 * 2;

        event.put("dtstart", startTime);
        event.put("dtend", endTime);

        event.put("allDay", 0); // 0 for false, 1 for true
        event.put("eventStatus", 1);
        event.put("visibility", 0);
        event.put("transparency", 0);
        event.put("hasAlarm", 0); // 0 for false, 1 for true

        Uri eventsUri = Uri.parse(getCalendarUriBase()+"events");

        Uri insertedUri = getContentResolver().insert(eventsUri, event);
        return insertedUri;
    }
    
    private String getCalendarUriBase() {
        String calendarUriBase = null;
        Uri calendars = Uri.parse("content://calendar/calendars");
        Cursor managedCursor = null;
        try {
            managedCursor = managedQuery(calendars, null, null, null, null);
        } catch (Exception e) {
            // eat
        }

        if (managedCursor != null) {
            calendarUriBase = "content://calendar/";
        } else {
            calendars = Uri.parse("content://com.android.calendar/calendars");
            try {
                managedCursor = managedQuery(calendars, null, null, null, null);
            } catch (Exception e) {
                // eat
            }

            if (managedCursor != null) {
                calendarUriBase = "content://com.android.calendar/";
            }

        }

        return calendarUriBase;
    }
    
    private int ListSelectedCalendars() {
    	int result = 0;
    	int count = 0;
        String[] projection = new String[] { "_id", "name" };
        String selection = "selected=1";
        String path = "calendars";

        Cursor managedCursor = getCalendarManagedCursor(projection, selection,
                path);

        if (managedCursor != null && managedCursor.moveToFirst()) {

            Log.i(TAG, "Listing Selected Calendars Only");

            int nameColumn = managedCursor.getColumnIndex("name");
            int idColumn = managedCursor.getColumnIndex("_id");
            
        	this.calNames = new String[managedCursor.getCount()];
        	this.calIds = new int[managedCursor.getCount()];
        	
            do {
                String calName = managedCursor.getString(nameColumn);
                int calId = Integer.parseInt(managedCursor.getString(idColumn));
                
                if(calName != null && calName != ""){
                	this.calNames[count] = calName;
                    this.calIds[count] = calId;
                    Log.i(TAG, calName);
                    count++;
                }
                
                Log.i(TAG, "Found Calendar '" + calName + "' (ID="
                        + calId + ")");
                if (calName != null && calName.contains("Fanbible Test")) {
                    result = calId;
                }
            } while (managedCursor.moveToNext());
        } else {
            Log.i(TAG, "No Calendars");
        }
        Log.i(TAG, Integer.toString(result));
        return result;

    }

    
    private Cursor getCalendarManagedCursor(String[] projection,
            String selection, String path) {
        Uri calendars = Uri.parse("content://calendar/" + path);

        Cursor managedCursor = null;
        try {
            managedCursor = managedQuery(calendars, projection, selection,
                    null, null);
        } catch (IllegalArgumentException e) {
            Log.w(TAG, "Failed to get provider at ["
                    + calendars.toString() + "]");
        }

        if (managedCursor == null) {
            // try again
            calendars = Uri.parse("content://com.android.calendar/" + path);
            try {
                managedCursor = managedQuery(calendars, projection, selection,
                        null, null);
            } catch (IllegalArgumentException e) {
                Log.w(TAG, "Failed to get provider at ["
                        + calendars.toString() + "]");
            }
        }
        return managedCursor;
    }
    
    private void initialize(){
    	this.eventList=(ListView)findViewById(R.id.lv_events);
        this.eventList.setOnItemClickListener(this);
        this.eventList.setOnScrollListener(this);
        registerForContextMenu(this.eventList);
        
		LayoutInflater inflater = this.getLayoutInflater();
	    View footerView = inflater.inflate(R.layout.event_footer_view, null);

	    this.eventList.addFooterView(footerView);
	    
        this.eventsList = new ArrayList<Events>();
        this.spRegion = (Spinner) findViewById(R.id.sp_region);
    	this.spRegion.setOnItemSelectedListener(this);
    	this.spCal = (Spinner) findViewById(R.id.sp_cal);
    	this.spCal.setOnItemSelectedListener(this);
    	this.btRegion = (Button) findViewById(R.id.bt_region);
    	this.btRegion.setOnClickListener(this);
    	
    	this.tvHeaderText = (TextView) findViewById(R.id.tv_header_text);
    	
    	this.regions = new ArrayList<String>();
    	this.regions.add(this.locationPrefs.get_preference("city_name"));
    	this.calNames = new String[2];
    	this.calNames[0] = "test";
    	this.calNames[1] = "test2";
    	this.eventAdapter = new EventRowAdapter(this, this.eventsList);
    	
    	this.ivLoaderImage=(ImageView)findViewById(R.id.loader_image);
    	//this.ivLoaderImage.setBackgroundResource(R.drawable.loader);
    	this.frameAnimation = (AnimationDrawable) this.ivLoaderImage.getBackground();
    	this.ivLoaderImage.post(new Starter());
    	
    	

    	 // Start the animation (looped playback by default).
    	 
    }
    
    public void populate_sp_region(){
        ArrayAdapter <CharSequence> adapter = new ArrayAdapter <CharSequence> (this, android.R.layout.simple_spinner_item );
    	adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	
    	for(int i = 0; i < this.regions.size(); i++){
    		adapter.add(this.regions.get(i));
    	}
    	
    	this.spRegion.setAdapter(adapter);
	}
    
    public void populate_view(){
		int first = this.eventList.getFirstVisiblePosition();
		View top_child = eventList.getChildAt(0);
		int top;
		
		if(top_child == null){
			top = 0;
		} else {
			top = top_child.getTop();
		}
		this.eventAdapter = new EventRowAdapter(this, this.eventsList);
        // By using setAdpater method in listview we an add string array in list.
		eventList.setAdapter(this.eventAdapter);
		/*View footerView = ((LayoutInflater)this.getSystemService(this.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.event_footer_view, null, false);
		eventList.addFooterView(footerView);*/
		eventList.setSelectionFromTop(first, top);
	}
    
    public static InputStream getInputStreamFromUrl(String url) {
    	  InputStream content = null;
    	  try {
    	    HttpClient httpclient = new DefaultHttpClient();
    	    HttpResponse response = httpclient.execute(new HttpGet(url));
    	    content = response.getEntity().getContent();
    	  } catch (Exception e) {
    		  Log.v(TAG, e.toString());
    	    //Log.("[GET REQUEST]", "Network exception", e);
    	  }
    	    return content;
    	}
    	// see http://androidsnippets.com/executing-a-http-get-request-with-httpclient
    
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

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		Intent eventViewActivity = new Intent(this, EventView.class);
		eventViewActivity.putExtra("event",this.eventsList.get(position));
  		startActivity(eventViewActivity);
		Log.v(TAG, this.eventsList.get(position).get_title());
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		switch(parent.getId()){
			case R.id.sp_region:
				this.tvHeaderText.setText(this.regions.get(arg2));
				//this.btRegion.setText(this.regions.get(arg2));
				break;
			case R.id.sp_cal:
				if(this.selectedEvent > 0){
					Log.v(TAG, Integer.toString(arg2));
					Log.v(TAG, Integer.toString(this.calIds[arg2]));
					Uri newEvent = MakeNewCalendarEntry(this.calIds[arg2], this.selectedEvent);
					this.show_toast("Event added to calender");
				}
				break;
		}
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
			case R.id.bt_region:
				Intent selectLocationActivity = new Intent(this, SelectLocation.class);
				selectLocationActivity.putExtra("return",true);
				startActivityForResult(selectLocationActivity, LOCATION_ACTIVITY);
		  		//startActivity(selectLocationActivity);
				//this.spRegion.performClick();
				break;
		}
		
	}
	
	 public void onActivityResult(int requestCode, int resultCode, Intent data) {     
    	super.onActivityResult(requestCode, resultCode, data); 
	    	switch(requestCode) { 
		    	case LOCATION_ACTIVITY:
		    		this.eventList.setVisibility(View.GONE);
		    		TextView tv_loading = (TextView) findViewById(R.id.tv_loading);
					tv_loading.setVisibility(View.VISIBLE);
		    		this.eventsList.clear();
		    		this.eventPage = 1;
		            GetEventsTask task = new GetEventsTask(getApplicationContext());
		    		task.execute(0);
		    		this.tvHeaderText.setText(this.locationPrefs.get_preference("city_name"));
		    		//this.btRegion.setText(this.locationPrefs.get_preference("city_name"));
		    		break;
	    	}
	 }

	@Override
	public void onScroll(AbsListView arg0, int position, int arg2, int arg3) {
		// TODO Auto-generated method stub
		//int multiply = this.eventPage - 1;
		
		if(this.eventPage > 1 && this.show_more_events){
			if(loading && (position < this.eventsList.size() - 16)){
				//this.loading = false;
			}
			
			if((position > this.eventsList.size() - 16) && (!loading)){
				//this.loading = true;
				Log.v(TAG, "loading more");
				/*Log.v(TAG, Integer.toString(position));
				curl.get("http://www.fanbible.com/api/events.php?p=" + this.eventPage);
				newEvents = curl.decode();
				
				for(int i = 0; i < newEvents.size(); i++){
					this.eventsList.add(newEvents.get(i));
				}*/
				
				GetEventsTask task = new GetEventsTask(getApplicationContext());
				task.execute(0);
				//get_result(task);
				Log.v(TAG, "finished");
				//this.eventPage++;
				//this.eventsList.notifyAll();
				//this.eventAdapter.notifyDataSetChanged();
		        //this.populate_view();
			}
		}
		
	}
	
	@Override
	public void onScrollStateChanged(AbsListView arg0, int arg1) {
		
	}
	
	public void notifyChange(){
		this.eventAdapter.notifyDataSetChanged();
	}
	
	public void show_toast(String message){
		Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
		toast.show();
	}
	
	private class GetEventsTask extends AsyncTask{
		private final String TAG = "fanbible";
		private boolean no_more = false;
		boolean success;
		    public GetEventsTask(Context context){
		    	Log.v(TAG, "start");
		    }
		 
		    protected void onPreExecute(){
		    	EventList.this.loading = true;
		    	EventList.this.ivLoaderImage.setVisibility(View.VISIBLE);
		    	Log.v(TAG, "pre");
		    }
		    
		    @Override
		    protected void onPostExecute(Object updated)   {
		    	EventList.this.loading = false;
		    	EventList.this.ivLoaderImage.setVisibility(View.INVISIBLE);
		    	if(this.success && !this.no_more){
		    		EventList.this.eventPage++;
		    	} else {
		    		EventList.this.show_more_events = false;
		    	}
		    	Log.v(TAG, "post");
		    }
		    
		    @Override
		    protected void onProgressUpdate(Object... params){
				Log.v(TAG, "progressUpdate");
				
				if(this.success){
					if(EventList.this.eventPage == 1){
						TextView tv_loading = (TextView) findViewById(R.id.tv_loading);
						tv_loading.setVisibility(View.GONE);
						EventList.this.eventList.setVisibility(View.VISIBLE);
					}
					
					EventList.this.eventAdapter.notifyDataSetChanged();
					Log.v(TAG, "Success" + Integer.toString(EventList.this.eventPage));
					//
					Log.v(TAG, "Success" + Integer.toString(EventList.this.eventPage));
					if(this.no_more){
						TextView tv_event_footer = (TextView) findViewById(R.id.tv_event_footer);
						if(EventList.this.eventsList.isEmpty()){
							tv_event_footer.setText(EventList.this.getString(R.string.no_events_list_footer));
							EventList.this.show_toast(EventList.this.getString(R.string.no_events_list_city));
						} else {
							tv_event_footer.setText(EventList.this.getString(R.string.all_events_list_footer));
						}
						
					}
				} else {
					if(EventList.this.eventPage == 1){
						TextView tv_loading = (TextView) findViewById(R.id.tv_loading);
						tv_loading.setText(EventList.this.getString(R.string.general_error));
					} else {
						TextView tv_event_footer = (TextView) findViewById(R.id.tv_event_footer);
						tv_event_footer.setText(EventList.this.getString(R.string.general_error));
					}
					
					EventList.this.show_toast(EventList.this.getString(R.string.general_error));
				}
				
			}

			@Override
			protected Object doInBackground(Object... params) {
				Log.v(TAG, "do");
				ArrayList<Events> newEvents = new ArrayList<Events>();
				Log.v(TAG, "Get" + Integer.toString(EventList.this.eventPage));
				if(EventList.this.eventPage == 1){
					this.success = curl.get("http://www.fanbible.com/api/events.php?p=" + EventList.this.eventPage + "&city=" + EventList.this.locationPrefs.get_preference("city_id"));
				} else {
					this.success = curl.get("http://www.fanbible.com/api/events.php?p=" + EventList.this.eventPage + "&city=" + EventList.this.locationPrefs.get_preference("city_id"));
				}
				
				
				if(success){
					newEvents = curl.events_decode();
					
					if(newEvents.size() < 20){
						this.no_more = true;
					}
					
					for(int i = 0; i < newEvents.size(); i++){
						EventList.this.eventsList.add(newEvents.get(i));
					}
					
					publishProgress();
					Log.v(TAG, "finished2");
					return newEvents;
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

	@Override
	public boolean onLongClick(View v) {
		return false;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.event_list, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case R.id.refresh:  
	        	this.eventPage = 1;
	        	this.eventsList.clear();
	        	this.eventList.setVisibility(View.GONE);
	        	TextView tv_loading = (TextView) findViewById(R.id.tv_loading);
				tv_loading.setVisibility(View.VISIBLE);
	        	GetEventsTask task = new GetEventsTask(getApplicationContext());
				task.execute(0);
	        	break;
	    }
	    return true;
	}

	
}

