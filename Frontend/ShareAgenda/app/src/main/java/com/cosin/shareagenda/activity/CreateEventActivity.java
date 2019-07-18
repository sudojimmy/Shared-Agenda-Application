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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.cosin.shareagenda.R;
import com.cosin.shareagenda.access.net.CallbackHandler;
import com.cosin.shareagenda.model.ApiClient;
import com.cosin.shareagenda.model.ApiErrorResponse;
import com.cosin.shareagenda.model.Model;
import com.cosin.shareagenda.util.CalendarEventBiz;
import com.google.gson.Gson;

import net.gotev.speech.GoogleVoiceTypingDisabledException;
import net.gotev.speech.Speech;
import net.gotev.speech.SpeechDelegate;
import net.gotev.speech.SpeechRecognitionNotAvailable;
import net.gotev.speech.SpeechUtil;
import net.gotev.speech.ui.SpeechProgressView;

import org.apache.commons.lang3.ObjectUtils;

import java.util.List;

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

    public static final String SELECTED_DATE = "SELECTED_DATE";
    public static final String SELECTED_TIME = "SELECTED_TIME";

    private EditText eventName;
    private EditText eventDescription;
    private Spinner eventType;
    private Spinner repeatType;
    private EditText eventLocation;
    private EditText startTimePicker;
    private EditText endTimePicker;
    private EditText startDatePicker;
    private EditText endDatePicker;
    private ImageButton micButton;
    private LinearLayout linearLayout;
    private SpeechProgressView progress;

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

        ArrayAdapter<EventType> eventAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, EventType.values());

        ArrayAdapter<EventRepeat> repeatAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, EventRepeat.values());

        eventType.setAdapter(eventAdapter);
        repeatType.setAdapter(repeatAdapter);

        String selectedDate = getIntent().getStringExtra(SELECTED_DATE);
        String selectedTime = getIntent().getStringExtra(SELECTED_TIME);
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
                    String body = (String) message.obj;
                    // TODO update view
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
//
//        try {
//            Speech.getInstance().stopTextToSpeech();
//            Speech.getInstance().startListening(progress, CreateEventActivity.this);
//
//        } catch (SpeechRecognitionNotAvailable exc) {
//            showSpeechNotSupportedDialog();
//
//        } catch (GoogleVoiceTypingDisabledException exc) {
//            showEnableGoogleVoiceTyping();
//        }
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

//                    text.setText(result);

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
        for (int i = 0;i < splits.length; i++) {
            String str = splits[i];
            if (i+1>= splits.length) {
                return;
            }
            if (str.contains("location")) {

                eventLocation.setText(splits[i+1]);
            }
//            switch (str){
//                case "location":
//                    eventLocation.setText(splits[i+1]);
//                    break;
////                case ""
//            }
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

