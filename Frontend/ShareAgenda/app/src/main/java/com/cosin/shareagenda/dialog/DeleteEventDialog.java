package com.cosin.shareagenda.dialog;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.alamkanak.weekview.WeekView;
import com.cosin.shareagenda.access.net.CallbackHandler;
import com.cosin.shareagenda.api.ApiClient;
import com.cosin.shareagenda.api.ApiErrorResponse;
import com.google.gson.Gson;

import cn.pedant.SweetAlert.SweetAlertDialog;
import types.Event;

import static com.cosin.shareagenda.access.net.CallbackHandler.HTTP_FAILURE;
import static com.cosin.shareagenda.access.net.CallbackHandler.SUCCESS;

public class DeleteEventDialog extends SweetAlertDialog {
    private final WeekView weekView;
    private final Event event;
    private SweetAlertDialog alertDialog;

    public DeleteEventDialog(Context context, int alertType, WeekView weekView, Event event) {
        super(context, alertType);
        this.weekView = weekView;
        this.event = event;
        init();
    }

    void init() {
        setTitleText("Delete Event \"" + event.getEventname()+ "\"?");
        setContentText("Won't be able to recover this action!");
        setConfirmText("Yes,delete it!");
        setCancelText("Cancel");
        setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {
                ApiClient.deleteEvent(event.getEventId(), new CallbackHandler(deleteEventHandler));
                sDialog.changeAlertType(SweetAlertDialog.PROGRESS_TYPE);
                sDialog.setContentText("Deleting ...");
                alertDialog = sDialog;
            }
        });
    }

    private Handler deleteEventHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(android.os.Message message) {
            final Gson gson = new Gson();
            switch (message.what) {
                case SUCCESS:
                    alertDialog
                            .setTitleText("Deleted!")
                            .setContentText("Your event has been deleted!")
                            .setConfirmText("OK")
                            .showCancelButton(false)
                            .setConfirmClickListener(null)
                            .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                    weekView.notifyDatasetChanged();
                    break;
                case HTTP_FAILURE:
                    ApiErrorResponse errorResponse = gson.fromJson((String) message.obj, ApiErrorResponse.class);
                    alertDialog
                            .setTitleText("Error!")
                            .setContentText(errorResponse.getMessage())
                            .setConfirmText("OK")
                            .showCancelButton(false)
                            .setConfirmClickListener(null)
                            .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                    break;
                default:
                    Toast.makeText(getContext(), (String) message.obj, Toast.LENGTH_SHORT).show();
            }
        }
    };
}
