package vickypatel.ca.shiftmanager.Activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import vickypatel.ca.shiftmanager.Activities.ActivityAddJob;
import vickypatel.ca.shiftmanager.R;
import vickypatel.ca.shiftmanager.adapters.JobsAdapter;
import vickypatel.ca.shiftmanager.adapters.NavigationAdapter;
import vickypatel.ca.shiftmanager.callbacks.ItemTouchHelperAdapter;
import vickypatel.ca.shiftmanager.callbacks.LongPressHelper;
import vickypatel.ca.shiftmanager.callbacks.SimpleItemTouchHelperCallback;
import vickypatel.ca.shiftmanager.database.DatabaseAdapter;
import vickypatel.ca.shiftmanager.extras.Constants;

public class ActivityJobs extends AppCompatActivity implements LongPressHelper {
    RecyclerView mRecycleView, navigationRecycleView;
    RecyclerView.Adapter mAdapter, navigationAdapter;
    RecyclerView.LayoutManager mLayoutManager, navigationLayoutManager;
    DrawerLayout navigationDrawer;
    ActionBarDrawerToggle navigationDrawerToggle;
    Dialog dialog;
    MenuItem editButton, deleteButton;
    LinearLayout tittleLayout, contentLayout;
    TextView txtTitle, txtMessage;
    int jobId = Constants.ZERO;
    int position = Constants.NEGATIVE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobs);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Navigation Drawer
        navigationRecycleView = (RecyclerView) findViewById(R.id.menuRecycleView);
        navigationRecycleView.setHasFixedSize(true);

        navigationAdapter = new NavigationAdapter(this);
        navigationRecycleView.setAdapter(navigationAdapter);

        navigationLayoutManager = new LinearLayoutManager(this);
        navigationRecycleView.setLayoutManager(navigationLayoutManager);

        navigationDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationDrawerToggle = new ActionBarDrawerToggle(this, navigationDrawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };

        navigationDrawer.setDrawerListener(navigationDrawerToggle);
        navigationDrawerToggle.syncState();

        mRecycleView = (RecyclerView) findViewById(R.id.jobsList);
        mRecycleView.setHasFixedSize(true);

        mAdapter = new JobsAdapter(this);
        mRecycleView.setAdapter(mAdapter);

        mLayoutManager = new LinearLayoutManager(this);
        mRecycleView.setLayoutManager(mLayoutManager);


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
    public boolean onPrepareOptionsMenu(Menu menu) {
        editButton = menu.findItem(R.id.action_edit);
        deleteButton = menu.findItem(R.id.action_delete);
        editButton.setVisible(false);
        deleteButton.setVisible(false);
        return true;
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

        switch (item.getItemId()) {
            case R.id.action_edit:
                System.out.println("edit called");
                Intent i = new Intent(getApplicationContext(), ActivityAddJob.class);
                i.putExtra(Constants.JOB_ID, jobId);
                startActivity(i);
                return true;

            case R.id.action_delete:
                initializeSimpleDialog();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }

    }

    @Override
    public void onJobSelected(int jobId, int position) {
        this.jobId = jobId;
        this.position = position;
        editButton.setVisible(true);
        deleteButton.setVisible(true);
    }



    public void initializeSimpleDialog() {
        //Custom dialog
        dialog = new Dialog(this);
        // hide to default title for Dialog
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // inflate the layout dialog_layout.xml and set it as contentView
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.custom_dialog, null, false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(view);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

        // Retrieve views from the inflated dialog layout and update their values
        tittleLayout = (LinearLayout) dialog.findViewById(R.id.layout_dialog_tittle);
        txtTitle = (TextView) dialog.findViewById(R.id.txt_dialog_title);
        txtTitle.setText("Tittle");

        contentLayout = (LinearLayout) dialog.findViewById(R.id.layout_dialog_content);
        txtMessage = (TextView) dialog.findViewById(R.id.txt_dialog_message);
        txtMessage.setText("Content.");

        Button btnOk = (Button) dialog.findViewById(R.id.btn_ok);
        btnOk.setText("DELETE");
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                DatabaseAdapter adapter = new DatabaseAdapter(ActivityJobs.this);
                int deletedRow = adapter.deleteJobWithId(jobId);
                adapter.deleteAllShiftForJob(jobId);
                adapter.deleteAllPaysForJob(jobId);


                System.out.println(deletedRow + " row deleted");
                System.out.println(position + " position");
                if (deletedRow > 0) {
                    mAdapter.notifyItemRemoved(position);
                    finish();
                    startActivity(getIntent());
                }
            }
        });

        Button btnCancel = (Button) dialog.findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close the dialog
                dialog.dismiss();
            }
        });

        txtTitle.setText("Delete a Job?");
        txtMessage.setText("Are you sure want to delete a job and It will delete all content related to this job from your device.");
        // Display the dialog
        dialog.show();


    }

}
