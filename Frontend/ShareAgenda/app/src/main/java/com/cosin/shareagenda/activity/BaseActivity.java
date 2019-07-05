package com.cosin.shareagenda.activity;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.cosin.shareagenda.R;
import com.cosin.shareagenda.config.AgendaApplication;
import com.cosin.shareagenda.model.Model;
import com.cosin.shareagenda.util.AppHelper;
import com.cosin.shareagenda.util.HandleMenu;

import types.Account;

public abstract class BaseActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    protected DrawerLayout drawer;
    public NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // bind view
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        // init navigationView
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        TextView username = headerView.findViewById(R.id.username);
        TextView poster = headerView.findViewById(R.id.tv_poster);
        username.setText(AgendaApplication.getUserInfo().getNickname());
        poster.setText(AgendaApplication.getUserDescription());

        loadData();

        loadContentView();

        initView();
    }

    // need be override in derived class
    protected void loadContentView() {
        android.support.design.widget.CoordinatorLayout
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
}
