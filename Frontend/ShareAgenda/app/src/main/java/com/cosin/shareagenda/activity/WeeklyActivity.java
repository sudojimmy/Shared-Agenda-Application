package com.cosin.shareagenda.activity;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cosin.shareagenda.R;
import com.cosin.shareagenda.adapter.FriendsEventAdapter;
import com.cosin.shareagenda.adapter.WeeklyEventAdapter;
import com.cosin.shareagenda.config.SystemConfig;
import com.cosin.shareagenda.entity.FriendEvent;
import com.cosin.shareagenda.util.GenData;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class WeeklyActivity extends MainTitleActivity
        implements GestureDetector.OnGestureListener {
    protected Calendar cal;
    private List<FriendEvent> weekEvts;
    private int[] ids = {R.id.week_label0, R.id.week_label1, R.id.week_label2,
            R.id.week_label3, R.id.week_label4, R.id.week_label5, R.id.week_label6};

    GestureDetector gestureDetector;

    @Override
    protected  String titleName() {
        SimpleDateFormat sdf = new SimpleDateFormat("d/M/yyyy EEEE");
        return sdf.format(cal.getTime());
    }

    @Override
    protected void loadContentView() {
        LayoutInflater inflater = getLayoutInflater();
        inflater.inflate(R.layout.activity_weekly, coordinatorLayout);
    }

    @Override
    protected void loadData() {
        //
        cal = Calendar.getInstance();
        cal.setTime(new Date());

        weekEvts = GenData.getWeeklyEvents();
    }

    protected void refreshData(int d) {
        //
        weekEvts = GenData.getNextWeeklyEvents();
    }

    @Override
    protected void initView() {
        super.initView();

        // init panel
        Resources resources = this.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        float timeWidth = resources.getDimension(R.dimen.friends_events_time);
        int labelWidth = (int)(dm.widthPixels - timeWidth) / weekEvts.size();

        // label
        LinearLayout llWeekLabel = findViewById(R.id.ll_weekLabel);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                labelWidth,
                LinearLayout.LayoutParams.MATCH_PARENT);
        int i = 0;
        for (FriendEvent e: weekEvts) {
            TextView tv = new TextView(this);
            tv.setId(ids[i++]);
            tv.setLayoutParams(lp);
            tv.setGravity(Gravity.CENTER);
            tv.setText(e.getDateDM() + "\n" + e.getFriendName());
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, resources.getDimension(R.dimen.weekly_label_text_size));
            if (e.isToday())
                tv.setTextColor(resources.getColor(R.color.colorAccent));
            else
                tv.setTextColor(resources.getColor(R.color.colorPrimaryDark));
            llWeekLabel.addView(tv);
        }

        // recycleview
        RecyclerView recyclerView = findViewById(R.id.recycle_week);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(new WeeklyEventAdapter(weekEvts));

        gestureDetector = new GestureDetector(this);
    }

    protected void refreshView() {
        // label
        int i = 0;
        for (FriendEvent e: weekEvts) {
            TextView tv = findViewById(ids[i++]);
            tv.setText(e.getDateDM() + "\n" + e.getFriendName());
            if (e.isToday())
                tv.setTextColor(getResources().getColor(R.color.colorAccent));
            else
                tv.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        // event panel
        RecyclerView recyclerView = findViewById(R.id.recycle_week);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(new WeeklyEventAdapter(weekEvts));
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event){
        super.dispatchTouchEvent(event);
        return gestureDetector.onTouchEvent(event);
    }

    @Override
    public boolean onFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {
        if (Math.abs(velocityX) > SystemConfig.FLY_MIN_VX) {
            if (me2.getX() - me1.getX() > SystemConfig.FLY_MIN_DX) {
                refreshData(-1);
                refreshView();
            }
            else if (me1.getX() - me2.getX() > SystemConfig.FLY_MIN_DX) {
                refreshData(1);
                refreshView();
            }
        }

        return false;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        // TODO Auto-generated method stub
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        // TODO Auto-generated method stub
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        // TODO Auto-generated method stub
        return false;
    }
}
