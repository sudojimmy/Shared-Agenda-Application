package com.cosin.shareagenda.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;

import com.cosin.shareagenda.R;
import com.cosin.shareagenda.activity.AccountEventMessageActivity;
import com.cosin.shareagenda.activity.ContactsActivity;
import com.cosin.shareagenda.activity.ExploreActivity;
import com.cosin.shareagenda.activity.GroupsActivity;
import com.cosin.shareagenda.activity.NewCalendarActivity;
import com.cosin.shareagenda.activity.ProfileActivity;

import java.util.HashMap;
import java.util.Map;

public class HandleMenu {
    static final Map<Integer, Class> activity_map = new HashMap<Integer, Class>() {{
        put( R.id.nav_calendar, NewCalendarActivity.class);
//        put( R.id.nav_weekCalendar, WeeklyActivity.class);
        put( R.id.nav_groups, GroupsActivity.class);
        put( R.id.nav_friends, ContactsActivity.class);
        put( R.id.nav_publicEvents, ExploreActivity.class);
//        put( R.id.nav_groupEvents, ProfileActivity.class);
        put( R.id.nav_events, AccountEventMessageActivity.class);
        put( R.id.nav_profile, ProfileActivity.class);
    }};


    public static void routeMain(Context context, MenuItem menuItem) {
        int id = menuItem.getItemId();
        if (activity_map.containsKey(id)) {
            Class c = activity_map.get(id);
            assert c != null;
            if (!c.getSimpleName().equals(context.getClass().getSimpleName()) ||
                    onOtherCalendar(context)) {
                Intent intent = new Intent(context, c);
                context.startActivity(intent);
                ((Activity)context).finish();
            }
        }
    }

    private static boolean onOtherCalendar(Context context) {
        if (context.getClass().getSimpleName().equals(NewCalendarActivity.class.getSimpleName())) {
            NewCalendarActivity nca = (NewCalendarActivity) context;
            return nca.onOtherCalendar();
        }
        return false;
    }
}
