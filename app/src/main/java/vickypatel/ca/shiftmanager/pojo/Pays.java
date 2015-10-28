package vickypatel.ca.shiftmanager.pojo;

import java.util.Date;

/**
 * Created by VickyPatel on 2015-10-09.
 */
public class Pays {
    private Date payStartDate;
    private Date payEndDate;
    float totalHours;
    float totalTax;
    float grossPay;
    float netPay;
    int jobId;

    public Date getPayStartDate() {
        return payStartDate;
    }

    public void setPayStartDate(Date payStartDate) {
        this.payStartDate = payStartDate;
    }

    public Date getPayEndDate() {
        return payEndDate;
    }

    public void setPayEndDate(Date payEndDate) {
        this.payEndDate = payEndDate;
    }

    public float getTotalHours() {
        return totalHours;
    }

    public void setTotalHours(float totalHours) {
        this.totalHours = totalHours;
    }

    public float getTotalTax() {
        return totalTax;
    }

    public void setTotalTax(float totalTax) {
        this.totalTax = totalTax;
    }

    public float getGrossPay() {
        return grossPay;
    }

    public void setGrossPay(float grossPay) {
        this.grossPay = grossPay;
    }

    public float getNetPay() {
        return netPay;
    }

    public void setNetPay(float netPay) {
        this.netPay = netPay;
    }

    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }
}
