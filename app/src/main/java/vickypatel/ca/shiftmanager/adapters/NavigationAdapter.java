package vickypatel.ca.shiftmanager.adapters;


/**
 * Created by VickyPatel on 2015-10-08.
 */


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;

import vickypatel.ca.shiftmanager.Activities.ActivityJobs;
import vickypatel.ca.shiftmanager.Activities.ActivityPayTime;
import vickypatel.ca.shiftmanager.Activities.ActivityShifts;
import vickypatel.ca.shiftmanager.Activities.ActivitySummary;
import vickypatel.ca.shiftmanager.Activities.ContactUsActivity;
import vickypatel.ca.shiftmanager.R;

public class NavigationAdapter extends RecyclerView.Adapter<NavigationAdapter.ViewHolder> {
    public Context context;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM1 = 1;
    private static final int TYPE_DIVIDER = 2;
    private static final int TYPE_ITEM2 = 3;

    private String[] titles = {"Jobs", "Shifts"};
    private String[] titles2 = {"Pay Time", "Summary", "Settings"};
    private int[] icons = {R.drawable.ic_jobs, R.drawable.ic_shifts, R.drawable.ic_pay_time, R.drawable.ic_summary, R.drawable.ic_settings};
    private String name = "Vicky";
    private int profile = R.drawable.ic_add;
    private String email = "vicky.trainerpl.us";


    public NavigationAdapter(Context contexts) {
        this.context = contexts;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder = null;
        if (viewType == TYPE_HEADER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_navigation_drawer, parent, false);
            viewHolder = new ViewHolder(view, viewType);

        } else if (viewType == TYPE_ITEM1) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_row_navigation_drawer, parent, false);
            viewHolder = new ViewHolder(view, viewType);

        } else if (viewType == TYPE_DIVIDER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.divider_navigation_drawer, parent, false);
            viewHolder = new ViewHolder(view, viewType);

        } else if (viewType == TYPE_ITEM2) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_row_navigation_drawer, parent, false);
            viewHolder = new ViewHolder(view, viewType);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (holder.Holderid == 0) {
            InputStream ims1 = null;
            InputStream ims2 = null;
            try {
                ims1 = context.getAssets().open("background_pattern.png");
                ims2 = context.getAssets().open("profile.jpg");
            } catch (IOException e) {
                e.printStackTrace();
            }
            Drawable d1 = Drawable.createFromStream(ims1, null);
            Drawable d2 = Drawable.createFromStream(ims2, null);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                holder.headerLayout.setBackground(d1);
            } else {
                holder.headerLayout.setBackgroundDrawable(d1);
            }
            holder.profile.setImageDrawable(d2);
            holder.name.setText(name);
            holder.email.setText(email);
        } else if (holder.Holderid == 1) {
            holder.textView.setText(titles[position - 1]);
            holder.imageView.setImageResource(icons[position - 1]);
        } else if (holder.Holderid == 2) {
            holder.lineView.setBackgroundColor(context.getResources().getColor(R.color.divider));
        } else if (holder.Holderid == 3) {
            int n = titles.length + 2;
            holder.textView2.setText(titles2[position - n]);
            holder.imageView2.setImageResource(icons[position - n + 2]);
        }
    }

    @Override
    public int getItemCount() {
        return 1 + titles.length + 1 + titles2.length;
    }

    @Override
    public int getItemViewType(int position) {
        int currentPosition = 0;
        if (position == 0) {
            currentPosition = TYPE_HEADER;
        } else if (position >= 1 && position < titles.length + 1) {
            currentPosition = TYPE_ITEM1;
        } else if (position == titles.length + 1) {
            currentPosition = TYPE_DIVIDER;
        } else if (position > titles.length + 1) {
            currentPosition = TYPE_ITEM2;
        }
        return currentPosition;
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        int Holderid;
        TextView textView;
        ImageView imageView;
        TextView textView2;
        ImageView imageView2;
        RelativeLayout headerLayout;
        View lineView;
        ImageView profile;
        TextView name;
        TextView email;
        LinearLayout menuLayout1, menuLayout2;

        public ViewHolder(View itemView, int viewType) {
            super(itemView);

            if (viewType == TYPE_HEADER) {
                name = (TextView) itemView.findViewById(R.id.name);
                email = (TextView) itemView.findViewById(R.id.email);
                profile = (ImageView) itemView.findViewById(R.id.circleImageView);
                headerLayout = (RelativeLayout) itemView.findViewById(R.id.header_layout);
                Holderid = 0;
            } else if (viewType == TYPE_ITEM1) {
                textView = (TextView) itemView.findViewById(R.id.rowText);
                imageView = (ImageView) itemView.findViewById(R.id.rowIcon);
                menuLayout1 = (LinearLayout) itemView.findViewById(R.id.menu_layout);
                menuLayout1.setOnClickListener(this);
                Holderid = 1;
            } else if (viewType == TYPE_DIVIDER) {
                lineView = itemView.findViewById(R.id.line);
                Holderid = 2;
            } else if (viewType == TYPE_ITEM2) {
                textView2 = (TextView) itemView.findViewById(R.id.rowText);
                imageView2 = (ImageView) itemView.findViewById(R.id.rowIcon);
                menuLayout2 = (LinearLayout) itemView.findViewById(R.id.menu_layout);
                menuLayout2.setOnClickListener(this);
                Holderid = 3;
            }

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.menu_layout:
                    if (getAdapterPosition() == 1) {
                        Intent i = new Intent(context, ActivityJobs.class);
                        context.startActivity(i);
                    } else if (getAdapterPosition() == 2) {
                        Intent i = new Intent(context, ActivityShifts.class);
                        context.startActivity(i);
                    } else if (getAdapterPosition() == 4) {
                        Intent i = new Intent(context, ActivityPayTime.class);
                        context.startActivity(i);
                    } else if (getAdapterPosition() == 5) {
                        Intent i = new Intent(context, ActivitySummary.class);
                        context.startActivity(i);
                    } else if (getAdapterPosition() == 6) {
                        Intent i = new Intent(context, ContactUsActivity.class);
                        context.startActivity(i);
                    }
                    break;
            }
        }
    }

}
