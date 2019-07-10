package com.cosin.shareagenda.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.cosin.shareagenda.R;
import com.cosin.shareagenda.adapter.GroupContactsAdapter;
import com.cosin.shareagenda.entity.ContactEntity;
import com.cosin.shareagenda.util.GenData;

import java.util.List;

public class GroupsActivity extends MainTitleActivity {
    private List<ContactEntity> groups;

    @Override
    protected void initView() {
        super.initView();

        RecyclerView rvContacts = findViewById(R.id.rvGroups);
        rvContacts.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvContacts.setLayoutManager(layoutManager);
        GroupContactsAdapter conAdapter = new GroupContactsAdapter(this, groups);
        rvContacts.setAdapter(conAdapter);

        LinearLayout ll = findViewById(R.id.llCreateGroup);
        ll.setOnClickListener(new View.OnClickListener() {
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
    protected void loadData() {
        groups = GenData.loadGroups();
    }

    @Override
    protected String titleName() {
        return "Groups";
    }
}
