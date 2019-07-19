package com.cosin.shareagenda.dialog;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import androidx.recyclerview.widget.RecyclerView;

import com.cosin.shareagenda.R;

import cn.pedant.SweetAlert.SweetAlertDialog;
import types.Account;

public class DisplayFriendAccountDialog extends DisplayAccountBaseDialog {
    private DisplayFriendAccountDialog conAdapter;
    private RecyclerView.Adapter adapter;


    public DisplayFriendAccountDialog(Context context,
                                       Account account,
                                       int position,
                                       RecyclerView.Adapter adapter) {
        super(context, account, position);

        this.adapter = adapter;
    }


    @Override
    protected void initView() {
        super.initView();

        Button btn = findViewById(R.id.manage);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // delete the friend
                new DeleteFriendDialog(
                        context,
                        getDisplayAccountBaseDialog(),
                        SweetAlertDialog.WARNING_TYPE,
                        DisplayFriendAccountDialog.super.getAccount(),
                        DisplayFriendAccountDialog.super.getPosition(),
                        adapter)
                        .show();
            }
        });

    }
}
