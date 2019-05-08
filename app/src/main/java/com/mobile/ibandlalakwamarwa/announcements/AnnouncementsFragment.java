package com.mobile.ibandlalakwamarwa.announcements;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.mobile.ibandlalakwamarwa.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class AnnouncementsFragment extends Fragment {
    private static String TAG = AnnouncementsFragment.class.getSimpleName();
    private final static String SHARE_PREFERENCES_FILE = "announcements";
    private final static String SHARED_PREFERENCES_KEY = "announcements_key";

    private ArrayList<Announcement> announcements;
    private RecyclerView recyclerView;

    private ProgressDialog progressBar;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    Gson gson = new Gson();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_announcements, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedPreferences = requireActivity().getApplicationContext().getSharedPreferences(SHARE_PREFERENCES_FILE, MODE_PRIVATE);

        progressBar = new ProgressDialog(requireActivity());
        recyclerView = view.findViewById(R.id.announcements_recycler_view);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        announcements = new ArrayList<>();

        FloatingActionButton fab = view.findViewById(R.id.add_announcements);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showScreen();
            }
        });

        progressBar.setTitle("Izimemezelo");
        progressBar.setMessage("Loading.........");

        progressBar.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFD4D9D0")));

        progressBar.setIndeterminate(false);
        progressBar.setCancelable(false);

        progressBar.show();
    }

    private void showScreen() {
        AddAnnouncementFragment addConferencce = new AddAnnouncementFragment();
        FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_layout, addConferencce);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onResume() {
        super.onResume();

        requireActivity().setTitle("Izimemezelo");
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onStart() {
        super.onStart();

        if(!loadAnnouncementFromSharedPreferences().isEmpty()) {
            RecyclerView.Adapter adapter = new AnnouncementsAdapter((new ArrayList(loadAnnouncementFromSharedPreferences())));
            recyclerView.setAdapter(adapter);
            hideProgressView();
        } else {

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Announcements");

            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    announcements.clear();

                    for (DataSnapshot announcementSnapshot : dataSnapshot.getChildren()) {
                        Announcement announcement = announcementSnapshot.getValue(Announcement.class);

                        announcements.add(announcement);
                    }

                    editor = sharedPreferences.edit();
                    editor.putString(SHARED_PREFERENCES_KEY, gson.toJson(announcements));
                    editor.apply();

                    RecyclerView.Adapter adapter = new AnnouncementsAdapter(announcements);
                    recyclerView.setAdapter(adapter);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e(TAG, "onCancelled", databaseError.toException());
                }
            });
            hideProgressView();
        }
    }

    private List<Announcement> loadAnnouncementFromSharedPreferences(){
        String itemsListString = sharedPreferences.getString(SHARED_PREFERENCES_KEY, "");
        Announcement announcementsArray [] = gson.fromJson(itemsListString, Announcement[].class);

        if(announcementsArray  != null){
            return Arrays.asList(announcementsArray );
        }

        return new ArrayList<>();
    }

    private void hideProgressView() {
        progressBar.dismiss();
    }
}
