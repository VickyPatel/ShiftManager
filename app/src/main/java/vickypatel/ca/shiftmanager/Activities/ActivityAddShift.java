package vickypatel.ca.shiftmanager.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import vickypatel.ca.shiftmanager.R;
import vickypatel.ca.shiftmanager.database.DatabaseAdapter;
import vickypatel.ca.shiftmanager.extras.Constants;
import vickypatel.ca.shiftmanager.fragments.DatePickerFragment;
import vickypatel.ca.shiftmanager.fragments.TimePickerFragment;
import vickypatel.ca.shiftmanager.pojo.Shifts;

public class ActivityAddShift extends AppCompatActivity implements DatePickerFragment.OnDateSelectedListener, TimePickerFragment.OnTimeSelectedListener, View.OnClickListener {

    public TextView shiftStartDate, shiftEndDate, startTime, endTime, totalTime;
    LinearLayout shiftStartDateLayout, shiftEndDateLayout, startTimeLayout, endTimeLayout;
    FloatingActionButton fab;
    DialogFragment newFragment;
    Bundle bundle = new Bundle();
    Calendar c1 = Calendar.getInstance();
    Calendar c2 = Calendar.getInstance();
    int jobId = Constants.ZERO;
    String tempTime = Constants.NA;
    float totalHours = Constants.ZERO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_shift);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bundle = getIntent().getExtras();
        if (bundle != null) {
            jobId = bundle.getInt(Constants.JOB_ID);
        }

        shiftStartDate = (TextView) findViewById(R.id.shiftStartDateTextView);
        shiftEndDate = (TextView) findViewById(R.id.shiftEndDateTextView);
        startTime = (TextView) findViewById(R.id.startTimeTextView);
        endTime = (TextView) findViewById(R.id.endTimeTextview);
        totalTime = (TextView) findViewById(R.id.totalTimeTextView);
        shiftStartDateLayout = (LinearLayout) findViewById(R.id.shiftStartDateLayout);
        shiftEndDateLayout = (LinearLayout) findViewById(R.id.shiftEndDateLayout);
        startTimeLayout = (LinearLayout) findViewById(R.id.startTimeLayout);
        endTimeLayout = (LinearLayout) findViewById(R.id.endTimeLayout);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d, ''yy");
        String formattedDate = sdf.format(c1.getTime());
        shiftStartDate.setText(formattedDate);
        shiftEndDate.setText(formattedDate);

        setTotalTime();
        shiftStartDateLayout.setOnClickListener(this);
        shiftEndDateLayout.setOnClickListener(this);
        startTimeLayout.setOnClickListener(this);
        endTimeLayout.setOnClickListener(this);
        fab.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.shiftStartDateLayout:
                newFragment = new DatePickerFragment();
                bundle.putString(Constants.DATE_TYPE, Constants.START_DATE);
                newFragment.setArguments(bundle);
                newFragment.show(getSupportFragmentManager(), "datePicker");
                break;
            case R.id.shiftEndDateLayout:
                newFragment = new DatePickerFragment();
                bundle.putString(Constants.DATE_TYPE, Constants.END_DATE);
                newFragment.setArguments(bundle);
                newFragment.show(getSupportFragmentManager(), "datePicker");
                break;
            case R.id.startTimeLayout:
                newFragment = new TimePickerFragment();
                bundle.putString(Constants.TIME_TYPE, Constants.START_TIME);
                newFragment.setArguments(bundle);
                newFragment.show(getSupportFragmentManager(), "timePicker");
                break;
            case R.id.endTimeLayout:
                newFragment = new TimePickerFragment();
                bundle.putString(Constants.TIME_TYPE, Constants.END_TIME);
                newFragment.setArguments(bundle);
                newFragment.show(getSupportFragmentManager(), "timePicker");
                break;
            case R.id.fab:
                collectData();
                break;
        }
    }

    @Override
    public void onTimeSelected(int hourOfDay, int minute, String timeType) {
        String time = hourOfDay % 12 + ":" + ((minute < 10) ? "0" + minute : minute) + " " + ((hourOfDay >= 12) ? "PM" : "AM");
        if (timeType.equals(Constants.START_TIME)) {
            startTime.setText(time);
        } else if (timeType.equals(Constants.END_TIME)) {
            endTime.setText(time);
        }
        setTotalTime();

    }

    @Override
    public void onDateSelected(int year, int month, int day, String dateType) {

        SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d, ''yy");

        String formattedDate = Constants.NA;
        if (dateType.equals(Constants.START_DATE)) {
            c1.set(year, month, day);
            c2.set(year, month, day);
            formattedDate = sdf.format(c1.getTime());
            shiftStartDate.setText(formattedDate);
            shiftEndDate.setText(formattedDate);
        } else if (dateType.equals(Constants.END_DATE)) {
            c2.set(year, month, day);
            formattedDate = sdf.format(c2.getTime());
            shiftEndDate.setText(formattedDate);
        }

        setTotalTime();
    }

    public void setTotalTime() {
        try {
            String time1 = startTime.getText().toString();
            String time2 = endTime.getText().toString();
            String tempDate1 = c1.get(Calendar.DATE) + "/" + c1.get(Calendar.MONTH) + "/" + c1.get(Calendar.YEAR);
            String tempDate2 = c2.get(Calendar.DATE) + "/" + c2.get(Calendar.MONTH) + "/" + c2.get(Calendar.YEAR);
            String format = "dd/MM/yyyy hh:mm a";

            SimpleDateFormat sdf = new SimpleDateFormat(format);

            Date dateObj1 = sdf.parse(tempDate1 + " " + time1);
            Date dateObj2 = sdf.parse(tempDate2 + " " + time2);

            long diff = dateObj2.getTime() - dateObj1.getTime();
            double diffInHours = diff / ((double) 1000 * 60 * 60);
            System.out.println(diffInHours);
            System.out.println("Hours " + (int) diffInHours);
            System.out.println("Minutes " + (int) Math.round((diffInHours - (int) diffInHours) * 60));
            int min = (int) Math.round((diffInHours - (int) diffInHours) * 60);
            tempTime = (int) diffInHours + ":" + ((min < 10) ? "0" + min : min) + " HR";

            totalTime.setText(tempTime);

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void collectData() {
        Shifts newShift = new Shifts();
        try {
            String sTime = startTime.getText().toString().trim();
            String eTime = endTime.getText().toString().trim();
            String tTime = tempTime.substring(0, tempTime.length() - 2).trim();
            String str[] = tTime.split(":");
            float tempTotalHour = Float.parseFloat(str[0]) + (Float.parseFloat(str[1]) / 60);
            totalHours = Float.parseFloat(String.valueOf(tempTotalHour));

            c1.set(Calendar.MINUTE, 0);
            c1.set(Calendar.SECOND, 0);
            c1.set(Calendar.HOUR_OF_DAY, 0);

            c2.set(Calendar.MINUTE, 0);
            c2.set(Calendar.SECOND, 0);
            c2.set(Calendar.HOUR_OF_DAY, 0);

            newShift.setStartDate(c1.getTime());
            newShift.setEndDate(c2.getTime());
            newShift.setStartTime(sTime);
            newShift.setEndTime(eTime);
            newShift.setTotalHours(totalHours);
            newShift.setPaymentStatus(Constants.STATUS_UNPAID);
            newShift.setJobId(jobId);
        } catch (Exception e) {
            System.out.println(e);
        }
        DatabaseAdapter adapter = new DatabaseAdapter(ActivityAddShift.this);
        long insertedRow = adapter.insertIntoShifts(newShift);
        System.out.println(insertedRow + " inserted row");

        if (insertedRow > 0) {
            adapter.updateJobForHour(jobId, totalHours);

            Intent intent = new Intent(this, ActivityShifts.class);
            intent.putExtra(Constants.JOB_ID, jobId);
            this.startActivity(intent);
        }

    }

}
