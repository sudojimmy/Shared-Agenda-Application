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


//    public DisplayAccountBaseDialog(Context context, String accountId, int position) {
//        super(context);
//        ApiClient.getAccount(accountId, new CallbackHandler(handler));
//
//        this.position = position;
//    }

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
    }

    public Account getAccount() {
        return account;
    }

    public int getPosition() {
        return position;
    }

    private void updateView(){
        ((TextView) findViewById(R.id.friendName)).setText(account.getNickname());
        ((TextView) findViewById(R.id.profile_email)).setText(account.getAccountId());
        ((TextView) findViewById(R.id.profile_description)).setText(account.getDescription());

        if (account.getProfileImageUrl() != null) {
            ImageView profileImage = findViewById(R.id.profile_image);
            Picasso.with(getContext()).load(account.getProfileImageUrl())
                    .centerInside().fit().into(profileImage);
        }
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

//    Handler handler = new Handler(Looper.getMainLooper()) {
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
//                            .withProfileImageUrl(resp.getProfileImageUrl())
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
