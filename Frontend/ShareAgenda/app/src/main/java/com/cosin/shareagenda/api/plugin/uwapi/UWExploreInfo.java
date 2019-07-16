package com.cosin.shareagenda.api.plugin.uwapi;

import com.cosin.shareagenda.api.plugin.ExploreInfo;

import types.UWCourse;

import static com.cosin.shareagenda.api.plugin.uwapi.UWaterlooApiConstant.CODE;

public class UWExploreInfo extends ExploreInfo {
    private UWCourse uwCourse;

    public UWExploreInfo(String title, String description, UWCourse uwCourse) {
        super(title, description);
        this.uwCourse = uwCourse;
    }

    public UWCourse getUwCourse() {
        return uwCourse;
    }

    @Override
    public String getType() {
        return CODE;
    }
}
