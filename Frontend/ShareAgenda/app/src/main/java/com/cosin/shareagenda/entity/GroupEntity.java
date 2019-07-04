package com.cosin.shareagenda.entity;

import java.io.Serializable;

public class GroupEntity implements Serializable {
    private String groupId;
    private String groupName;

    public GroupEntity(String groupId, String groupName) {
        this.groupId = groupId;
        this.groupName = groupName;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getGroupName() {
        return groupName;
    }
}
