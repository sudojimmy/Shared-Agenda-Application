package com.cosin.shareagenda.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.widget.Toast;

import com.cosin.shareagenda.R;
import com.cosin.shareagenda.adapter.EventAdapter;
import com.cosin.shareagenda.config.SystemConfig;
import com.cosin.shareagenda.dialog.DialogReceiver;
import com.cosin.shareagenda.dialog.SendEventRequestDialog;
import com.cosin.shareagenda.entity.EventEntity;
import com.cosin.shareagenda.util.GenData;
import com.cosin.shareagenda.view.ItemViewListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CalendarActivity extends MainTitleActivity
        implements GestureDetector.OnGestureListener, ItemViewListener, DialogReceiver {
    protected Calendar cal;
    private List<EventEntity> eventList;
    private int quarter;

    GestureDetector gestureDetector;

    @Override
    protected  String titleName() {
        SimpleDateFormat sdf = new SimpleDateFormat("d / M  yyyy  EEEE");
        return sdf.format(cal.getTime());
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_calendar;
    }

    @Override
    protected void loadData() {
        //
        cal = Calendar.getInstance();
        cal.setTime(new Date());

        eventList = GenData.putEventList();
    }

    protected void refreshData(int d) {
        //
        cal.add(Calendar.DATE, d);
        eventList = GenData.getEventList();
    }

    @Override
    protected void initView() {
        super.initView();

        // init event panel
        RecyclerView recyclerView = findViewById(R.id.recycle_view);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(new EventAdapter(eventList, this));

        gestureDetector = new GestureDetector(this);
    }

    protected void refreshView() {
        // title
        SimpleDateFormat sdf = new SimpleDateFormat("d / M  yyyy  EEEE");
        changeTitle(sdf.format(cal.getTime()));

        // event panel
        RecyclerView recyclerView = findViewById(R.id.recycle_view);
        EventAdapter adpt = (EventAdapter)recyclerView.getAdapter();
        adpt.setEventList(eventList);
        adpt.notifyDataSetChanged();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event){
        super.dispatchTouchEvent(event);
        return gestureDetector.onTouchEvent(event);
    }

    @Override
    public boolean onFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {
        //Log.d("AAA",String.format("DX= %f  VX= %f",me2.getX()-me1.getX(),velocityX));
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
    public void receive(Object ret) {
        // to create user event by cal and quarter
        Intent intent = new Intent(this, CreateEventAcitivty.class);
        this.startActivity(intent);
    }

    @Override
    public void dealwithItem(Object item) {
        quarter = (int)item;
        int h, m;
        h = quarter / 4;
        m = (quarter % 4) * 15;
        SimpleDateFormat sdf = new SimpleDateFormat("d / M  yyyy  EEEE");

        new SendEventRequestDialog(this,
                "My New Event",
                sdf.format(cal.getTime()),
                String.format("%d : %02d", h, m)).show();
    }
}
