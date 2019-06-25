package com.cosin.shareagenda.activity;

import com.cosin.shareagenda.R;
import com.cosin.shareagenda.view.TitleBar;

public class MainTitleActivity extends OldMainActivity {

    @Override
    protected void initView() {
        // init toolbar
        TitleBar titlebar = findViewById(R.id.titlebar);
        titlebar.setContext(this);
        titlebar.setTitleText(titleName());
    }
}
