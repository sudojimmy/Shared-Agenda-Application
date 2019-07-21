package com.cosin.shareagenda.dialog;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cosin.shareagenda.R;
import com.cosin.shareagenda.access.net.CallbackHandler;
import com.cosin.shareagenda.api.ApiClient;
import com.cosin.shareagenda.api.ApiErrorResponse;
import com.google.gson.Gson;

import cn.pedant.SweetAlert.SweetAlertDialog;
import types.Event;
import types.EventRepeat;

import static com.cosin.shareagenda.access.net.CallbackHandler.SUCCESS;

public class DisplayEventRequestDialog extends BaseDialog {
    private final Event event;
    private final boolean showJoinBtn;

    public DisplayEventRequestDialog(Context context, Event event, boolean showJoinBtn) {
        super(context);
        this.event = event;
        this.showJoinBtn = showJoinBtn;
    }

    @Override
    protected void loadView() {
        window.setContentView(R.layout.display_event);
    }

    @Override
    protected void initView() {
        ((TextView)findViewById(R.id.type)).setText(event.getType().toString());
        ((TextView)findViewById(R.id.eventName)).setText(event.getEventname());
        ((TextView)findViewById(R.id.eventLocation)).setText(event.getLocation());
        ((TextView)findViewById(R.id.eventDescription)).setText(event.getDescription());

        TextView tvTime = findViewById(R.id.tvTime);
        tvTime.setText(String.format("%s - %s", event.getStartTime(), event.getEndTime()));

        TextView tvDate = findViewById(R.id.tvDate);
        tvDate.setText(event.getRepeat().getStartDate());

        TextView tvRepeat = findViewById(R.id.tvRepeat);
        if (event.getRepeat().getType().equals(EventRepeat.ONCE)) {
            tvRepeat.setText(event.getRepeat().getType().toString());
        } else {
            tvRepeat.setText(String.format("Every %s until %s",
                    event.getRepeat().getType().toString(),
                    event.getRepeat().getEndDate()));
        }

        TextView tvStarterId = findViewById(R.id.tvStarterId);
        tvStarterId.setText(event.getStarterId());

        Button btn = findViewById(R.id.joinButton);
        if (showJoinBtn) {
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ApiClient.joinEvent(event.getEventId(), new CallbackHandler(handler));
                }
            });
        } else {
            btn.setVisibility(View.GONE);
        }
    }

    Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message message) {
            final Gson gson = new Gson();
            switch (message.what) {
                case SUCCESS:
                    new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("SUCCESS")
                            .setContentText("Joined Event: " + event.getEventname())
                            .setConfirmText("OK").setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismissWithAnimation();
                            DisplayEventRequestDialog.this.dismiss();
                        }
                    }).show();
                    break;
                case CallbackHandler.HTTP_FAILURE:
                    ApiErrorResponse errorResponse = gson.fromJson((String) message.obj, ApiErrorResponse.class);
                    Toast.makeText(context, errorResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(context, (String) message.obj, Toast.LENGTH_SHORT).show();
            }
        }
    };
    @Override
    protected Object dealwithRet() {
        // no selected item in this dialog
        return true;
    }
}
