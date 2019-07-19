package com.cosin.shareagenda.dialog;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.cosin.shareagenda.R;

import cn.pedant.SweetAlert.SweetAlertDialog;
import types.Account;

public class DisplayAddAccountDialog extends DisplayAccountBaseDialog {
    private RecyclerView.Adapter adapter;

    public DisplayAddAccountDialog(Context context,
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
                        adapter)
                        .show();

            }
        });

    }



}
