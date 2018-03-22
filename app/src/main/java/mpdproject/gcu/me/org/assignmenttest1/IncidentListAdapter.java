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
 * List Adapter for current incidents
 */

public class IncidentListAdapter extends BaseAdapter  {

    Context context;
    String[] data;
    LinkedList<Item> incidentData;
    private static LayoutInflater inflater = null;

    public IncidentListAdapter(Context context, String[] data){

        this.context = context;
        this.data = data;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public IncidentListAdapter(Context context, LinkedList<Item> incidentData){
        this.context = context;
        this.incidentData = incidentData;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return incidentData.size();
    }

    @Override
    public Object getItem(int position) {
        return incidentData.get(position).toString();
    }

    public Item getEvent(int position){
        return incidentData.get(position);
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

        Item item = getEvent(position);
        TextView text;

        text = (TextView) view.findViewById(R.id.text1);
        text.setText(item.getTitle());

        return view;
    }

}
