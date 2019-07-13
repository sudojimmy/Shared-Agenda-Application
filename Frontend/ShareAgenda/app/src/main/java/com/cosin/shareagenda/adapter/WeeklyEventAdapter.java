package com.cosin.shareagenda.adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cosin.shareagenda.R;
import com.cosin.shareagenda.config.SystemConfig;
import com.cosin.shareagenda.entity.FriendEvent;
import com.cosin.shareagenda.util.CalendarEventBiz;
import com.cosin.shareagenda.view.ItemViewListener;
import com.cosin.shareagenda.view.WeekEventsView;

import java.util.List;

public class WeeklyEventAdapter extends RecyclerView.Adapter<WeeklyEventAdapter.ViewHolder> {
    private List<FriendEvent> weekEvts;
    private ItemViewListener listener;

    static class ViewHolder extends RecyclerView.ViewHolder {
        WeekEventsView eventView;

        public ViewHolder(View view) {
            super(view);
            eventView = view.findViewById(R.id.weeklyItem);
        }
    }

    public WeeklyEventAdapter(List<FriendEvent> weeklyEvts, ItemViewListener listener) {
        this.weekEvts = weeklyEvts;
        this.listener = listener;
    }

    public void setWeekEvts(List<FriendEvent> weeklyEvts) {
        this.weekEvts = weeklyEvts;
    }

    @Override
    public WeeklyEventAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.weekly_events_item, parent, false);
        final WeeklyEventAdapter.ViewHolder viewHolder = new WeeklyEventAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(WeeklyEventAdapter.ViewHolder viewHolder, int position) {
        int quarter = SystemConfig.SATRT_QAURTER + position;
        boolean[] ev = new boolean[weekEvts.size()];
        String[] name = new String[weekEvts.size()];
        for (int i = 0; i < weekEvts.size(); i++) {
            String ret = CalendarEventBiz.checkQuarterInEventName(quarter, weekEvts.get(i));
            if (ret.isEmpty()) {
                ev[i] = false;
                name[i] = ret;
            } else {
                ev[i] = true;
                if ("~".equals(ret)) {
                    name[i] = "";
                } else {
                    name[i] = ret;
                }
            }
        }

        viewHolder.eventView.setCld(weekEvts.get(0).getDate(), quarter, ev, name);
        viewHolder.eventView.setListener(listener);
    }

    @Override
    public int getItemCount() {
        return SystemConfig.END_QUARTER - SystemConfig.SATRT_QAURTER + 1;
    }
}
