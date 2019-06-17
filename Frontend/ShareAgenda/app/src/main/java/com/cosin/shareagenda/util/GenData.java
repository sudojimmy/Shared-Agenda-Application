package com.cosin.shareagenda.util;

import com.cosin.shareagenda.entity.EventEntity;
import com.cosin.shareagenda.entity.FriendEvent;
import com.cosin.shareagenda.entity.FriendEventItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GenData {
    public static List<EventEntity> putEventList() {
        List<EventEntity> eventList = new ArrayList<>();
        for (int i = 36; i < 42; i++) {
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
        for (int i = 61; i < 85; i++) {
            eventList.add(new EventEntity(i));
        }

        return eventList;
    }

    public static void send(String msg) {
        /*
        Map<String, String> paramters;
        OkHttpUtils
                .post()
                .url("")
                .params(paramters)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(android.telecom.Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        callback.execute(response);
                    }
                });
        */
    }

    public static List<FriendEvent> getFriendsEvents() {
        List<FriendEvent> aryFriendsEvent = new ArrayList<>();
        List<FriendEventItem> lst = new ArrayList<>();
        lst.add(new FriendEventItem(40,2));
        lst.add(new FriendEventItem(46,2));
        lst.add(new FriendEventItem(51,1));
        aryFriendsEvent.add(new FriendEvent("joe", "21", lst));
        List<FriendEventItem> lst2 = new ArrayList<>();
        lst2.add(new FriendEventItem(41,2));
        lst2.add(new FriendEventItem(44,2));
        lst2.add(new FriendEventItem(54,1));
        aryFriendsEvent.add(new FriendEvent("keli", "21", lst2));
        List<FriendEventItem> lst3 = new ArrayList<>();
        lst3.add(new FriendEventItem(42,2));
        lst3.add(new FriendEventItem(45,3));
        lst3.add(new FriendEventItem(55,1));
        aryFriendsEvent.add(new FriendEvent("salingulas", "21", lst3));
        List<FriendEventItem> lst4 = new ArrayList<>();
        lst4.add(new FriendEventItem(45,2));
        lst4.add(new FriendEventItem(49,2));
        lst4.add(new FriendEventItem(53,1));
        aryFriendsEvent.add(new FriendEvent("peter", "21", lst4));
        List<FriendEventItem> lst5 = new ArrayList<>();
        lst5.add(new FriendEventItem(48,1));
        lst5.add(new FriendEventItem(51,2));
        lst5.add(new FriendEventItem(54,1));
        aryFriendsEvent.add(new FriendEvent("tom", "21", lst5));
        return aryFriendsEvent;
    }
}
