package vickypatel.ca.shiftmanager.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

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

    public JobsAdapter(Context context) {
        this.context = context;
        DatabaseAdapter adapter = new DatabaseAdapter(context);
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

    }

    @Override
    public int getItemCount() {
        return jobs.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView companyName, position;
        ImageView imageView;


        public ViewHolder(View itemView, int viewType) {
            super(itemView);

            companyName = (TextView) itemView.findViewById(R.id.company_name_text);
            position = (TextView) itemView.findViewById(R.id.position_text);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            System.out.println("I m clicked at position" + getAdapterPosition());
            Intent intent = new Intent(context, ActivityShifts.class);
            intent.putExtra(Constants.JOB_ID, getAdapterPosition());
            context.startActivity(intent);

        }
    }
}
