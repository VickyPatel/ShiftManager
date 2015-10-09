package vickypatel.ca.shiftmanager.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;

import vickypatel.ca.shiftmanager.R;
import vickypatel.ca.shiftmanager.database.DatabaseAdapter;
import vickypatel.ca.shiftmanager.extras.Constants;
import vickypatel.ca.shiftmanager.pojo.Jobs;

public class ActivityPayTime extends AppCompatActivity {

    DatabaseAdapter adapter;
    ArrayList<Jobs> jobs = new ArrayList<>();
    public EditText totalHoursEditText, totalTaxEditText, grossPayEditText, netPayEditText;
    public float totalHours, totalTax, grossPay, netPay;
    boolean error = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_time);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Pay Time");
        setSupportActionBar(toolbar);

        totalHoursEditText = (EditText) findViewById(R.id.input_total_hour);
        totalTaxEditText = (EditText) findViewById(R.id.input_total_tax);
        grossPayEditText = (EditText) findViewById(R.id.input_gross_pay);
        netPayEditText = (EditText) findViewById(R.id.input_net_pay);
        Spinner jobSpinner = (Spinner) findViewById(R.id.job_spinner);

        adapter = new DatabaseAdapter(this);
        jobs = adapter.getJobs();
        String[] items = new String[jobs.size()];
        for (int i = 0; i < jobs.size(); i++) {
            items[i] = jobs.get(i).getCompanyName();
        }


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, items);

        jobSpinner.setAdapter(adapter);

        jobSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
              
            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                System.out.println("function call");
                collectData();

                if (!error) {

                    System.out.println(" no error");
                }
            }
        });
    }

    public void collectData() {
        String tHours = (totalHoursEditText.getText().toString());
        String tTax = (totalTaxEditText.getText().toString());
        String gPay = (grossPayEditText.getText().toString());
        String nPAy = (netPayEditText.getText().toString());
        error = false;

        if (tHours.equals("")) {
            error = true;
            totalHoursEditText.setError("This field is required");
        } else {
            try {
                totalHours = Float.parseFloat(tHours);
            } catch (Exception e) {
                error = true;
                totalHoursEditText.setError("Please Enter in number format");
            }
        }
        if (tTax.equals("")) {
            error = true;
            totalTaxEditText.setError("This field is required");
        } else {
            try {
                totalTax = Float.parseFloat(tTax);
            } catch (Exception e) {
                error = true;
                totalTaxEditText.setError("Please Enter in number format");
            }
        }
        if (gPay.equals("")) {
            error = true;
            grossPayEditText.setError("This field is required");
        } else {
            try {
                grossPay = Float.parseFloat(gPay);
            } catch (Exception e) {
                error = true;
                grossPayEditText.setError("Please Enter in number format");
            }
        }
        if (nPAy.equals("")) {
            error = true;
            netPayEditText.setError("This field is required");
        } else {
            try {
                netPay = Float.parseFloat(nPAy);
            } catch (Exception e) {
                error = true;
                netPayEditText.setError("Please Enter in number format");
            }
        }

    }

}
