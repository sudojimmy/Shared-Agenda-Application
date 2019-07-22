package com.cosin.shareagenda.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.cosin.shareagenda.R;
import com.cosin.shareagenda.access.net.CallbackHandler;
import com.cosin.shareagenda.api.ApiClient;
import com.cosin.shareagenda.api.ApiErrorResponse;
import com.cosin.shareagenda.dialog.DisplayFriendRequestAccountDialog;
import com.google.gson.Gson;
import com.mcxtzhang.swipemenulib.SwipeMenuLayout;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import types.Account;
import types.FriendMessage;
import types.Message;
import types.MessageType;
import types.ReplyStatus;

import static com.cosin.shareagenda.access.net.CallbackHandler.HTTP_FAILURE;
import static com.cosin.shareagenda.access.net.CallbackHandler.SUCCESS;

public class FriendMessageAdapter extends RecyclerView.Adapter<FriendMessageAdapter.ViewHolder> {
    private List<FriendMessage> messages;
    private Context context;
    private int positionDel;


    public FriendMessageAdapter(Context context) {
        this.context = context;
        messages = new ArrayList<>();
    }

    public FriendMessageAdapter(List<FriendMessage> messages) {
        setMessages(messages);
    }

    private boolean isFriendMessage(Message message) {
        return message.getType().equals(MessageType.FRIEND);
    }

    public void setMessages(List<FriendMessage> messages) {
        this.messages = messages;
        notifyDataSetChanged();
    }

    public void removeMessage() {
        messages.remove(positionDel);
        notifyItemRemoved(positionDel);
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        SwipeMenuLayout swipe;
        TextView viewName;
        ImageButton viewAccept;
        ImageButton viewDecline;
        private final ImageView profileImage;

        public ViewHolder(View view) {
            super(view);
            swipe = view.findViewById(R.id.swp);
            viewName = view.findViewById(R.id.tvFriendMessage);
            viewAccept = view.findViewById(R.id.imgBtnAcceptFriendRequest);
            viewDecline = view.findViewById(R.id.imgBtnDeclineFriendRequest);
            profileImage = view.findViewById(R.id.imageView);

        }
    }

    @Override
    public FriendMessageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_msg_swipe, parent, false);
        final FriendMessageAdapter.ViewHolder viewHolder =
                new FriendMessageAdapter.ViewHolder(view);
        return viewHolder;
    }

    private Context getContext(){
        return this.context;
    }


    @Override
    public void onBindViewHolder(FriendMessageAdapter.ViewHolder viewHolder, int position) {
        viewHolder.viewName.setText(messages.get(position).getAccount().getAccountId());

        Account account = messages.get(position).getAccount();
        viewHolder.viewName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // senderId of message

                DisplayFriendRequestAccountDialog displayFriendRequestAccountDialog =
                        new DisplayFriendRequestAccountDialog(
                                getContext(),
                                account,
                                position);
                displayFriendRequestAccountDialog.show();

            }
        });

        if (account.getProfileImageUrl() != null) {
            Picasso.with(getContext())
                    .load(account.getProfileImageUrl())
                    .centerInside()
                    .fit()
                    .into(viewHolder.profileImage);
        }

        viewHolder.viewAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApiClient.replyFriend(messages.get(position).getMessageId(), ReplyStatus.ACCEPT, new CallbackHandler(handler));

                FriendMessageAdapter.this.positionDel = position;
                viewHolder.swipe.smoothClose();
                //messages.remove(position);
                //notifyItemRemoved(position);
                //notifyDataSetChanged();
            }
        });
        viewHolder.viewDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApiClient.replyFriend(messages.get(position).getMessageId(), ReplyStatus.DECLINE, new CallbackHandler(handler));

                FriendMessageAdapter.this.positionDel = position;
                viewHolder.swipe.smoothClose();
                //messages.remove(position);
                //notifyItemRemoved(position);
                //notifyDataSetChanged();
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
                    removeMessage();

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
