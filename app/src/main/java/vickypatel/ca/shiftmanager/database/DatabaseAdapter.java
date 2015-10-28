package vickypatel.ca.shiftmanager.database;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import vickypatel.ca.shiftmanager.Activities.ActivityJobs;
import vickypatel.ca.shiftmanager.extras.Constants;
import vickypatel.ca.shiftmanager.pojo.Jobs;
import vickypatel.ca.shiftmanager.pojo.Pays;
import vickypatel.ca.shiftmanager.pojo.Shifts;

/**
 * Created by VickyPatel on 2015-09-29.
 */
public class DatabaseAdapter {

    DatabaseHelper helper;
    Context context;
    final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    public DatabaseAdapter(Context context) {
        this.context = context;
        helper = new DatabaseHelper(context);
    }

    public long insertIntoJobs(Jobs newJob) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues cv = new ContentValues();

        SharedPreferences sharedpreferences;
        sharedpreferences = context.getSharedPreferences(Constants.INFO_FILE, Context.MODE_PRIVATE);
        int jobId = (sharedpreferences.getInt(Constants.LAST_JOB_ID, Constants.ZERO));
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt(Constants.LAST_JOB_ID, jobId + 1);
        editor.commit();

        cv.put(DatabaseHelper.JOB_ID, jobId);
        cv.put(DatabaseHelper.COMPANY_NAME, newJob.getCompanyName());
        cv.put(DatabaseHelper.POSITION, newJob.getPosition());
        cv.put(DatabaseHelper.HOURLY_RATE, newJob.getHourlyRate());
        cv.put(DatabaseHelper.PAID_HOUR, Constants.ZERO);
        cv.put(DatabaseHelper.HALF_PAID_HOUR, Constants.ZERO);
        cv.put(DatabaseHelper.UNPAID_HOUR, Constants.ZERO);
        long id = db.insert(DatabaseHelper.JOB_TABLE_NAME, null, cv);
        return id;
    }

    public long updateJob(Jobs job) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String where = DatabaseHelper.JOB_ID + " = " + job.getJobId();
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.JOB_ID, job.getJobId());
        cv.put(DatabaseHelper.COMPANY_NAME, job.getCompanyName());
        cv.put(DatabaseHelper.POSITION, job.getPosition());
        cv.put(DatabaseHelper.HOURLY_RATE, job.getHourlyRate());
        long id = db.update(DatabaseHelper.JOB_TABLE_NAME, cv, where, null);
        return id;
    }

    public long updateJobForHour(int jobId, float hours) {
        SQLiteDatabase db = helper.getWritableDatabase();
        Jobs job = getJobWithJobId(jobId);
        String where = DatabaseHelper.JOB_ID + " = " + jobId;
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.UNPAID_HOUR, hours + job.getUnpaidHour());
        long id = db.update(DatabaseHelper.JOB_TABLE_NAME, cv, where, null);
        return id;
    }

    public ArrayList<Jobs> getJobs() {
        ArrayList<Jobs> jobs = new ArrayList<>();
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor jobCursor = db.query(DatabaseHelper.JOB_TABLE_NAME, null, null, null, null, null, null);
        while (jobCursor.moveToNext()) {
            Jobs job = new Jobs();
            job.setJobId(jobCursor.getInt(jobCursor.getColumnIndex(DatabaseHelper.JOB_ID)));
            job.setCompanyName(jobCursor.getString(jobCursor.getColumnIndex(DatabaseHelper.COMPANY_NAME)));
            job.setPosition(jobCursor.getString(jobCursor.getColumnIndex(DatabaseHelper.POSITION)));
            job.setHourlyRate(jobCursor.getFloat(jobCursor.getColumnIndex(DatabaseHelper.HOURLY_RATE)));
            job.setPaidHour(jobCursor.getFloat(jobCursor.getColumnIndex(DatabaseHelper.PAID_HOUR)));
            job.setUnpaidHour(jobCursor.getFloat(jobCursor.getColumnIndex(DatabaseHelper.UNPAID_HOUR)));
            job.setHalfPaidHour(jobCursor.getFloat(jobCursor.getColumnIndex(DatabaseHelper.HALF_PAID_HOUR)));

            jobs.add(job);
        }
        return jobs;

    }

    public Jobs getJobWithJobId(int jobId) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String where = DatabaseHelper.JOB_ID + " = " + jobId;
        Cursor jobCursor = db.query(DatabaseHelper.JOB_TABLE_NAME, null, where, null, null, null, null);
        Jobs job = new Jobs();
        while (jobCursor.moveToNext()) {
            job.setJobId(jobCursor.getInt(jobCursor.getColumnIndex(DatabaseHelper.JOB_ID)));
            job.setCompanyName(jobCursor.getString(jobCursor.getColumnIndex(DatabaseHelper.COMPANY_NAME)));
            job.setPosition(jobCursor.getString(jobCursor.getColumnIndex(DatabaseHelper.POSITION)));
            job.setHourlyRate(jobCursor.getFloat(jobCursor.getColumnIndex(DatabaseHelper.HOURLY_RATE)));
            job.setPaidHour(jobCursor.getFloat(jobCursor.getColumnIndex(DatabaseHelper.PAID_HOUR)));
            job.setUnpaidHour(jobCursor.getFloat(jobCursor.getColumnIndex(DatabaseHelper.UNPAID_HOUR)));
            job.setHalfPaidHour(jobCursor.getFloat(jobCursor.getColumnIndex(DatabaseHelper.HALF_PAID_HOUR)));

        }
        return job;
    }

    public int deleteJobWithId(int jobId) {
        deleteAllShiftForJob(jobId);
        SQLiteDatabase db = helper.getWritableDatabase();
        String where = DatabaseHelper.JOB_ID + " = " + jobId;
        return db.delete(DatabaseHelper.JOB_TABLE_NAME, where, null);
    }

    public int deleteAllShiftForJob(int jobId) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String where = DatabaseHelper.JOB_FOREIGN_KEY + " = " + jobId;
        return db.delete(DatabaseHelper.SHIFT_TABLE_NAME, where, null);
    }

    public long insertIntoShifts(Shifts newShift) {
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

    public int deleteShift(int shiftId, int jobId) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] args = {"" + shiftId, "" + jobId};
        String where = DatabaseHelper.SHIFT_ID + " = ? AND " + DatabaseHelper.JOB_FOREIGN_KEY + " = ? ";
        return db.delete(DatabaseHelper.SHIFT_TABLE_NAME, where, args);
    }


    public ArrayList<Shifts> getShifts(int jobId) {
        ArrayList<Shifts> shifts = new ArrayList<>();
        SQLiteDatabase db = helper.getWritableDatabase();
        String where = DatabaseHelper.JOB_FOREIGN_KEY + " = " + jobId;
        Cursor shiftCursor = db.query(DatabaseHelper.SHIFT_TABLE_NAME, null, where, null, null, null, null);
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
            shift.setShiftId(shiftCursor.getInt(shiftCursor.getColumnIndex(DatabaseHelper.SHIFT_ID)));
            shift.setStartTime(shiftCursor.getString(shiftCursor.getColumnIndex(DatabaseHelper.START_TIME)));
            shift.setEndTime(shiftCursor.getString(shiftCursor.getColumnIndex(DatabaseHelper.END_TIME)));
            shift.setTotalHours(Float.parseFloat(shiftCursor.getString(shiftCursor.getColumnIndex(DatabaseHelper.SHIFT_TOTAL_HOURS))));
            shift.setPaymentStatus(shiftCursor.getString(shiftCursor.getColumnIndex(DatabaseHelper.PAYMENT_STATUS)));
            shift.setJobId(shiftCursor.getInt(shiftCursor.getColumnIndex(DatabaseHelper.JOB_FOREIGN_KEY)));

            shifts.add(shift);

        }
        //Sort ArrayList by date before add
        Collections.sort(shifts, new Comparator<Shifts>() {
                    @Override
                    public int compare(Shifts lhs, Shifts rhs) {
                        return lhs.getStartDate().compareTo(rhs.getStartDate());
                    }
                }
        );

        return shifts;

    }

    public ArrayList<Shifts> getAllShifts() {
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
            shift.setShiftId(shiftCursor.getInt(shiftCursor.getColumnIndex(DatabaseHelper.SHIFT_ID)));
            shift.setStartTime(shiftCursor.getString(shiftCursor.getColumnIndex(DatabaseHelper.START_TIME)));
            shift.setEndTime(shiftCursor.getString(shiftCursor.getColumnIndex(DatabaseHelper.END_TIME)));
            shift.setTotalHours(Float.parseFloat(shiftCursor.getString(shiftCursor.getColumnIndex(DatabaseHelper.SHIFT_TOTAL_HOURS))));
            shift.setPaymentStatus(shiftCursor.getString(shiftCursor.getColumnIndex(DatabaseHelper.PAYMENT_STATUS)));
            shift.setJobId(shiftCursor.getInt(shiftCursor.getColumnIndex(DatabaseHelper.JOB_FOREIGN_KEY)));

            shifts.add(shift);

        }
        //Sort ArrayList by date before add
        Collections.sort(shifts, new Comparator<Shifts>() {
                    @Override
                    public int compare(Shifts lhs, Shifts rhs) {
                        return lhs.getStartDate().compareTo(rhs.getStartDate());
                    }
                }
        );

        return shifts;

    }

    public ArrayList<Shifts> getShiftsWithStatus(int jobId, String status) {
        ArrayList<Shifts> shifts = new ArrayList<>();
        SQLiteDatabase db = helper.getWritableDatabase();
        String where = DatabaseHelper.JOB_FOREIGN_KEY + " = ?" + " AND " + DatabaseHelper.PAYMENT_STATUS + " = ?";
        String[] args = {"" + jobId, "" + status};
        Cursor shiftCursor = db.query(DatabaseHelper.SHIFT_TABLE_NAME, null, where, args, null, null, null);
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
            shift.setShiftId(shiftCursor.getInt(shiftCursor.getColumnIndex(DatabaseHelper.SHIFT_ID)));
            shift.setStartTime(shiftCursor.getString(shiftCursor.getColumnIndex(DatabaseHelper.START_TIME)));
            shift.setEndTime(shiftCursor.getString(shiftCursor.getColumnIndex(DatabaseHelper.END_TIME)));
            shift.setTotalHours(Float.parseFloat(shiftCursor.getString(shiftCursor.getColumnIndex(DatabaseHelper.SHIFT_TOTAL_HOURS))));
            shift.setPaymentStatus(shiftCursor.getString(shiftCursor.getColumnIndex(DatabaseHelper.PAYMENT_STATUS)));
            shift.setJobId(shiftCursor.getInt(shiftCursor.getColumnIndex(DatabaseHelper.JOB_FOREIGN_KEY)));

            shifts.add(shift);

        }
        //Sort ArrayList by date before add
        Collections.sort(shifts, new Comparator<Shifts>() {
                    @Override
                    public int compare(Shifts lhs, Shifts rhs) {
                        return lhs.getStartDate().compareTo(rhs.getStartDate());
                    }
                }
        );

        return shifts;

    }

    public Date getNextShiftDate(int jobId) {
        ArrayList<Shifts> shifts = getShifts(jobId);
        Date nextShiftDate = new Date();
        Calendar today = Calendar.getInstance();
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.HOUR, 0);

        System.out.println(today.getTime() + " today date");
        Calendar shiftDate = Calendar.getInstance();
        for (int i = 0; i < shifts.size(); i++) {
            shiftDate.setTime(shifts.get(i).getStartDate());
            shiftDate.set(Calendar.SECOND, 0);
            shiftDate.set(Calendar.MINUTE, 0);
            shiftDate.set(Calendar.HOUR, 0);
            System.out.println(shiftDate.getTime() + " shift date");
            if ((shiftDate.getTime().after(today.getTime()))) {
                nextShiftDate = shiftDate.getTime();
                break;
            } else if (shiftDate.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
                    shiftDate.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR)) {
                nextShiftDate = shiftDate.getTime();
                break;
            } else {
                nextShiftDate = shiftDate.getTime();
            }
        }

        System.out.println(nextShiftDate);
        return nextShiftDate;
    }

    public long updateShifts(int jobId, float totalHours) {
        ArrayList<Shifts> shifts = getShiftsWithStatus(jobId, Constants.STATUS_UNPAID);
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        long updatedRow = Constants.NEGATIVE;
        String where = Constants.NA;

        //get current job
        Jobs currentJob = getJobWithJobId(jobId);
        double currentJobUnpaidHours = currentJob.getUnpaidHour();
        /* Update current Job */
        if (currentJobUnpaidHours >= totalHours) {
            cv.put(DatabaseHelper.UNPAID_HOUR, currentJobUnpaidHours - totalHours);
            where = DatabaseHelper.JOB_ID + " = " + jobId;
            updatedRow = db.update(DatabaseHelper.JOB_TABLE_NAME, cv, where, null);
        }

        /* Update all shift */
        for (int i = 0; i < shifts.size(); i++) {
            float shiftHour = shifts.get(i).getTotalHours();
            cv = new ContentValues();
            where = DatabaseHelper.JOB_FOREIGN_KEY + " = " + jobId + " AND " + DatabaseHelper.SHIFT_ID + " = " + shifts.get(i).getShiftId();
            if (shiftHour <= totalHours) {
                cv.put(DatabaseHelper.PAYMENT_STATUS, Constants.STATUS_PAID);
                updatedRow = db.update(DatabaseHelper.SHIFT_TABLE_NAME, cv, where, null);
                totalHours = totalHours - shiftHour;
            } else {
                cv.put(DatabaseHelper.PAYMENT_STATUS, Constants.STATUS_HALF_PAID);
                updatedRow = db.update(DatabaseHelper.SHIFT_TABLE_NAME, cv, where, null);
                totalHours = shiftHour - totalHours;
                /* Update half Paid Hour for current Job*/
                if (updatedRow > 0) {
                    cv = new ContentValues();
                    cv.put(DatabaseHelper.HALF_PAID_HOUR, totalHours);
                    where = DatabaseHelper.JOB_ID + " = " + jobId;
                    updatedRow = db.update(DatabaseHelper.JOB_TABLE_NAME, cv, where, null);
                }
                break;
            }
        }
        return updatedRow;
    }

    public long insertIntoPays(Pays payment) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.PAY_START_DATE, dateFormat.format(payment.getPayStartDate()));
        cv.put(DatabaseHelper.PAY_END_DATE, dateFormat.format(payment.getPayEndDate()));
        cv.put(DatabaseHelper.TOTAL_HOUR, payment.getTotalHours());
        cv.put(DatabaseHelper.GROSS_PAY, payment.getGrossPay());
        cv.put(DatabaseHelper.TOTAL_TAX, payment.getTotalTax());
        cv.put(DatabaseHelper.NET_PAY, payment.getNetPay());
        cv.put(DatabaseHelper.JOB_FOREIGN_KEY, payment.getJobId());
        long id = db.insert(DatabaseHelper.PAY_TABLE_NAME, null, cv);
        return id;
    }

    public ArrayList<Pays> getAllPays() {
        ArrayList<Pays> pays = new ArrayList<>();
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor payCursor = db.query(DatabaseHelper.PAY_TABLE_NAME, null, null, null, null, null, null);
        while (payCursor.moveToNext()) {
            Pays payment = new Pays();
            String sDate = payCursor.getString(payCursor.getColumnIndex(DatabaseHelper.PAY_START_DATE));
            String eDate = payCursor.getString(payCursor.getColumnIndex(DatabaseHelper.PAY_END_DATE));
            try {
                payment.setPayStartDate(dateFormat.parse(sDate));
                payment.setPayEndDate(dateFormat.parse(eDate));
            } catch (Exception e) {
                e.printStackTrace();
            }
            payment.setTotalHours(payCursor.getFloat(payCursor.getColumnIndex(DatabaseHelper.TOTAL_HOUR)));
            payment.setTotalTax(payCursor.getFloat(payCursor.getColumnIndex(DatabaseHelper.TOTAL_TAX)));
            payment.setGrossPay(payCursor.getFloat(payCursor.getColumnIndex(DatabaseHelper.GROSS_PAY)));
            payment.setNetPay(Float.parseFloat(payCursor.getString(payCursor.getColumnIndex(DatabaseHelper.NET_PAY))));
            payment.setJobId(payCursor.getInt(payCursor.getColumnIndex(DatabaseHelper.JOB_FOREIGN_KEY)));

            pays.add(payment);

        }

        return pays;

    }

    public static class DatabaseHelper extends SQLiteOpenHelper {

        private static final String DATABASE_NAME = "shiftManager.db";
        //Every time Change Version Value when database is modified
        private static final int DATABASE_VERSION = 6;

        //JOBS TABLE
        private static final String JOB_TABLE_NAME = "jobs";
        private static final String JOB_ID = "_id";
        private static final String COMPANY_NAME = "company_name";
        private static final String POSITION = "position";
        private static final String HOURLY_RATE = "hourly_rate";
        private static final String PAID_HOUR = "paid_hour";
        private static final String UNPAID_HOUR = "unpaid_hour";
        private static final String HALF_PAID_HOUR = "half_paid_hour";

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

        //PAY TABLE
        private static final String PAY_TABLE_NAME = "pays";
        private static final String PAY_ID = "_id";
        private static final String PAY_START_DATE = "start_date";
        private static final String PAY_END_DATE = "end_date";
        private static final String TOTAL_HOUR = "total_hour";
        private static final String GROSS_PAY = "gross_pay";
        private static final String TOTAL_TAX = "total_tax";
        private static final String NET_PAY = "net_pay";


        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            System.out.println("onCreate form database helper");

            db.execSQL("CREATE TABLE IF NOT EXISTS " + JOB_TABLE_NAME + " ("
                    + JOB_ID + " INTEGER PRIMARY KEY, "
                    + COMPANY_NAME + " VARCHAR(100), "
                    + POSITION + "  VARCHAR(100), "
                    + HOURLY_RATE + " VARCHAR(50), "
                    + PAID_HOUR + " VARCHAR(50), "
                    + UNPAID_HOUR + " VARCHAR(50), "
                    + HALF_PAID_HOUR + " VARCHAR(50) "
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

            db.execSQL("CREATE TABLE IF NOT EXISTS " + PAY_TABLE_NAME + " ("
                    + PAY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + TOTAL_HOUR + " VARCHAR(50), "
                    + GROSS_PAY + "  VARCHAR(50), "
                    + TOTAL_TAX + " VARCHAR(50), "
                    + NET_PAY + "  VARCHAR(50), "
                    + JOB_FOREIGN_KEY + "  INTEGER "
                    + ");");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            System.out.println("onUpgrade form database helper");
            System.out.println("Old Version is " + oldVersion + " and New Version is " + newVersion);

            if (oldVersion < 6) {
                db.execSQL("ALTER TABLE " + PAY_TABLE_NAME + " ADD COLUMN " + PAY_START_DATE +  " DATE;");
                db.execSQL("ALTER TABLE " + PAY_TABLE_NAME + " ADD COLUMN " + PAY_END_DATE +  " DATE;");
            }


        }
    }
}
