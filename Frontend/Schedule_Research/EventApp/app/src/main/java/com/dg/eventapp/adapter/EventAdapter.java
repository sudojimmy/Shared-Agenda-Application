package com.dg.eventapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dg.eventapp.R;
import com.dg.eventapp.entity.EventEntity;
import com.dg.eventapp.view.EvtView;

import java.util.ArrayList;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {
    ArrayList<EventEntity> events;
    public EventAdapter(Context context, ArrayList<EventEntity> list) {
        this.events = list;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        EvtView evtView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            evtView = itemView.findViewById(R.id.evtItem);
        }
    }

    @NonNull
    @Override
    public EventAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.event_items, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull EventAdapter.ViewHolder viewHolder, int i) {

        viewHolder.evtView.setEvent(events.get(i));
    }

    @Override
    public int getItemCount() {
        return events.size();
    }
}
