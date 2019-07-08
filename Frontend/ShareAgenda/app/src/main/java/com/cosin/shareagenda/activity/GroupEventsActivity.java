package com.cosin.shareagenda.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cosin.shareagenda.R;
import com.cosin.shareagenda.adapter.FriendsEventAdapter;
import com.cosin.shareagenda.config.SystemConfig;
import com.cosin.shareagenda.dialog.DialogReceiver;
import com.cosin.shareagenda.dialog.SendEventRequestDialog;
import com.cosin.shareagenda.entity.FriendEvent;
import com.cosin.shareagenda.entity.GroupEntity;
import com.cosin.shareagenda.util.GenData;
import com.cosin.shareagenda.view.ItemViewListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class GroupEventsActivity extends MainTitleActivity
        implements GestureDetector.OnGestureListener, ItemViewListener, DialogReceiver {
    private GroupEntity group;
    private List<FriendEvent> friendEvts;
    Calendar cal;

    GestureDetector gestureDetector;

    @Override
    protected  String titleName() {
        // changing in refreshView()
        return "";
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_group_events;
    }

    @Override
    protected void loadData() {
        Intent intent = getIntent();
        group = (GroupEntity)intent.getSerializableExtra("group");

        cal = Calendar.getInstance();
        cal.setTime(new Date());

        refreshData(0);
    }

    protected void refreshData(int d) {
        cal.add(Calendar.DATE, d);
        friendEvts = GenData.getFriendsEvents();
    }

    @Override
    protected void initView() {
        super.initView();

        // recycleview
        RecyclerView recyclerView = findViewById(R.id.recycle_ge);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(new FriendsEventAdapter(this));

        refreshView();

        gestureDetector = new GestureDetector(this);
    }

    protected void refreshView() {
        // title
        SimpleDateFormat sdf = new SimpleDateFormat("d / M  yyyy  EEEE");
        changeTitle(sdf.format(cal.getTime()));

        // init panel
        Resources resources = this.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        float timeWidth = resources.getDimension(R.dimen.friends_events_time);
        int labelWidth = (int)(dm.widthPixels - timeWidth) / friendEvts.size();

        // label
        LinearLayout llFeLabel = findViewById(R.id.ll_feLabel);
        llFeLabel.removeAllViews();
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
            tv.setTextSize(resources.getDimension(R.dimen.friends_label_text_size));
            tv.setTextColor(resources.getColor(R.color.colorPrimaryDark));
            llFeLabel.addView(tv);
        }

        // recycleview
        RecyclerView recyclerView = findViewById(R.id.recycle_ge);
        FriendsEventAdapter adpt = (FriendsEventAdapter)recyclerView.getAdapter();
        adpt.setFriendEvts(friendEvts);
        adpt.notifyDataSetChanged();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event){
        super.dispatchTouchEvent(event);
        return gestureDetector.onTouchEvent(event);
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (Math.abs(velocityX) > SystemConfig.FLY_MIN_VX) {
            if (e2.getX() - e1.getX() > SystemConfig.FLY_MIN_DX) {
                refreshData(-1);
                refreshView();
            }
            else if (e1.getX() - e2.getX() > SystemConfig.FLY_MIN_DX) {
                refreshData(1);
                refreshView();
            }
        }

        return false;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public void receive(Object ret) {
        // to create group event by cal and quarter
        Intent intent = new Intent(this, CreateEventActivity.class);
        this.startActivity(intent);
    }

    @Override
    public void dealwithItem(Object item) {
        int quarter = (int)item;
        int h, m;
        h = quarter / 4;
        m = (quarter % 4) * 15;
        SimpleDateFormat sdf = new SimpleDateFormat("d / M  yyyy  EEEE");

        new SendEventRequestDialog(this,
                group.getName(),
                sdf.format(cal.getTime()),
                String.format("%d : %02d", h, m)).show();
    }
}
