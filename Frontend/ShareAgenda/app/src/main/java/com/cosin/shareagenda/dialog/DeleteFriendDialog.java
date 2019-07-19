package com.cosin.shareagenda.dialog;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import com.cosin.shareagenda.access.net.CallbackHandler;
import com.cosin.shareagenda.api.ApiClient;

import cn.pedant.SweetAlert.SweetAlertDialog;
import types.Account;

public class DeleteFriendDialog extends FriendDialog {
    private SweetAlertDialog alertDialog;

    public DeleteFriendDialog(Context context, DisplayAccountBaseDialog displayAccountDialog, int alertType, Account friendAccount, int position, RecyclerView.Adapter adapter) {
        super(context, displayAccountDialog, alertType, friendAccount, position, adapter);
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
                        new CallbackHandler(handler));
                sDialog.changeAlertType(SweetAlertDialog.PROGRESS_TYPE);
                sDialog.setContentText("Deleting ...");
                alertDialog = sDialog;
            }
        });


    }

    @Override
    public String getTitleTextText() {
        return "Deleted!";
    }

    @Override
    public String getContentText() {
        return "Your friendAccount has been deleted!";
    }
}

