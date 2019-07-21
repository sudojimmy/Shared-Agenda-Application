package com.cosin.shareagenda.activity;

import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;

import androidx.viewpager.widget.PagerAdapter;

import com.cosin.shareagenda.R;
import com.cosin.shareagenda.access.net.CallbackHandler;
import com.cosin.shareagenda.adapter.EventMessagePagerAdapter;
import com.cosin.shareagenda.api.ApiClient;
import com.cosin.shareagenda.api.ApiErrorResponse;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import types.EventMessage;
import types.GetEventMessageQueueResponse;

import static com.cosin.shareagenda.access.net.CallbackHandler.HTTP_FAILURE;
import static com.cosin.shareagenda.access.net.CallbackHandler.SUCCESS;

public class AccountEventMessageActivity extends EventMessagesActivity {
    List<EventMessage> eventMsgs = new ArrayList<>();

    @Override
    protected int getContentView() {
        return R.layout.activity_event_message;
    }

    @Override
    protected void loadData() {
        ApiClient.getEventMessageQueue(new CallbackHandler(getEventMsgHandler));
    }

    @Override
    protected boolean loadMessage() {
        return false;
    }

    @Override
    protected void initView() {
        super.initView();
    }

    @Override
    protected PagerAdapter getAdapter() {
        return new EventMessagePagerAdapter(eventMsgs, this);
    }

    @Override
    protected String onEmptyText() {
        return "No More Event Request";
    }

    @Override
    protected String titleName() {
        return "Event Requests";
    }

    Handler getEventMsgHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(android.os.Message message) {
            final Gson gson = new Gson();
            switch (message.what) {
                case SUCCESS:
                    String body = (String) message.obj;
                    GetEventMessageQueueResponse resp = gson.fromJson(body, GetEventMessageQueueResponse.class);
                    eventMsgs = resp.getMessageList();
                    ((EventMessagePagerAdapter)adapter).setEvents(eventMsgs);
                    defaultText.setVisibility(eventMsgs.isEmpty() ? View.VISIBLE : View.GONE);
                    break;
                case HTTP_FAILURE:
                    ApiErrorResponse errorResponse = gson.fromJson((String) message.obj, ApiErrorResponse.class);
                    Toast.makeText(AccountEventMessageActivity.this, errorResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(AccountEventMessageActivity.this, (String) message.obj, Toast.LENGTH_SHORT).show();
            }
        }
    };
}
