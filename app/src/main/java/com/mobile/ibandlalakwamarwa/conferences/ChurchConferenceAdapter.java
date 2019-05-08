package com.mobile.ibandlalakwamarwa.conferences;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mobile.ibandlalakwamarwa.R;

import java.util.ArrayList;

public class ChurchConferenceAdapter extends RecyclerView.Adapter<ChurchConferenceAdapter.MyViewHolder> {

    private ArrayList<ChurchConference> conferences;

    public ChurchConferenceAdapter(ArrayList<ChurchConference> conferences){
        this.conferences = conferences;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.annual_conference_row, viewGroup, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int listPosition) {

        TextView titleTextView = myViewHolder.titleTextView;
        TextView startDateTextView = myViewHolder.startDateTextView;
        TextView endDateTextView = myViewHolder.endDateTextView ;
        TextView startTimeTextView = myViewHolder.startTimeTextView ;
        TextView endTimeTextView = myViewHolder.endTimeTextView;
        TextView addressTextView = myViewHolder.addressTextView ;
        TextView organizerTextView = myViewHolder.organizerTextView;
        TextView contactTextView = myViewHolder.contactTextView;

        titleTextView.setText(conferences.get(listPosition).getTitle());
        startDateTextView.setText(conferences.get(listPosition).getStartDate());
        endDateTextView.setText(conferences.get(listPosition).getEndDate());
        startTimeTextView.setText(conferences.get(listPosition).getStartTime());
        endTimeTextView.setText(conferences.get(listPosition).getEndTime());
        addressTextView.setText(conferences.get(listPosition).getAddress());
        organizerTextView.setText(conferences.get(listPosition).getOrganizer());
        contactTextView.setText(conferences.get(listPosition).getOrganizerContacts());
    }

    @Override
    public int getItemCount() {
        return conferences.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView startDateTextView;
        TextView endDateTextView;
        TextView startTimeTextView;
        TextView endTimeTextView;
        TextView addressTextView;
        TextView organizerTextView;
        TextView contactTextView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            this.titleTextView = itemView.findViewById(R.id.annual_conf_title);
            this.startDateTextView = itemView.findViewById(R.id.annual_conf_start_date);
            this.endDateTextView = itemView.findViewById(R.id.annual_conf_endDate);
            this.startTimeTextView = itemView.findViewById(R.id.annual_conf_start_time);
            this.endTimeTextView = itemView.findViewById(R.id.annual_conf_endtime);
            this.addressTextView = itemView.findViewById(R.id.annual_conf_address);
            this.organizerTextView = itemView.findViewById(R.id.annual_conf_organizer);
            this.contactTextView = itemView.findViewById(R.id.annual_conf_contact);
        }
    }
}
