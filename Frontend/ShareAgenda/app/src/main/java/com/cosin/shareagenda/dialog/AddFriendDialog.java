package com.cosin.shareagenda.dialog;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.cosin.shareagenda.access.net.CallbackHandler;
import com.cosin.shareagenda.adapter.SearchFriendsAdapter;
import com.cosin.shareagenda.api.ApiClient;
import com.cosin.shareagenda.api.ApiErrorResponse;
import com.google.gson.Gson;

import cn.pedant.SweetAlert.SweetAlertDialog;
import types.Account;

import static com.cosin.shareagenda.access.net.CallbackHandler.HTTP_FAILURE;
import static com.cosin.shareagenda.access.net.CallbackHandler.SUCCESS;

public class AddFriendDialog extends SweetAlertDialog {
    private final Account friendAccount;
    private SweetAlertDialog alertDialog;

    private DisplayAccountBaseDialog displayAccountRequestDialog;
    private SearchFriendsAdapter searchFriendsAdapter;
    private int position;

    public AddFriendDialog(Context context,
                              DisplayAccountBaseDialog displayAccountDialog,
                              int alertType,
                              Account friendAccount,
                              int position,
                              SearchFriendsAdapter searchFriendsAdapter) {
        super(context, alertType);
        this.displayAccountRequestDialog = displayAccountDialog;
        this.friendAccount = friendAccount;

        this.position = position;
        this.searchFriendsAdapter = searchFriendsAdapter;

        alertDialog = this;

        ApiClient.inviteFriend(friendAccount.getAccountId(),
                new CallbackHandler(addAccountHandler));
        //setContentText("Pending...");
    }


    private Handler addAccountHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(android.os.Message message) {
            final Gson gson = new Gson();
            switch (message.what) {
                case SUCCESS:
                    alertDialog
                            .setTitleText("Added!")
                            .setContentText("Your friend invitation has been sent!")
                            .setConfirmText("OK")
                            .showCancelButton(false)
                            .setConfirmClickListener(null)
                            .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);

                    displayAccountRequestDialog.dismiss();
                    if(searchFriendsAdapter != null) {
                        searchFriendsAdapter.removeElementFromContactList(position);
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
