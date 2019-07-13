package com.cosin.shareagenda.activity;

import android.content.Intent;
import android.content.res.Resources;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cosin.shareagenda.R;
import com.cosin.shareagenda.adapter.WeeklyEventAdapter;
import com.cosin.shareagenda.config.AgendaApplication;
import com.cosin.shareagenda.config.SystemConfig;
import com.cosin.shareagenda.dialog.DialogReceiver;
import com.cosin.shareagenda.dialog.SendEventRequestDialog;
import com.cosin.shareagenda.entity.FriendEvent;
import com.cosin.shareagenda.entity.UserEntity;
import com.cosin.shareagenda.util.GenData;
import com.cosin.shareagenda.view.ItemViewListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class WeeklyActivity extends MainTitleActivity
        implements GestureDetector.OnGestureListener, ItemViewListener, DialogReceiver {
    protected UserEntity user;
    protected Calendar cal;
    protected List<FriendEvent> weekEvts;
    protected int[] ids = {R.id.week_label0, R.id.week_label1, R.id.week_label2,
            R.id.week_label3, R.id.week_label4, R.id.week_label5, R.id.week_label6};

    GestureDetector gestureDetector;

    @Override
    protected  String titleName() {
        SimpleDateFormat sdf = new SimpleDateFormat("d / M  yyyy  EEEE");
        return sdf.format(cal.getTime());
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_weekly;
    }

    @Override
    protected void loadData() {
        setUser();

        //
        cal = Calendar.getInstance();
        cal.setTime(new Date());

        weekEvts = GenData.getWeeklyEvents();
    }

    protected void setUser() {
        user = AgendaApplication.getUserInfo();
    }

    protected void refreshData(int d) {
        // load user weekly events
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
            tv.setTextSize(getResources().getDimension(R.dimen.weekly_label_text_size));
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
        recyclerView.setAdapter(new WeeklyEventAdapter(weekEvts, this));

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
        WeeklyEventAdapter adpt = (WeeklyEventAdapter)(recyclerView.getAdapter());
        adpt.setWeekEvts(weekEvts);
        adpt.notifyDataSetChanged();
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

    @Override
    public void dealwithItem(Object item) {
        // item=d / M  yyyy|  EEEE|quarter
        String[] ret = ((String)item).split("\\|");
        int q = Integer.parseInt(ret[2]);
        int h, m;
        h = q / 4;
        m = (q % 4) * 15;

        new SendEventRequestDialog(this,
                user.getNickname(),
                ret[0] + ret[1],
                String.format("%d : %02d", h, m)).show();
    }

    @Override
    public void receive(Object ret) {
        // to create event
        Intent intent = new Intent(this, CreateEventActivity.class);
        this.startActivity(intent);
    }
}
