package com.ly.chatroom.client.service;

import com.ly.chatroom.commom.Message;
import com.ly.chatroom.commom.Message_State;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class private_group_Message {
    public int count = 0;
    //该类提供和私聊/群聊相关的服务方法
    public void private_talk(String sender, String receiver, String content) {
        //此方法用来发送私聊信息
        Message message = new Message();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setContent(content);
        //认为发送的消息是普通的信息包
        message.setMessage_type(Message_State.Message_common);
        message.setTime(new java.util.Date().toString());
        System.out.println("\n"
                + message.getTime() + " " + message.getSender() + " to " + message.getReceiver() + " : " + message.getContent());
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                    Manage_Thread.get_thread(sender).getSocket().getOutputStream());
            objectOutputStream.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void group_talk(String sender, String content) {
        //此方法用来发送群聊消息
        count++;
        Message message = new Message();
        message.setSender(sender);
        message.setContent(content);
        message.setMessage_type(Message_State.Message_group);
        message.setTime(new java.util.Date().toString());
        message.setCount_group_message(count);
        //把Message发送给服务端
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                    Manage_Thread.get_thread(sender).getSocket().getOutputStream());
            objectOutputStream.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void search(String sender,String keyword){
        //此方法用来查找聊天记录
        Message message = new Message();
        message.setSender(sender);
        message.setMessage_type(Message_State.Message_search);
        message.setContent(keyword);
        //把message发送给服务端
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                    Manage_Thread.get_thread(sender).getSocket().getOutputStream());
            objectOutputStream.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void save_chatting_history(String sender,String path){
        Message message = new Message();
        message.setContent(path);
        message.setSender(sender);
        message.setMessage_type(Message_State.Message_path);
        //把message发送给服务端
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                    Manage_Thread.get_thread(sender).getSocket().getOutputStream());
            objectOutputStream.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
