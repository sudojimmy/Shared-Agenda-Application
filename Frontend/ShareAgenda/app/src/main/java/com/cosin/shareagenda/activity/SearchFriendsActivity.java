package com.cosin.shareagenda.activity;

import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cosin.shareagenda.R;
import com.cosin.shareagenda.access.net.CallbackHandler;
import com.cosin.shareagenda.adapter.SearchFriendsAdapter;
import com.cosin.shareagenda.api.ApiClient;
import com.cosin.shareagenda.api.ApiErrorResponse;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import types.Account;
import types.ExploreAccountResponse;

import static com.cosin.shareagenda.access.net.CallbackHandler.HTTP_FAILURE;
import static com.cosin.shareagenda.access.net.CallbackHandler.SUCCESS;
import static com.cosin.shareagenda.model.Model.model;

public class SearchFriendsActivity extends MainTitleActivity {
    private List<Account> friends;
    private SearchFriendsAdapter searchFriendsAdapter;

    @Override
    protected int getContentView() {
        return R.layout.activity_search_friends;
    }

    @Override
    protected void loadData() {
        //friends = GenData.loadFriends();
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
        searchFriendsAdapter = new SearchFriendsAdapter(this);
        recyclerView.setAdapter(searchFriendsAdapter);

        SearchView search = findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search.requestFocusFromTouch();
                search.onActionViewExpanded();
            }
        });
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                ApiClient.exploreAccount(model.getUser().getAccountId(), s, new CallbackHandler(handler));
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }

    Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(android.os.Message message) {
            final Gson gson = new Gson();
            switch (message.what) {
                case SUCCESS:
                    String body = (String) message.obj;
                    ExploreAccountResponse resp = gson.fromJson(body, ExploreAccountResponse.class);
                    friends = new ArrayList<>();
                    friends.addAll(resp.getAccountList());
                    searchFriendsAdapter.setFriends(friends);
                    break;
                case HTTP_FAILURE:
                    ApiErrorResponse errorResponse = gson.fromJson((String) message.obj, ApiErrorResponse.class);
                    Toast.makeText(SearchFriendsActivity.this, errorResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(SearchFriendsActivity.this, (String) message.obj, Toast.LENGTH_SHORT).show();
            }
        }
    };
}
