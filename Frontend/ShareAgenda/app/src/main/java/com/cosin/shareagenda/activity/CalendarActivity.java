package com.cosin.shareagenda.activity;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.widget.Toast;

import com.cosin.shareagenda.R;
import com.cosin.shareagenda.adapter.EventAdapter;
import com.cosin.shareagenda.entity.EventEntity;
import com.cosin.shareagenda.util.GenData;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CalendarActivity extends MainTitleActivity
        implements GestureDetector.OnGestureListener {
    private List<EventEntity> eventList;
    private float MIN_DX = 300;
    private float MIN_VX = 150;

    GestureDetector gestureDetector;

    @Override
    protected  String titleName() {
        SimpleDateFormat sdf=new SimpleDateFormat("d/M/yyyy EEEE");
        return sdf.format(new Date());
    }

    @Override
    protected void loadContentView() {
        LayoutInflater inflater = getLayoutInflater();
        inflater.inflate(R.layout.action_1, coordinatorLayout);
    }

    @Override
    protected void loadData() {
        //
        eventList = GenData.putEventList();
    }

    @Override
    protected void initView() {
        super.initView();

        // init event panel
        RecyclerView recyclerView = findViewById(R.id.recycle_view);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(new EventAdapter(eventList));

        gestureDetector = new GestureDetector(this);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event){
        super.dispatchTouchEvent(event);
        return gestureDetector.onTouchEvent(event);
    }

    @Override
    public boolean onFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {
        Log.d("AAA",String.format("DX= %f  VX= %f",me2.getX()-me1.getX(),velocityX));
        if (Math.abs(velocityX) > MIN_VX) {
            if (me2.getX() - me1.getX() > MIN_DX) {
                Toast.makeText(this,"Right",Toast.LENGTH_SHORT).show();
            }
            else if (me1.getX() - me2.getX() > MIN_DX) {
                Toast.makeText(this,"Left",Toast.LENGTH_SHORT).show();
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
