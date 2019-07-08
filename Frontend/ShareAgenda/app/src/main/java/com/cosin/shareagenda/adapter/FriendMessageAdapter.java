package com.cosin.shareagenda.adapter;

import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cosin.shareagenda.R;
import com.cosin.shareagenda.access.net.CallbackHandler;
import com.cosin.shareagenda.model.ApiClient;
import com.cosin.shareagenda.model.ApiErrorResponse;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import types.Message;
import types.MessageType;
import types.ReplyStatus;

import static com.cosin.shareagenda.access.net.CallbackHandler.HTTP_FAILURE;
import static com.cosin.shareagenda.access.net.CallbackHandler.SUCCESS;

public class FriendMessageAdapter extends RecyclerView.Adapter<FriendMessageAdapter.ViewHolder> {
    private List<Message> messages;

    public FriendMessageAdapter() {
        messages = new ArrayList<>();
    }

    public FriendMessageAdapter(List<Message> messages) {
        setMessages(messages);
    }

    private boolean isFriendMessage(Message message) {
        return message.getType().equals(MessageType.FRIEND);
    }

    public void setMessages(List<Message> messages) {
        for (Message msg : messages) {
            if (isFriendMessage(msg)) {
                this.messages.add(msg);
            }
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView viewName;
        ImageButton viewAccept;
        ImageButton viewDecline;

        public ViewHolder(View view) {
            super(view);
            viewName = view.findViewById(R.id.tvFriendMessage);
            viewAccept = view.findViewById(R.id.imgBtnAcceptFriendRequest);
            viewDecline = view.findViewById(R.id.imgBtnDeclineFriendRequest);
        }
    }

    @Override
    public FriendMessageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.friend_message_item, parent, false);
        final FriendMessageAdapter.ViewHolder viewHolder =
                new FriendMessageAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(FriendMessageAdapter.ViewHolder viewHolder, int position) {
        viewHolder.viewName.setText(messages.get(position).getSenderId());
        viewHolder.viewAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApiClient.replyFriend(messages.get(position).getMessageId(), ReplyStatus.ACCEPT, new CallbackHandler(handler));

                messages.remove(position);
                notifyItemRemoved(position);
                notifyDataSetChanged();
            }
        });
        viewHolder.viewDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApiClient.replyFriend(messages.get(position).getMessageId(), ReplyStatus.DECLINE, new CallbackHandler(handler));

                messages.remove(position);
                notifyItemRemoved(position);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(android.os.Message message) {
            final Gson gson = new Gson();
            switch (message.what) {
                case SUCCESS:
                    String body = (String) message.obj;
//                    GetMessageQueueResponse resp = gson.fromJson(body, GetMessageQueueResponse.class);
                    break;
                case HTTP_FAILURE:
                    ApiErrorResponse errorResponse = gson.fromJson((String) message.obj, ApiErrorResponse.class);
//                    Toast.makeText(FriendMessageActivity.this, errorResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    break;
                default:
//                    Toast.makeText(FriendMessageActivity.this, (String) message.obj, Toast.LENGTH_SHORT).show();
            }
        }
    };

}
