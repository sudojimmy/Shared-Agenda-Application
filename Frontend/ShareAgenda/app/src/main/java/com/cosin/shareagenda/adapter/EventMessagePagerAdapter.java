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
import androidx.viewpager.widget.PagerAdapter;

import com.cosin.shareagenda.R;
import com.cosin.shareagenda.access.net.CallbackHandler;
import com.cosin.shareagenda.api.ApiClient;
import com.cosin.shareagenda.api.ApiErrorResponse;
import com.google.gson.Gson;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import types.Event;
import types.EventMessage;
import types.EventRepeat;
import types.ReplyStatus;

import static com.cosin.shareagenda.access.net.CallbackHandler.SUCCESS;

public class EventMessagePagerAdapter extends PagerAdapter {

    private List<EventMessage> eventMsgs;
    private LayoutInflater layoutInflater;
    private Context context;

    public EventMessagePagerAdapter(List<EventMessage> eventMsgs, Context context) {
        this.eventMsgs = eventMsgs;
        this.context = context;
    }

    @Override
    public int getCount() {
        return eventMsgs.size();
    }

    public void setEvents(List<EventMessage> eventMsgs) {
        this.eventMsgs = eventMsgs;
        notifyDataSetChanged();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        if (eventMsgs.contains(object)) {
            return eventMsgs.indexOf(object);
        } else {
            return POSITION_NONE;
        }

    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_event_message, container, false);
        EventMessage msg = eventMsgs.get(position);
        Event event = msg.getEvent();

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
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ApiClient.replyEvent(msg.getMessageId(), ReplyStatus.ACCEPT,
                        new CallbackHandler(getHandler(position, "Accepted")));
            }
        });

        Button btnDecline = view.findViewById(R.id.btnDecline);
        btnDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ApiClient.replyEvent(msg.getMessageId(), ReplyStatus.DECLINE,
                        new CallbackHandler(getHandler(position, "Declined")));
            }
        });

        container.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }

    private Handler getHandler(int position, String replyStatusStr) {
        return new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message message) {
                final Gson gson = new Gson();
                switch (message.what) {
                    case SUCCESS:
                        new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("SENT")
                                .setContentText(replyStatusStr + " Event Request!")
                                .setConfirmText("OK").setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                eventMsgs.remove(position);
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
