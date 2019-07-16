package com.cosin.shareagenda.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.cosin.shareagenda.R;
import com.cosin.shareagenda.access.net.CallbackHandler;
import com.cosin.shareagenda.api.ApiClient;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import types.Event;

import static com.cosin.shareagenda.access.net.CallbackHandler.SUCCESS;

public class ExploreEventsAdapter extends RecyclerView.Adapter<ExploreEventsAdapter.ViewHolder> {
    private List<Event> events;
    private int removePosition;

    public ExploreEventsAdapter() {
        this.events = new ArrayList<>();
    }
    public ExploreEventsAdapter(Context context, List<Event> events){
        this.events = events;
    }

    public void setEventList(List<Event> events) {
        this.events = events;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvEventTitle;
        TextView tvEventDescription;
        ImageView ivAddEvent;

        public ViewHolder(View view) {
            super(view);
            tvEventTitle = view.findViewById(R.id.tvEventTitle);
            tvEventDescription = view.findViewById(R.id.tvEventDescription);
            ivAddEvent = view.findViewById((R.id.ivAddEvent));
        }
    }
    @Override
    public ExploreEventsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.explore_event_item,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ExploreEventsAdapter.ViewHolder holder, int position) {
        holder.tvEventTitle.setText(events.get(position).getEventname());
        holder.tvEventDescription.setText(events.get(position).getDescription());
        holder.ivAddEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Event event = events.get(position);
                ApiClient.joinEvent(event.getEventId(), new CallbackHandler(handler));
                removePosition = position;
            }
        });
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(android.os.Message message) {
            final Gson gson = new Gson();
            switch (message.what) {
                case SUCCESS:
                    events.remove(removePosition);
                    notifyItemRemoved(removePosition);
                    break;
            }
        }
    };
}
