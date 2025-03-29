package com.ly.chatroom.commom;

import java.io.Serializable;
import java.util.Set;

// 表示客户端和服务器端通信使得消息对象
public class Message implements Serializable {
//接收消息的对象
    private String sender;
    private String receiver;
//    消息的内容
    private String content;
//    发送消息的时间
    private String time;
//    发送消息的类型【可以在借口中定义消息类型】
    private String Message_State;
    private Set history;//保存聊天记录会返回一个全是message的集合

    public Set getHistory() {
        return history;
    }

    public void setHistory(Set history) {
        this.history = history;
    }

    public int getCount_group_message() {
        return count_group_message;
    }

    public void setCount_group_message(int count_group_message) {
        this.count_group_message = count_group_message;
    }

    private int count_group_message;



    public String getMessage_type() {
        return Message_State;
    }

    public void setMessage_type(String message_type) {
        this.Message_State = message_type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
