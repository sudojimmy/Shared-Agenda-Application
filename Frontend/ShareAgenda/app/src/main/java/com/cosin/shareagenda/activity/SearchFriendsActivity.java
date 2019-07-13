package com.cosin.shareagenda.activity;

import android.os.Handler;
import android.os.Looper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.cosin.shareagenda.R;
import com.cosin.shareagenda.access.net.CallbackHandler;
import com.cosin.shareagenda.adapter.SearchFriendsAdapter;
import com.cosin.shareagenda.model.ApiClient;
import com.cosin.shareagenda.model.ApiErrorResponse;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import types.Account;
import types.GetAccountResponse;

import static com.cosin.shareagenda.access.net.CallbackHandler.HTTP_FAILURE;
import static com.cosin.shareagenda.access.net.CallbackHandler.SUCCESS;

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
        EditText etSearch = findViewById(R.id.edit_search_friend);
        RecyclerView recyclerView = findViewById(R.id.rvFriendsAdd);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);
        searchFriendsAdapter = new SearchFriendsAdapter();
        recyclerView.setAdapter(searchFriendsAdapter);

        ImageButton imgBtn = findViewById(R.id.imgBtnSearch);
        imgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*fetch edit*/
                ApiClient.getAccount(etSearch.getText().toString(), new CallbackHandler(handler));
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
                    GetAccountResponse resp = gson.fromJson(body, GetAccountResponse.class);
                    friends = new ArrayList<>();
                    friends.add(new Account()
                            .withNickname(resp.getNickname())
                            .withAccountId(resp.getAccountId())
                            .withDescription(resp.getDescription())
                    );
                    searchFriendsAdapter.setFriends(friends);
                    searchFriendsAdapter.notifyDataSetChanged();
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
