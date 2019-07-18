package com.cosin.shareagenda.dialog;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.cosin.shareagenda.R;
import com.cosin.shareagenda.access.net.CallbackHandler;
import com.cosin.shareagenda.api.ApiClient;
import com.cosin.shareagenda.api.ApiErrorResponse;
import com.google.gson.Gson;

import types.Account;
import types.GetAccountResponse;

import static com.cosin.shareagenda.access.net.CallbackHandler.HTTP_FAILURE;
import static com.cosin.shareagenda.access.net.CallbackHandler.SUCCESS;

public class DisplayFriendRequestAccountDialog extends DisplayAccountBaseDialog {
    private DisplayFriendRequestAccountDialog conAdapter;


    public DisplayFriendRequestAccountDialog(Context context,
                                      String accountId,
                                      int position) {
        super(context, accountId, position);
        ApiClient.getAccount(accountId, new CallbackHandler(handlerFriendRequest));

    }

    @Override
    protected void initView() {
        Button btn = findViewById(R.id.manage);
        btn.setVisibility(View.GONE);
    }

    Handler handlerFriendRequest = new Handler(Looper.getMainLooper()) {
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
                            .withDescription(resp.getDescription()));
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
