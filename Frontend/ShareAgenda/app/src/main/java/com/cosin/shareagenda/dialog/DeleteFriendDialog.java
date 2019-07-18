package com.cosin.shareagenda.dialog;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.cosin.shareagenda.access.net.CallbackHandler;
import com.cosin.shareagenda.adapter.FriendContactsAdapter;
import com.cosin.shareagenda.api.ApiClient;
import com.cosin.shareagenda.api.ApiErrorResponse;
import com.google.gson.Gson;

import cn.pedant.SweetAlert.SweetAlertDialog;
import types.Account;

import static com.cosin.shareagenda.access.net.CallbackHandler.HTTP_FAILURE;
import static com.cosin.shareagenda.access.net.CallbackHandler.SUCCESS;

public class DeleteFriendDialog extends SweetAlertDialog {
    private final Account friendAccount;
    private SweetAlertDialog alertDialog;

    private DisplayAccountBaseDialog displayAccountRequestDialog;
    private FriendContactsAdapter friendContactsAdapter;
    private int position;

    public DeleteFriendDialog(Context context,
                              DisplayAccountBaseDialog displayAccountDialog,
                              int alertType,
                              Account friendAccount,
                              int position,
                              FriendContactsAdapter friendContactsAdapter) {
        super(context, alertType);
        this.displayAccountRequestDialog = displayAccountDialog;
        this.friendAccount = friendAccount;

        this.position = position;
        this.friendContactsAdapter = friendContactsAdapter;

        init();
    }

    void init() {
        setTitleText("Delete Account \"" + friendAccount.getNickname()+ "\"?");
        setContentText("Won't be able to recover this action!");
        setConfirmText("Yes,delete it!");
        setCancelText("Cancel");
        setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {
                ApiClient.deleteFriend(friendAccount.getAccountId(),
                        new CallbackHandler(deleteAccountHandler));
                sDialog.changeAlertType(SweetAlertDialog.PROGRESS_TYPE);
                sDialog.setContentText("Deleting ...");
                alertDialog = sDialog;
            }
        });


    }

    private Handler deleteAccountHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(android.os.Message message) {
            final Gson gson = new Gson();
            switch (message.what) {
                case SUCCESS:
                    alertDialog
                            .setTitleText("Deleted!")
                            .setContentText("Your friendAccount has been deleted!")
                            .setConfirmText("OK")
                            .showCancelButton(false)
                            .setConfirmClickListener(null)
                            .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);

                    displayAccountRequestDialog.dismiss();

                    if (friendContactsAdapter != null) {
                        friendContactsAdapter.removeElementFromContactList(position);
                    }
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
