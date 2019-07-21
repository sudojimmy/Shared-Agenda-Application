package com.cosin.shareagenda.activity;

import com.baoyz.widget.PullRefreshLayout;
import com.cosin.shareagenda.R;

public abstract class RefreshableActivity extends MainTitleActivity {
    private PullRefreshLayout refreshView;

    @Override
    protected void initView() {
        super.initView();
        refreshView = (PullRefreshLayout) findViewById(R.id.swipeRefreshLayout);

        refreshView.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });

    }
    protected void stopRefreshing() {
        refreshView.setRefreshing(false);
    }
}
