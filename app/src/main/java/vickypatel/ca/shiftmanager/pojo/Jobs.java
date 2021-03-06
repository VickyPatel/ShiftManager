package vickypatel.ca.shiftmanager.pojo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by VickyPatel on 2015-09-29.
 */
public class Jobs implements Parcelable {
    private int jobId;
    private String companyName;
    private String position;
    private double hourlyRate;
    private double unpaidHour;
    private double paidHour;
    private double halfPaidHour;

    public Jobs() {
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public double getHourlyRate() {
        return hourlyRate;
    }

    public double getUnpaidHour() {
        return unpaidHour;
    }

    public void setUnpaidHour(double unpaidHour) {
        this.unpaidHour = unpaidHour;
    }

    public double getPaidHour() {
        return paidHour;
    }

    public void setPaidHour(double paidHour) {
        this.paidHour = paidHour;
    }

    public double getHalfPaidHour() {
        return halfPaidHour;
    }

    public void setHalfPaidHour(double halfPaidHour) {
        this.halfPaidHour = halfPaidHour;
    }

    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    public void setHourlyRate(double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    protected Jobs(Parcel in) {
        companyName = in.readString();
        position = in.readString();
        hourlyRate = in.readDouble();
    }

    public static final Creator<Jobs> CREATOR = new Creator<Jobs>() {
        @Override
        public Jobs createFromParcel(Parcel in) {
            return new Jobs(in);
        }

        @Override
        public Jobs[] newArray(int size) {
            return new Jobs[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(companyName);
        dest.writeString(position);
        dest.writeDouble(hourlyRate);
    }
}
