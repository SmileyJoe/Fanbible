package com.joedev.fanbible;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TableRow.LayoutParams;
import android.widget.Toast;

public class EventView extends Activity implements OnScrollListener, OnClickListener {
	Events event = new Events();
	private static final String TAG = "fanbible";
	private ImageView iv_favourite_star_blank;
	private ImageView iv_favourite_star_full;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.event_view);
		this.event = (Events) getIntent().getSerializableExtra("event");
		this.iv_favourite_star_blank = (ImageView) findViewById(R.id.iv_favourite_star_blank);
		this.iv_favourite_star_blank.setOnClickListener(this);
		this.iv_favourite_star_full = (ImageView) findViewById(R.id.iv_favourite_star_full);
		this.iv_favourite_star_full.setOnClickListener(this);
		TableLayout tl_details;
		
		tl_details = (TableLayout)findViewById(R.id.tl_details);
		
		TextView tv_event_title = new TextView(this);
		tv_event_title = (TextView)findViewById(R.id.tv_event_title);
		tv_event_title.setText(this.event.get_title());
		
        // add the row to the table //
    	//tl_details.addView(this.add_row("Title: ", this.event.get_title()));
		tl_details.addView(this.add_row("Title: ", this.event.get_title(), 0));
    	tl_details.addView(this.add_row("Location: ", this.event.get_location(), 1));
    	tl_details.addView(this.add_row("Artists: ", arrayList_text(this.event.get_artists()), 2));
    	tl_details.addView(this.add_row("Date: ", this.event.get_dateTime() + "\n(" + this.event.get_dateSmart() + ")", 3));
    	tl_details.addView(this.add_row("Place: ", arrayList_text(this.event.get_places()), 4));
    	tl_details.addView(this.add_row("Weblink: ", "http://www.fanbible.com/" + this.event.get_url(), 5));
    	if(this.event.get_fee().compareTo("") != 0){
    		tl_details.addView(this.add_row("Fee: ", this.event.get_fee(), 6));
    	}
    	
	}
	
	public TableRow add_row(String title, String content, int id){
		TableRow tr = new TableRow(this);
		TextView td_title = new TextView(this);
		TextView td_content = new TextView(this);
		td_title.setText(title);
		td_title.setLayoutParams(new LayoutParams(
		         LayoutParams.FILL_PARENT,
		         LayoutParams.WRAP_CONTENT));
		
		td_title.setPadding(10, 0, 0, 0);
		td_title.setBackgroundColor(0xFFDDDDDD);
		td_title.setTextColor(0xFF000000);
		
		LinearLayout.LayoutParams params_title = (LinearLayout.LayoutParams)td_title.getLayoutParams();
		params_title.setMargins(10, 0, 0, 10); //substitute parameters for left, top, right, bottom
		td_title.setLayoutParams(params_title);

		
		td_content.setText(content);
		td_content.setLayoutParams(new LayoutParams(
		         LayoutParams.FILL_PARENT,
		         LayoutParams.WRAP_CONTENT));
		td_content.setPadding(0, 0, 10, 0);
		td_content.setTextColor(0xFF000000);

		
		LinearLayout.LayoutParams params_content = (LinearLayout.LayoutParams)td_content.getLayoutParams();
		params_content.setMargins(10, 0, 0, 10); //substitute parameters for left, top, right, bottom
		td_content.setLayoutParams(params_content);
		
		switch(id){
			case 5:
				td_content.setOnClickListener(this);
				td_content.setId(id);
				
				td_content.setTextColor(Color.BLUE);
				
				SpannableString newContent = new SpannableString(content);
				newContent.setSpan(new UnderlineSpan(), 0, newContent.length(), 0);
				td_content.setText(newContent);
				break;
		}
		
		tr = new TableRow(this);
		
		// add in the round number //
        tr.setId(100);
        tr.setLayoutParams(new LayoutParams(
                LayoutParams.FILL_PARENT,
                LayoutParams.WRAP_CONTENT)); 
        
        tr.addView(td_title);
        tr.addView(td_content);
        
        return tr;
	}
	
	 public String arrayList_text(ArrayList<String> tempArray){
    	boolean first = true;
    	String tempText = "";
    	
    	for(int i=0;i<tempArray.size();i++){
        	if(first){
        		first = false;
        		tempText += tempArray.get(i);
        	} else {
        		tempText += "\n" + tempArray.get(i);
        	}
        }
    	
    	return tempText;
    }

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		
		
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		Log.v("fanbible", Integer.toString(scrollState));
		
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
			case 5:
				((TextView) v).setTextColor(Color.GREEN);
				this.open_link("www.fanbible.com/" + this.event.get_url());
				break;
			case R.id.iv_favourite_star_blank:
				this.iv_favourite_star_blank.setVisibility(View.GONE);
				this.iv_favourite_star_full.setVisibility(View.VISIBLE);
				break;
			case R.id.iv_favourite_star_full:
				this.iv_favourite_star_blank.setVisibility(View.VISIBLE);
				this.iv_favourite_star_full.setVisibility(View.GONE);
				break;
		}
	}
	

	public void open_link(String link){
		if (!link.startsWith("http://") && !link.startsWith("https://")){
			link = "http://" + link;
		}
			
		Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
		startActivity(browserIntent);
	}
	
    public void share(String Text){
    	Intent sendIntent = new Intent(android.content.Intent.ACTION_SEND);
    	sendIntent.setType("text/plain");
    	sendIntent.putExtra(Intent.EXTRA_TEXT, Text);
    	startActivity(Intent.createChooser(sendIntent,this.getString(R.string.share_heading_text)));
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.event_view, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case R.id.share:     
	        	this.share(this.getString(R.string.share_text) + this.event.get_url());
	        	break;
	    }
	    return true;
	}
}
