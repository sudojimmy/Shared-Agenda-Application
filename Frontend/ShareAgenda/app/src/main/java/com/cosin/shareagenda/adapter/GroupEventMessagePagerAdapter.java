package com.cosin.shareagenda.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.PagerAdapter;

import com.cosin.shareagenda.R;
import com.cosin.shareagenda.access.net.CallbackHandler;
import com.cosin.shareagenda.api.ApiClient;
import com.cosin.shareagenda.api.ApiErrorResponse;
import com.google.gson.Gson;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import types.Event;
import types.EventRepeat;

import static com.cosin.shareagenda.access.net.CallbackHandler.SUCCESS;

public class GroupEventMessagePagerAdapter extends PagerAdapter {

    private List<Event> events;
    private LayoutInflater layoutInflater;
    private Context context;

    public GroupEventMessagePagerAdapter(List<Event> events, Context context) {
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

    @Override
    public int getItemPosition(@NonNull Object object) {
        if (events.contains(object)) {
            return events.indexOf(object);
        } else {
            return POSITION_NONE;
        }

    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_event_message, container, false);
        Event event = events.get(position);

        TextView tvDescription = view.findViewById(R.id.tvDescription);
        if (!event.getDescription().isEmpty()) {
            tvDescription.setText(event.getDescription());
        }

        TextView tvTitle = view.findViewById(R.id.tvTitle);
        tvTitle.setText(event.getEventname());

        TextView tvLocation = view.findViewById(R.id.tvLocation);
        if (!event.getLocation().isEmpty()) {
            tvLocation.setText(event.getLocation());
        }

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

        Button btnAccept = view.findViewById(R.id.btn2);
        btnAccept.setText("JOIN");
        btnAccept.setTextColor(ContextCompat.getColor(context, R.color.ivory));
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ApiClient.joinEvent(event.getEventId(), new CallbackHandler(getHandler(position)));
            }
        });
        Button btnDecline = view.findViewById(R.id.btn1);
        btnDecline.setVisibility(View.GONE);

        container.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }

    private Handler getHandler(int position) {
        return new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message message) {
                final Gson gson = new Gson();
                switch (message.what) {
                    case SUCCESS:
                        new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("SUCCESS")
                                .setContentText("Joined Event: " + events.get(position).getEventname())
                                .setConfirmText("OK").setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
//                                events.remove(position);
                                notifyDataSetChanged();
                                sweetAlertDialog.dismissWithAnimation();
                            }
                        }).show();
                        break;
                    case CallbackHandler.HTTP_FAILURE:
                        ApiErrorResponse errorResponse = gson.fromJson((String) message.obj, ApiErrorResponse.class);
                        Toast.makeText(context, errorResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(context, (String) message.obj, Toast.LENGTH_SHORT).show();
                }
            }
        };
    }
}
