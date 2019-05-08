package com.mobile.ibandlalakwamarwa.announcements;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mobile.ibandlalakwamarwa.MainActivity;
import com.mobile.ibandlalakwamarwa.R;

public class AddAnnouncementFragment extends Fragment {
    private EditText titleEditText;
    private TextInputEditText messageEditText;
    private Snackbar snackbar;

    DatabaseReference databaseReference;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_announcements, container, false);
        snackbar = Snackbar.make(container, "Isimemezelo sakho sibe yimpumelelo", Snackbar.LENGTH_INDEFINITE)
                .setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //show previous fragment
                    }
                });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        databaseReference = FirebaseDatabase.getInstance().getReference("Announcements");

        titleEditText = view.findViewById(R.id.input_title);
        messageEditText = view.findViewById(R.id.input_announcement);
        Button addBtn = view.findViewById(R.id.add_announcement_button);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAnnouncement();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        requireActivity().setTitle("Thumela Isimemezelo");
    }

    private void addAnnouncement(){
        String title = titleEditText.getText().toString().trim();
        String message = messageEditText.getText().toString().trim();

        if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(message)){
            String id = databaseReference.push().getKey();

            Announcement announcement = new Announcement(title, message);

            databaseReference.child(id).setValue(announcement);
        }
        showSnackBar();

        sendNotification("Sender's name", title, message);


        if(requireFragmentManager().getBackStackEntryCount() > 0){
            requireFragmentManager().popBackStackImmediate();
        }
    }

    private void sendNotification(String title, String subject, String body) {
        NotificationCompat.BigPictureStyle style = new NotificationCompat.BigPictureStyle();

        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Intent intent = new Intent(requireActivity().getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(requireActivity().getApplicationContext(), 0, intent,0);

        NotificationManager notificationManager = (NotificationManager)requireActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "101";

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            @SuppressLint("WrongConstant") NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID,
                    "Notification", NotificationManager.IMPORTANCE_MAX);

            //Configure Notification Channel
            notificationChannel.setDescription("Church Notifications");
            notificationChannel.enableLights(true);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);

            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(requireContext(), NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.logo_blue)
                .setContentTitle(title)
                .setAutoCancel(true)
                .setSound(defaultSound)
                .setContentText(body)
                .setContentIntent(pendingIntent)
                .setStyle(style)
                .setWhen(System.currentTimeMillis())
                .setPriority(Notification.PRIORITY_MAX);


        notificationManager.notify(1, notificationBuilder.build());
    }

    public void showSnackBar(){
        final View view = snackbar.getView();
        TextView textView = view.findViewById(android.support.design.R.id.snackbar_text);
        textView.setMaxLines(2);

        view.setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.green_color));
        snackbar.setActionTextColor(ContextCompat.getColor(view.getContext(), R.color.white_color));
        snackbar.show();
    }
}
