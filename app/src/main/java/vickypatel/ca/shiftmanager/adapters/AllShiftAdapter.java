package vickypatel.ca.shiftmanager.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;

import vickypatel.ca.shiftmanager.R;
import vickypatel.ca.shiftmanager.callbacks.ItemTouchHelperAdapter;
import vickypatel.ca.shiftmanager.database.DatabaseAdapter;
import vickypatel.ca.shiftmanager.extras.Constants;
import vickypatel.ca.shiftmanager.pojo.Jobs;
import vickypatel.ca.shiftmanager.pojo.Shifts;

/**
 * Created by vicky on 05/11/15.
 */
public class AllShiftAdapter extends RecyclerView.Adapter<AllShiftAdapter.ViewHolder> implements ItemTouchHelperAdapter {

    ArrayList<Shifts> shifts = new ArrayList<>();
    public Context context;
    public CalendarDay selectedDate;
    public DatabaseAdapter adapter;

    public AllShiftAdapter(Context context, CalendarDay selectedDate) {
        this.context = context;
        this.selectedDate = selectedDate;
        adapter = new DatabaseAdapter(context);
        shifts = adapter.getShiftsWithDate(selectedDate);
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

        Jobs currentJob = adapter.getJobWithJobId(shifts.get(position).getJobId());


        holder.startTime.setText(shifts.get(position).getStartTime());
        holder.endTime.setText(shifts.get(position).getEndTime());

        //Create Canvas and add Company first Letter on it
        BitmapDrawable drawable = (BitmapDrawable) holder.companyFab.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        Bitmap workingBitmap = Bitmap.createBitmap(bitmap);
        Bitmap src = workingBitmap.copy(Bitmap.Config.ARGB_8888, true);
        Bitmap dest = Bitmap.createBitmap(src.getWidth(), src.getHeight(), Bitmap.Config.ARGB_8888);
        String yourText = currentJob.getCompanyName().substring(0,1).toUpperCase();
        Canvas cs = new Canvas(dest);
        Paint tPaint = new Paint();
        tPaint.setTextSize(150);
        tPaint.setColor(context.getApplicationContext().getResources().getColor(Constants.DATE_COLORS[0]));
        tPaint.setStyle(Paint.Style.FILL);
        cs.drawBitmap(src, 0f, 0f, null);
        float height = tPaint.measureText("yY");
        float width = tPaint.measureText(yourText);
        float x_coord = (src.getWidth() - width)/2;
        cs.drawText(yourText, x_coord, height + 25f, tPaint); // 15f is to put space between top edge and the text, if you want to change it, you can
        BitmapDrawable finalDrawable = new BitmapDrawable(context.getResources(), dest);
        holder.companyFab.setBackgroundDrawable(finalDrawable);

        holder.companyFab.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(Constants.DATE_COLORS[0])));

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
