package com.cosin.shareagenda.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cosin.shareagenda.R;
import com.cosin.shareagenda.entity.EventEntity;
import com.cosin.shareagenda.entity.FriendEvent;
import com.cosin.shareagenda.view.EventView;
import com.cosin.shareagenda.view.FriendsEventsView;

import java.util.Iterator;
import java.util.List;

public class FriendsEventAdapter extends RecyclerView.Adapter<FriendsEventAdapter.ViewHolder> {
    private List<FriendEvent> friendEvts;

    static class ViewHolder extends RecyclerView.ViewHolder {
        FriendsEventsView eventView;

        public ViewHolder(View view) {
            super(view);
            eventView = view.findViewById(R.id.friendItem);
        }
    }

    public FriendsEventAdapter(List<FriendEvent> friendEvts) {
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
        int quarter = 36 + position;
        boolean[] ev = new boolean[friendEvts.size()];
        for (int i = 0; i < friendEvts.size(); i++) {
            ev[i] = friendEvts.get(i).checkQuarterInEvent(quarter);
        }

        viewHolder.eventView.setCld(quarter, ev);
    }

    @Override
    public int getItemCount() {
        return 84 - 36 + 1;
    }
}
