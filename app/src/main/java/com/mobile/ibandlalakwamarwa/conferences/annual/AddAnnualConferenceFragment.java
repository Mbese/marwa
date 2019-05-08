package com.mobile.ibandlalakwamarwa.conferences.annual;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mobile.ibandlalakwamarwa.R;
import com.mobile.ibandlalakwamarwa.conferences.ChurchConference;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddAnnualConferenceFragment extends Fragment implements View.OnTouchListener {

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    private TextInputEditText startDateEditText;
    private TextInputEditText endDateEditText;
    private TextInputEditText startTimeEditText;
    private TextInputEditText endTimeEditText;
    private TextInputEditText addressEditText;
    private TextInputEditText organizerEditText;
    private TextInputEditText contactEditText;

    private Spinner spinner;
    private DatePickerDialog startDatePickerDialog;
    private DatePickerDialog endDatePickerDialog;
    private TimePickerDialog startTimePickerDialog;
    private TimePickerDialog endTimePickerDialog;
    private Calendar memorableDate;

    private Snackbar snackbar;

    DatabaseReference databaseAnnualConference;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_annual_conferences, container, false);

        snackbar = Snackbar.make(container, "Ukufaka kwakho inkonzo kube yimpumelelo", Snackbar.LENGTH_INDEFINITE)
                .setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //show previous fragment
                    }
                });

        return view;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        databaseAnnualConference = FirebaseDatabase.getInstance().getReference("AnnualConferences");

        spinner = view.findViewById(R.id.conference_spinner);
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(requireContext(), R.array.annual_conferences, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);


        startDateEditText = view.findViewById(R.id.input_startDate);
        startDateEditText.setOnTouchListener(this);
        endDateEditText = view.findViewById(R.id.input_endDate);
        endDateEditText.setOnTouchListener(this);
        startTimeEditText = view.findViewById(R.id.input_startTime);
        startTimeEditText.setOnTouchListener(this);
        endTimeEditText = view.findViewById(R.id.input_endTime);
        endTimeEditText.setOnTouchListener(this);
        addressEditText = view.findViewById(R.id.input_conf_address);
        organizerEditText = view.findViewById(R.id.input_conf_organizer);
        contactEditText = view.findViewById(R.id.input_conf_organizer_contacts);
        Button submitButton = view.findViewById(R.id.annualConfButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addConference();
            }
        });

        initStartDatePicker();
        initSEndDatePicker();

        initStartTime();
        initEndTime();
    }

    private void initStartTime() {
        Calendar calendar = Calendar.getInstance();
        startTimePickerDialog = new TimePickerDialog(requireContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                startTimeEditText.setText(new StringBuilder().append(pad(hourOfDay))
                        .append(":").append(pad(minute)));

            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
        startTimePickerDialog.setTitle("");
    }

    private void initEndTime() {
        Calendar calendar = Calendar.getInstance();
        endTimePickerDialog = new TimePickerDialog(requireContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                endTimeEditText.setText(new StringBuilder().append(pad(hourOfDay))
                        .append(":").append(pad(minute)));
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
        endTimePickerDialog.setTitle("");
    }

    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

    private void initStartDatePicker() {
        Calendar calendar = Calendar.getInstance();
        startDatePickerDialog = new DatePickerDialog(requireActivity(), new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                memorableDate = Calendar.getInstance();
                memorableDate.set(year, monthOfYear, dayOfMonth);
                startDateEditText.setText(DATE_FORMAT.format(memorableDate.getTime()));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        startDatePickerDialog.setTitle("");
    }

    private void initSEndDatePicker() {
        Calendar calendar = Calendar.getInstance();
        endDatePickerDialog = new DatePickerDialog(requireActivity(), new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                memorableDate = Calendar.getInstance();
                memorableDate.set(year, monthOfYear, dayOfMonth);
                endDateEditText.setText(DATE_FORMAT.format(memorableDate.getTime()));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        endDatePickerDialog.setTitle("");
    }

    private void addConference() {
        String title = spinner.getSelectedItem().toString();
        String startDate = startDateEditText.getText().toString().trim();
        String endDate = endDateEditText.getText().toString().trim();
        String startTime = startTimeEditText.getText().toString().trim();
        String endTime = endTimeEditText.getText().toString().trim();
        String address = addressEditText.getText().toString().trim();
        String organizer = organizerEditText.getText().toString().trim();
        String contact = contactEditText.getText().toString().trim();

        if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(startDate) &&
                !TextUtils.isEmpty(endDate) && !TextUtils.isEmpty(startTime) &&
                !TextUtils.isEmpty(endTime) && !TextUtils.isEmpty(address) &&
                !TextUtils.isEmpty(organizer) && !TextUtils.isEmpty(contact)) {

            String id = databaseAnnualConference.push().getKey();

            ChurchConference conference = new ChurchConference(title, startDate, endDate,
                    startTime, endTime, address, organizer, contact);

            databaseAnnualConference.child(id).setValue(conference);
        }

        showSnackBar();

        if (requireFragmentManager().getBackStackEntryCount() > 0) {
            requireFragmentManager().popBackStackImmediate();
        }

    }

    public void showSnackBar() {
        final View view = snackbar.getView();
        TextView textView = view.findViewById(android.support.design.R.id.snackbar_text);
        textView.setMaxLines(2);

        view.setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.green_color));
        snackbar.setActionTextColor(ContextCompat.getColor(view.getContext(), R.color.white_color));
        snackbar.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        requireActivity().setTitle("Faka Inkonzo");
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View view, MotionEvent event) {
        int id = view.getId();

        if (id == R.id.input_startDate) {
            startDatePickerDialog.show();
            return true;
        } else if (id == R.id.input_endDate) {
            endDatePickerDialog.show();
            return true;
        } else if (id == R.id.input_startTime) {
            startTimePickerDialog.show();
            return true;
        } else if (id == R.id.input_endTime) {
            endTimePickerDialog.show();
            return true;
        }
        return false;
    }
}
