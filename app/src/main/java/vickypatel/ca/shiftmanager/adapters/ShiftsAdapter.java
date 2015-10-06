package vickypatel.ca.shiftmanager.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

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

    public ShiftsAdapter(Context context) {
        this.context = context;
        DatabaseAdapter adapter = new DatabaseAdapter(context);
        shifts = adapter.getShifts();
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
        shifts.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_row_shifts, parent, false);
        ViewHolder viewHolder = new ViewHolder(view, viewType);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(shifts.get(position).getStartDate());
        holder.startDate.setText(cal.get(Calendar.DATE)+"");
        cal.setTime(shifts.get(position).getEndDate());
        holder.endDate.setText(cal.get(Calendar.DATE)+"");
        holder.startTime.setText(shifts.get(position).getStartTime());
        holder.endTime.setText(shifts.get(position).getEndTime());
        holder.totalHours.setText(shifts.get(position).getTotalHours());

        switch (shifts.get(position).getPaymentStatus()){

            case Constants.STATUS_PAID:
                holder.paymentStatus.setText("PAID");
                break;
            case Constants.STATUS_UNPAID:
                holder.paymentStatus.setText("UNPAID");
                break;
            case Constants.STATUS_HAULT:
                holder.paymentStatus.setText("HAULT");
                break;

        }
    }

    @Override
    public int getItemCount() {
        return shifts.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView startDate, endDate, startTime, endTime, paymentStatus, totalHours;


        public ViewHolder(View itemView, int viewType) {
            super(itemView);

            startDate = (TextView) itemView.findViewById(R.id.startDate);
            endDate = (TextView) itemView.findViewById(R.id.endDate);
            startTime = (TextView) itemView.findViewById(R.id.startTime);
            endTime = (TextView) itemView.findViewById(R.id.endTime);
            paymentStatus = (TextView) itemView.findViewById(R.id.paymentStatus);
            totalHours = (TextView) itemView.findViewById(R.id.totalHours);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {


        }
    }
}
