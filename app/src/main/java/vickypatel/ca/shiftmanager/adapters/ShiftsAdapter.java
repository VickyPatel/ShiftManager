package vickypatel.ca.shiftmanager.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.regex.Pattern;

import javax.sql.StatementEvent;

import vickypatel.ca.shiftmanager.Activities.ActivityShifts;
import vickypatel.ca.shiftmanager.R;
import vickypatel.ca.shiftmanager.callbacks.ItemTouchHelperAdapter;
import vickypatel.ca.shiftmanager.database.DatabaseAdapter;
import vickypatel.ca.shiftmanager.extras.Constants;
import vickypatel.ca.shiftmanager.pojo.Jobs;
import vickypatel.ca.shiftmanager.pojo.Shifts;

/**
 * Created by VickyPatel on 2015-10-03.
 */
public class ShiftsAdapter extends RecyclerView.Adapter<ShiftsAdapter.ViewHolder> implements ItemTouchHelperAdapter {

    ArrayList<Shifts> shifts = new ArrayList<>();
    public Context context;
    int jobId = Constants.ZERO;

    public ShiftsAdapter(Context context, int jobId) {
        this.context = context;
        this.jobId = jobId;
        DatabaseAdapter adapter = new DatabaseAdapter(context);
        if(this.jobId == Constants.ZERO){
            shifts = adapter.getAllShifts();
        }else {
            shifts = adapter.getShifts(jobId);
        }
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(shifts, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(shifts, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        DatabaseAdapter adapter = new DatabaseAdapter(context);
        int deletedRow = adapter.deleteShift(shifts.get(position).getShiftId(), jobId);

        System.out.println(deletedRow);

        if (deletedRow > 0) {
            shifts.remove(position);
            notifyItemRemoved(position);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_row_shifts, parent, false);
        return new ViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(shifts.get(position).getStartDate());
        holder.startDate.setText(getMonthName(cal.get(Calendar.MONTH)) + " " + cal.get(Calendar.DATE));

        holder.startTime.setText(shifts.get(position).getStartTime());
        holder.endTime.setText(shifts.get(position).getEndTime());
        float temp = shifts.get(position).getTotalHours();
        System.out.println((int) temp + " hr total");
        double tempTotal = (temp % 1) * 0.6;
        tempTotal = (double) Math.round(tempTotal * 100d) / 100d;
        int min = (int)(tempTotal * 100);
        String str = (int) temp + ":" + ((min < 10) ? "0"+ min : min ) + " HR";
        holder.totalHours.setText(str);

        switch (shifts.get(position).getPaymentStatus()) {
            case Constants.STATUS_PAID:
                holder.paymentStatus.setText("PAID");
                holder.paymentStatus.setTextColor(context.getResources().getColor(R.color.positiveAction));
                break;
            case Constants.STATUS_UNPAID:
                holder.paymentStatus.setText("UNPAID");
                holder.paymentStatus.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                break;
            case Constants.STATUS_HALF_PAID:
                holder.paymentStatus.setText("HALF PAID");
                holder.paymentStatus.setTextColor(context.getResources().getColor(R.color.colorAccent));
                break;

        }
    }

    @Override
    public int getItemCount() {
        return shifts.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        TextView startDate, endDate, startTime, endTime, paymentStatus, totalHours;
        LinearLayout undoLayout;

        public ViewHolder(View itemView, int viewType) {
            super(itemView);

            startDate = (TextView) itemView.findViewById(R.id.startDate);
            startTime = (TextView) itemView.findViewById(R.id.startTime);
            endTime = (TextView) itemView.findViewById(R.id.endTime);
            paymentStatus = (TextView) itemView.findViewById(R.id.paymentStatus);
            totalHours = (TextView) itemView.findViewById(R.id.totalHours);
            undoLayout = (LinearLayout) itemView.findViewById(R.id.undo);


        }

        @Override
        public void onClick(View v) {

        }

        @Override
        public boolean onLongClick(View v) {
            Toast.makeText(context.getApplicationContext(), "You have pressed it long", Toast.LENGTH_LONG).show();
            return true;
        }
    }

    public static String getMonthName(int month) {
        String[] monthNames = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        return monthNames[month];
    }
}
