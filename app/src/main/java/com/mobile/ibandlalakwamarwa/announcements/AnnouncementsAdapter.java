package com.mobile.ibandlalakwamarwa.announcements;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mobile.ibandlalakwamarwa.R;

import java.util.ArrayList;

public class AnnouncementsAdapter extends RecyclerView.Adapter<AnnouncementsAdapter.MyViewHolder> {

    private ArrayList<Announcement> announcements;

    public AnnouncementsAdapter(ArrayList<Announcement> announcements){
        this.announcements = announcements;
    }

    @NonNull
    @Override
    public AnnouncementsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.announcements_row, viewGroup, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnnouncementsAdapter.MyViewHolder myViewHolder, int listPosition) {
        TextView titleTextView = myViewHolder.titleTextView;
        TextView messageTextView = myViewHolder.messageTextView;

        titleTextView.setText(announcements.get(listPosition).getTitle());
        messageTextView.setText(announcements.get(listPosition).getMessage());
    }

    @Override
    public int getItemCount() {
        return announcements.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView messageTextView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            this.titleTextView = itemView.findViewById(R.id.title_text);
            this.messageTextView = itemView.findViewById(R.id.message_text);
        }
    }
}
