package com.cosin.shareagenda.activity;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.cosin.shareagenda.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import types.EventMessage;
import types.EventType;

public abstract class EventMessagesActivity extends MainTitleActivity {
    ViewPager viewPager;
    PagerAdapter adapter;
    List<EventMessage> eventMsgs = new ArrayList<>();
    protected TextView defaultText;

    @Override
    protected int getContentView() {
        return R.layout.activity_event_message;
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
        adapter = getAdapter();

        viewPager = findViewById(R.id.viewPager);
        defaultText = findViewById(R.id.defaultText);
        defaultText.setText(onEmptyText());
        defaultText.setVisibility(eventMsgs.isEmpty() ? View.VISIBLE : View.GONE);
        viewPager.setAdapter(adapter);
        viewPager.setPadding(50, 50, 50, 50);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Integer color = Color.rgb(224, 224, 224);
                if (eventMsgs.size() > position) {
                    Integer tmp = colorMap.get(eventMsgs.get(position).getEvent().getType());
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

    protected abstract PagerAdapter getAdapter();

    protected abstract String onEmptyText();
}
