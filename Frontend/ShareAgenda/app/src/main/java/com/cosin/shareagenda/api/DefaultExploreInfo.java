package com.cosin.shareagenda.api;

import com.cosin.shareagenda.api.plugin.ExploreInfo;

import types.Event;

public class DefaultExploreInfo extends ExploreInfo {
    public static final String DEFAULT_TYPE = "DEFAULT";
    private Event event;

    public DefaultExploreInfo(String title, String description, Event event) {
        super(title, description);
        this.event = event;
    }

    public Event getEvent() {
        return event;
    }

    @Override
    public String getType() {
        return DEFAULT_TYPE;
    }
}
