package com.cosin.shareagenda.activity;

import com.cosin.shareagenda.R;
import com.cosin.shareagenda.view.TitleBar;

public abstract class MainTitleActivity extends BaseActivity {

    @Override
    protected void initView() {
        // init toolbar
        TitleBar titlebar = findViewById(R.id.titlebar);
        titlebar.setContext(this);
        titlebar.setTitleText(titleName());
    }

    // could be override in derived class
    protected abstract String titleName();

    protected void changeTitle(String title) {
        TitleBar titlebar = findViewById(R.id.titlebar);
        titlebar.setTitleText(title);
    }
}
