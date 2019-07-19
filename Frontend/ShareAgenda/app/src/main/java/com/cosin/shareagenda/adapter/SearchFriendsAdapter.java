package com.cosin.shareagenda.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.cosin.shareagenda.R;
import com.cosin.shareagenda.dialog.DisplayAddAccountDialog;

import java.util.ArrayList;
import java.util.List;

import types.Account;

public class SearchFriendsAdapter extends RecyclerView.Adapter<SearchFriendsAdapter.ViewHolder> {
    private List<Account> friends;
    private Context context;

    public SearchFriendsAdapter(Context context) {
        this.context = context;
        friends = new ArrayList<>();
    }

    public SearchFriendsAdapter(List<Account> friends, Context context) {
        this.context = context;
        this.friends = friends;
    }

    public void setFriends(List<Account> friends) {
        this.friends = friends;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView viewName;

        public ViewHolder(View view) {
            super(view);
            viewName = view.findViewById(R.id.tvSearchFriendName);
        }
    }

    public Context getContext(){
        return context;
    }

    private SearchFriendsAdapter getSearchFriendAdapter(){
        return this;
    }

    @Override
    public SearchFriendsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_friends_item, parent, false);
        final SearchFriendsAdapter.ViewHolder viewHolder =
                new SearchFriendsAdapter.ViewHolder(view);
        return viewHolder;
    }

    public void removeElementFromContactList(int position){
        friends.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();

    }

    @Override
    public void onBindViewHolder(SearchFriendsAdapter.ViewHolder viewHolder, int position) {
        viewHolder.viewName.setText(friends.get(position).getNickname());

        viewHolder.viewName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DisplayAddAccountDialog(
                        getContext(),
                        friends.get(position),
                        position,
                        getSearchFriendAdapter() ).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }
}
