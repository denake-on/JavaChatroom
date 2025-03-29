package com.ly.chatroom.server.service;

import java.util.HashMap;
import java.util.Set;

public class Manage_Thread {
    private static HashMap<String, server_connact_client_thread> thread_set = new HashMap<>();

    public static HashMap<String, server_connact_client_thread> getThread_set() {
        return thread_set;
    }


    public static void add_thread(String id, server_connact_client_thread thread) {
        thread_set.put(id, thread);
    }

    public static server_connact_client_thread get_thread(String id) {
        return thread_set.get(id);
    }

    public static String get_online_users() {
        String online_users = "";
        Set<String> keys = thread_set.keySet();
        for (String key : keys) {
//            key 是循环变量，它在每次迭代中会自动被赋予集合中的下一个元素的值；keys 是要遍历的集合
            online_users += key;
            online_users += " ";
        }
        return online_users;
    }
    //此方法用来移除thread
    public static void remove_thread(String id){
        thread_set.remove(id);
    }
}
