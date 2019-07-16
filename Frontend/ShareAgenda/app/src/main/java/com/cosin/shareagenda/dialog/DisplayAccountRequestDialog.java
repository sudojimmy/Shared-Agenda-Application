package com.cosin.shareagenda.dialog;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cosin.shareagenda.R;
import com.cosin.shareagenda.access.net.CallbackHandler;
import com.cosin.shareagenda.activity.ContactsActivity;
import com.cosin.shareagenda.adapter.FriendContactsAdapter;
import com.cosin.shareagenda.entity.DisplayableEvent;
import com.cosin.shareagenda.model.ApiClient;
import com.cosin.shareagenda.model.ApiErrorResponse;
import com.google.gson.Gson;

import java.util.List;

import types.Account;
import types.CreateAccountResponse;
import types.GetAccountResponse;
import types.GetFriendQueueResponse;

import static com.cosin.shareagenda.access.net.CallbackHandler.HTTP_FAILURE;
import static com.cosin.shareagenda.access.net.CallbackHandler.SUCCESS;

public class DisplayAccountRequestDialog extends BaseDialog {
    private Account account;
    private DisplayAccountRequestDialog conAdapter;


    public DisplayAccountRequestDialog(Context context, String accountid) {
        super(context);
        ApiClient.getAccount(accountid, new CallbackHandler(handler));
    }

    @Override
    protected void loadView() {
        window.setContentView(R.layout.activity_friend_account_popup);
    }



    @Override
    protected void initView() {
        ((TextView)findViewById(R.id.friendName)).setText(account.getNickname());
        ((TextView)findViewById(R.id.profile_email)).setText(account.getAccountId());
        ((TextView)findViewById(R.id.profile_description)).setText(account.getDescription());

        Button btn = findViewById(R.id.manage);
        btn.setOnClickListener(onClickListener);
    }

    @Override
    protected Object dealwithRet() {
        // no selected item in this dialog
        return true;
    }

    public void setAccount(Account account) {
        this.account = account;
        notifyAll();
        //notifyDataSetChanged();
    }


    Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(android.os.Message message) {
            final Gson gson = new Gson();
            switch (message.what) {
                case SUCCESS:
                    String body = (String) message.obj;
                    GetAccountResponse resp = gson.fromJson(body, GetAccountResponse.class);
                    conAdapter.setAccount(new Account()
                            .withAccountId(resp.getAccountId())
                            .withCalendarId(resp.getCalendarId())
                            .withFriendQueueId(resp.getFriendQueueId())
                            .withGroupQueueId(resp.getGroupQueueId())
                            .withMessageQueueId(resp.getMessageQueueId())
                            .withNickname(resp.getNickname())
                            .withDescription(resp.getDescription()));//setContactList(resp.getFriendList());
                    break;
                case HTTP_FAILURE:
                    ApiErrorResponse errorResponse = gson.fromJson((String) message.obj, ApiErrorResponse.class);
                    //Toast.makeText(DisplayAccountRequestDialog.this, errorResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    break;
                default:
                    //Toast.makeText(DisplayAccountRequestDialog.this, (String) message.obj, Toast.LENGTH_SHORT).show();
            }
        }
    };
}
