package vickypatel.ca.shiftmanager.Activities;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CalendarView;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;

import vickypatel.ca.shiftmanager.R;
import vickypatel.ca.shiftmanager.database.DatabaseAdapter;
import vickypatel.ca.shiftmanager.decorators.EventDecorator;
import vickypatel.ca.shiftmanager.decorators.OneDayDecorator;
import vickypatel.ca.shiftmanager.extras.Constants;
import vickypatel.ca.shiftmanager.pojo.Jobs;
import vickypatel.ca.shiftmanager.pojo.Shifts;

public class ActivitySummary extends AppCompatActivity implements OnDateSelectedListener, OnMonthChangedListener {
    private static final DateFormat FORMATTER = SimpleDateFormat.getDateInstance();
    MaterialCalendarView widget;
    ArrayList<CalendarDay> checkDates = new ArrayList<>();
    ArrayList<Integer> eventInterval = new ArrayList<>();
    ArrayList<CalendarDay> multipleEvent = new ArrayList<>();
    DatabaseAdapter db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Summary");
        setSupportActionBar(toolbar);
        db = new DatabaseAdapter(ActivitySummary.this);
        widget = (MaterialCalendarView) findViewById(R.id.calendarView);

        widget.setOnDateChangedListener(this);
        widget.setOnMonthChangedListener(this);


        widget.setShowOtherDates(MaterialCalendarView.SHOW_ALL);


        new InitCalendar().executeOnExecutor(Executors.newSingleThreadExecutor());

        Calendar calendar = Calendar.getInstance();
        widget.setSelectedDate(calendar.getTime());

    }


    @Override
    public void onDateSelected(MaterialCalendarView widget, CalendarDay date, boolean selected) {
        if (selected) {
            Toast.makeText(this, getSelectedDatesString(), Toast.LENGTH_LONG).show();
            if (checkDates.contains(date)) {
                Toast.makeText(ActivitySummary.this,"selected dates", Toast.LENGTH_LONG).show();
            }
            if(multipleEvent.contains(date)){
                Toast.makeText(ActivitySummary.this,"multiple dates", Toast.LENGTH_LONG).show();
            }
        }
        widget.invalidateDecorators();
    }

    @Override
    public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
        Toast.makeText(this, getSelectedDatesString(), Toast.LENGTH_LONG).show();
    }

    private String getSelectedDatesString() {
        CalendarDay date = widget.getSelectedDate();
        if (date == null) {
            return "No Selection";
        }
        return FORMATTER.format(date.getDate());
    }


    /**
     * Simulate an API call to show how to add decorators
     */
    private class InitCalendar extends AsyncTask<Void, Void, List<CalendarDay>> {

        @Override
        protected List<CalendarDay> doInBackground(@NonNull Void... voids) {
            try {
                Thread.sleep(0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            ArrayList<CalendarDay> dates = new ArrayList<>();
            ArrayList<Jobs> jobs = db.getJobs();
            for (int i = 0; i < jobs.size(); i++) {
                ArrayList<Shifts> shifts = db.getShifts(jobs.get(i).getJobId());
                for (int j = 0; j < shifts.size(); j++) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(shifts.get(j).getStartDate());
                    CalendarDay day = CalendarDay.from(calendar);
                    dates.add(day);
                }
                eventInterval.add(dates.size());
            }

            for (int k = 0; k < dates.size(); k++) {
                if (Collections.frequency(dates, dates.get(k)) > 1) {
                    multipleEvent.add(dates.get(k));
                }
            }
            return dates;
        }

        @Override
        protected void onPostExecute(@NonNull List<CalendarDay> calendarDays) {
            super.onPostExecute(calendarDays);

            if (isFinishing()) {
                return;
            }
            checkDates = (ArrayList<CalendarDay>) calendarDays;

            int start = Constants.ZERO, end;
            for (int i = 0; i < eventInterval.size(); i++) {
                end = eventInterval.get(i);
                List<CalendarDay> tempCalendarDay = calendarDays.subList(start, end);
                widget.addDecorator(new EventDecorator(getResources().getColor(Constants.DATE_COLORS[i]), tempCalendarDay));
                start = end;
            }
            widget.addDecorator(new EventDecorator(getResources().getColor(R.color.colorAccent), multipleEvent));

        }
    }
}
