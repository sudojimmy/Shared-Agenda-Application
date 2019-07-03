package com.cosin.shareagenda.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.cosin.shareagenda.R;
import com.cosin.shareagenda.entity.GroupEntity;
import com.cosin.shareagenda.util.GenData;

import java.util.List;

public class GroupsActivity extends MainTitleActivity {
    private List<GroupEntity> groups;

    @Override
    protected void initView() {
        super.initView();

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
