package com.cosin.shareagenda.dialog;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.cosin.shareagenda.R;
import com.cosin.shareagenda.access.net.CallbackHandler;
import com.cosin.shareagenda.activity.ContactsActivity;
import com.cosin.shareagenda.adapter.SearchFriendsAdapter;
import com.cosin.shareagenda.api.ApiClient;
import com.cosin.shareagenda.api.ApiErrorResponse;
import com.cosin.shareagenda.entity.DisplayableEvent;
import com.google.common.io.Resources;
import com.google.gson.Gson;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import types.Account;
import types.CreateAccountResponse;
import types.GetAccountResponse;
import types.GetFriendQueueResponse;

import static com.cosin.shareagenda.access.net.CallbackHandler.HTTP_FAILURE;
import static com.cosin.shareagenda.access.net.CallbackHandler.SUCCESS;

public class DisplayAddAccountDialog extends DisplayAccountBaseDialog {
    //   private Account account;
    private DisplayAddAccountDialog conAdapter;
    private SearchFriendsAdapter searchFriendAdapter;
    //   private int position;


    public DisplayAddAccountDialog(Context context,
                                   String accountId,
                                   int position,
                                   SearchFriendsAdapter searchFriendAdapter) {
        super(context, accountId, position);
        ApiClient.getAccount(accountId, new CallbackHandler(handler));

        this.searchFriendAdapter = searchFriendAdapter;

//        this.position = position;
    }




    @Override
    protected void initView() {
        Button btn = findViewById(R.id.manage);
        String add = "ADD";
        btn.setText(add);
        btn.setTextColor(ContextCompat.getColor(context, R.color.success));
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // add the friend
                new AddFriendDialog(
                        context,
                        getDisplayAccountBaseDialog(),
                        SweetAlertDialog.PROGRESS_TYPE,
                        DisplayAddAccountDialog.super.getAccount(),
                        DisplayAddAccountDialog.super.getPosition(),
                        searchFriendAdapter)
                        .show();

            }
        });

    }



}
