package vickypatel.ca.shiftmanager.Activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import vickypatel.ca.shiftmanager.R;
import vickypatel.ca.shiftmanager.fragments.DatePickerFragment;

public class ActivityAddShift extends AppCompatActivity implements DatePickerFragment.OnDateSelectedListener {

    public EditText shiftDate;
    public  ImageButton selectDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_shift);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Add Shift");
        setSupportActionBar(toolbar);

        shiftDate = (EditText) findViewById(R.id.shiftDateEditText);
        selectDate = (ImageButton) findViewById(R.id.selectDate);
        shiftDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "datePicker");


            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }


    @Override
    public void onDateSelected(int year, int month, int day) {
        System.out.println(year);
        Calendar c = Calendar.getInstance();
        c.set(year, month, day);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = sdf.format(c.getTime());
        shiftDate.setText(formattedDate);


    }
}
