package com.cosin.shareagenda.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.cosin.shareagenda.R;

import java.util.List;

import types.Event;
import types.EventRepeat;

public class EventMessagePagerAdapter extends PagerAdapter {

    private List<Event> events;
    private LayoutInflater layoutInflater;
    private Context context;

    public EventMessagePagerAdapter(List<Event> events, Context context) {
        this.events = events;
        this.context = context;
    }

    @Override
    public int getCount() {
        return events.size();
    }

    public void setEvents(List<Event> events) {
        this.events = events;
        notifyDataSetChanged();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_event_message, container, false);

        Event event = events.get(position);

        TextView tvTitle = view.findViewById(R.id.tvTitle);
        tvTitle.setText(event.getEventname());

        TextView tvLocation = view.findViewById(R.id.tvLocation);
        tvLocation.setText(event.getLocation());

        TextView tvType = view.findViewById(R.id.tvType);
        tvType.setText(event.getType().toString());

        TextView tvTime = view.findViewById(R.id.tvTime);
        tvTime.setText(String.format("%s - %s", event.getStartTime(), event.getEndTime()));

        TextView tvDate = view.findViewById(R.id.tvDate);
        tvDate.setText(event.getRepeat().getStartDate());

        TextView tvRepeat = view.findViewById(R.id.tvRepeat);
        if (event.getRepeat().getType().equals(EventRepeat.ONCE)) {
            tvRepeat.setText(event.getRepeat().getType().toString());
        } else {
            tvRepeat.setText(String.format("Every %s until %s",
                    event.getRepeat().getType().toString(),
                    event.getRepeat().getEndDate()));
        }

        TextView tvStarterId = view.findViewById(R.id.tvStarterId);
        tvStarterId.setText(event.getStarterId());

        Button btnAccept = view.findViewById(R.id.btnAccept);
        // TODO listener

        Button btnDecline = view.findViewById(R.id.btnDecline);
        // TODO listener


        container.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}
