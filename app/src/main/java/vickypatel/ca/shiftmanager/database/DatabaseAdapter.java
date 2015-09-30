package vickypatel.ca.shiftmanager.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import vickypatel.ca.shiftmanager.Activities.ActivityJobs;
import vickypatel.ca.shiftmanager.pojo.Jobs;

/**
 * Created by VickyPatel on 2015-09-29.
 */
public class DatabaseAdapter {

    DatabaseHelper helper;

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

    public static class DatabaseHelper extends SQLiteOpenHelper {

        private static final String DATABASE_NAME = "shiftManager.db";
        //Every time Change Version Value when database is modified
        private static final int DATABASE_VERSION = 1;

        //JOBS TABLE
        private static final String JOB_TABLE_NAME = "jobs";
        private static final String JOB_ID = "_id";
        private static final String COMPANY_NAME = "company_name";
        private static final String POSITION = "position";
        private static final String HOURLY_RATE = "hourly_rate";

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            System.out.println("onCreate form database helper");

            db.execSQL("CREATE TABLE " + JOB_TABLE_NAME + " ("
                    + JOB_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COMPANY_NAME + " VARCHAR(100), "
                    + POSITION + "  VARCHAR(100), "
                    + HOURLY_RATE + " VARCHAR(50) "
                    + ");");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
