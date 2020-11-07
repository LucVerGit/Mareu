package com.example.ui;
import android.app.DatePickerDialog;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import androidx.recyclerview.widget.RecyclerView;


import com.example.ui.controler.AddMeetingFragment;
import com.example.ui.controler.MeetingDetailActivity;
import com.example.ui.controler.MeetingListRecyclerViewAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import DI.DI;
import Event.AddMeetingEvent;
import Event.DeleteMeetingEvent;
import Model.Meeting;
import Service.MeetingApiService;


public class MainActivity extends AppCompatActivity{
    Toolbar mToolbar;
    RecyclerView mRecyclerView;
    FloatingActionButton mAddMeetingButton;
    MeetingListRecyclerViewAdapter mRecyclerViewAdapter;

    MeetingApiService mApiService = DI.getMeetingApiService();
    List<Meeting> mMeetingsArrayList = new ArrayList<>();
    Boolean isDateFiltered = false;
    Boolean isLocationFiltered = false;
    Date dateFilterSelected;
    String roomFilterSelected = "";
    String [] listOfMeetingRooms;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAddMeetingButton = findViewById(R.id.add_meetings);
        mRecyclerView = findViewById(R.id.list_meetings);
        listOfMeetingRooms = listRoomsInStrings();

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);


        mRecyclerViewAdapter = new MeetingListRecyclerViewAdapter(mApiService.getMeetings());
        mRecyclerView.setAdapter(mRecyclerViewAdapter);

        // Add meeting button listener
        mAddMeetingButton.setOnClickListener(v -> {
            Intent addActivityIntent = new Intent(MainActivity.this, AddMeetingFragment.class);
            startActivity(addActivityIntent);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater Inflater = getMenuInflater();
        Inflater.inflate(R.menu.menu_filter, menu);
        return true;
    }

    /**
     * Menu Filter selection
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            // Room filter
            case R.id.menuItem1:
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                mBuilder.setTitle(R.string.meeting_room_filter);
                mBuilder.setSingleChoiceItems(listOfMeetingRooms, -1, (dialog, which) -> {
                    System.out.println(which);
                    roomFilterSelected = listOfMeetingRooms[which];
                    initList();
                });
                mBuilder.setPositiveButton(R.string.Ok, null);
                mBuilder.setNeutralButton(R.string.cancel, (dialog, which) -> {
                    isLocationFiltered = false;
                    initList();
                });
                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
                isLocationFiltered = true;
                return true;

            // Date filter
            case R.id.menuItem2:
                final Calendar cldr = Calendar.getInstance();
                int year = cldr.get(Calendar.YEAR);
                int month = cldr.get(Calendar.MONTH);
                int day = cldr.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(MainActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        (view, year1, month1, dayOfMonth) -> {
                            Calendar calendar = Calendar.getInstance();
                            calendar.set(Calendar.YEAR, year1);
                            calendar.set(Calendar.MONTH, month1);
                            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                            dateFilterSelected = calendar.getTime();
                            initList();

                        }, year, month, day);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.show();
                isDateFiltered = true;
                return true;

            // Clear filter
            case R.id.menuItem3:
                isDateFiltered = false;
                isLocationFiltered = false;
                initList();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Init the List of meetings
     */
    private void initList() {
        // Filter the list if one is set
        mMeetingsArrayList = mApiService.meetingListFilter(isDateFiltered, isLocationFiltered, roomFilterSelected, dateFilterSelected);
        // Set the RecyclerViewAdapter
        mRecyclerView.setAdapter(new MeetingListRecyclerViewAdapter(mMeetingsArrayList));
    }

    // App life cycles
    @Override
    public void onResume() {
        super.onResume();
        initList();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    // Delete the meeting from the list when trash icon is clicked
    @Subscribe
    public void onDeleteMeeting(DeleteMeetingEvent event) {
        mApiService.deleteMeeting(event.meeting);
        initList();
    }

    // Go to the DetailActivity when a meeting cell is clicked
    @Subscribe
    public void onClickMeeting(AddMeetingEvent event) {
        Intent detailActivityIntent = new Intent(MainActivity.this, MeetingDetailActivity.class);
        Bundle info = new Bundle();
        info.putParcelable("Meeting", event.meeting);
        detailActivityIntent.putExtras(info);
        startActivity(detailActivityIntent);
    }

    // List the rooms in strings for the singleChoicePicker
    public String [] listRoomsInStrings() {
        String [] list = new String [mApiService.getRooms().size()];
        for (int i = 0 ; i < mApiService.getRooms().size(); i++){
            list[i] = mApiService.getRooms().get(i).getRoom();
        }
        return list;
    }
}