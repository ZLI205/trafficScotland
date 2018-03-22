/**
 * ZICONG LI
 * S1635332
 */

package mpdproject.gcu.me.org.assignmenttest1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity
{
    private Button incidentsButton;
    private Button roadworksButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        incidentsButton = (Button)findViewById(R.id.incidentsButton);
        roadworksButton = (Button)findViewById(R.id.roadworksButton);

        //open incidents activity when button is clicked
        incidentsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CurrentIncidentsActivity.class);
                startActivity(intent);
            }
        });

        //open roadworks activity when button is clicked
        roadworksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PlannedRoadworksActivity.class);
                startActivity(intent);
            }
        });

    }

}
