package com.cosin.shareagenda.activity;

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

public class GroupMembersActivity extends MainTitleActivity {

    // intent extra parameter name
    public static final String GROUP_ID = "GROUP_ID";

    private GroupListAdapter conAdapter;
    private String groupId;

    private void loadIntentExtra() {
        this.groupId = getIntent().getStringExtra(GROUP_ID);
    }

    @Override
    protected void initView() {
        super.initView();

        loadIntentExtra();

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
        return R.layout.activity_groups;
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        new Handler().postDelayed(new Runnable() {
//            public void run() {
//                loadData();
//            }
//        }, 1000); // give backend some delay to update data
//    }

    @Override
    protected void loadData() {
        ApiClient.getGroup(groupId, new CallbackHandler(getGroupMemberHandler));
    }

    @Override
    protected String titleName() {
        return "Groups";
    }

    Handler getGroupMemberHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(android.os.Message message) {
            final Gson gson = new Gson();
            switch (message.what) {
                case SUCCESS:
                    String body = (String) message.obj;
                    GetGroupResponse resp = gson.fromJson(body, GetGroupResponse.class);
                    resp.getMembers();  //TODO
                    conAdapter.setMemberList(resp.getMembers());
                    break;
            }
        }
    };

}
