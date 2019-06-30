package com.cosin.shareagenda.activity;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.cosin.shareagenda.R;
import com.github.jjobes.slidedatetimepicker.SlideDateTimeListener;
import com.github.jjobes.slidedatetimepicker.SlideDateTimePicker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CreateEventAcitivty extends AppCompatActivity {
    private EditText eventName;
    private Spinner eventType;
    private Spinner repeatType;
    private EditText location;
    private EditText repeat;
    private EditText datePicker;
    private Button createButton;
    private String startDate;
    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");

    private SlideDateTimeListener listener = new SlideDateTimeListener() {

        @Override
        public void onDateTimeSet(Date date)
        {
            startDate = date.toString();
            // Do something with the date. This Date object contains
            // the date and time that the user has selected.
            datePicker.setText(dateFormat.format(date));
        }

        @Override
        public void onDateTimeCancel()
        {

            // Overriding onDateTimeCancel() is optional.
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create_event);
        eventName = (EditText) findViewById(R.id.eventName);
        datePicker = (EditText) findViewById(R.id.datePicker);
        createButton = (Button) findViewById(R.id.createButton);
        eventType = (Spinner) findViewById(R.id.typeDropDown);
        repeatType = (Spinner) findViewById(R.id.repeatDropDown) ;


        String[] eventTypeItems = new String[] { "WORK", "STUDY", "ENTERTAINMENT", "APPOINTMENT", "OTHER" };

        ArrayAdapter<String> eventAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, eventTypeItems);

        String[] repeatTypeItems = new String[] { "ONCE", "DAILY", "WEEKLY", "MONTHLY" };

        ArrayAdapter<String> repeatAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, repeatTypeItems);


        eventType.setAdapter(eventAdapter);

        eventType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
//                Log.v("item", (String) parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        repeatType.setAdapter(repeatAdapter);

        repeatType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
//                Log.v("item", (String) parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SlideDateTimePicker.Builder(getSupportFragmentManager())
                        .setListener(listener)
                        .setInitialDate(new Date())
                        //.setMinDate(minDate)
                        //.setMaxDate(maxDate)
//                        .setIs24HourTime(true)
                        //.setTheme(SlideDateTimePicker.HOLO_DARK)
                        //.setIndicatorColor(Color.parseColor("#990000"))
                        .build()
                        .show();
            }
        });
        Date date = new Date();
        datePicker.setText(dateFormat.format(date));

        eventName.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                System.out.println(s);

                // you can call or do what you want with your EditText here

                // eventName...
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });
    }
}
