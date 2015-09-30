package vickypatel.ca.shiftmanager;

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

public class AddJob extends AppCompatActivity {

    public EditText companyNameEditText, positionEditText, hourlyRateEditText;
    public String companyName, position;
    public double hourlyRate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_job);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Add Job");
        setSupportActionBar(toolbar);

        companyNameEditText = (EditText) findViewById(R.id.input_company_name);
        positionEditText = (EditText) findViewById(R.id.input_position);
        hourlyRateEditText = (EditText) findViewById(R.id.input_hourly_rate);


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

                if(!error){

                }


                System.out.println(companyName + " company name");
            }
        });
    }

}
