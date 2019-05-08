package com.mobile.ibandlalakwamarwa.conferences.regional;

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
import com.mobile.ibandlalakwamarwa.conferences.ChurchConference;
import com.mobile.ibandlalakwamarwa.conferences.ChurchConferenceAdapter;
import com.mobile.ibandlalakwamarwa.conferences.annual.AnnualConferencesFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static android.content.Context.MODE_PRIVATE;

public class RegionalConferencesFragment extends Fragment {
    private static String TAG = AnnualConferencesFragment.class.getSimpleName();

    private final static String SHARE_PREFERENCES_FILE = "regionalConference";
    private final static String SHARED_PREFERENCES_KEY = "regionalConference_key";

    private ArrayList<ChurchConference> conferences;
    private RecyclerView recyclerView;
    private ProgressDialog progressBar;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    Gson gson = new Gson();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_regional_conferences, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedPreferences = requireActivity().getApplicationContext().getSharedPreferences(SHARE_PREFERENCES_FILE, MODE_PRIVATE);
        progressBar = new ProgressDialog(requireActivity());

        recyclerView = view.findViewById(R.id.regional_conf_recycler_view);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        conferences = new ArrayList<>();

        FloatingActionButton fab = view.findViewById(R.id.add_regional_conf_btn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showScreen();
            }
        });

        progressBar.setTitle("Izinkonzo Zonkaka");
        progressBar.setMessage("Loading.........");

        progressBar.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFD4D9D0")));

        progressBar.setIndeterminate(false);
        progressBar.setCancelable(false);

        progressBar.show();
    }


    private void showScreen() {
        AddRegionalConferenceFragment addConferencce = new AddRegionalConferenceFragment();
        FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_layout, addConferencce);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        requireActivity().setTitle("Izinkonzo Zesifunda");
    }

    @Override
    public void onStart() {
        super.onStart();

        if(!loadConferencesFromSharedPreferences().isEmpty()){
            conferences.addAll(loadConferencesFromSharedPreferences());
            RecyclerView.Adapter adapter = new ChurchConferenceAdapter(conferences);
            recyclerView.setAdapter(adapter);
            hideProgressView();
        } else {

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("RegionalConferences");

            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    conferences.clear();

                    for (DataSnapshot announcementSnapshot : dataSnapshot.getChildren()) {
                        ChurchConference conference = announcementSnapshot.getValue(ChurchConference.class);

                        conferences.add(conference);
                    }

                    editor = sharedPreferences.edit();
                    editor.putString(SHARED_PREFERENCES_KEY, gson.toJson(conferences));
                    editor.apply();

                    RecyclerView.Adapter adapter = new ChurchConferenceAdapter(conferences);
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

    private Collection<? extends ChurchConference> loadConferencesFromSharedPreferences(){
        String itemsListString = sharedPreferences.getString(SHARED_PREFERENCES_KEY, "");
        ChurchConference churchConferences [] = gson.fromJson(itemsListString, ChurchConference[].class);

        if(churchConferences  != null){
            return Arrays.asList(churchConferences );
        }

        return new ArrayList<>();
    }

    private void hideProgressView() {
        progressBar.dismiss();
    }
}
