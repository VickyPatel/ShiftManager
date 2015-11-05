package vickypatel.ca.shiftmanager.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.KeyEvent;
import android.view.View;

import vickypatel.ca.shiftmanager.R;
import vickypatel.ca.shiftmanager.adapters.JobsAdapter;
import vickypatel.ca.shiftmanager.adapters.ShiftsAdapter;
import vickypatel.ca.shiftmanager.callbacks.ItemTouchHelperAdapter;
import vickypatel.ca.shiftmanager.callbacks.SimpleItemTouchHelperCallback;
import vickypatel.ca.shiftmanager.extras.Constants;

public class ActivityShifts extends AppCompatActivity {

    Bundle bundle = null;
    public int jobId = Constants.ZERO;
    RecyclerView mRecycleView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shifts);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Shifts");
        setSupportActionBar(toolbar);

        bundle = getIntent().getExtras();
        if (bundle != null) {
            jobId = bundle.getInt(Constants.JOB_ID);
        }

        mRecycleView = (RecyclerView) findViewById(R.id.shiftsList);
        mRecycleView.setHasFixedSize(true);

        mAdapter = new ShiftsAdapter(this, jobId);
        mRecycleView.setAdapter(mAdapter);

        mLayoutManager = new LinearLayoutManager(this);
        mRecycleView.setLayoutManager(mLayoutManager);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback((ItemTouchHelperAdapter) mAdapter, this);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(mRecycleView);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                Intent i = new Intent(getApplicationContext(), ActivityAddShift.class);
                i.putExtra(Constants.JOB_ID, jobId);
                startActivity(i);

            }
        });

//        if(jobId == Constants.ZERO){
//            fab.setVisibility(View.GONE);
//        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //Changes 'back' button action
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            startActivity(new Intent(this, ActivityJobs.class));
        }
        return true;
    }


}
