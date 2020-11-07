package com.example.ui.controler;

import android.graphics.BlendMode;
import android.graphics.BlendModeColorFilter;
import android.graphics.PorterDuff;
import android.os.Build;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ui.R;

import androidx.recyclerview.widget.RecyclerView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import Event.AddMeetingEvent;
import Event.DeleteMeetingEvent;
import Model.Delegate;
import Model.Meeting;

public class MeetingListRecyclerViewAdapter extends RecyclerView.Adapter<MeetingListRecyclerViewAdapter.ViewHolder> {

    private final List<Meeting> mMeetings;
    public MeetingListRecyclerViewAdapter(List<Meeting> items) {
        mMeetings = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_meeting, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Meeting meeting = mMeetings.get(position);

        // Setup TextViews
        // Name - Time - Room
        String meetingInfo = meeting.getName()+" - "+ DateFormat.format("HH:mm", meeting.getBeginTime()).toString()+" - "+meeting.getLocation().getRoom();
        holder.mMeetingInfo.setText(meetingInfo);
        // Avatar color

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            holder.mMeetingAvatar.setColorFilter(new BlendModeColorFilter(meeting.getAvatarColor(), BlendMode.SRC_IN));
        } else {
            holder.mMeetingAvatar.setColorFilter(meeting.getAvatarColor() , PorterDuff.Mode.SRC_IN);
        }

        // Delegate
        String delegates = "";
        for (Delegate delegate : meeting.getDelegates()){
            delegates += delegate.getEmail() + " ";
        }
        holder.mDelegatesList.setText(delegates);

        // Delete icon click listener
        holder.mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new DeleteMeetingEvent(meeting));
            }
        });

        // Meeting click listener
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new AddMeetingEvent(meeting));
            }
        });
    }

    // Function required for proper operation of the RecyclerView
    @Override
    public int getItemCount() {
        return mMeetings.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView mMeetingAvatar ;
        TextView mMeetingInfo;
        TextView mDelegatesList;
        ImageButton mDeleteButton;

        ViewHolder(View view) {
            super(view);
            mMeetingAvatar = view.findViewById(R.id.item_list_avatar);
            mMeetingInfo = view.findViewById(R.id.item_list_info);
            mDelegatesList = view.findViewById(R.id.item_list_delegate);
            mDeleteButton = view.findViewById(R.id.item_list_delete_button);
        }
    }
}