package com.mobile.ibandlalakwamarwa;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mobile.ibandlalakwamarwa.announcements.Announcement;
import com.mobile.ibandlalakwamarwa.announcements.AnnouncementsFragment;
import com.mobile.ibandlalakwamarwa.conferences.ChurchConference;
import com.mobile.ibandlalakwamarwa.conferences.regional.RegionalConferencesFragment;
import com.mobile.ibandlalakwamarwa.conferences.annual.AnnualConferencesFragment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static String TAG = MainActivity.class.getSimpleName();

    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private ArrayList<Announcement> announcements = new ArrayList<>();
    private ArrayList<ChurchConference> conferences = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(),R.drawable.logo_blue);
        ImageConverter.getRoundedCornerBitmap(bitmap, 100);


        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_layout, new HomeScreenFragment())
                .addToBackStack(HomeScreenFragment.class.getSimpleName())
                .commit();
    }

    @Override
    protected void onStart() {
        super.onStart();

        auth.signInWithEmailAndPassword("lungelomelusi@gmail.com","Password@1");


        initAnnouncementsData();
        initAnnualConferencesData();
    }

    private void initAnnouncementsData() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Announcements");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                announcements.clear();

                for (DataSnapshot announcementSnapshot : dataSnapshot.getChildren()) {
                    Announcement announcement = announcementSnapshot.getValue(Announcement.class);

                    announcements.add(announcement);
                }

//                cache.setAnnouncements(announcements);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "onCancelled", databaseError.toException());
            }
        });
    }

    private void initAnnualConferencesData() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("AnnualConferences");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                conferences.clear();

                for (DataSnapshot announcementSnapshot : dataSnapshot.getChildren()) {
                    ChurchConference conference = announcementSnapshot.getValue(ChurchConference.class);

                    conferences.add(conference);
                }

//                conferencesCache.setConferences(conferences);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "onCancelled", databaseError.toException());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();

        if(id == R.id.announcements) {
            replaceFragment(new AnnouncementsFragment());
        } else if(id == R.id.home){
            replaceFragment(new HomeScreenFragment());
        } else if (id == R.id.about_btn){
            replaceFragment(new AboutFragment());
        } else if(id == R.id.annual_conferences){
            replaceFragment(new AnnualConferencesFragment());
        } else if (id == R.id.regional_conferences) {
            replaceFragment(new RegionalConferencesFragment());
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void replaceFragment(Fragment fragment) {
        this.getSupportFragmentManager().beginTransaction().replace(R.id.main_layout, fragment).commit();
    }
}
