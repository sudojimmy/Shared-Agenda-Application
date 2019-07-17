package com.cosin.shareagenda.dialog;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import androidx.core.content.ContextCompat;

import com.cosin.shareagenda.R;
import com.cosin.shareagenda.access.net.CallbackHandler;
import com.cosin.shareagenda.adapter.SearchFriendsAdapter;
import com.cosin.shareagenda.api.ApiClient;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class DisplayAddAccountDialog extends DisplayAccountBaseDialog {
    private DisplayAddAccountDialog conAdapter;
    private SearchFriendsAdapter searchFriendAdapter;


    public DisplayAddAccountDialog(Context context,
                                   String accountId,
                                   int position,
                                   SearchFriendsAdapter searchFriendAdapter) {
        super(context, accountId, position);
        ApiClient.getAccount(accountId, new CallbackHandler(handler));

        this.searchFriendAdapter = searchFriendAdapter;

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
