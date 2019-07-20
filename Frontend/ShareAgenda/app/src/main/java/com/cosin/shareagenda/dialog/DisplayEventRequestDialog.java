package com.cosin.shareagenda.dialog;

import android.content.Context;
import android.widget.Button;
import android.widget.TextView;

import com.cosin.shareagenda.R;

import types.Event;
import types.EventRepeat;

public class DisplayEventRequestDialog extends BaseDialog {
    private final Event event;

    public DisplayEventRequestDialog(Context context, Event event) {
        super(context);
        this.event = event;
    }

    @Override
    protected void loadView() {
        window.setContentView(R.layout.activity_event_popup);
    }

    @Override
    protected void initView() {
        ((TextView)findViewById(R.id.type)).setText(event.getType().toString());
        ((TextView)findViewById(R.id.eventName)).setText(event.getEventname());
        ((TextView)findViewById(R.id.eventLocation)).setText(event.getLocation());
        ((TextView)findViewById(R.id.startTime)).setText(event.getStartTime());
        ((TextView)findViewById(R.id.endTime)).setText(event.getEndTime());
        ((TextView)findViewById(R.id.startDate)).setText(event.getRepeat().getStartDate());
        ((TextView)findViewById(R.id.endDate)).setText(event.getRepeat().getEndDate());
        ((TextView)findViewById(R.id.eventDescription)).setText(event.getDescription());

        EventRepeat eventRepeat = event.getRepeat().getType();
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
