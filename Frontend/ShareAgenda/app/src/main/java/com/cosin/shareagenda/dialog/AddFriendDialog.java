package com.cosin.shareagenda.dialog;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import com.cosin.shareagenda.access.net.CallbackHandler;
import com.cosin.shareagenda.api.ApiClient;

import types.Account;

public class AddFriendDialog extends FriendDialog {
    public AddFriendDialog(Context context, DisplayAccountBaseDialog displayAccountDialog, int alertType, Account friendAccount, int position, RecyclerView.Adapter adapter) {
        super(context, displayAccountDialog, alertType, friendAccount, position, adapter);
        init();
    }

    void init() {
        ApiClient.inviteFriend(friendAccount.getAccountId(),
                new CallbackHandler(handler));
    }

    @Override
    public String getTitleTextText() {
        return "Added!";
    }

    @Override
    public String getContentText() {
        return "Your friend invitation has been sent!";
    }
}
