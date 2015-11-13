package vickypatel.ca.shiftmanager.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ParcelFormatException;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import vickypatel.ca.shiftmanager.R;
import vickypatel.ca.shiftmanager.database.DatabaseAdapter;
import vickypatel.ca.shiftmanager.extras.Constants;
import vickypatel.ca.shiftmanager.pojo.Jobs;

public class ActivityAddJob extends AppCompatActivity {

    public EditText companyNameEditText, positionEditText, hourlyRateEditText;
    public String companyName, position;
    public double hourlyRate;
    private Bundle bundle;
    private int jobId = Constants.ZERO;
    DatabaseAdapter adapter;
    private Jobs job;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_job);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        companyNameEditText = (EditText) findViewById(R.id.input_company_name);
        positionEditText = (EditText) findViewById(R.id.input_position);
        hourlyRateEditText = (EditText) findViewById(R.id.input_hourly_rate);

        bundle = getIntent().getExtras();
        if (bundle != null) {
            jobId = bundle.getInt(Constants.JOB_ID);
        }

        adapter = new DatabaseAdapter(this);
        if(jobId != Constants.ZERO){
            job = adapter.getJobWithJobId(jobId);
            companyNameEditText.setText(job.getCompanyName());
            positionEditText.setText(job.getPosition());
            hourlyRateEditText.setText(job.getHourlyRate()+"");
        }


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                companyName = companyNameEditText.getText().toString();
                position = positionEditText.getText().toString();
                String tempHourlyRate = hourlyRateEditText.getText().toString();
                boolean error = false;

                if (companyName.equals("")) {
                    error = true;
                    companyNameEditText.setError("This field is required");
                }
                if (position.equals("")) {
                    error = true;
                    positionEditText.setError("This field is required");
                }
                if (tempHourlyRate.equals("")) {
                    error = true;
                    hourlyRateEditText.setError("This field is required");
                }
                try {
                    hourlyRate = Float.parseFloat(tempHourlyRate);
                }catch (Exception e){
                    error = true;
                    hourlyRateEditText.setError("Please Enter in number format");
                }

                if(!error ){

                    Jobs newJob = new Jobs();
                    newJob.setCompanyName(companyName);
                    newJob.setPosition(position);
                    newJob.setHourlyRate(hourlyRate);
                    newJob.setJobId(jobId);
                    DatabaseAdapter adapter = new DatabaseAdapter(ActivityAddJob.this);

                    if(jobId != Constants.ZERO){

                        long updatedRow = adapter.updateJob(newJob);
                        System.out.println(updatedRow + " updated row");
                        if(updatedRow > 0){
                            startActivity(new Intent(ActivityAddJob.this, ActivityJobs.class));
                        }
                    }else{
                        long insertedRow = adapter.insertIntoJobs(newJob);
                        System.out.println(insertedRow + " inserted row");
                        if(insertedRow > 0){
                            startActivity(new Intent(ActivityAddJob.this, ActivityJobs.class));
                        }
                    }


                }

            }
        });
    }

}
