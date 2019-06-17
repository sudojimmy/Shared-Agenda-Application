package com.cosin.shareagenda.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cosin.shareagenda.R;
import com.cosin.shareagenda.entity.EventEntity;
import com.cosin.shareagenda.view.EventView;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {
    private List<EventEntity> eventList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        EventView eventView;

        public ViewHolder(View view) {
            super(view);
            eventView = view.findViewById(R.id.evtItem);
        }
    }

    public EventAdapter(List<EventEntity> eventList) {
        this.eventList = eventList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_item, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        EventEntity evt = eventList.get(position);
        viewHolder.eventView.setEvent(evt);
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }
}
