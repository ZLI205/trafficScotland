/**
 * ZICONG LI
 * S1635332
 */

package mpdproject.gcu.me.org.assignmenttest1;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;

public class CurrentIncidentsActivity extends AppCompatActivity {
    private ProgressDialog prog;
    private String TAG = CurrentIncidentsActivity.class.getSimpleName();   //activity tag for testing and debugging
    XMLparser xmlParser = new XMLparser();
    LinkedList<Item> incidentList;
    LinkedList<Item> titles;
    ListView incidentViewList;
    private FloatingActionButton btnRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_incidents);

        //show progress dialog
        prog = ProgressDialog.show(this, "Traffic Scotland","loading data...");

        //start background task to load data to listview
        new LoadDataTask().execute();

        //initialize views
        setViews();
    }

    public void setViews(){
        btnRefresh = (FloatingActionButton) findViewById(R.id.btnRefresh);
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show progress dialog
                prog = ProgressDialog.show(v.getContext(), "Traffic Scotland","loading data...");

                //start background task to load data to listview
                new LoadDataTask().execute();
            }
        });

        incidentViewList = (ListView) findViewById(R.id.current_incidents_list);

        //listen for roadwork item click
        incidentViewList.setOnItemClickListener( new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View View, int position,
                                    long id) {
                Log.e("ITEM CLICKED", Integer.toString(position));
                String item = (String) parent.getItemAtPosition(position);
                Item choice = titles.get(position);
                item = titles.get(position).getTitle();
                //show roadwork information on dialog
                showCustomDialog(choice);
            }
        });

    }

    private void populateList() {
        titles = new  LinkedList<Item>();

        for (Item anItem : incidentList) {
            titles.add(anItem);
        }

        incidentViewList.setAdapter(new IncidentListAdapter(this, titles));
    }

    private void searchIncidents(String query){
        titles = new  LinkedList<Item>();
        for (Item anItem : incidentList) {
            if(anItem.getTitle().toLowerCase().contains(query.toLowerCase())){
                titles.add(anItem);
            }
        }

        incidentViewList.setAdapter(new IncidentListAdapter(this, titles));
    }

    //dialog to show roadwork details
    private void showCustomDialog(Item event)
    {
        // Custom dialog setup
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.incidents_dialog_layout);
        dialog.setTitle("    Incident Details");

        SimpleDateFormat df = new SimpleDateFormat("E dd.MM.yyyy");

        // Set the custom dialog components as a TextView and Button component
        TextView text = (TextView) dialog.findViewById(R.id.infoView);
        text.setText(event.getTitle());

        TextView txtMoreInfo = (TextView) dialog.findViewById(R.id.txtMoreInfo);
        txtMoreInfo.setText(event.getDescription());

        TextView txtPubDate = (TextView) dialog.findViewById(R.id.txtPubDate);
        txtPubDate.setText(event.getPubDate());

        TextView txtGeoLocation = (TextView) dialog.findViewById(R.id.txtGeoLocation);
        txtGeoLocation.setText(event.getGeorssPoint());


        Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
        dialogButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();

            }
        });

        dialog.show();
    }

    private class LoadDataTask extends AsyncTask<String, Integer, LinkedList<Item>> {

        @Override
        protected LinkedList<Item> doInBackground(String... params) {
            String result = "";
            URL aurl;
            URLConnection yc;
            BufferedReader in = null;
            String inputLine = "";


            Log.e(TAG,"in run");

            try
            {
                Log.e(TAG,"in try");
                aurl = new URL("http://trafficscotland.org/rss/feeds/currentincidents.aspx");
                yc = aurl.openConnection();
                in = new BufferedReader(new InputStreamReader(yc.getInputStream()));

                while ((inputLine = in.readLine()) != null)
                {
                    result = result + inputLine;
                    Log.e(TAG,inputLine);

                }
                in.close();

                if(result ==""){
                    Log.e("XML NOT FOUND", "CONNECTION FAILED");
                }
            }
            catch (IOException ae)
            {
                Log.e(TAG, "ioexception");
            }

            xmlParser.parseXML(result);
            return xmlParser.getItemList();
        }

        //parsing is complete
        protected void onPostExecute(LinkedList<Item> list) {
            //hide progress dialog
            if(prog.isShowing()){
                prog.dismiss();
            }
            incidentList = list;
            //set data to listview
            populateList();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.incidents_menu, menu);
        //get the searchview and set the searchable configuration
        // SearchManager searchManager = (SearchManager)getSystemService(Context.SEARCH_SERVICE);
        android.support.v7.widget.SearchView searchView = (android.support.v7.widget.SearchView)menu.findItem(R.id.incidents_search).getActionView();
        //assumes current activity is the searchable activity
        //searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                doSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }

            public void doSearch(String query){
                searchIncidents(query);
            }
        });
        return true;
    }
}
