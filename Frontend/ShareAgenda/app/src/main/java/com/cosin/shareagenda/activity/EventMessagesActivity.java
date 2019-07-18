package com.cosin.shareagenda.activity;

import android.animation.ArgbEvaluator;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.viewpager.widget.ViewPager;

import com.cosin.shareagenda.R;
import com.cosin.shareagenda.access.net.CallbackHandler;
import com.cosin.shareagenda.adapter.EventMessagePagerAdapter;
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
import types.GetEventMonthlyResponse;

import static com.cosin.shareagenda.access.net.CallbackHandler.HTTP_FAILURE;
import static com.cosin.shareagenda.access.net.CallbackHandler.SUCCESS;

public class EventMessagesActivity extends MainTitleActivity {

    ViewPager viewPager;
    EventMessagePagerAdapter adapter;
    ArgbEvaluator argbEvaluator = new ArgbEvaluator();
    List<Event> events = new ArrayList<>();
    private TextView defaultText;

    @Override
    protected int getContentView() {
        return R.layout.activity_event_message;
    }

    @Override
    protected void loadData() {
        ApiClient.getEventMonthly(Model.model.getUser().getAccountId(), 7, 2019, new CallbackHandler(getEventHandler));
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
    protected void initView() {
        super.initView();
        adapter = new EventMessagePagerAdapter(events, this);

        viewPager = findViewById(R.id.viewPager);
        defaultText = findViewById(R.id.defaultText);
        defaultText.setVisibility(events.isEmpty() ? View.VISIBLE : View.GONE);
        viewPager.setAdapter(adapter);
        viewPager.setPadding(50, 50, 50, 50);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Integer color = Color.rgb(224, 224, 224);
                if (!events.isEmpty()) {
                    Integer tmp = colorMap.get(events.get(position).getType());
                    if (tmp != null) {
                        color = tmp;
                    }
                }
                viewPager.setBackgroundColor(color);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        loadData();
    }

    @Override
    protected String titleName() {
        return "Explore Event";
    }

    Handler getEventHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(android.os.Message message) {
            final Gson gson = new Gson();
            switch (message.what) {
                case SUCCESS:
                    String body = (String) message.obj;
                    GetEventMonthlyResponse resp = gson.fromJson(body, GetEventMonthlyResponse.class);
                    events = resp.getEventList();
                    adapter.setEvents(events);
                    defaultText.setVisibility(events.isEmpty() ? View.VISIBLE : View.GONE);
                    break;
                case HTTP_FAILURE:
                    ApiErrorResponse errorResponse = gson.fromJson((String) message.obj, ApiErrorResponse.class);
                    Toast.makeText(EventMessagesActivity.this, errorResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(EventMessagesActivity.this, (String) message.obj, Toast.LENGTH_SHORT).show();
            }
        }
    };
}
