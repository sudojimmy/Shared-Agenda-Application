package com.cosin.shareagenda.util;

import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;

import com.cosin.shareagenda.R;
import com.cosin.shareagenda.activity.ContactsActivity;
import com.cosin.shareagenda.activity.GroupsActivity;
import com.cosin.shareagenda.activity.NewCalendarActivity;
import com.cosin.shareagenda.activity.ProfileActivity;
import com.cosin.shareagenda.activity.WeeklyActivity;

import java.util.HashMap;
import java.util.Map;

public class HandleMenu {
    static final Map<Integer, Class> activity_map = new HashMap<Integer, Class>() {{
        put( R.id.nav_calendar, NewCalendarActivity.class);
        put( R.id.nav_weekCalendar, WeeklyActivity.class);
        put( R.id.nav_groups, GroupsActivity.class);
        put( R.id.nav_friends, ContactsActivity.class);
//        put( R.id.nav_publicEvents, ProfileActivity.class);
//        put( R.id.nav_groupEvents, ProfileActivity.class);
//        put( R.id.nav_events, ProfileActivity.class);
        put( R.id.nav_profile, ProfileActivity.class);
    }};


    public static void routeMain(Context context, MenuItem menuItem) {
        int id = menuItem.getItemId();
        if (activity_map.containsKey(id)) {
            Class c = activity_map.get(id);
            assert c != null;
            if (!c.getSimpleName().equals(context.getClass().getSimpleName())) {
                Intent intent = new Intent(context, c);
                context.startActivity(intent);
            }
        }
    }
}
