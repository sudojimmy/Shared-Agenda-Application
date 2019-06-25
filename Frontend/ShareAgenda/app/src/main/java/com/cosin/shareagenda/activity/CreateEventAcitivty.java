package com.cosin.shareagenda.activity;


import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.RadioButton;

import com.cosin.shareagenda.R;

public class CreateEventAcitivty extends Activity {
    private EditText eventName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create_event);
        eventName = (EditText) findViewById(R.id.editText2);

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
