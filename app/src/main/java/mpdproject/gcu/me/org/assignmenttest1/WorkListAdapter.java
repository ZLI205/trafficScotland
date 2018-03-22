package mpdproject.gcu.me.org.assignmenttest1;

import java.util.LinkedList;

import android.R.integer;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * ZICONG LI
 * S1635332
 *
 * List Adapter for planned roadworks
 * Color of list items is set according to number of days
 */

public class WorkListAdapter extends BaseAdapter  {

	Context context;
	String[] data;
    LinkedList<Item> workData;
    private static LayoutInflater inflater = null;
    GradientDrawable gdPink;
    GradientDrawable gdAmber;
    GradientDrawable gdGreen;
    
    public WorkListAdapter(Context context, String[] data){
    	
    	this.context = context;
    	this.data = data;	
    	inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    
    public WorkListAdapter(Context context, LinkedList<Item> workData){
    	this.context = context;
    	this.workData = workData;
    	inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	 gdPink = new GradientDrawable(
 	            GradientDrawable.Orientation.TOP_BOTTOM,
 	            new int[] {0xff06292 , 0xffec407a});
 	    gdPink.setCornerRadius(0f);
 	    
 	   gdAmber = new GradientDrawable(
	            GradientDrawable.Orientation.TOP_BOTTOM,
	            new int[] {0xffEBA53D, 0xffE0A130, 0xffD69D24});
	    gdAmber.setCornerRadius(0f);
	    
	    gdGreen = new GradientDrawable(
 	            GradientDrawable.Orientation.TOP_BOTTOM,
 	            new int[] {0xff47D926, 0xff3ABC24, 0xff2E9F23});
 	    gdGreen.setCornerRadius(0f);
    }
     //return numbers of items
	public int getCount() {
		return workData.size();
	}

	@Override
	public Object getItem(int position) {
		return workData.get(position).toString();
	}
	
	public Item getEvent(int position){
		return workData.get(position);
	};

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;

		if (view == null)
            view = inflater.inflate(R.layout.item_layout, null);
		
		Item event = getEvent(position);
        TextView text;
        LinearLayout row;

        if(event.getDaysToComplete() >= 14){
        	text = (TextView) view.findViewById(R.id.text1);
        	text.setText(event.getTitle() + " \nDays: " + Integer.toString(event.getDaysToComplete()));
        	text.setBackgroundDrawable(gdGreen);
       
        }else if(event.getDaysToComplete() >= 7 && event.getDaysToComplete() < 14 ){
        	text = (TextView) view.findViewById(R.id.text1);
        	text.setBackgroundDrawable(gdAmber);
        	text.setText(event.getTitle() + " \nDays: " + Integer.toString(event.getDaysToComplete()));
        
        }else {
        	text = (TextView) view.findViewById(R.id.text1);
        	text.setBackgroundDrawable(gdPink);
        	text.setText(event.getTitle() + " \nDays: " + Integer.toString(event.getDaysToComplete()));
       
        }
        return view;
	}

}
