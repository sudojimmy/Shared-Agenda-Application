package com.cosin.shareagenda.api.plugin.uwapi;

import android.os.Handler;
import android.os.Looper;

import com.cosin.shareagenda.access.net.CallbackHandler;
import com.cosin.shareagenda.access.net.NetLoader;
import com.cosin.shareagenda.adapter.ExploreEventsAdapter;
import com.cosin.shareagenda.api.BaseApiClient;
import com.cosin.shareagenda.api.plugin.ExploreInfo;
import com.cosin.shareagenda.model.Model;
import com.google.gson.Gson;

import java.util.ArrayList;

import okhttp3.Callback;
import okhttp3.HttpUrl;
import types.UWGetCourseRequest;
import types.UWGetExamRequest;
import types.UWGetTermRequest;
import types.UWImportantDate;

import static com.cosin.shareagenda.access.net.CallbackHandler.SUCCESS;

public class UWaterlooApiClient extends BaseApiClient {
    public static final String SCHEMA = "https";
    public static final String HOST = "api.uwaterloo.ca";
    public static final String VERSION = "v2";
    public static final String API_KEY = "key";
    public static final String API_KEY_VALUE = "9905ad6eee99dfc86e0f30b6e98e3d46";

    public static final String TERMS = "terms";
    public static final String COURSES = "courses";

    private static final String JSON_POSTFIX = ".json";
    public static final String SCHEDULE = "schedule" + JSON_POSTFIX;
    public static final String EXAM_SCHEDULE = "examschedule" + JSON_POSTFIX;
    public static final String IMPORTANT_DATES = "importantdates" + JSON_POSTFIX;

    private static final UWaterlooApiClient instance = new UWaterlooApiClient();

    public static UWaterlooApiClient getInstance() {
        return instance;
    }

    // str format: +UW CS446 [1195]
    public boolean exploreEvent(String str, ExploreEventsAdapter adapter) {
        UWCourseQueryInfo info = UWQueryStringParser.parseQueryString(str);
        if (info == null) {
            return false;
        }
        getTermInfo(info.getTerm(), new CallbackHandler(termInfoHandler));
        getTermCourse(info, new CallbackHandler(getEventHandler(adapter)));
        getCourseExam(info, new CallbackHandler(getExamHandler(adapter)));
        return true;
    }

    private void getTermCourse(UWCourseQueryInfo info, Callback callback) {
        HttpUrl httpUrl = new HttpUrl.Builder()
                .scheme(SCHEMA)
                .host(HOST)
                .addPathSegment(VERSION)
                .addPathSegment(TERMS)
                .addPathSegment(info.getTerm())
                .addPathSegment(info.getSubject())
                .addPathSegment(info.getCatalog_number())
                .addPathSegment(SCHEDULE)
                .addQueryParameter(API_KEY, API_KEY_VALUE)
                .build();
        makePostRequest(httpUrl.toString(), callback);
    }

    private void getCourseExam(UWCourseQueryInfo info, Callback callback) {
        HttpUrl httpUrl = new HttpUrl.Builder()
                .scheme(SCHEMA)
                .host(HOST)
                .addPathSegment(VERSION)
                .addPathSegment(COURSES)
                .addPathSegment(info.getSubject())
                .addPathSegment(info.getCatalog_number())
                .addPathSegment(EXAM_SCHEDULE)
                .addQueryParameter(API_KEY, API_KEY_VALUE)
                .build();
        makePostRequest(httpUrl.toString(), callback);
    }

    private void getTermInfo(String term, Callback callback) {
        HttpUrl httpUrl = new HttpUrl.Builder()
                .scheme(SCHEMA)
                .host(HOST)
                .addPathSegment(VERSION)
                .addPathSegment(TERMS)
                .addPathSegment(term)
                .addPathSegment(IMPORTANT_DATES)
                .addQueryParameter(API_KEY, API_KEY_VALUE)
                .build();
        makePostRequest(httpUrl.toString(), callback);
    }

    private void makePostRequest(String url, Callback callback) {
        new NetLoader(url).GetRequest(callback);
    }

    private Handler termInfoHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(android.os.Message message) {
            final Gson gson = new Gson();
            switch (message.what) {
                case SUCCESS:
                    String body = (String) message.obj;
                    UWGetTermRequest uwResp = gson.fromJson(body, UWGetTermRequest.class);
                    setTermInfo(uwResp);
                    break;
            }
        }
    };

    private Handler getExamHandler(ExploreEventsAdapter adapter) {
        return new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(android.os.Message message) {
                final Gson gson = new Gson();
                switch (message.what) {
                    case SUCCESS:
                        String body = (String) message.obj;
                        System.out.println(body);
                        UWGetExamRequest uwResp = gson.fromJson(body, UWGetExamRequest.class);
                        ArrayList<ExploreInfo> infos = new ArrayList<>(UWEventPlugin.examToExploreInfo(uwResp));
                        adapter.addEventList(infos);
                        break;
                }
            }
        };
    }

    private Handler getEventHandler(ExploreEventsAdapter adapter) {
        return new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(android.os.Message message) {
                final Gson gson = new Gson();
                switch (message.what) {
                    case SUCCESS:
                        String body = (String) message.obj;
                        UWGetCourseRequest uwResp = gson.fromJson(body, UWGetCourseRequest.class);
                        ArrayList<ExploreInfo> infos = new ArrayList<>(UWEventPlugin.courseToExploreInfo(uwResp));
                        adapter.addEventList(infos);
                        break;
                }
            }
        };
    }

    private void setTermInfo(UWGetTermRequest uwResp) {
        for (UWImportantDate date : uwResp.getData()) {
            if (date.getTitle().contains("Lectures") &&
                    date.getTitle().contains("begin") &&
                    date.getTitle().contains("Waterloo")) {
                Model.model.setCurrentTermStart(date.getStart_date());
            } else if (date.getTitle().contains("Lectures") &&
                    date.getTitle().contains("end") &&
                    date.getTitle().contains("Waterloo")){
                Model.model.setCurrentTermEnd(date.getStart_date());
            }
        }
    }
}