package com.cosin.shareagenda.activity;

import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cosin.shareagenda.R;
import com.cosin.shareagenda.adapter.FriendsEventAdapter;
import com.cosin.shareagenda.entity.FriendEvent;
import com.cosin.shareagenda.util.GenData;

import java.util.Iterator;
import java.util.List;

public class FriendsEventsActivity extends MainTitleActivity {
    private List<FriendEvent> friendEvts;

    @Override
    protected  String titleName() {
        return getResources().getString(R.string.title_friends_calendar);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_friends_events;
    }

    @Override
    protected void loadData() {
        //
        friendEvts = GenData.getFriendsEvents();
    }

    @Override
    protected void initView() {
        super.initView();

        // init panel
        Resources resources = this.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        float timeWidth = resources.getDimension(R.dimen.friends_events_time);
        int labelWidth = (int)(dm.widthPixels - timeWidth) / friendEvts.size();

        // label
        LinearLayout llFeLabel = findViewById(R.id.ll_feLabel);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                labelWidth,
                LinearLayout.LayoutParams.MATCH_PARENT);
        Iterator<FriendEvent> it = friendEvts.iterator();
        while (it.hasNext()) {
            FriendEvent e = it.next();
            TextView tv = new TextView(this);
            tv.setLayoutParams(lp);
            tv.setGravity(Gravity.CENTER);
            tv.setText(e.getFriendName());
            //tv.setTextSize(16);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, resources.getDimension(R.dimen.friends_label_text_size));
            tv.setTextColor(resources.getColor(R.color.colorPrimaryDark));
            llFeLabel.addView(tv);
        }

        // recycleview
        RecyclerView recyclerView = findViewById(R.id.recycle_ge);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(new FriendsEventAdapter(friendEvts));
    }
}
