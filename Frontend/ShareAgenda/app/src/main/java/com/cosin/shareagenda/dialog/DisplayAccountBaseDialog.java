package com.cosin.shareagenda.dialog;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.cosin.shareagenda.R;
import com.squareup.picasso.Picasso;

import types.Account;

public class DisplayAccountBaseDialog extends BaseDialog {
    private Account account;
    private int position;


    public DisplayAccountBaseDialog(Context context, Account account, int position) {
        super(context);
        this.position = position;
        setAccount(account);
    }

    @Override
    protected void loadView(){
        window.setContentView(R.layout.activity_friend_account_popup);
    }

    protected DisplayAccountBaseDialog getDisplayAccountBaseDialog(){
        return this;
    }


    @Override
    protected void initView() {
        // implemented by subclass
        ((TextView) findViewById(R.id.friendName)).setText(account.getNickname());
        ((TextView) findViewById(R.id.profile_email)).setText(account.getAccountId());
        ((TextView) findViewById(R.id.profile_description)).setText(account.getDescription());

        if (account.getProfileImageUrl() != null) {
            ImageView profileImage = findViewById(R.id.profile_image);
            Picasso.with(getContext()).load(account.getProfileImageUrl())
                    .centerInside().fit().into(profileImage);
        }
    }

    public Account getAccount() {
        return account;
    }

    public int getPosition() {
        return position;
    }

    @Override
    protected Object dealwithRet() {
        // no selected item in this dialog
        return true;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
