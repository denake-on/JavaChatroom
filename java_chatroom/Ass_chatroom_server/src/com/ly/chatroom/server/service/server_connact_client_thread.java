package com.ly.chatroom.server.service;

import com.ly.chatroom.commom.Message;
import com.ly.chatroom.commom.Message_State;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

//该类和客户端始终保持通信
public class server_connact_client_thread extends Thread {
    //    这个线程需要持有socket
    private Socket socket;
    private String id;
    private chat_history ch = new chat_history();

    public chat_history getCh() {
        return ch;
    }

    public void setCh(chat_history ch) {
        this.ch = ch;
    }

    //    来个构造器
    public server_connact_client_thread(String id, Socket socket) {
        this.socket = socket;
        this.id = id;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
//        在后台一直和客户端进行通信，因此使用while循环
        while (true) {
            try {
                System.out.println("stay in touch with" + id);
                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                Message message = (Message) objectInputStream.readObject();
//                根据message的类型，做相应的业务处理
                if (message.getMessage_type().equals(Message_State.Message_get_online_friend)) {
                    System.out.println(message.getSender() + " requests to see the list of online users");
                    String s = Manage_Thread.get_online_users();
                    //现在要返回一个message对象给客户端
                    Message message1 = new Message();
                    message1.setMessage_type(Message_State.Message_return_online_friend);
                    message1.setContent(s);
                    message1.setReceiver(message.getSender());
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                    objectOutputStream.writeObject(message1);
                } else if (message.getMessage_type().equals(Message_State.Message_Client_Exit)) {
                    //在客户端请求退出时，把线程持有的Socket关闭，并且推出该线程的run方法
                    System.out.println(id + " requests to log out");
                    socket.close();
                    break;
                } else if (message.getMessage_type().equals(Message_State.Message_common)) {
                    //根据Message得到相关的receiver消息，然后用管理线程的集合拿到receiver对应的socket
                    String receiver_id = message.getReceiver();
                    server_connact_client_thread thread = Manage_Thread.get_thread(receiver_id);
                    //现在可以把这个message转发给receiver
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                            thread.getSocket().getOutputStream());
                    objectOutputStream.writeObject(message);//客户不在线可以保存到数据库，这样就可以实现离线留言

                } else if (message.getMessage_type().equals(Message_State.Message_group)) {
                    //这一段处理接收到的群聊消息
                    //把得到的消息添加到集合中
                    ch.add_history(message);//每一个线程都对应单独的ch
                    //把线程都遍历一遍
                    String sender_id = message.getSender();
                    Set<String> keys = Manage_Thread.getThread_set().keySet();
                    for (String key : keys) {
                        server_connact_client_thread thread = Manage_Thread.get_thread(key);
                        ObjectOutputStream objectOutputStream = new ObjectOutputStream(thread.getSocket().getOutputStream());
                        objectOutputStream.writeObject(message);
                    }
                } else if (message.getMessage_type().equals(Message_State.Message_search)) {
                    //遍历所有线程，把每个线程的集合都汇总到一起
                    Set<Message> combine = new HashSet<>();
                    Set<String> keys = Manage_Thread.getThread_set().keySet();
                    for (String key : keys) {
                        server_connact_client_thread thread = Manage_Thread.get_thread(key);
                        combine.addAll(thread.getCh().get_history());
                    }
                    String keyword = message.getContent();
                    boolean flag = false;
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                            Manage_Thread.get_thread(message.getSender()).getSocket().getOutputStream());
                    System.out.println(combine.isEmpty());
                    Message all_in_one = new Message();
                    String s = "";
                    for (Message element : combine) {
                        //如果消息内容/发送人中包含Keyword，则返回对应的消息
                        if (element.getContent().contains(keyword) || element.getSender().contains(keyword)) {
                            flag = true;
                            element.setMessage_type(Message_State.Message_search);
                            s += element.getTime() + " " + element.getSender() + " :" + element.getContent()+"\n";
                        }
                    }
                    if (flag == false) {
                        all_in_one.setContent("empty");
                    }else{
                        all_in_one.setContent(s);
                    }
                    all_in_one.setMessage_type(Message_State.Message_search);
                    objectOutputStream.writeObject(all_in_one);
                }else if(message.getMessage_type().equals(Message_State.Message_path)){
                    Set<Message> combine = new HashSet<>();
                    Set<String> keys = Manage_Thread.getThread_set().keySet();
                    for (String key : keys) {
                        server_connact_client_thread thread = Manage_Thread.get_thread(key);
                        combine.addAll(thread.getCh().get_history());
                    }
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                            Manage_Thread.get_thread(message.getSender()).getSocket().getOutputStream());
                    Message all_in_one = new Message();
                    all_in_one.setHistory(combine);//讨论空集的情况
                    all_in_one.setContent(message.getContent());
                    all_in_one.setMessage_type(Message_State.Message_path);
                    objectOutputStream.writeObject(all_in_one);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
