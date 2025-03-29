package com.ly.chatroom.server.service;

import com.ly.chatroom.commom.Message;
import com.ly.chatroom.commom.User;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class server {
//    因为在thread中还需要用到Serversocket，所以把它写成属性
    private ServerSocket serverSocket = null;

    //    在监听9999端口，等待客户端的连接，验证用户合法性，并保持通信
    public server(){
        System.out.println("The server is listening on port 9999.");
        try {
            serverSocket = new ServerSocket(9999);
//            持续性监听
            while(true){
                Socket accept = serverSocket.accept();//没有客户端连接就会阻塞在此
                System.out.println("Connected successfully");
                //现在要拿到socket对象相关的输入流
                ObjectInputStream objectInputStream = new ObjectInputStream(accept.getInputStream());
//                服务端在userclient_serivic连接到9999端口，先发送一个User对象
//                把此对象输入流向下转型User
                User u = (User) objectInputStream.readObject();
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(accept.getOutputStream());
                Message message = new Message();
                if((u.getPassword()).equals("00000000")){
//                    向客户端发送message对象，其中，messageSTATE为“1”
                    message.setMessage_type("1");
                    objectOutputStream.writeObject(message);
//                    创建一个线程，和客户端保持通信
                    server_connact_client_thread serverConnactClientThread = new server_connact_client_thread(u.getId(), accept);
                    serverConnactClientThread.start();//启动线程
                    //把线程对象放入集合中管理
                    Manage_Thread.add_thread(u.getId(),serverConnactClientThread);
                }else{
//                    如果客户不合法，默认密码不正确则发送“2”
                    message.setMessage_type("2");
                    objectOutputStream.writeObject(message);
                    //如果登录失败，则socket已经没用了，此时关闭socket
                    accept.close();
                }
//                在这里不能close相关的流，不然服务器端会报错“Socket is closed”
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            try {
                serverSocket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
