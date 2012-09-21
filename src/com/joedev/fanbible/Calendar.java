package com.joedev.fanbible;

import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

public class Calendar extends Activity {
	private Events event = new Events();
	private String[] calNames;
	private int[] calIds;
	
	public void set_event(Events event){
		this.event = event;
	}
	
	public int[] get_cal_ids(){
		return this.calIds;
	}
	
	public String[] get_cal_names(){
		return this.calNames;
	}
	
    private Uri MakeNewCalendarEntry(int calId, int position) {
        ContentValues event = new ContentValues();

        event.put("calendar_id", calId);
        event.put("title", this.event.get_title());
        event.put("description", arrayList_csv(this.event.get_artists()));
        event.put("eventLocation", arrayList_csv(this.event.get_places()));

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
                    count++;
                }
                
                if (calName != null && calName.contains("Fanbible Test")) {
                    result = calId;
                }
            } while (managedCursor.moveToNext());
        } else {
        }
        return result;

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
    
    private Cursor getCalendarManagedCursor(String[] projection,
            String selection, String path) {
        Uri calendars = Uri.parse("content://calendar/" + path);

        Cursor managedCursor = null;
        try {
            managedCursor = managedQuery(calendars, projection, selection,
                    null, null);
        } catch (IllegalArgumentException e) {
        }

        if (managedCursor == null) {
            // try again
            calendars = Uri.parse("content://com.android.calendar/" + path);
            try {
                managedCursor = managedQuery(calendars, projection, selection,
                        null, null);
            } catch (IllegalArgumentException e) {
            }
        }
        return managedCursor;
    }
}
