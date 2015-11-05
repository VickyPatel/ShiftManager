package vickypatel.ca.shiftmanager.adapters;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

import vickypatel.ca.shiftmanager.R;
import vickypatel.ca.shiftmanager.callbacks.ItemTouchHelperAdapter;
import vickypatel.ca.shiftmanager.database.DatabaseAdapter;
import vickypatel.ca.shiftmanager.extras.Constants;
import vickypatel.ca.shiftmanager.pojo.Shifts;

/**
 * Created by vicky on 05/11/15.
 */
public class AllShiftAdapter extends RecyclerView.Adapter<AllShiftAdapter.ViewHolder> implements ItemTouchHelperAdapter {

    ArrayList<Shifts> shifts = new ArrayList<>();
    public Context context;

    public AllShiftAdapter(Context context) {
        this.context = context;
        DatabaseAdapter adapter = new DatabaseAdapter(context);
        shifts = adapter.getAllShifts();

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_row_all_shift, parent, false);
        return new ViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(shifts.get(position).getStartDate());

        holder.startTime.setText(shifts.get(position).getStartTime());
        holder.endTime.setText(shifts.get(position).getEndTime());

//        float temp = shifts.get(position).getTotalHours();
//        System.out.println((int) temp + " hr total");
//        double tempTotal = (temp % 1) * 0.6;
//        tempTotal = (double) Math.round(tempTotal * 100d) / 100d;
//        int min = (int) (tempTotal * 100);
//        String str = (int) temp + ":" + ((min < 10) ? "0" + min : min) + " HR";
//        holder.totalHours.setText(str);

//        switch (shifts.get(position).getPaymentStatus()) {
//            case Constants.STATUS_PAID:
//                holder.paymentStatus.setText("PAID");
//                holder.paymentStatus.setTextColor(context.getResources().getColor(R.color.positiveAction));
//                break;
//            case Constants.STATUS_UNPAID:
//                holder.paymentStatus.setText("UNPAID");
//                holder.paymentStatus.setTextColor(context.getResources().getColor(R.color.colorPrimary));
//                break;
//            case Constants.STATUS_HALF_PAID:
//                holder.paymentStatus.setText("HALF PAID");
//                holder.paymentStatus.setTextColor(context.getResources().getColor(R.color.colorAccent));
//                break;
//
//        }
    }

    @Override
    public int getItemCount() {
        return shifts.size();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        return false;
    }

    @Override
    public void onItemDismiss(int position) {

    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        TextView startDate, endDate, startTime, endTime, paymentStatus, totalHours;
        LinearLayout undoLayout;
        FloatingActionButton companyFab;

        public ViewHolder(View itemView, int viewType) {
            super(itemView);

            startTime = (TextView) itemView.findViewById(R.id.startTime);
            endTime = (TextView) itemView.findViewById(R.id.endTime);
//            paymentStatus = (TextView) itemView.findViewById(R.id.paymentStatus);
//            totalHours = (TextView) itemView.findViewById(R.id.totalHours);
            undoLayout = (LinearLayout) itemView.findViewById(R.id.undo);
            companyFab = (FloatingActionButton) itemView.findViewById(R.id.company_fab);


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

}
