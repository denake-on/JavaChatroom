package com.ly.chatroom.client.service;

import java.util.HashMap;

public class Manage_Thread {
//    该类管理client连接到server的线程
//     把多个Hashmap放入一个Hashmap集合，key是用户id,value就是线程
    private static HashMap<String,Client_connact_Server> thread_set = new HashMap<>();
//   通过此方法能够将线程加入到集合中去
    public static void add_thread(String id,Client_connact_Server thread){
        thread_set.put(id,thread);
    }
//    此方法能够通过userid得到对应的线程
    public static Client_connact_Server get_thread(String id){
        return thread_set.get(id);
    }
}
