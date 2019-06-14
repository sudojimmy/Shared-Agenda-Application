package com.cosin.shareagenda.util;

import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;

import com.cosin.shareagenda.R;
import com.cosin.shareagenda.activity.CalendarActivity;
import com.cosin.shareagenda.activity.GroupEventsActivity;
import com.cosin.shareagenda.activity.WeeklyActivity;

public class HandleMenu {

    public static void routeMain(Context context, MenuItem menuItem) {
        int id = menuItem.getItemId();

        if (id == R.id.nav_calendar) {
            // Handle calendar
            if (!"CalendarActivity".equals(context.getClass().getSimpleName())) {
                Intent intent = new Intent(context, CalendarActivity.class);
                context.startActivity(intent);
            }
        } else if (id == R.id.nav_weekCalendar) {
            if (!"WeeklyActivity".equals(context.getClass().getSimpleName())) {
                Intent intent = new Intent(context, WeeklyActivity.class);
                context.startActivity(intent);
            }
        } else if (id == R.id.nav_groups) {
            if (!"GroupEventsActivity".equals(context.getClass().getSimpleName())) {
                Intent intent = new Intent(context, GroupEventsActivity.class);
                context.startActivity(intent);
            }
        } else if (id == R.id.nav_friends) {

        } else if (id == R.id.nav_publicEvents) {

        } else if (id == R.id.nav_groupEvents) {

        } else if (id == R.id.nav_events) {

        } else if (id == R.id.nav_profile) {

        }
    }
}
