package com.cosin.shareagenda.dialog;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.cosin.shareagenda.R;

import types.Account;

public class DisplayFriendRequestAccountDialog extends DisplayAccountBaseDialog {
    private DisplayFriendRequestAccountDialog conAdapter;
    private ImageView profileImage;


    public DisplayFriendRequestAccountDialog(Context context, Account account, int position) {
        super(context, account, position);
    }

    @Override
    protected void initView() {
        super.initView();

        Button btn = findViewById(R.id.manage);
        btn.setVisibility(View.GONE);

        profileImage = findViewById(R.id.profile_image);
    }
}
