package com.cosin.shareagenda.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cosin.shareagenda.R;
import com.cosin.shareagenda.entity.UserEntity;

import java.util.List;

public class SearchFriendsAdapter extends RecyclerView.Adapter<SearchFriendsAdapter.ViewHolder> {
    private List<UserEntity> friends;

    public SearchFriendsAdapter(List<UserEntity> friends) {
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
}
