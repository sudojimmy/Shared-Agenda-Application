package com.cosin.shareagenda.api.plugin.uwapi;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.cosin.shareagenda.access.net.CallbackHandler;
import com.cosin.shareagenda.api.ApiClient;
import com.cosin.shareagenda.model.Model;

import java.util.ArrayList;

import types.Event;
import types.UWCourse;
import types.UWGetCourseRequest;

import static com.cosin.shareagenda.api.plugin.uwapi.UWaterlooApiConstant.CODE;
import static com.cosin.shareagenda.api.plugin.uwapi.UWaterlooApiConstant.HINT;

public class UWEventPlugin {
    public static UWaterlooApiClient getClient() {
        return UWaterlooApiClient.getInstance();
    }

    public static String getType() {
        return CODE;
    }

    // ignore case
    public static boolean isActive(String str) {
        return str.toUpperCase().startsWith(CODE);
    }

    public static ArrayList<UWExploreInfo> toExploreInfo(UWGetCourseRequest request) {
        ArrayList<UWExploreInfo> info = new ArrayList<>();
        for (UWCourse course : request.getData()) {
            info.add(new UWExploreInfo(UWEventReader.readTitle(course),
                    UWEventReader.readDescription(course), course));
        }
        return info;
    }

    public static void addClass(UWCourse course) {
        ArrayList<Event> events = UWEventReader.courseToEvents(course,
                Model.model.getCurrentTermStart(), Model.model.getCurrentTermEnd());
        ApiClient.createRelatedEvents(events, new CallbackHandler(handler));
    }

    private static Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message message) {}
    };


    public static String getHint() {
        return HINT;
    }
}
