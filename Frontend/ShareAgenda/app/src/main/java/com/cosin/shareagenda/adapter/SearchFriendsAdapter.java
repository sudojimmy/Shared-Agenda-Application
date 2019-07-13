package com.cosin.shareagenda.adapter;

import android.os.Handler;
import android.os.Looper;
import androidx.recyclerview.widget.RecyclerView;
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

import types.Account;

import static com.cosin.shareagenda.access.net.CallbackHandler.HTTP_FAILURE;
import static com.cosin.shareagenda.access.net.CallbackHandler.SUCCESS;

public class SearchFriendsAdapter extends RecyclerView.Adapter<SearchFriendsAdapter.ViewHolder> {
    private List<Account> friends;

    public SearchFriendsAdapter() {
        friends = new ArrayList<>();
    }

    public SearchFriendsAdapter(List<Account> friends) {
        this.friends = friends;
    }

    public void setFriends(List<Account> friends) {
        this.friends = friends;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView viewName;
        ImageButton viewAdd;

        public ViewHolder(View view) {
            super(view);
            viewName = view.findViewById(R.id.tvSearchFriendName);
            viewAdd = view.findViewById(R.id.imgBtnAddFriend);
        }
    }

    @Override
    public SearchFriendsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_friends_item, parent, false);
        final SearchFriendsAdapter.ViewHolder viewHolder =
                new SearchFriendsAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(SearchFriendsAdapter.ViewHolder viewHolder, int position) {
        viewHolder.viewName.setText(friends.get(position).getNickname());
        viewHolder.viewAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApiClient.inviteFriend(friends.get(position).getAccountId(), new CallbackHandler(handler));
                /* Add friend here */
                /*--------*/

                friends.remove(position);
                notifyItemRemoved(position);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return friends.size();
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
