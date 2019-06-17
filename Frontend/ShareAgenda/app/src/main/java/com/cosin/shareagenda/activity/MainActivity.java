package com.cosin.shareagenda.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.cosin.shareagenda.R;
import com.cosin.shareagenda.adapter.EventAdapter;
import com.cosin.shareagenda.entity.EventEntity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.cosin.shareagenda.util.HandleMenu;
import com.cosin.shareagenda.util.AppHelper;
import com.cosin.shareagenda.util.GenData;
import com.cosin.shareagenda.view.TitleBar;
//import com.zhy.http.okhttp.OkHttpUtils;
//import com.zhy.http.okhttp.callback.StringCallback;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    protected DrawerLayout drawer;
    public NavigationView navigationView;
    public android.support.design.widget.CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // bind view
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);

        loadContentView();

        // init navigationView
        navigationView.setNavigationItemSelectedListener(this);
        if (AppHelper.getUserInfo() != null) {
            View headerView = navigationView.getHeaderView(0);
            TextView username = headerView.findViewById(R.id.username);
            username.setText(AppHelper.getUserInfo().getNickname());
        }

        loadData();

        initView();
    }

    // could be override in derived class
    protected  String titleName() {
        return "";
    }

    // need be override in derived class
    protected void loadContentView() {
    }

    // need be override in derived class
    protected void loadData() {
    }

    // need be override in derived class
    protected void initView() {
    }

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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        HandleMenu.routeMain(this, item);

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}