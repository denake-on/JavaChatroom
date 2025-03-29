package com.ly.chatroom.client.service;

import com.ly.chatroom.client.version.InputUtility;
import com.ly.chatroom.commom.Message;
import com.ly.chatroom.commom.Message_State;

import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class Client_connact_Server extends Thread {

    //   这个线程要持有socket
    private Socket socket;

    public Client_connact_Server(Socket socket) {
        this.socket = socket;
    }

    public Socket getSocket() {
        return socket;
    }


    //    这个线程要实现thread中的run方法
    public void run() {
//        因为thread需要一直在后台和服务器进行通信，因此我们使用While循环
        while (true) {
            try {
                //System.out.println("waiting for the message");
                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                Message message = (Message) objectInputStream.readObject();//这里是向下转型，需要使用强转
//                如果message没有发送信息，线程会阻塞在这里，拿到message对象之后就可以进行处理
//                当服务端发送message对象时，根据其类型做出相应的业务处理
                if (message.getMessage_type().equals(Message_State.Message_return_online_friend)) {
//                    取出在线用户信息,规定返回内容为不同的用户id中间用空格隔开
                    String[] online_users = message.getContent().split(" ");
                    System.out.println("here is the list of the online users:");
                    for (int i = 0; i < online_users.length; i++) {
                        System.out.println("user: " + online_users[i]);
                    }
                } else if (message.getMessage_type().equals(Message_State.Message_common)) {
//                    接收到的为普通的聊天消息如何解码
                    System.out.println(
                            "\n" + message.getTime() + " " + message.getSender() + " to " + message.getReceiver() + " : " + message.getContent());
                } else if (message.getMessage_type().equals(Message_State.Message_group)) {
                    //接收到的为群聊消息，进行解码
                    System.out.println("\n" + message.getTime());
                    System.out.println(message.getSender() + "(have sent " + message.getCount_group_message() + " messages)" + " to all: " + message.getContent());
                    ;
                } else if (message.getMessage_type().equals(Message_State.Message_search)) {
                    System.out.println();
                    if (message.getContent().equals("empty")) {
                        System.out.println("no message found that meets the requirements!");
                    } else {
                        System.out.println("here is the message meets the requirement:");
                        System.out.println(message.getContent());
                    }
                }else if(message.getMessage_type().equals(Message_State.Message_path)){
                    String path = message.getContent();
//                    遍历Message的集合得到字符串
                    Set<Message> message_history = new HashSet<>();
                    message_history = message.getHistory();
                    String s = "";
                    for (Message element : message_history) {
                        s += element.getTime() + " " + element.getSender() + " :" + element.getContent()+"\n";
                    }
                    FileWriter fileWriter = new FileWriter(path);
                    fileWriter.write(s);
                    fileWriter.flush();
                    System.out.println(s);
                    System.out.println("saved.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
