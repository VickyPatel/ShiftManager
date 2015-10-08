package vickypatel.ca.shiftmanager.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import vickypatel.ca.shiftmanager.Activities.ActivityAddJob;
import vickypatel.ca.shiftmanager.R;
import vickypatel.ca.shiftmanager.adapters.JobsAdapter;
import vickypatel.ca.shiftmanager.callbacks.ItemTouchHelperAdapter;
import vickypatel.ca.shiftmanager.callbacks.LongPressHelper;
import vickypatel.ca.shiftmanager.callbacks.SimpleItemTouchHelperCallback;
import vickypatel.ca.shiftmanager.extras.Constants;

public class ActivityJobs extends AppCompatActivity implements LongPressHelper, View.OnClickListener{
    RecyclerView mRecycleView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    Button editButton, deleteButton;
    int jobId = Constants.ZERO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobs);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("ActivityJobs");
        setSupportActionBar(toolbar);

        mRecycleView = (RecyclerView) findViewById(R.id.jobsList);
        mRecycleView.setHasFixedSize(true);

        mAdapter = new JobsAdapter(this);
        mRecycleView.setAdapter(mAdapter);

        mLayoutManager = new LinearLayoutManager(this);
        mRecycleView.setLayoutManager(mLayoutManager);


        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback((ItemTouchHelperAdapter) mAdapter, this);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(mRecycleView);

        editButton = (Button) findViewById(R.id.edit_button);
        deleteButton = (Button) findViewById(R.id.delete_button);

        editButton.setOnClickListener(this);
        deleteButton.setOnClickListener(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ActivityAddJob.class);
                startActivity(i);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_jobs, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onJobSelected(int jobId) {
        this.jobId = jobId;
        editButton.setVisibility(View.VISIBLE);
        deleteButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.edit_button:
                System.out.println("edit called");
                Intent i = new Intent(getApplicationContext(), ActivityAddJob.class);
                i.putExtra(Constants.JOB_ID, jobId);
                startActivity(i);
                break;
            case  R.id.delete_button:
                break;
        }
    }
}
