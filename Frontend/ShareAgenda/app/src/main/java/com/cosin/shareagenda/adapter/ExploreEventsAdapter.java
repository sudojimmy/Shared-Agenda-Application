package com.cosin.shareagenda.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.cosin.shareagenda.R;
import com.cosin.shareagenda.access.net.CallbackHandler;
import com.cosin.shareagenda.api.ApiClient;
import com.cosin.shareagenda.api.DefaultExploreInfo;
import com.cosin.shareagenda.api.plugin.ExploreInfo;
import com.cosin.shareagenda.api.plugin.uwapi.UWEventPlugin;
import com.cosin.shareagenda.api.plugin.uwapi.UWCourseExploreInfo;
import com.cosin.shareagenda.api.plugin.uwapi.UWExamExploreInfo;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import static com.cosin.shareagenda.access.net.CallbackHandler.SUCCESS;

public class ExploreEventsAdapter extends RecyclerView.Adapter<ExploreEventsAdapter.ViewHolder> {
    private final Context context;
    private List<ExploreInfo> info;
    private int removePosition;

    public ExploreEventsAdapter(Context context) {
        this.context = context;
        this.info = new ArrayList<>();
    }
    public ExploreEventsAdapter(Context context, Context context1, List<ExploreInfo> info){
        this.context = context1;
        this.info = info;
    }

    public void setEventList(List<ExploreInfo> info) {
        this.info = info;
        notifyDataSetChanged();
    }

    public void addEventList(List<ExploreInfo> info) {
        this.info.addAll(info);
        notifyDataSetChanged();
    }

    public void clearEventList() {
        this.info.clear();
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
        holder.tvEventTitle.setText(info.get(position).getTitle());
        holder.tvEventDescription.setText(info.get(position).getDescription());
        holder.ivAddEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExploreInfo event = info.get(position);
                if (event instanceof UWCourseExploreInfo) {
                    UWEventPlugin.addClass(((UWCourseExploreInfo) event).getUwCourse(), new CallbackHandler(handler));
                    Toast.makeText(context, "ADDED: " + event.getTitle(), Toast.LENGTH_SHORT).show();
                } else if (event instanceof UWExamExploreInfo) {
                    UWEventPlugin.addExam(((UWExamExploreInfo) event), new CallbackHandler(handler));
                    Toast.makeText(context, "ADDED: " + event.getTitle(), Toast.LENGTH_SHORT).show();
                } else {
                    ApiClient.joinEvent(((DefaultExploreInfo)event).getEvent().getEventId(), new CallbackHandler(handler));
                }
                removePosition = position;
            }
        });
    }

    @Override
    public int getItemCount() {
        return info.size();
    }

    Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(android.os.Message message) {
            final Gson gson = new Gson();
            switch (message.what) {
                case SUCCESS:
                    info.remove(removePosition);
                    notifyItemRemoved(removePosition);
                    break;
            }
        }
    };
}
