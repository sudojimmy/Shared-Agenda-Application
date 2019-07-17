package com.cosin.shareagenda.dialog;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import com.cosin.shareagenda.R;
import com.cosin.shareagenda.access.net.CallbackHandler;
import com.cosin.shareagenda.adapter.FriendContactsAdapter;
import com.cosin.shareagenda.api.ApiClient;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class DisplayAccountRequestDialog extends DisplayAccountBaseDialog {
 //   private Account account;
    private DisplayAccountRequestDialog conAdapter;
    private FriendContactsAdapter friendContactsAdapter;
 //   private int position;


    public DisplayAccountRequestDialog(Context context,
                                       String accountId,
                                       int position,
                                       FriendContactsAdapter friendContactsAdapter) {
        super(context, accountId, position);
        ApiClient.getAccount(accountId, new CallbackHandler(handler));

        this.friendContactsAdapter = friendContactsAdapter;
//        this.position = position;
    }


    @Override
    protected void initView() {
        Button btn = findViewById(R.id.manage);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // delete the friend
                new DeleteFriendDialog(
                        context,
                        getDisplayAccountBaseDialog(),
                        SweetAlertDialog.WARNING_TYPE,
                        DisplayAccountRequestDialog.super.getAccount(),
                        DisplayAccountRequestDialog.super.getPosition(),
                        friendContactsAdapter)
                        .show();

                // todo
                //ApiClient.deleteFriend(account.getAccountId(), new CallbackHandler(handlerDeleteFriend));

            }
        });

    }
}
