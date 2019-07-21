package com.cosin.shareagenda.activity;

import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cosin.shareagenda.R;
import com.cosin.shareagenda.access.net.CallbackHandler;
import com.cosin.shareagenda.adapter.FriendMessageAdapter;
import com.cosin.shareagenda.api.ApiClient;
import com.cosin.shareagenda.api.ApiErrorResponse;
import com.google.gson.Gson;

import types.GetNewFriendMessageQueueResponse;

import static com.cosin.shareagenda.access.net.CallbackHandler.HTTP_FAILURE;
import static com.cosin.shareagenda.access.net.CallbackHandler.SUCCESS;

public class FriendMessageActivity extends RefreshableActivity {
    private FriendMessageAdapter friendMessageAdapter;

    @Override
    protected int getContentView() {
        return R.layout.activity_friend_message;
    }

    @Override
    protected void loadData() {
        ApiClient.getNewFriendMessageQueue(new CallbackHandler(handler));
    }

    @Override
    protected boolean loadMessage() {
        return false;
    }

    @Override
    protected String titleName() {
        return getResources().getString(R.string.title_friend_message);
    }

    @Override
    protected void initView() {
        super.initView();

        // recycleview
        RecyclerView recyclerView = findViewById(R.id.rvFriendsMsg);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);
        friendMessageAdapter = new FriendMessageAdapter(this);
        recyclerView.setAdapter(friendMessageAdapter);
    }
    Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(android.os.Message message) {
            final Gson gson = new Gson();
            switch (message.what) {
                case SUCCESS:
                    String body = (String) message.obj;
                    GetNewFriendMessageQueueResponse resp
                            = gson.fromJson(body, GetNewFriendMessageQueueResponse.class);
                    friendMessageAdapter.setMessages(resp.getMessageList());
                    break;
                case HTTP_FAILURE:
                    ApiErrorResponse errorResponse = gson.fromJson((String) message.obj, ApiErrorResponse.class);
                    Toast.makeText(FriendMessageActivity.this, errorResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(FriendMessageActivity.this, (String) message.obj, Toast.LENGTH_SHORT).show();
            }
            stopRefreshing();
        }
    };

}
