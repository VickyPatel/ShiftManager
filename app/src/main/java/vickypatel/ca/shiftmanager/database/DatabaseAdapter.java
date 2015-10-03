package vickypatel.ca.shiftmanager.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import vickypatel.ca.shiftmanager.Activities.ActivityJobs;
import vickypatel.ca.shiftmanager.pojo.Jobs;
import vickypatel.ca.shiftmanager.pojo.Shifts;

/**
 * Created by VickyPatel on 2015-09-29.
 */
public class DatabaseAdapter {

    DatabaseHelper helper;
    final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    public DatabaseAdapter(Context context) {
        helper = new DatabaseHelper(context);
    }

    public long insertIntoJobs(Jobs newJob) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.COMPANY_NAME, newJob.getCompanyName());
        cv.put(DatabaseHelper.POSITION, newJob.getPosition());
        cv.put(DatabaseHelper.HOURLY_RATE, newJob.getHourlyRate());
        long id = db.insert(DatabaseHelper.JOB_TABLE_NAME, null, cv);
        return id;
    }

    public ArrayList<Jobs> getJobs() {
        ArrayList<Jobs> jobs = new ArrayList<>();
        SQLiteDatabase db = helper.getWritableDatabase();

        Cursor jobCursor = db.query(DatabaseHelper.JOB_TABLE_NAME, null, null, null, null, null, null);

        while (jobCursor.moveToNext()) {
            Jobs job = new Jobs();
            job.setCompanyName(jobCursor.getString(jobCursor.getColumnIndex(DatabaseHelper.COMPANY_NAME)));
            job.setPosition(jobCursor.getString(jobCursor.getColumnIndex(DatabaseHelper.POSITION)));
            job.setHourlyRate(jobCursor.getFloat(jobCursor.getColumnIndex(DatabaseHelper.HOURLY_RATE)));
            jobs.add(job);
        }
        return jobs;

    }

    public long insertIntoShifts(Shifts newShift) {
        System.out.println("insert method");
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.START_DATE, dateFormat.format(newShift.getStartDate()));
        cv.put(DatabaseHelper.END_DATE, dateFormat.format(newShift.getEndDate()));
        cv.put(DatabaseHelper.START_TIME, newShift.getStartTime());
        cv.put(DatabaseHelper.END_TIME, newShift.getEndTime());
        cv.put(DatabaseHelper.SHIFT_TOTAL_HOURS, newShift.getTotalHours());
        cv.put(DatabaseHelper.PAYMENT_STATUS, newShift.getPaymentStatus());
        cv.put(DatabaseHelper.JOB_FOREIGN_KEY, newShift.getJobId());
        long id = db.insert(DatabaseHelper.SHIFT_TABLE_NAME, null, cv);
        return id;
    }

    public ArrayList<Shifts> getShifts() {
        ArrayList<Shifts> shifts = new ArrayList<>();
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor shiftCursor = db.query(DatabaseHelper.SHIFT_TABLE_NAME, null, null, null, null, null, null);
        while (shiftCursor.moveToNext()) {
            Shifts shift = new Shifts();
            String sDate = shiftCursor.getString(shiftCursor.getColumnIndex(DatabaseHelper.START_DATE));
            String eDate = shiftCursor.getString(shiftCursor.getColumnIndex(DatabaseHelper.END_DATE));
            try {
                shift.setStartDate(dateFormat.parse(sDate));
                shift.setEndDate(dateFormat.parse(eDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            shift.setStartTime(shiftCursor.getString(shiftCursor.getColumnIndex(DatabaseHelper.START_TIME)));
            shift.setEndTime(shiftCursor.getString(shiftCursor.getColumnIndex(DatabaseHelper.END_TIME)));
            shift.setTotalHours(shiftCursor.getString(shiftCursor.getColumnIndex(DatabaseHelper.SHIFT_TOTAL_HOURS)));
            shift.setPaymentStatus(shiftCursor.getString(shiftCursor.getColumnIndex(DatabaseHelper.PAYMENT_STATUS)));
            shift.setJobId(shiftCursor.getInt(shiftCursor.getColumnIndex(DatabaseHelper.JOB_ID)));

            shifts.add(shift);

        }
        return shifts;

    }

    public static class DatabaseHelper extends SQLiteOpenHelper {

        private static final String DATABASE_NAME = "shiftManager.db";
        //Every time Change Version Value when database is modified
        private static final int DATABASE_VERSION = 3;

        //JOBS TABLE
        private static final String JOB_TABLE_NAME = "jobs";
        private static final String JOB_ID = "_id";
        private static final String COMPANY_NAME = "company_name";
        private static final String POSITION = "position";
        private static final String HOURLY_RATE = "hourly_rate";

        //SHIFT TABLE
        private static final String SHIFT_TABLE_NAME = "shifts";
        private static final String SHIFT_ID = "_id";
        private static final String START_DATE = "start_date";
        private static final String END_DATE = "end_date";
        private static final String START_TIME = "start_time";
        private static final String END_TIME = "end_time";
        private static final String SHIFT_TOTAL_HOURS = "shift_total_hours";
        private static final String PAYMENT_STATUS = "payment_status";
        private static final String JOB_FOREIGN_KEY = "job_id";


        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            System.out.println("onCreate form database helper");

            db.execSQL("CREATE TABLE IF NOT EXISTS " + JOB_TABLE_NAME + " ("
                    + JOB_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COMPANY_NAME + " VARCHAR(100), "
                    + POSITION + "  VARCHAR(100), "
                    + HOURLY_RATE + " VARCHAR(50) "
                    + ");");

            db.execSQL("CREATE TABLE IF NOT EXISTS " + SHIFT_TABLE_NAME + " ("
                    + SHIFT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + START_DATE + " DATE, "
                    + END_DATE + "  DATE, "
                    + START_TIME + " VARCHAR(50), "
                    + END_TIME + "  VARCHAR(100), "
                    + SHIFT_TOTAL_HOURS + "  VARCHAR(100), "
                    + PAYMENT_STATUS + "  VARCHAR(5), "
                    + JOB_FOREIGN_KEY + "  INTEGER "
                    + ");");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            System.out.println("onUpgrade form database helper");
        }
    }
}
