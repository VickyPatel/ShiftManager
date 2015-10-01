package vickypatel.ca.shiftmanager.Activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import vickypatel.ca.shiftmanager.R;
import vickypatel.ca.shiftmanager.extras.Constants;
import vickypatel.ca.shiftmanager.fragments.DatePickerFragment;
import vickypatel.ca.shiftmanager.fragments.TimePickerFragment;

public class ActivityAddShift extends AppCompatActivity implements DatePickerFragment.OnDateSelectedListener, TimePickerFragment.OnTimeSelectedListener, View.OnClickListener {

    public TextView shiftDate, startTime, endTime, totalTime;
    public Button selectDate;
    LinearLayout shiftDateLayout, startTimeLayout, endTimeLayout;
    FloatingActionButton fab;
    DialogFragment newFragment;
    Bundle bundle = new Bundle();
    Calendar c = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_shift);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Add Shift");
        setSupportActionBar(toolbar);

        shiftDate = (TextView) findViewById(R.id.shiftDateTextView);
        startTime = (TextView) findViewById(R.id.startTimeTextView);
        endTime = (TextView) findViewById(R.id.endTimeTextview);
        totalTime = (TextView) findViewById(R.id.totalTimeTextView);
        shiftDateLayout = (LinearLayout) findViewById(R.id.shiftDateLayout);
        startTimeLayout = (LinearLayout) findViewById(R.id.startTimeLayout);
        endTimeLayout = (LinearLayout) findViewById(R.id.endTimeLayout);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        shiftDateLayout.setOnClickListener(this);
        startTimeLayout.setOnClickListener(this);
        endTimeLayout.setOnClickListener(this);
        fab.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.shiftDateLayout:
                newFragment = new DatePickerFragment();
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

        try {
            String time1 = startTime.getText().toString();
            String time2 = endTime.getText().toString();
            String tempDate = c.get(Calendar.DATE) + "/" + c.get(Calendar.MONTH) + "/" + c.get(Calendar.YEAR);
            String format = "dd/MM/yyyy hh:mm a";

            SimpleDateFormat sdf = new SimpleDateFormat(format);

            Date dateObj1 = sdf.parse(tempDate + " " + time1);
            Date dateObj2 = sdf.parse(tempDate + " " + time2);

            long diff = dateObj2.getTime() - dateObj1.getTime();
            double diffInHours = diff / ((double) 1000 * 60 * 60);
            System.out.println(diffInHours);
            System.out.println("Hours " + (int) diffInHours);
            System.out.println("Minutes " +(int) Math.round((diffInHours - (int) diffInHours) * 60));
            String tempTime = (int) diffInHours + ":" + (int) Math.round((diffInHours - (int) diffInHours) * 60) + "HR";

            totalTime.setText(tempTime);

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDateSelected(int year, int month, int day) {
        c.set(year, month, day);
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d, ''yy");
        String formattedDate = sdf.format(c.getTime());
        shiftDate.setText(formattedDate);


    }
}
