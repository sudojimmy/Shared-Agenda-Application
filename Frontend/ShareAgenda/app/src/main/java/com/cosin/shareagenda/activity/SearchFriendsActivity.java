package com.cosin.shareagenda.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.cosin.shareagenda.R;
import com.cosin.shareagenda.adapter.SearchFriendsAdapter;
import com.cosin.shareagenda.entity.UserEntity;
import com.cosin.shareagenda.util.GenData;

import java.util.List;

public class SearchFriendsActivity extends MainTitleActivity {
    private List<UserEntity> friends;

    @Override
    protected int getContentView() {
        return R.layout.activity_search_friends;
    }

    @Override
    protected void loadData() {
        friends = GenData.loadFriends();
    }

    @Override
    protected String titleName() {
        return getResources().getString(R.string.title_search_friends);
    }

    @Override
    protected void initView() {
        super.initView();

        // recycleview
        RecyclerView recyclerView = findViewById(R.id.rvFriendsAdd);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(new SearchFriendsAdapter(friends));
    }
}
