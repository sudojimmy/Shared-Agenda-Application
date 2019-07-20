package com.cosin.shareagenda.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;

import com.cosin.shareagenda.R;
import com.cosin.shareagenda.access.net.CallbackHandler;
import com.cosin.shareagenda.adapter.GroupEventMessagePagerAdapter;
import com.cosin.shareagenda.api.ApiClient;
import com.cosin.shareagenda.api.ApiErrorResponse;
import com.cosin.shareagenda.model.Model;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import types.Event;
import types.EventType;
import types.GetGroupCalendarEventListResponse;
import types.Group;

import static com.cosin.shareagenda.access.net.CallbackHandler.HTTP_FAILURE;
import static com.cosin.shareagenda.access.net.CallbackHandler.SUCCESS;

public class GroupEventMessagesActivity extends EventMessagesActivity {
//    EventMessagePagerAdapter adapter;
    List<Event> eventMsgs = new ArrayList<>();
    private Group group;

    @Override
    protected int getContentView() {
        return R.layout.activity_event_message;
    }

    @Override
    protected void loadData() {
        ApiClient.getGroupCalendarEventList(group.getGroupId(), new CallbackHandler(getEventMsgHandler));
    }


    // https://htmlcolorcodes.com/color-chart/material-design-color-chart/
    private static final Map<EventType, Integer> colorMap = Collections.unmodifiableMap(
            new HashMap<EventType, Integer>() {{
                put(EventType.APPOINTMENT, Color.rgb(255, 205, 210));
                put(EventType.ENTERTAINMENT, Color.rgb(255, 245, 157));
                put(EventType.OTHER, Color.rgb(215, 204, 200));
                put(EventType.WORK, Color.rgb(197, 202, 233));
                put(EventType.STUDY, Color.rgb(200, 230, 201));
            }});

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        group = Model.model.getCurrentGroup();
        super.onCreate(savedInstanceState);
    }

    @Override
    protected PagerAdapter getAdapter() {
        return new GroupEventMessagePagerAdapter(eventMsgs, this);
    }

    @Override
    protected String onEmptyText() {
        return "No Available Group Event";
    }

    @Override
    protected String titleName() {
        return group.getName() + " Group Event";
    }

    Handler getEventMsgHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(android.os.Message message) {
            final Gson gson = new Gson();
            switch (message.what) {
                case SUCCESS:
                    String body = (String) message.obj;
                    GetGroupCalendarEventListResponse resp = gson.fromJson(body, GetGroupCalendarEventListResponse.class);
                    eventMsgs = resp.getEventList();
                    ((GroupEventMessagePagerAdapter)adapter).setEvents(eventMsgs);
                    defaultText.setVisibility(eventMsgs.isEmpty() ? View.VISIBLE : View.GONE);
                    break;
                case HTTP_FAILURE:
                    ApiErrorResponse errorResponse = gson.fromJson((String) message.obj, ApiErrorResponse.class);
                    Toast.makeText(GroupEventMessagesActivity.this, errorResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(GroupEventMessagesActivity.this, (String) message.obj, Toast.LENGTH_SHORT).show();
            }
        }
    };
}
