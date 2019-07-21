package com.cosin.shareagenda.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cosin.shareagenda.R;
import com.cosin.shareagenda.access.net.CallbackHandler;
import com.cosin.shareagenda.adapter.GroupListAdapter;
import com.cosin.shareagenda.api.ApiClient;
import com.google.gson.Gson;

import types.GetGroupResponse;

import static com.cosin.shareagenda.access.net.CallbackHandler.SUCCESS;

public class GroupMembersActivity extends RefreshableActivity {

    // intent extra parameter name
    public static final String GROUP_ID = "GROUP_ID";
    public static final String GROUP_NAME = "GROUP_NAME";

    private GroupListAdapter conAdapter;
    private String groupId;
    private String groupName;

    private void loadIntentExtra() {
        this.groupId = getIntent().getStringExtra(GROUP_ID);
        this.groupName = getIntent().getStringExtra(GROUP_NAME);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loadIntentExtra();
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        super.initView();

        // set each member layout row
        RecyclerView rvContacts = findViewById(R.id.group_list_view);
        rvContacts.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvContacts.setLayoutManager(layoutManager);
        conAdapter = new GroupListAdapter(this);
        rvContacts.setAdapter(conAdapter);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_group_member;
    }

    @Override
    protected void loadData() {
        ApiClient.getGroup(groupId, new CallbackHandler(getGroupMemberHandler));
    }

    @Override
    protected String titleName() {
        return this.groupName;
    }

    Handler getGroupMemberHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(android.os.Message message) {
            final Gson gson = new Gson();
            switch (message.what) {
                case SUCCESS:
                    String body = (String) message.obj;
                    GetGroupResponse resp = gson.fromJson(body, GetGroupResponse.class);
                    conAdapter.setOwnerId(resp.getOwnerId());
                    conAdapter.setMemberList(resp.getMembers());
                    break;
            }
            stopRefreshing();
        }
    };

}
