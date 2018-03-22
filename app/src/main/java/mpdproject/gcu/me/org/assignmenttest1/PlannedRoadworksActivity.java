/**
 * ZICONG LI
 * S1635332
 */

package mpdproject.gcu.me.org.assignmenttest1;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.DatePicker;
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

public class PlannedRoadworksActivity extends AppCompatActivity implements OnClickListener{
    private ProgressDialog prog;
    private String TAG = PlannedRoadworksActivity.class.getSimpleName();    //activity tag for testing and debugging
    XMLparser xmlParser = new XMLparser();
    LinkedList<Item> workList;
    LinkedList<Item> currentRoadWorks;
    LinkedList<Item> titles;
    ListView workViewList;
    private Button btnDate;
    private Button btnViewAll;
    private FloatingActionButton btnRefresh;

    TextView txtCurrentDate;

    private Boolean bViewAll = false;
    final int Date_Dialog_ID = 0;
    int cDay, cMonth, cYear;
    Calendar cDate;
    int sDay,sMonth,sYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planned_roadworks);

        //show progress dialog
        prog = ProgressDialog.show(this, "Traffic Scotland","loading data...");

        //start background task to load data to listview
        new LoadDataTask().execute();

        //initialize views
        setViews();
    }

    public void setViews(){
        btnDate = (Button) findViewById(R.id.btnDate);
        btnDate.setOnClickListener(this);
        btnViewAll = (Button)findViewById(R.id.btnViewAll);
        btnViewAll.setOnClickListener(this);
        btnRefresh = (FloatingActionButton) findViewById(R.id.btnRefresh);
        btnRefresh.setOnClickListener(this);

        //Calendar/Date data
        cDate=Calendar.getInstance();
        cDay=cDate.get(Calendar.DAY_OF_MONTH);
        cMonth=cDate.get(Calendar.MONTH);
        cYear=cDate.get(Calendar.YEAR);
        sDay = cDay;
        sMonth = cMonth;
        sYear = cYear;

        txtCurrentDate = (TextView)findViewById(R.id.txtSelectedDate);
        updateDateDisplay(sYear, sMonth, sDay);

        workViewList = (ListView) findViewById(R.id.road_work_list);

        //listen for roadwork item click
        workViewList.setOnItemClickListener( new OnItemClickListener() {

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
        //Get Current Date
        Date currentDate = new Date(sYear - 1900, sMonth, sDay);
        titles = new  LinkedList<Item>();
        int i = 0;
        for (Item anItem : workList) {

            //calculate number of days/ length of time
            long diff = anItem.getEndDate().getTime() - anItem.getStartDate().getTime();
            int iDiff = (int) (diff/1000/60/60/24);

            anItem.setDaysToComplete(iDiff);

            if(bViewAll){
                titles.add(anItem);
            }
            else {
                if( currentDate.before(anItem.getEndDate()) && currentDate.after(anItem.getStartDate()) ||
                        currentDate.equals(anItem.getStartDate())|| currentDate.equals(anItem.getEndDate())){
                    titles.add(anItem);
                    i++;
                }else{}
            }
        }

        workViewList.setAdapter(new WorkListAdapter(this, titles));
    }

    private void searchRoadworks(String query){
        titles = new  LinkedList<Item>();
        int i = 0;
        for (Item anItem : workList) {
            if(anItem.getTitle().toLowerCase().contains(query.toLowerCase())){
                titles.add(anItem);
            }
            i++;
        }

        workViewList.setAdapter(new WorkListAdapter(this, titles));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnDate:
                //show date picker
                showDialog(Date_Dialog_ID);
                break;
            case R.id.btnViewAll:
                //View All RoadWorks
                if(!bViewAll){
                    bViewAll = true;
                    btnViewAll.setText("View Date");
                }
                else{
                    bViewAll = false;
                    btnViewAll.setText("View All");
                }
                populateList();
                break;
            case R.id.btnRefresh:

                Log.e(TAG,"in refresh button handler");
                prog = ProgressDialog.show(this, "Traffic Scotland","loading data...");
                new LoadDataTask().execute();
                break;

        }

    }

    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case Date_Dialog_ID:
                return new DatePickerDialog(this, onDateSet, cYear, cMonth, cDay);
        }
        return null;
    }

    private OnDateSetListener onDateSet = new OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            Log.e(TAG, "DATE SET");
            sYear = year;
            sMonth = monthOfYear;
            sDay = dayOfMonth;
            updateDateDisplay(sYear, sMonth, sDay);
            populateList();
        }
    };

    private void updateDateDisplay(int year,int month,int date) {
        txtCurrentDate.setText(date+"-"+(month+1)+"-"+year);
    }

    //dialog to show roadwork details
    private void showCustomDialog(Item event)
    {
        // Custom dialog setup
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.roadwork_dialog_layout);
        dialog.setTitle("    Roadwork Details");

        SimpleDateFormat df = new SimpleDateFormat("E dd.MM.yyyy");

        // Set the custom dialog components as a TextView and Button component
        TextView text = (TextView) dialog.findViewById(R.id.infoView);
        text.setText(event.getTitle());

        String delay_info = "unavailable";

        if(event.getDelayInfo() != "")
        {
            delay_info = event.getDelayInfo();
        }

        TextView txtDelayInfo = (TextView) dialog.findViewById(R.id.txtDelayInfo);
        txtDelayInfo.setText(delay_info);

        TextView txtStartDate = (TextView) dialog.findViewById(R.id.txtStartDate);
        Date start_date = event.getStartDate();
        txtStartDate.setText(df.format(start_date));

        TextView txtEndDate = (TextView) dialog.findViewById(R.id.txtEndDate);
        Date end_date = event.getEndDate();
        txtEndDate.setText(df.format(end_date));

        TextView txtPubDate = (TextView) dialog.findViewById(R.id.txtPubDate);
        txtPubDate.setText(event.getPubDate());

        TextView txtGeoLocation = (TextView) dialog.findViewById(R.id.txtGeoLocation);
        txtGeoLocation.setText(event.getGeorssPoint());


        Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
        dialogButton.setOnClickListener(new OnClickListener()
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
                aurl = new URL("http://www.trafficscotland.org/rss/feeds/plannedroadworks.aspx");
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
            currentRoadWorks = list;
            workList = list;
            //set data to listview
            populateList();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.roadworks_menu, menu);
        //get the searchview and set the searchable configuration
        // SearchManager searchManager = (SearchManager)getSystemService(Context.SEARCH_SERVICE);
        android.support.v7.widget.SearchView searchView = (android.support.v7.widget.SearchView)menu.findItem(R.id.roadworks_search).getActionView();
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
                searchRoadworks(query);
            }
        });
        return true;
    }

}
