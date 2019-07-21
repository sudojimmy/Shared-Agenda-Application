package com.cosin.shareagenda.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cosin.shareagenda.R;
import com.cosin.shareagenda.access.net.CallbackHandler;
import com.cosin.shareagenda.adapter.GroupContactsAdapter;
import com.cosin.shareagenda.api.ApiClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import types.GetGroupListResponse;

import static com.cosin.shareagenda.access.net.CallbackHandler.SUCCESS;

public class GroupsActivity extends RefreshableActivity {
    private GroupContactsAdapter conAdapter;

    @Override
    protected void initView() {
        super.initView();

        RecyclerView rvContacts = findViewById(R.id.rvGroups);
        rvContacts.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvContacts.setLayoutManager(layoutManager);
        conAdapter = new GroupContactsAdapter(this);
        rvContacts.setAdapter(conAdapter);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GroupsActivity.this, CreateGroupActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_groups;
    }

    @Override
    protected void onResume() {
        super.onResume();

        new Handler().postDelayed(new Runnable() {
            public void run() {
                loadData();
            }
        }, 1000); // give backend some delay to update data
    }

    @Override
    protected void loadData() {
        ApiClient.getGroupList(new CallbackHandler(getGroupHandler));

    }

    @Override
    protected String titleName() {
        return "Groups";
    }

    Handler getGroupHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(android.os.Message message) {
            final Gson gson = new Gson();
            switch (message.what) {
                case SUCCESS:
                    String body = (String) message.obj;
                    GetGroupListResponse resp = gson.fromJson(body, GetGroupListResponse.class);
                    conAdapter.setContactList(resp.getGroupList());
                    break;
            }
            stopRefreshing();
        }
    };

}
