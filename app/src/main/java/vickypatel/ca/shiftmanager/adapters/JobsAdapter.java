package vickypatel.ca.shiftmanager.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import vickypatel.ca.shiftmanager.Activities.ActivityShifts;
import vickypatel.ca.shiftmanager.R;
import vickypatel.ca.shiftmanager.database.DatabaseAdapter;
import vickypatel.ca.shiftmanager.extras.Constants;
import vickypatel.ca.shiftmanager.pojo.Jobs;

/**
 * Created by VickyPatel on 2015-09-30.
 */
public class JobsAdapter extends RecyclerView.Adapter<JobsAdapter.ViewHolder> {

    ArrayList<Jobs> jobs = new ArrayList<>();
    public Context context;
    DatabaseAdapter adapter;

    public JobsAdapter(Context context) {
        this.context = context;
        adapter = new DatabaseAdapter(context);
        jobs = adapter.getJobs();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_row_jobs, parent, false);
        ViewHolder viewHolder = new ViewHolder(view, viewType);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.companyName.setText(jobs.get(position).getCompanyName());
        holder.position.setText(jobs.get(position).getPosition());

        Date nextShiftDate = adapter.getNextShiftDate(jobs.get(position).getJobId());
        Calendar c = Calendar.getInstance();
        Calendar today = Calendar.getInstance();
        c.setTime(nextShiftDate);
        String day = new SimpleDateFormat("EE, MMM dd/yyyy").format(c.getTime());
        holder.nextShiftDate.setText(day);
        if(c.getTime().after(today.getTime())){
            holder.nextShiftDate.setTextColor(context.getResources().getColor(R.color.colorAccent));
        }else if(c.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
                c.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR)){
            holder.nextShiftDate.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        }else if(c.getTime().before(today.getTime())){
            holder.nextShiftDate.setTextColor(context.getResources().getColor(R.color.positiveAction));
        }

    }

    @Override
    public int getItemCount() {
        return jobs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        TextView companyName, position, nextShiftDate;
        RelativeLayout jobsLayout;
        ImageView imageView;


        public ViewHolder(View itemView, int viewType) {
            super(itemView);
            jobsLayout = (RelativeLayout) itemView.findViewById(R.id.jobs_layout);
            companyName = (TextView) itemView.findViewById(R.id.company_name_text);
            position = (TextView) itemView.findViewById(R.id.position_text);
            nextShiftDate = (TextView) itemView.findViewById(R.id.next_shift_date_text);

            jobsLayout.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            System.out.println("I m clicked at position" + getAdapterPosition());
            Intent intent = new Intent(context, ActivityShifts.class);
            intent.putExtra(Constants.JOB_ID, jobs.get(getAdapterPosition()).getJobId());
            context.startActivity(intent);

        }

        @Override
        public boolean onLongClick(View v) {
            v.setBackgroundColor(context.getApplicationContext().getResources().getColor(R.color.divider));
            v.setTag(1,123);
            return true;
        }
    }
}
