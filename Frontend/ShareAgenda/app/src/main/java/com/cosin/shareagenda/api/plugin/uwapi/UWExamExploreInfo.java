package com.cosin.shareagenda.api.plugin.uwapi;

import com.cosin.shareagenda.api.plugin.ExploreInfo;

import types.UWExamSection;

import static com.cosin.shareagenda.api.plugin.uwapi.UWaterlooApiConstant.CODE;

public class UWExamExploreInfo extends ExploreInfo {

    private final String course;
    private final UWExamSection examSection;

    public UWExamExploreInfo(String title, String description, String course, UWExamSection examSection) {
        super(title, description);
        this.course = course;
        this.examSection = examSection;
    }

    public UWExamSection getExam() {
        return examSection;
    }

    @Override
    public String getType() {
        return CODE;
    }

    public String getCourse() {
        return course;
    }
}
