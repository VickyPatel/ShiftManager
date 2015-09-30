package vickypatel.ca.shiftmanager.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import vickypatel.ca.shiftmanager.R;
import vickypatel.ca.shiftmanager.extras.Constants;

public class ActivityShifts extends AppCompatActivity {

    Bundle bundle = null;
    public int jobId = Constants.ZERO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shifts);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Shifts");
        setSupportActionBar(toolbar);



        bundle = getIntent().getExtras();
        if (bundle != null) {
            jobId = bundle.getInt(Constants.JOB_ID);
        }

        System.out.println(jobId + " job id");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                Intent i = new Intent(getApplicationContext(), ActivityAddShift.class);
                startActivity(i);

            }
        });
    }

}
