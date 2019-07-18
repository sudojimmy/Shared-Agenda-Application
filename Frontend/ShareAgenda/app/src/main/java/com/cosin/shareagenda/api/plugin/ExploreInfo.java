package com.cosin.shareagenda.api.plugin;

public abstract class ExploreInfo {
    private String title;
    private String description;

    public ExploreInfo(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public abstract String getType();
}
