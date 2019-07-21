package com.cosin.shareagenda.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.cosin.shareagenda.R;
import com.cosin.shareagenda.access.net.CallbackHandler;
import com.cosin.shareagenda.api.ApiClient;
import com.cosin.shareagenda.model.Model;
import com.cosin.shareagenda.util.HandleMenu;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import types.GetMessageQueueResponse;
import types.MessageType;

import static com.cosin.shareagenda.access.net.CallbackHandler.SUCCESS;

public abstract class BaseActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    protected DrawerLayout drawer;
    public NavigationView navigationView;
    private FloatingActionButton news;
    private Thread msgThread;
    private List<types.Message>msgQueue = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadData();

        setContentView(R.layout.activity_main);

        // bind view
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        news = findViewById(R.id.fabNews);
        news.hide();

        // init navigationView
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        TextView username = headerView.findViewById(R.id.username);
        TextView poster = headerView.findViewById(R.id.tv_poster);
        username.setText(Model.model.getUser().getNickname());
        poster.setText(Model.model.getUser().getDescription());

        loadContentView();

        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        msgThread = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    while (!Thread.currentThread().isInterrupted()) {
                        Thread.sleep(3000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (Model.model.isLoggedIn() && loadMessage()) {
                                    ApiClient.getMessageQueue(new CallbackHandler(fetchMsgHandler));
                                }
                            }
                        });
                    }
                } catch (InterruptedException ignored) {
                }

            }
        });
        msgThread.start();
    }

    protected boolean loadMessage() {
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        msgThread.interrupt();
    }

    // need be override in derived class
    protected void loadContentView() {
        CoordinatorLayout
                coordinatorLayout = findViewById(R.id.coordinatorLayout);
        LayoutInflater inflater = getLayoutInflater();
        inflater.inflate(getContentView(), coordinatorLayout);
    }

    protected abstract int getContentView();

    // need be override in derived class
    protected abstract void loadData();

    // need be override in derived class
    protected abstract void initView();

    public void openDrawer() {
        drawer.openDrawer(GravityCompat.START);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        HandleMenu.routeMain(this, item);

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    Handler fetchMsgHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message message) {
            final Gson gson = new Gson();
            switch (message.what) {
                case SUCCESS:
                    String body = (String) message.obj;
                    GetMessageQueueResponse resp = gson.fromJson(body, GetMessageQueueResponse.class);

                    types.Message msg = setMsgQueueAndGetFirst(resp.getMessageList());
                    if (msg != null) {
                        setMsgButton(msg);
                    }
                    break;
            }
        }
    };

    private void setMsgButton(types.Message msg) {
        Intent intent;
        switch (msg.getType()) {
            case EVENT:
                intent = new Intent(BaseActivity.this, AccountEventMessageActivity.class);
                break;
            case FRIEND:
                intent = new Intent(BaseActivity.this, FriendMessageActivity.class);
                break;
            default: // UNSUPPORTED
                return;
        }

        news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent);
                BaseActivity.this.finish();
            }
        });
        news.show();
    }

    private types.Message setMsgQueueAndGetFirst(List<types.Message> messageList) {
        types.Message first = null;
        for (types.Message m : messageList) {
            if (!m.getType().equals(MessageType.RESPONSE)) {
                msgQueue.add(m);
                if (first == null) {
                    first = m;
                }
            }
        }
        return first;
    }
}
