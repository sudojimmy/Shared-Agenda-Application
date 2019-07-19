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


    public DisplayFriendRequestAccountDialog(Context context,
                                      Account account,
                                      int position) {
        super(context, account, position);
        //ApiClient.getAccount(accountId, new CallbackHandler(handlerFriendRequest));

    }

    @Override
    protected void initView() {
        Button btn = findViewById(R.id.manage);
        btn.setVisibility(View.GONE);

        profileImage = findViewById(R.id.profile_image);
    }

//    Handler handlerFriendRequest = new Handler(Looper.getMainLooper()) {
//        @Override
//        public void handleMessage(android.os.Message message) {
//            final Gson gson = new Gson();
//            switch (message.what) {
//                case SUCCESS:
//                    String body = (String) message.obj;
//                    GetAccountResponse resp = gson.fromJson(body, GetAccountResponse.class);
//                    setAccount(new Account()
//                            .withAccountId(resp.getAccountId())
//                            .withCalendarId(resp.getCalendarId())
//                            .withFriendQueueId(resp.getFriendQueueId())
//                            .withGroupQueueId(resp.getGroupQueueId())
//                            .withMessageQueueId(resp.getMessageQueueId())
//                            .withNickname(resp.getNickname())
//                            .withDescription(resp.getDescription()));
//                    break;
//                case HTTP_FAILURE:
//                    ApiErrorResponse errorResponse = gson.fromJson((String) message.obj, ApiErrorResponse.class);
//                    Toast.makeText(context, errorResponse.getMessage(), Toast.LENGTH_SHORT).show();
//                    break;
//                default:
//                    Toast.makeText(context, (String) message.obj, Toast.LENGTH_SHORT).show();
//            }
//        }
//    };

}
