package com.cosin.shareagenda.util;

import com.cosin.shareagenda.config.SystemConfig;
import com.cosin.shareagenda.entity.ContactEntity;
import com.cosin.shareagenda.entity.EventEntity;
import com.cosin.shareagenda.entity.FriendEvent;
import com.cosin.shareagenda.entity.GroupEntity;
import com.cosin.shareagenda.entity.SimpleEventEntity;
import com.cosin.shareagenda.entity.UserEntity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GenData {
    public static List<EventEntity> putEventList() {
        List<EventEntity> eventList = new ArrayList<>();
        for (int i = SystemConfig.SATRT_QAURTER; i < 42; i++) {
            eventList.add(new EventEntity(i));
        }
        eventList.add(new EventEntity(3, 1, 42, 1, "Standing", "KKK"));
        for (int i = 43; i < 48; i++) {
            eventList.add(new EventEntity(i));
        }
        eventList.add(new EventEntity(4, 1, 48, 3, "Lunch Meeting", "AAAA"));
        for (int i = 51; i < 53; i++) {
            eventList.add(new EventEntity(i));
        }
        eventList.add(new EventEntity(4, 1, 53, 1, "Testing Small1", "SSS1"));
        eventList.add(new EventEntity(4, 1, 54, 1, "Testing Small2", "SSS2"));
        for (int i = 55; i < 58; i++) {
            eventList.add(new EventEntity(i));
        }
        eventList.add(new EventEntity(4, 1, 58, 3, "Management Course", "CCCC"));
        for (int i = 61; i <= SystemConfig.END_QUARTER; i++) {
            eventList.add(new EventEntity(i));
        }
        return eventList;
    }

    public static List<EventEntity> getEventList() {
        List<EventEntity> eventList = new ArrayList<>();
        for (int i = SystemConfig.SATRT_QAURTER; i < 40; i++) {
            eventList.add(new EventEntity(i));
        }
        eventList.add(new EventEntity(3, 1, 40, 2, "YYYY", "KKK"));
        for (int i = 42; i < 46; i++) {
            eventList.add(new EventEntity(i));
        }
        eventList.add(new EventEntity(4, 1, 46, 3, "Lunch Meeting", "AAAA"));
        for (int i = 49; i < 54; i++) {
            eventList.add(new EventEntity(i));
        }
        eventList.add(new EventEntity(4, 1, 54, 2, "Testing Small1", "SSS1"));
        eventList.add(new EventEntity(4, 1, 56, 1, "Testing Small2", "SSS2"));
        for (int i = 57; i < 60; i++) {
            eventList.add(new EventEntity(i));
        }
        eventList.add(new EventEntity(4, 1, 60, 3, "Management Course", "CCCC"));
        for (int i = 63; i <= SystemConfig.END_QUARTER; i++) {
            eventList.add(new EventEntity(i));
        }
        return eventList;
    }

    public static List<FriendEvent> getFriendsEvents() {
        List<FriendEvent> aryFriendsEvent = new ArrayList<>();
        List<SimpleEventEntity> lst = new ArrayList<>();
        lst.add(new SimpleEventEntity(40,2,""));
        lst.add(new SimpleEventEntity(46,2,""));
        lst.add(new SimpleEventEntity(51,1,""));
        aryFriendsEvent.add(new FriendEvent(new Date(2019,5,21),"joe", lst));
        lst = new ArrayList<>();
        lst.add(new SimpleEventEntity(41,2,""));
        lst.add(new SimpleEventEntity(44,2,""));
        lst.add(new SimpleEventEntity(54,1,""));
        aryFriendsEvent.add(new FriendEvent(new Date(2019,5,21),"keli", lst));
        lst = new ArrayList<>();
        lst.add(new SimpleEventEntity(42,2,""));
        lst.add(new SimpleEventEntity(45,3,""));
        lst.add(new SimpleEventEntity(55,1,""));
        aryFriendsEvent.add(new FriendEvent(new Date(2019,5,21),"salingulas", lst));
        lst = new ArrayList<>();
        lst.add(new SimpleEventEntity(45,2,""));
        lst.add(new SimpleEventEntity(49,2,""));
        lst.add(new SimpleEventEntity(53,1,""));
        aryFriendsEvent.add(new FriendEvent(new Date(2019,5,21),"peter", lst));
        lst = new ArrayList<>();
        lst.add(new SimpleEventEntity(48,1,""));
        lst.add(new SimpleEventEntity(51,2,""));
        lst.add(new SimpleEventEntity(54,1,""));
        aryFriendsEvent.add(new FriendEvent(new Date(2019,5,21),"tom",lst));
        return aryFriendsEvent;
    }

    public static List<FriendEvent> getWeeklyEvents() {
        List<FriendEvent> aryFriendsEvent = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("d/M/yyyy");
        try {
            List<SimpleEventEntity> lst = new ArrayList<>();
            lst.add(new SimpleEventEntity(42, 2, "Math"));
            lst.add(new SimpleEventEntity(51, 2, "Physical"));
            lst.add(new SimpleEventEntity(54, 1, "Flying"));
            aryFriendsEvent.add(new FriendEvent(sdf.parse("23/6/2019"), "Sun", lst));
            lst = new ArrayList<>();
            lst.add(new SimpleEventEntity(43, 2, "Fatti"));
            lst.add(new SimpleEventEntity(46, 2, "Pand"));
            lst.add(new SimpleEventEntity(51, 1, "Oak"));
            aryFriendsEvent.add(new FriendEvent(sdf.parse("24/6/2019"), "Mon", lst));
            lst= new ArrayList<>();
            lst.add(new SimpleEventEntity(40, 2, "Yoo"));
            lst.add(new SimpleEventEntity(44, 2, "Paper"));
            lst.add(new SimpleEventEntity(54, 1, "Turnkey"));
            aryFriendsEvent.add(new FriendEvent(sdf.parse("25/6/2019"), "Tue", lst));
            lst = new ArrayList<>();
            lst.add(new SimpleEventEntity(41, 3, "Proportion"));
            lst.add(new SimpleEventEntity(45, 3, "Statics"));
            lst.add(new SimpleEventEntity(55, 1, "Gour"));
            aryFriendsEvent.add(new FriendEvent(sdf.parse("26/6/2019"), "Wed", lst));
            lst = new ArrayList<>();
            lst.add(new SimpleEventEntity(43, 2, "Ramam"));
            lst.add(new SimpleEventEntity(49, 2, "Labin"));
            lst.add(new SimpleEventEntity(53, 1, "Windows"));
            aryFriendsEvent.add(new FriendEvent(sdf.parse("27/6/2019"), "Thur", lst));
            lst = new ArrayList<>();
            lst.add(new SimpleEventEntity(45, 1, "Sorray"));
            lst.add(new SimpleEventEntity(51, 2, "Nano"));
            lst.add(new SimpleEventEntity(54, 1, "Vite"));
            aryFriendsEvent.add(new FriendEvent(sdf.parse("28/6/2019"), "Fri", lst));
            lst = new ArrayList<>();
            lst.add(new SimpleEventEntity(42, 1, "Zind"));
            lst.add(new SimpleEventEntity(51, 2, "Kirkir"));
            lst.add(new SimpleEventEntity(54, 1, "David"));
            aryFriendsEvent.add(new FriendEvent(sdf.parse("29/6/2019"), "Sat", lst));
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
        return aryFriendsEvent;
    }

    // need cache
    public static List<FriendEvent> getNextWeeklyEvents() {
        List<FriendEvent> aryFriendsEvent = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("d/M/yyyy");
        try {
            List<SimpleEventEntity> lst = new ArrayList<>();
            lst.add(new SimpleEventEntity(48, 1, "Math"));
            lst.add(new SimpleEventEntity(51, 2, "Physical"));
            lst.add(new SimpleEventEntity(54, 4, "Flying"));
            aryFriendsEvent.add(new FriendEvent(sdf.parse("30/6/2019"), "Sun", lst));
            lst = new ArrayList<>();
            lst.add(new SimpleEventEntity(40, 2, "Fatti"));
            lst.add(new SimpleEventEntity(46, 2, "Pand"));
            lst.add(new SimpleEventEntity(51, 1, "Oak"));
            aryFriendsEvent.add(new FriendEvent(sdf.parse("1/7/2019"), "Mon", lst));
            lst = new ArrayList<>();
            lst.add(new SimpleEventEntity(41, 2, "Yoo"));
            lst.add(new SimpleEventEntity(44, 2, "Statics"));
            lst.add(new SimpleEventEntity(54, 1, "Turkey"));
            aryFriendsEvent.add(new FriendEvent(sdf.parse("2/7/2019"), "Tue", lst));
            lst = new ArrayList<>();
            lst.add(new SimpleEventEntity(42, 2, "Proportion"));
            lst.add(new SimpleEventEntity(45, 3, "WWWert"));
            lst.add(new SimpleEventEntity(55, 1, "Gour"));
            aryFriendsEvent.add(new FriendEvent(sdf.parse("3/7/2019"), "Wed", lst));
            lst = new ArrayList<>();
            lst.add(new SimpleEventEntity(45, 2, "Ramam"));
            lst.add(new SimpleEventEntity(49, 2, "Labin"));
            lst.add(new SimpleEventEntity(53, 1, "Windows"));
            aryFriendsEvent.add(new FriendEvent(sdf.parse("4/7/2019"), "Thur", lst));
            lst = new ArrayList<>();
            lst.add(new SimpleEventEntity(48, 1, "Sorray"));
            lst.add(new SimpleEventEntity(51, 2, "Nano"));
            lst.add(new SimpleEventEntity(54, 1, "Vite"));
            aryFriendsEvent.add(new FriendEvent(sdf.parse("5/7/2019"), "Fri", lst));
            lst = new ArrayList<>();
            lst.add(new SimpleEventEntity(42, 5, "Zind"));
            lst.add(new SimpleEventEntity(51, 2, "Kirkir"));
            lst.add(new SimpleEventEntity(54, 1, "David"));
            aryFriendsEvent.add(new FriendEvent(sdf.parse("6/7/2019"), "Sat", lst));
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
        return aryFriendsEvent;
    }

    public static List<UserEntity> loadFriends() {
        List<UserEntity> friends = new ArrayList<>();
        friends.add(new UserEntity("1","Alice Brown"));
        friends.add(new UserEntity("1","Timesfs gdv"));
        friends.add(new UserEntity("1","Latino dsdv"));
        friends.add(new UserEntity("1","Pai Kandou"));
        friends.add(new UserEntity("1","Grace Dgfgf"));
        friends.add(new UserEntity("1","Grace sffdggd"));
        friends.add(new UserEntity("1","Grace itggfdf"));
        friends.add(new UserEntity("1","Grace dxgbfh"));
        friends.add(new UserEntity("1","Grace ecfresgtdtyybhf"));
        friends.add(new UserEntity("1","Grace dfsrvrttt"));
        return friends;
    }

    public static List<ContactEntity> loadGroups() {
        List<ContactEntity> groups= new ArrayList<>();
        groups.add(new GroupEntity("1","Physics"));
        groups.add(new GroupEntity("2","Cosin"));
        groups.add(new GroupEntity("3","Rabin"));
        groups.add(new GroupEntity("4","Titanium"));
        return groups;
    }
}
