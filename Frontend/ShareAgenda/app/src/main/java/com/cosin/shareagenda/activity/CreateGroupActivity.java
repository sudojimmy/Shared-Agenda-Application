package com.cosin.shareagenda.activity;

import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.cosin.shareagenda.R;
import com.cosin.shareagenda.access.net.CallbackHandler;
import com.cosin.shareagenda.adapter.GroupMemberAdapter;
import com.cosin.shareagenda.entity.VO_Member;
import com.cosin.shareagenda.api.ApiClient;
import com.cosin.shareagenda.api.ApiErrorResponse;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import types.Account;
import types.GetFriendQueueResponse;

import static com.cosin.shareagenda.access.net.CallbackHandler.HTTP_FAILURE;
import static com.cosin.shareagenda.access.net.CallbackHandler.SUCCESS;

public class CreateGroupActivity extends MainTitleActivity {
    private List<VO_Member> members = new ArrayList<>();
    private GroupMemberAdapter groupAdapter;
    private TextView groupDescription;
    private TextView groupName;

    @Override
    protected String titleName() {
        return "Create Group";
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_create_group;
    }

    @Override
    protected void loadData() {
        ApiClient.getFriendQueue(new CallbackHandler(getFriendHandler));
    }

    @Override
    protected void initView() {
        super.initView();

        groupDescription = findViewById(R.id.inGroupDesc);
        groupName = findViewById(R.id.inGroupName);
        RecyclerView rv = findViewById(R.id.rvContacts);
        StaggeredGridLayoutManager sgl =
                new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL);
        rv.setLayoutManager(sgl);
        groupAdapter = new GroupMemberAdapter(members, this);
        rv.setAdapter(groupAdapter);

        Button btnCreateGroup = findViewById(R.id.btnCreateGroup);
        btnCreateGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateGroup();
            }
        });
    }

    private void CreateGroup() {
        ApiClient.createGroup(groupName.getText().toString(),
                groupDescription.getText().toString(),
                filterMember(members),
                new CallbackHandler(createGroupHandler));
    }

    private static ArrayList<String> filterMember(List<VO_Member> members) {
        ArrayList<String> lst = new ArrayList();
        for (VO_Member m : members) {
            if (m.isElected()) {
                lst.add(m.getMember().getAccountId());
            }
        }
        return lst;
    }

    Handler getFriendHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(android.os.Message message) {
            final Gson gson = new Gson();
            switch (message.what) {
                case SUCCESS:
                    String body = (String) message.obj;
                    GetFriendQueueResponse resp = gson.fromJson(body, GetFriendQueueResponse.class);
                    for (Account friend: resp.getFriendList()) {
                        members.add(new VO_Member(friend));
                    }
                    groupAdapter.setMembers(members);
                    break;
            }
        }
    };

    Handler createGroupHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(android.os.Message message) {
            final Gson gson = new Gson();
            switch (message.what) {
                case SUCCESS:
                    finish();
                    break;
                case HTTP_FAILURE:
                    ApiErrorResponse errorResponse = gson.fromJson((String) message.obj, ApiErrorResponse.class);
                    Toast.makeText(CreateGroupActivity.this, errorResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(CreateGroupActivity.this, (String) message.obj, Toast.LENGTH_SHORT).show();
            }
        }
    };
}
