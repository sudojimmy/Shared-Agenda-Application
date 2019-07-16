package com.cosin.shareagenda.dialog;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cosin.shareagenda.R;
import com.cosin.shareagenda.access.net.CallbackHandler;
import com.cosin.shareagenda.activity.ContactsActivity;
import com.cosin.shareagenda.adapter.FriendContactsAdapter;
import com.cosin.shareagenda.api.ApiClient;
import com.cosin.shareagenda.api.ApiErrorResponse;
import com.cosin.shareagenda.entity.DisplayableEvent;
import com.google.gson.Gson;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import types.Account;
import types.CreateAccountResponse;
import types.GetAccountResponse;
import types.GetFriendQueueResponse;

import static com.cosin.shareagenda.access.net.CallbackHandler.HTTP_FAILURE;
import static com.cosin.shareagenda.access.net.CallbackHandler.SUCCESS;

public class DisplayAccountRequestDialog extends BaseDialog {
    private Account account;
    private DisplayAccountRequestDialog conAdapter;

    private FriendContactsAdapter friendContactsAdapter;
    private int position;


    public DisplayAccountRequestDialog(Context context,
                                       String accountId,
                                       int position,
                                       FriendContactsAdapter friendContactsAdapter) {
        super(context);
        ApiClient.getAccount(accountId, new CallbackHandler(handler));

        this.friendContactsAdapter = friendContactsAdapter;
        this.position = position;
    }

    @Override
    protected void loadView() {
        window.setContentView(R.layout.activity_friend_account_popup);
    }

    private DisplayAccountRequestDialog getDisplayAccountRequestDialog(){
        return this;
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
                        getDisplayAccountRequestDialog(),
                        SweetAlertDialog.WARNING_TYPE,
                        account,
                        position,
                        friendContactsAdapter)
                        .show();

                // todo
                //ApiClient.deleteFriend(account.getAccountId(), new CallbackHandler(handlerDeleteFriend));

            }
        });

    }


    private void updateView(){
        ((TextView)findViewById(R.id.friendName)).setText(account.getNickname());
        ((TextView)findViewById(R.id.profile_email)).setText(account.getAccountId());
        ((TextView)findViewById(R.id.profile_description)).setText(account.getDescription());
    }

    @Override
    protected Object dealwithRet() {
        // no selected item in this dialog
        return true;
    }

    public void setAccount(Account account) {
        this.account = account;
        updateView();
    }

    Handler handlerDeleteFriend = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(android.os.Message message) {
            final Gson gson = new Gson();
            switch (message.what) {
                case SUCCESS:
                    String body = (String) message.obj;
//                    GetMessageQueueResponse resp = gson.fromJson(body, GetMessageQueueResponse.class);
                    break;
                case HTTP_FAILURE:
                    ApiErrorResponse errorResponse = gson.fromJson((String) message.obj, ApiErrorResponse.class);
//                    Toast.makeText(FriendMessageActivity.this, errorResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    break;
                default:
//                    Toast.makeText(FriendMessageActivity.this, (String) message.obj, Toast.LENGTH_SHORT).show();
            }
        }
    };

    Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(android.os.Message message) {
            final Gson gson = new Gson();
            switch (message.what) {
                case SUCCESS:
                    String body = (String) message.obj;
                    GetAccountResponse resp = gson.fromJson(body, GetAccountResponse.class);
                    setAccount(new Account()
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
                    Toast.makeText(context, errorResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(context, (String) message.obj, Toast.LENGTH_SHORT).show();
            }
        }
    };
}
