package vickypatel.ca.shiftmanager.Activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import vickypatel.ca.shiftmanager.R;
import vickypatel.ca.shiftmanager.database.DatabaseAdapter;
import vickypatel.ca.shiftmanager.extras.Constants;
import vickypatel.ca.shiftmanager.fragments.DatePickerFragment;
import vickypatel.ca.shiftmanager.fragments.TimePickerFragment;
import vickypatel.ca.shiftmanager.pojo.Jobs;
import vickypatel.ca.shiftmanager.pojo.Pays;

public class ActivityPayTime extends AppCompatActivity implements DatePickerFragment.OnDateSelectedListener, View.OnClickListener {

    DatabaseAdapter adapter;
    ArrayList<Jobs> jobs = new ArrayList<>();
    public TextView payStartDate, payEndDate, startTime, endTime, totalTime;
    public EditText totalHoursEditText, totalTaxEditText, grossPayEditText, netPayEditText;
    LinearLayout payStartDateLayout, payEndDateLayout, startTimeLayout, endTimeLayout;
    public float totalHours, totalTax, grossPay, netPay;
    boolean error = false;
    public int jobId = Constants.NEGATIVE;
    Dialog dialog;
    Button editButton, deleteButton;
    LinearLayout tittleLayout, contentLayout;
    TextView txtTitle, txtMessage;
    DialogFragment newFragment;
    Bundle bundle = new Bundle();
    Calendar c1 = Calendar.getInstance();
    Calendar c2 = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_time);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        payStartDate = (TextView) findViewById(R.id.payStartDateTextView);
        payEndDate = (TextView) findViewById(R.id.payEndDateTextView);
        payStartDateLayout = (LinearLayout) findViewById(R.id.payStartDateLayout);
        payEndDateLayout = (LinearLayout) findViewById(R.id.payEndDateLayout);

        totalHoursEditText = (EditText) findViewById(R.id.input_total_hour);
        totalTaxEditText = (EditText) findViewById(R.id.input_total_tax);
        grossPayEditText = (EditText) findViewById(R.id.input_gross_pay);
        netPayEditText = (EditText) findViewById(R.id.input_net_pay);
        Spinner jobSpinner = (Spinner) findViewById(R.id.job_spinner);

        payStartDateLayout.setOnClickListener(this);
        payEndDateLayout.setOnClickListener(this);


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
                System.out.println("position : " + position);
                jobId = jobs.get(position).getJobId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                collectData();
                if (!error) {
                    System.out.println(" no error");

                    c1.set(Calendar.MINUTE, 0);
                    c1.set(Calendar.SECOND, 0);
                    c1.set(Calendar.HOUR_OF_DAY, 0);

                    c2.set(Calendar.MINUTE, 0);
                    c2.set(Calendar.SECOND, 0);
                    c2.set(Calendar.HOUR_OF_DAY, 0);

                    Pays newPayment = new Pays();
                    newPayment.setPayStartDate(c1.getTime());
                    newPayment.setPayEndDate(c2.getTime());
                    newPayment.setGrossPay(grossPay);
                    newPayment.setTotalHours(totalHours);
                    newPayment.setTotalTax(totalTax);
                    newPayment.setNetPay(netPay);
                    newPayment.setJobId(jobId);

                    DatabaseAdapter adapter = new DatabaseAdapter(ActivityPayTime.this);
                    long id = adapter.insertIntoPays(newPayment);
                    System.out.println(id + " row inserted");
                    if (id > 0) {
                        id = updateShiftData(jobId);
                        System.out.println(id + " row updated");
                        if (id > 0) {
                            showDialogMessage();
                        }

                    }
                }
            }
        });
    }

    private long updateShiftData(int jobId) {
        DatabaseAdapter adapter = new DatabaseAdapter(ActivityPayTime.this);
        return adapter.updateShifts(jobId, totalHours);
    }

    private void showDialogMessage() {
        //Custom dialog
        dialog = new Dialog(this);
        // hide to default title for Dialog
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // inflate the layout dialog_layout.xml and set it as contentView
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.custom_dialog, null, false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(view);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

        // Retrieve views from the inflated dialog layout and update their values
        tittleLayout = (LinearLayout) dialog.findViewById(R.id.layout_dialog_tittle);
        txtTitle = (TextView) dialog.findViewById(R.id.txt_dialog_title);
        txtTitle.setText("Tittle");

        contentLayout = (LinearLayout) dialog.findViewById(R.id.layout_dialog_content);
        txtMessage = (TextView) dialog.findViewById(R.id.txt_dialog_message);
        txtMessage.setText("Content.");

        Button btnCancel = (Button) dialog.findViewById(R.id.btn_cancel);
        btnCancel.setVisibility(View.GONE);
        Button btnOk = (Button) dialog.findViewById(R.id.btn_ok);
        btnOk.setText("OK");
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(ActivityPayTime.this, ActivityShifts.class);
                intent.putExtra(Constants.JOB_ID, jobId);
                startActivity(intent);
            }
        });
        txtTitle.setText("Awesome Work");
        txtMessage.setText("Good job done. Keep it up");
        // Display the dialog
        dialog.show();

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.payStartDateLayout:
                newFragment = new DatePickerFragment();
                bundle.putString(Constants.DATE_TYPE, Constants.START_DATE);
                newFragment.setArguments(bundle);
                newFragment.show(getSupportFragmentManager(), "datePicker");
                break;
            case R.id.payEndDateLayout:
                newFragment = new DatePickerFragment();
                bundle.putString(Constants.DATE_TYPE, Constants.END_DATE);
                newFragment.setArguments(bundle);
                newFragment.show(getSupportFragmentManager(), "datePicker");
                break;
        }
    }

    @Override
    public void onDateSelected(int year, int month, int day, String dateType) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d, ''yy");

        String formattedDate = Constants.NA;
        if (dateType.equals(Constants.START_DATE)) {
            c1.set(year, month, day);
            c2.set(year, month, day);
            formattedDate = sdf.format(c1.getTime());
            payStartDate.setText(formattedDate);
        } else if (dateType.equals(Constants.END_DATE)) {
            c2.set(year, month, day);
            formattedDate = sdf.format(c2.getTime());
            payEndDate.setText(formattedDate);
        }

    }


}
