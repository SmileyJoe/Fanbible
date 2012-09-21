package com.joedev.fanbible;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.SectionIndexer;
import android.widget.TextView;




/*class MyCursorAdapter extends SimpleCursorAdapter implements SectionIndexer{
    AlphabetIndexer alphaIndexer;
            public MyCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to) {
                    super(context, layout, c, from, to);
alphaIndexer=new AlphabetIndexer(c, myCursor.getColumnIndex(DbUtil.KEY_NAME), " ABCDEFGHIJKLMNOPQRSTUVWXYZ");
//you have just to instanciate the indexer class like this

//cursor,index of the sorted colum,a string representing the alphabeth (pay attention on the blank char at the beginning of the sequence)
            }

            @Override
            public int getPositionForSection(int section) {                
                    return alphaIndexer.getPositionForSection(section); //use the indexer
            }

            @Override
            public int getSectionForPosition(int position) {                       
                    return alphaIndexer.getSectionForPosition(position); //use the indexer
            }
            @Override
            public Object[] getSections() {
                    return alphaIndexer.getSections(); //use the indexer
            }
}*/


public class LocationRowAdapter extends ArrayAdapter<Countries> implements SectionIndexer {
	private Context context;
    private ArrayList<Countries> countries;
    private static final String TAG = "fanbible";

    
    private HashMap<String, Integer> alphaIndexer;
    private String[] sections;

    public LocationRowAdapter(Context context, ArrayList<Countries> countries) {
    	super(context, R.layout.location_row, countries);
        this.context = context;
        this.countries = countries;
        Log.v(TAG, "initial size: " + Integer.toString(countries.size()));
        
        
        
        this.alphaIndexer = new HashMap<String, Integer>();
        int size = this.countries.size();

        for (int x = 0; x < size; x++) {
            Countries s = this.countries.get(x);

	// get the first letter of the store
            String ch =  s.get_country().substring(0, 1);
	// convert to uppercase otherwise lowercase a -z will be sorted after upper A-Z
            ch = ch.toUpperCase();

	// HashMap will prevent duplicates
            this.alphaIndexer.put(ch, x);
        }

        Set<String> sectionLetters = this.alphaIndexer.keySet();
        Log.v(TAG, "section letters :" + sectionLetters.toString());
    // create a list from the set to sort
        ArrayList<String> sectionList = new ArrayList<String>(sectionLetters); 
        Log.v(TAG, "section list :" + sectionList.toString());
        Collections.sort(sectionList);
        Log.v(TAG, "section list 1:" + sectionList.toString());
        
        
       this.sections = new String[sectionList.size()];
       
       Log.v(TAG, "sections: " + this.sections);

        sectionList.toArray(this.sections);
        
        
    }
    
	@Override
	public int getCount() {
		Log.v(TAG, "test6");
		Log.v(TAG, "size: " + Integer.toString(this.countries.size()));
		return this.countries.size();
	}

	@Override
	public Countries getItem(int position) {
		return this.countries.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup viewGroup) {
		Countries country = this.countries.get(position);
		Log.v(TAG, "test");
		Log.v(TAG, Integer.toString(position));
		if (convertView == null) {
			 LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	         convertView = inflater.inflate(R.layout.location_row, null);
        }
		
//		Button h = (Button) convertView.findViewById(R.id.bt_country);
//        h.setText(this.sections[getSectionForPosition(position)]);
        //h.setVisibility(View.VISIBLE);
        
		TextView tv_location_title = (TextView) convertView.findViewById(R.id.tv_location_title);
		tv_location_title.setText(country.get_country());
		
		return convertView;
	}

	@Override
	public int getPositionForSection(int section) {
		Log.v(TAG, "pos for sec:" + Integer.toString(this.alphaIndexer.get(sections[section])));
        return this.alphaIndexer.get(sections[section]);
    }
	
	@Override
    public int getSectionForPosition(int position) {
		Log.v(TAG, "test1");
        return position;
    }

	@Override
    public Object[] getSections() {
		Log.v(TAG, "test2");
         return this.sections;
    }

}
