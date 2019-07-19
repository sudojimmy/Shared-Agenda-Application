package com.cosin.shareagenda.dialog;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.cosin.shareagenda.adapter.FriendContactsAdapter;
import com.cosin.shareagenda.adapter.GroupListAdapter;
import com.cosin.shareagenda.api.ApiErrorResponse;
import com.google.gson.Gson;

import cn.pedant.SweetAlert.SweetAlertDialog;
import types.Account;

import static com.cosin.shareagenda.access.net.CallbackHandler.HTTP_FAILURE;
import static com.cosin.shareagenda.access.net.CallbackHandler.SUCCESS;

public abstract class FriendDialog extends SweetAlertDialog {
    protected final Account friendAccount;

    private DisplayAccountBaseDialog displayAccountRequestDialog;
    private RecyclerView.Adapter adapter;
    private int position;

    public FriendDialog(Context context,
                        DisplayAccountBaseDialog displayAccountDialog,
                        int alertType,
                        Account friendAccount,
                        int position,
                        RecyclerView.Adapter adapter) {
        super(context, alertType);
        this.displayAccountRequestDialog = displayAccountDialog;
        this.friendAccount = friendAccount;

        this.position = position;
        this.adapter = adapter;
    }

    public abstract String getTitleTextText();

    public abstract String getContentText();

    protected Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(android.os.Message message) {
            final Gson gson = new Gson();
            switch (message.what) {
                case SUCCESS:
                    FriendDialog.this
                            .setTitleText(getTitleTextText())
                            .setContentText(getContentText())
                            .setConfirmText("OK")
                            .showCancelButton(false)
                            .setConfirmClickListener(null)
                            .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);

                    displayAccountRequestDialog.dismiss();
                    if (adapter instanceof FriendContactsAdapter) {
                        ((FriendContactsAdapter)adapter).removeElementFromContactList(position);
                    } else if (adapter instanceof GroupListAdapter) {
                        ((GroupListAdapter)adapter).updateInformation();
                    }
                    break;
                case HTTP_FAILURE:
                    ApiErrorResponse errorResponse = gson.fromJson((String) message.obj, ApiErrorResponse.class);
                    FriendDialog.this
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

