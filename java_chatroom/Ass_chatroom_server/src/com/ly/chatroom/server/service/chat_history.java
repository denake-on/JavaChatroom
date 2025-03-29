package com.ly.chatroom.server.service;

import com.ly.chatroom.commom.Message;

import java.util.HashSet;
import java.util.Set;

public class chat_history {
    //此方法用来储存聊天记录
    private Set<Message> chat_history_set = new HashSet<>();

    public void add_history(Message message) {
        //此方法用于将聊天记录添加到集合中
        chat_history_set.add(message);
        System.out.println("add message to history" + message.getSender() + " " + message.getContent());
    }

    public Set<Message> get_history() {
        return chat_history_set;
    }

}
