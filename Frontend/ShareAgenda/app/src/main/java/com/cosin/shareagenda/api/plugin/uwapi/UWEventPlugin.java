package com.cosin.shareagenda.api.plugin.uwapi;

import com.cosin.shareagenda.access.net.CallbackHandler;
import com.cosin.shareagenda.api.ApiClient;
import com.cosin.shareagenda.model.Model;

import java.util.ArrayList;

import types.Event;
import types.UWCourse;
import types.UWExamSection;
import types.UWGetCourseRequest;
import types.UWGetExamRequest;

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

    public static ArrayList<UWCourseExploreInfo> courseToExploreInfo(UWGetCourseRequest request) {
        ArrayList<UWCourseExploreInfo> info = new ArrayList<>();
        for (UWCourse course : request.getData()) {
            info.add(new UWCourseExploreInfo(UWEventReader.readTitle(course),
                    UWEventReader.readDescription(course), course));
        }
        return info;
    }

    public static ArrayList<UWExamExploreInfo> examToExploreInfo(UWGetExamRequest request) {
        ArrayList<UWExamExploreInfo> info = new ArrayList<>();
        for (UWExamSection examSection : request.getData().getSections()) {
            info.add(new UWExamExploreInfo(
                    "Final Exam: " + request.getData().getCourse(),
                    "section: " + examSection.getSection(),
                    request.getData().getCourse(),
                    examSection));
        }
        return info;
    }

    public static void addClass(UWCourse course, CallbackHandler callbackHandler) {
        ArrayList<Event> events = UWEventReader.courseToEvents(course,
                Model.model.getCurrentTermStart(), Model.model.getCurrentTermEnd());
        ApiClient.createRelatedEvents(events, callbackHandler);
    }

    public static void addExam(UWExamExploreInfo examExploreInfo, CallbackHandler callbackHandler) {
        Event events = UWEventReader.examToEvent(examExploreInfo,
                Model.model.getCurrentTermStart(), Model.model.getCurrentTermEnd());
        ApiClient.createEvent(events, callbackHandler);
    }


    public static String getHint() {
        return HINT;
    }
}
