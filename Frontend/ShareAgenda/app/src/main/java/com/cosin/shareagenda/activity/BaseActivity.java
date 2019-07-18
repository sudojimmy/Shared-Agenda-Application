package com.cosin.shareagenda.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.cosin.shareagenda.R;
import com.cosin.shareagenda.config.AgendaApplication;
import com.cosin.shareagenda.util.HandleMenu;
import com.google.android.material.navigation.NavigationView;

public abstract class BaseActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    protected DrawerLayout drawer;
    public NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadData();

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

        loadContentView();

        initView();
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
}
