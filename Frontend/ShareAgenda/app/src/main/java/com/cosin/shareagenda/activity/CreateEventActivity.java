package com.cosin.shareagenda.activity;


import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.cosin.shareagenda.R;
import com.cosin.shareagenda.access.net.CallbackHandler;
import com.cosin.shareagenda.api.ApiClient;
import com.cosin.shareagenda.api.ApiErrorResponse;
import com.cosin.shareagenda.model.Model;
import com.cosin.shareagenda.util.CalendarEventBiz;
import com.google.gson.Gson;
import com.joestelmach.natty.Parser;

import net.gotev.speech.GoogleVoiceTypingDisabledException;
import net.gotev.speech.Speech;
import net.gotev.speech.SpeechDelegate;
import net.gotev.speech.SpeechRecognitionNotAvailable;
import net.gotev.speech.SpeechUtil;
import net.gotev.speech.ui.SpeechProgressView;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import types.Event;
import types.EventRepeat;
import types.EventState;
import types.EventType;
import types.Permission;
import types.PermissionType;
import types.Repeat;

import static com.cosin.shareagenda.access.net.CallbackHandler.SUCCESS;

public class CreateEventActivity extends AppCompatActivity {

    private final int PERMISSIONS_REQUEST = 1;
    private final String[] daily = new String[]{"daily", "every day"};
    private final String[] weekly = new String[]{"weekly", "every week"};
    private final String[] monthly = new String[]{"monthly", "every month"};
    private final String[] yearly = new String[]{"yearly", "every year"};


    private final String[] work = new String[]{"work", "company"};
    private final String[] study = new String[]{"study", "learn"};
    private final String[] entertainment = new String[]{"entertainment", "play"};
    private final String[] appointment = new String[]{"appointment"};

    public static final String SELECTED_DATE = "SELECTED_DATE";
    public static final String SELECTED_TIME = "SELECTED_TIME";
    public static final String SELECTED_ID = "SELECTED_ID";

    private EditText eventName;
    private EditText eventDescription;
    private Spinner eventType;
    private Spinner repeatType;
    private EditText eventLocation;
    private EditText startTimePicker;
    private EditText endTimePicker;
    private EditText startDatePicker;
    private EditText endDatePicker;
    private ImageView micButton;
    private LinearLayout linearLayout;
    private SpeechProgressView progress;

    private Switch privateEvent;
    private String calendarType;
    private String targetId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Speech.init(this, getPackageName());

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
        micButton = findViewById(R.id.micButton);
        micButton.setOnClickListener(view -> onSpeakClick());
        linearLayout = findViewById(R.id.linearLayout);
        progress = findViewById(R.id.progress);
        privateEvent = findViewById(R.id.privateEvent);


