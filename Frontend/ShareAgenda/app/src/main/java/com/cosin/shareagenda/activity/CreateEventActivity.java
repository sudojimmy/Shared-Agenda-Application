package com.cosin.shareagenda.activity;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.cosin.shareagenda.R;
import com.cosin.shareagenda.access.net.CallbackHandler;
import com.cosin.shareagenda.model.ApiClient;
import com.cosin.shareagenda.model.ApiErrorResponse;
import com.cosin.shareagenda.model.Model;
import com.cosin.shareagenda.util.CalendarEventBiz;
import com.google.gson.Gson;

import types.Event;
import types.EventRepeat;
import types.EventState;
import types.EventType;
import types.Permission;
import types.PermissionType;
import types.Repeat;

import static com.cosin.shareagenda.access.net.CallbackHandler.SUCCESS;

public class CreateEventActivity extends AppCompatActivity {
    private EditText eventName;
    private EditText eventDescription;
    private Spinner eventType;
    private Spinner repeatType;
    private EditText eventLocation;
    private EditText startTimePicker;
    private EditText endTimePicker;
    private EditText startDatePicker;
    private EditText endDatePicker;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create_event);
        eventName = findViewById(R.id.eventName);
        eventDescription = findViewById(R.id.eventDescription);
        eventLocation = findViewById(R.id.eventLocation);
        startTimePicker = findViewById(R.id.startTimePicker);
        endTimePicker = findViewById(R.id.endTimePicker);
        startDatePicker = findViewById(R.id.startDatePicker);
        endDatePicker = findViewById(R.id.endDatePicker);
        eventType = findViewById(R.id.typeDropDown);
        repeatType = findViewById(R.id.repeatDropDown);


        ArrayAdapter<EventType> eventAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, EventType.values());

        ArrayAdapter<EventRepeat> repeatAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, EventRepeat.values());

        eventType.setAdapter(eventAdapter);
        repeatType.setAdapter(repeatAdapter);

        startTimePicker.setText(CalendarEventBiz.getCurrentTimeString());
        endTimePicker.setText(CalendarEventBiz.getCurrentTimeString());
        startDatePicker.setText(CalendarEventBiz.getCurrentDateString());
        endDatePicker.setText(CalendarEventBiz.getCurrentDateString());
    }

    public void showStartTimePickerDialog(View view) {
        TimePickerFragment newFragment = new TimePickerFragment();
        newFragment.setTimePicker(startTimePicker);
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    public void showEndTimePickerDialog(View view) {
        TimePickerFragment newFragment = new TimePickerFragment();
        newFragment.setTimePicker(endTimePicker);
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    public void showStartDatePickerDialog(View view) {
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.setDatePicker(startDatePicker);
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void showEndDatePickerDialog(View view) {
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.setDatePicker(endDatePicker);
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }


    Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message message) {
            final Gson gson = new Gson();
            switch (message.what) {
                case SUCCESS:
                    String body = (String) message.obj;
                    // TODO update view
                    Intent intent = new Intent(CreateEventActivity.this, CalendarActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                case CallbackHandler.HTTP_FAILURE:
                    ApiErrorResponse errorResponse = gson.fromJson((String) message.obj, ApiErrorResponse.class);
                    Toast.makeText(CreateEventActivity.this, errorResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(CreateEventActivity.this, (String)message.obj, Toast.LENGTH_SHORT).show();
            }
        }
    };

    public void createEvent(View view) {
        Event event = new Event()
                .withStarterId(Model.model.getUser().getAccountId())    // starter always user
                .withState(EventState.ACTIVE)                           // state always active
                .withEventname(eventName.getText().toString())
                .withDescription(eventDescription.getText().toString())
                .withLocation(eventLocation.getText().toString())
                .withType(EventType.fromValue(eventType.getSelectedItem().toString()))
                .withRepeat(new Repeat()
                        .withType(EventRepeat.fromValue(repeatType.getSelectedItem().toString()))
                        .withStartDate(startDatePicker.getText().toString())
                        .withEndDate(endDatePicker.getText().toString()))
                .withStartTime(startTimePicker.getText().toString())
                .withEndTime(endTimePicker.getText().toString())
                .withPermission(new Permission().withType(PermissionType.PUBLIC));  // TODO
        ApiClient.createEvent(event, new CallbackHandler(handler));
    }

    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {
        EditText timePicker;

        void setTimePicker(EditText timePicker) {
            this.timePicker = timePicker;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            int hour = CalendarEventBiz.getCurrentHour();
            int minute = CalendarEventBiz.getCurrentMinute();

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    android.text.format.DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            timePicker.setText(CalendarEventBiz.toTimeString(hourOfDay, minute));
        }
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {
        EditText datePicker;

        void setDatePicker(EditText datePicker) {
            this.datePicker = datePicker;
        }
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            int year = CalendarEventBiz.getCurrentYear();
            int month = CalendarEventBiz.getCurrentMonth();
            int day = CalendarEventBiz.getCurrentDayOfMonth();

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            datePicker.setText(CalendarEventBiz.toDateString(year, month, day));
        }
    }
}

