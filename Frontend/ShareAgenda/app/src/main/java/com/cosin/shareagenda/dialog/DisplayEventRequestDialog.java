package com.cosin.shareagenda.dialog;

import android.content.Context;
import android.widget.Button;
import android.widget.TextView;

import com.cosin.shareagenda.R;

import types.Event;
import types.EventRepeat;
import types.Repeat;

public class DisplayEventRequestDialog extends BaseDialog {
    private String eventname;
    private String starterId;
    private String type;
    private String startTime;
    private String endTime;
    private String location;
    private String description;
    private String permission;
    private Repeat repeat;
    private String startDate;
    private String endDate;

    public DisplayEventRequestDialog(Context context,
                                     String eventname,
                                     String starterId,
                                     String type,
                                     String startTime,
                                     String endTime,
                                     String location,
                                     String description,
                                     String permission,
                                     Repeat repeat,
                                     String startDate,
                                     String endDate) {

        super(context);

        this.eventname = eventname;
        this.starterId = starterId;
        this.type = type;
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = location;
        this.description = description;
        this.permission = permission;
        this.repeat = repeat;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    protected void loadView() {
        window.setContentView(R.layout.activity_event_popup);
    }

    @Override
    protected void initView() {
        ((TextView)findViewById(R.id.type)).setText(type);
        ((TextView)findViewById(R.id.eventName)).setText(eventname);

        if (location.isEmpty()) {
            location = "¯\\\\_(ツ)_/¯";
        }
        ((TextView)findViewById(R.id.eventLocation)).setText(location);

        ((TextView)findViewById(R.id.startTime)).setText(startTime);
        ((TextView)findViewById(R.id.endTime)).setText(endTime);
        ((TextView)findViewById(R.id.startDate)).setText(startDate);
        ((TextView)findViewById(R.id.endDate)).setText(endDate);

        if (description.isEmpty()){
            description = "Add Your Unique Event Description";
        }
        ((TextView)findViewById(R.id.eventDescription)).setText(description);

        EventRepeat eventRepeat = repeat.getType();
        String repeatString;
        if (eventRepeat.equals(EventRepeat.ONCE)) {
            repeatString = "End On";
        } else if (eventRepeat.equals(EventRepeat.DAY)) {
            repeatString = "Repeat Daily";

        } else if (eventRepeat.equals(EventRepeat.WEEK)) {
            repeatString = "Repeat Weekly";

        } else if (eventRepeat.equals(EventRepeat.MONTH)) {
            repeatString = "Repeat Monthly";

        } else if (eventRepeat.equals(EventRepeat.YEAR)) {
            repeatString = "Repeat Yearly";

        } else {
            repeatString = "";
        }

        ((TextView)findViewById(R.id.repeatTag)).setText(repeatString);
        // todo
        // ((TextView)findViewById(R.id.)).setText(permission);
        // ((TextView)findViewById(R.id.)).setText(starterId);

        Button btn = findViewById(R.id.joinButton);
        btn.setOnClickListener(onClickListener);
    }

    @Override
    protected Object dealwithRet() {
        // no selected item in this dialog
        return true;
    }
}
