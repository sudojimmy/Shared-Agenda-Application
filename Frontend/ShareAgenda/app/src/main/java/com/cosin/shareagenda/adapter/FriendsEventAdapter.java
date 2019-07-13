package com.cosin.shareagenda.adapter;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cosin.shareagenda.R;
import com.cosin.shareagenda.config.SystemConfig;
import com.cosin.shareagenda.entity.FriendEvent;
import com.cosin.shareagenda.util.CalendarEventBiz;
import com.cosin.shareagenda.view.FriendsEventsView;
import com.cosin.shareagenda.view.ItemViewListener;

import java.util.ArrayList;
import java.util.List;

public class FriendsEventAdapter extends RecyclerView.Adapter<FriendsEventAdapter.ViewHolder> {
    private List<FriendEvent> friendEvts;
    private ItemViewListener listener;

    static class ViewHolder extends RecyclerView.ViewHolder {
        FriendsEventsView eventView;

        public ViewHolder(View view) {
            super(view);
            eventView = view.findViewById(R.id.friendItem);
        }
    }

    public FriendsEventAdapter(List<FriendEvent> friendEvts, ItemViewListener listener) {
        this.friendEvts = friendEvts;
        this.listener = listener;
    }

    public FriendsEventAdapter(ItemViewListener listener) {
        this.friendEvts = new ArrayList<>();
        this.listener = listener;
    }

    public void setFriendEvts(List<FriendEvent> friendEvts) {
        this.friendEvts = friendEvts;
    }

    @Override
    public FriendsEventAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friends_events_item, parent, false);
        final FriendsEventAdapter.ViewHolder viewHolder = new FriendsEventAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(FriendsEventAdapter.ViewHolder viewHolder, int position) {
        int quarter = SystemConfig.SATRT_QAURTER + position;
        boolean[] ev = new boolean[friendEvts.size()];
        for (int i = 0; i < friendEvts.size(); i++) {
            ev[i] = CalendarEventBiz.checkQuarterInEvent(quarter, friendEvts.get(i));
        }

        viewHolder.eventView.setCld(quarter, ev);
        viewHolder.eventView.setListener(listener);
    }

    @Override
    public int getItemCount() {
        return SystemConfig.END_QUARTER - SystemConfig.SATRT_QAURTER + 1;
    }
}
