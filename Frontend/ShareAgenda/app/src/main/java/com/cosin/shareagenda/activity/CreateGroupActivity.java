package com.cosin.shareagenda.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.cosin.shareagenda.R;
import com.cosin.shareagenda.adapter.GroupMemberAdapter;
import com.cosin.shareagenda.entity.ContactEntity;
import com.cosin.shareagenda.entity.UserEntity;
import com.cosin.shareagenda.entity.VO_Member;
import com.cosin.shareagenda.util.GenData;

import java.util.ArrayList;
import java.util.List;

public class CreateGroupActivity extends MainTitleActivity {
    private List<VO_Member> members;

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
        members = new ArrayList<>();
        for (UserEntity u : GenData.loadFriends()) {
            members.add(new VO_Member(u));
        }
    }

    @Override
    protected void initView() {
        super.initView();

        RecyclerView rv = findViewById(R.id.rvContacts);
        StaggeredGridLayoutManager sgl =
                new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);
        rv.setLayoutManager(sgl);
        rv.setAdapter(new GroupMemberAdapter(members));

        Button btnCreateGroup = findViewById(R.id.btnCreateGroup);
        btnCreateGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateGroup();
                CreateGroupActivity.this.finish();
            }
        });
    }

    private void CreateGroup() {
        //
    }
}
