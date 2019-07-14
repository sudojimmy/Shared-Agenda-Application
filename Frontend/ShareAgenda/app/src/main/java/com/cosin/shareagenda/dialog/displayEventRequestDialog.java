package com.cosin.shareagenda.dialog;

import android.content.Context;
import android.widget.Button;
import android.widget.TextView;

import com.cosin.shareagenda.R;

public class displayEventRequestDialog extends BaseDialog {
    private String eventname;
    private String starterId;
    private String type;
    private String startTime;
    private String endTime;
    private String location;
    private String description;
    private String permission;
    private String repeat;
    private String startDate;
    private String endDate;

    public displayEventRequestDialog(Context context,
                                     String eventname,
                                     String starterId,
                                     String type,
                                     String startTime,
                                     String endTime,
                                     String location,
                                     String description,
                                     String permission,
                                     String repeat,
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
        ((TextView)findViewById(R.id.eventLocation)).setText(location);
        ((TextView)findViewById(R.id.startTime)).setText(startTime);
        ((TextView)findViewById(R.id.endTime)).setText(endTime);
        ((TextView)findViewById(R.id.startDate)).setText(startDate);
        ((TextView)findViewById(R.id.endDate)).setText(endDate);
        ((TextView)findViewById(R.id.eventDescription)).setText(description);
        ((TextView)findViewById(R.id.repeatTag)).setText(repeat);
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