        ArrayAdapter<EventType> eventAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, EventType.values());

        ArrayAdapter<EventRepeat> repeatAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, EventRepeat.values());

        eventType.setAdapter(eventAdapter);
        repeatType.setAdapter(repeatAdapter);

        String selectedDate = getIntent().getStringExtra(SELECTED_DATE);
        String selectedTime = getIntent().getStringExtra(SELECTED_TIME);
        targetId = getIntent().getStringExtra(SELECTED_ID);
        calendarType = getIntent().getStringExtra(NewCalendarActivity.CALENDAR_ACTIVITY_TYPE);
        if (calendarType != null) {
            TextView createEventPageTitle = findViewById(R.id.createEventPageTitle);
            if (calendarType.equals(NewCalendarActivity.FRIEND_CALENDAR)) {
                createEventPageTitle.setText("Invitation");
            } else { // GROUP_CALENDAR
                createEventPageTitle.setText("Group Event");
            }
            privateEvent.setVisibility(View.GONE);
        }
        startTimePicker.setText(selectedTime);
        endTimePicker.setText(CalendarEventBiz.getNextHour(selectedTime));
        startDatePicker.setText(selectedDate);
        endDatePicker.setText(selectedDate);
        int[] colors = {
                ContextCompat.getColor(this, android.R.color.black),
                ContextCompat.getColor(this, android.R.color.darker_gray),
                ContextCompat.getColor(this, android.R.color.black),
                ContextCompat.getColor(this, android.R.color.holo_orange_dark),
                ContextCompat.getColor(this, android.R.color.holo_red_dark)
        };
        progress.setColors(colors);
    }

    @Override
    protected void onDestroy() {
        // prevent memory leaks when activity is destroyed
        Speech.getInstance().shutdown();
        super.onDestroy();
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
                    new SweetAlertDialog(CreateEventActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                            .setContentText("SUCCESS")
                            .setTitleText(getAlertTitleText())
                            .setConfirmText("OK").setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismissWithAnimation();
                            finish();
                        }
                    }).show();
                    break;
                case CallbackHandler.HTTP_FAILURE:
                    ApiErrorResponse errorResponse = gson.fromJson((String) message.obj, ApiErrorResponse.class);

                    Speech.getInstance().say(errorResponse.getMessage());
                    Toast.makeText(CreateEventActivity.this, errorResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(CreateEventActivity.this, (String)message.obj, Toast.LENGTH_SHORT).show();
            }
        }
    };

    private String getAlertTitleText() {
        if (calendarType != null && calendarType.equals(NewCalendarActivity.FRIEND_CALENDAR)) {
            return "Event Request";
        } else {
            return "Create Event";
        }
    }

    public void createEvent(View view) {
        // TODO friendCalendar -> Account; groupCalendar -> Group
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
                .withEndTime(endTimePicker.getText().toString());
        if (calendarType == null) {
            event.withPermission(new Permission().withType(
                    privateEvent.isChecked() ? PermissionType.PRIVATE : PermissionType.PUBLIC));
            ApiClient.createEvent(event, new CallbackHandler(handler));
        } else if (calendarType.equals(NewCalendarActivity.FRIEND_CALENDAR)) {
            event.setPermission(new Permission()
                    .withType(PermissionType.ACCOUNT)
                    .withPermitToId(targetId));
            ApiClient.inviteEvent(targetId, event, new CallbackHandler(handler));
        } else {
            event.setPermission(new Permission()
                    .withType(PermissionType.GROUP)
                    .withPermitToId(targetId));
            ApiClient.createEvent(event, new CallbackHandler(handler));
        }
    }

    public void onSpeakClick() {
        if (Speech.getInstance().isListening()) {
            Speech.getInstance().stopListening();
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                onRecordAudioPermissionGranted();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, PERMISSIONS_REQUEST);
            }
        }
    }

    private void onRecordAudioPermissionGranted() {
        micButton.setVisibility(View.GONE);
        linearLayout.setVisibility(View.VISIBLE);
        try {
            Speech.getInstance().stopTextToSpeech();
            // you must have android.permission.RECORD_AUDIO granted at this point
            Speech.getInstance().startListening(progress, new SpeechDelegate() {
                @Override
                public void onStartOfSpeech() {
                    Log.i("speech", "speech recognition is now active");
                }

                @Override
                public void onSpeechRmsChanged(float value) {
                    Log.d("speech", "rms is now: " + value);
                }

                @Override
                public void onSpeechPartialResults(List<String> results) {
                    StringBuilder str = new StringBuilder();
                    for (String res : results) {
                        str.append(res).append(" ");
                    }

                    Log.i("speech", "partial result: " + str.toString().trim());
                }

                @Override
                public void onSpeechResult(String result) {

                    micButton.setVisibility(View.VISIBLE);
                    linearLayout.setVisibility(View.GONE);


                    if (result.isEmpty()) {
                        Speech.getInstance().say(getString(R.string.repeat));

                    } else {
                        Speech.getInstance().say(result);
                        processSpeech(result);
                    }
                }
            });
        } catch (SpeechRecognitionNotAvailable exc) {
            Log.e("speech", "Speech recognition is not available on this device!");
            showSpeechNotSupportedDialog();
        } catch (GoogleVoiceTypingDisabledException exc) {
            Log.e("speech", "Google voice typing must be enabled!");
            showEnableGoogleVoiceTyping();

        }
    }

    private void processSpeech(String result) {
        String[] splits = result.split("fill |with |feel |set |field ");
        if (StringUtils.indexOfAny(result, daily)>=0) {
            repeatType.setSelection(1);
        } else if (StringUtils.indexOfAny(result, weekly)>=0) {
            repeatType.setSelection(2);
        } else if (StringUtils.indexOfAny(result, monthly)>=0) {
            repeatType.setSelection(3);
        } else if (StringUtils.indexOfAny(result, yearly)>=0) {
            repeatType.setSelection(4);
        }
        if (StringUtils.indexOfAny(result, work)>=0) {
            eventType.setSelection(0);
        } else if (StringUtils.indexOfAny(result, study)>=0) {
            eventType.setSelection(1);
        } else if (StringUtils.indexOfAny(result, entertainment)>=0) {
            eventType.setSelection(2);
        } else if (StringUtils.indexOfAny(result, appointment)>=0) {
            eventType.setSelection(3);
        }

        try {
            List<Date> dates = new Parser().parse(result).get(0).getDates();
            if (dates.size() >= 1) {
                Date start = dates.get(0);

                startDatePicker.setText(CalendarEventBiz.toDateString(start));
                startTimePicker.setText(CalendarEventBiz.toTimeString(start));
                if (dates.size() == 1) {
                    Date end = CalendarEventBiz.addHoursToDate(start, 1);

                    endDatePicker.setText(CalendarEventBiz.toDateString(end));
                    endTimePicker.setText(CalendarEventBiz.toTimeString(end));
                }
            }
            if (dates.size() >= 2) {
                Date end = dates.get(1);

                endDatePicker.setText(CalendarEventBiz.toDateString(end));
                endTimePicker.setText(CalendarEventBiz.toTimeString(end));
            }
        } catch(Exception e) {

        }

        for (int i = 0;i < splits.length; i++) {
            String str = splits[i];
            if (i+1>= splits.length) {
                return;
            }
            else if (str.contains("location")) {
                eventLocation.setText(splits[i+1]);
                i++;
            }
            else if (str.contains("name")) {
                eventName.setText(splits[i+1]);
                i++;
            }
            else if (str.contains("description")) {
                eventDescription.setText(splits[i+1]);
                i++;
            }
            else if (str.contains("repeat")) {
                String repeatType = splits[i+1];
                eventDescription.setText(splits[i+1]);
                i++;
            }
        }
        return;
    }


    private void showSpeechNotSupportedDialog() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        SpeechUtil.redirectUserToGoogleAppOnPlayStore(CreateEventActivity.this);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.speech_not_available)
                .setCancelable(false)
                .setPositiveButton(R.string.yes, dialogClickListener)
                .setNegativeButton(R.string.no, dialogClickListener)
                .show();
    }

    private void showEnableGoogleVoiceTyping() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.enable_google_voice_typing)
                .setCancelable(false)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // do nothing
                    }
                })
                .show();
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

