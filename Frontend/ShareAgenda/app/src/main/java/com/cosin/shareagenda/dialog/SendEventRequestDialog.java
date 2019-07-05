package com.cosin.shareagenda.dialog;

import android.content.Context;
import android.widget.Button;
import android.widget.TextView;

import com.cosin.shareagenda.R;

public class SendEventRequestDialog extends BaseDialog {
    private String userName;
    private String date;
    private String time;

    public SendEventRequestDialog(Context context, String userName, String date, String time) {
        super(context);
        this.userName = userName;
        this.date = date;
        this.time = time;
    }

    @Override
    protected void loadView() {
        window.setContentView(R.layout.dialog_send_event_request);
    }

    @Override
    protected void initView() {
        ((TextView)findViewById(R.id.tvUserName)).setText(userName);
        ((TextView)findViewById(R.id.tvDate)).setText(date);
        ((TextView)findViewById(R.id.tvTime)).setText(time);

        Button btn = findViewById(R.id.btnSendEventRequest);
        btn.setOnClickListener(onClickListener);
    }

    @Override
    protected Object dealwithRet() {
        // no selected item in this dialog
        return true;
    }
}
