package com.dg.eventapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.dg.eventapp.adapter.EventAdapter;
import com.dg.eventapp.entity.EventEntity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<EventEntity> events;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();

        initView();
    }

    private void initView(){
        RecyclerView rvSchedule = findViewById(R.id.rvSchedule);
        rvSchedule.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvSchedule.setLayoutManager(layoutManager);

        EventAdapter myAdapter = new EventAdapter(this,events);
        rvSchedule.setAdapter(myAdapter);

    }
    private void initData(){
        events = new ArrayList<EventEntity>();
        for(int i = 36; i < 45; i++){
            events.add(new EventEntity(i));
        }
        events.add(new EventEntity(1,1,"lunch","plaza",45,4));
        for(int i = 49; i < 80; i++){
            events.add(new EventEntity(i));
        }
    }
}
