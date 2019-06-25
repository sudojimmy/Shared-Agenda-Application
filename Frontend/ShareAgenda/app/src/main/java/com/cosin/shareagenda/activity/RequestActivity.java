package com.cosin.shareagenda.activity;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.cosin.shareagenda.R;
import com.cosin.shareagenda.access.net.NetLoader;
import com.cosin.shareagenda.entity.UserEntity;
import com.cosin.shareagenda.entity.net.TestResult;
import com.cosin.shareagenda.util.AppHelper;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RequestActivity extends MainTitleActivity implements View.OnClickListener {
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            TextView tv = findViewById(R.id.textView);
            switch (msg.what) {
                case 1:
                    try {
                        Gson gson = new Gson();
                        TestResult result = gson.fromJson((String)msg.obj, TestResult.class);
                        if (AppHelper.isNullOrEmpty(result.getErrmsg())) {
                            UserEntity user = result.getUser();
                            tv.setText(user.getNickname());
                        } else {
                            tv.setText(result.getErrmsg());
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    tv.setText("Network Error: " + (String) msg.obj);
                    break;
            }
        }
    };

    @Override
    protected  String titleName() {
        return getResources().getString(R.string.title_friends_calendar);
    }

    @Override
    protected void loadContentView() {
        LayoutInflater inflater = getLayoutInflater();
        inflater.inflate(R.layout.activity_request, coordinatorLayout);
    }

    @Override
    protected void loadData() {
        //
    }

    @Override
    protected void initView() {
        super.initView();

        titlebarHideRightButton();

        // init panel
        findViewById(R.id.button_post1).setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_post1:
                UserEntity user = new UserEntity("3","florie","ASD");
                TestResult test = new TestResult();
                test.setUser(user);
                Gson gson = new Gson();
                final RequestBody requestBody = new FormBody.Builder()
                        .add("rid", "1")
                        .add("user", gson.toJson(test))
                        .build();
                new NetLoader("test", requestBody)
                        .PostRequest(new RequestActivity.CallbackHandler());
                break;
        }
    }

    class CallbackHandler implements Callback {
        @Override
        public void onFailure(Call call, IOException e) {
            Message msg = handler.obtainMessage(2);
            msg.obj = e.getMessage();
            handler.sendMessage(msg);
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            Message msg = handler.obtainMessage(1);
            msg.obj = response.body().string();
            handler.sendMessage(msg);
        }
    }
}
