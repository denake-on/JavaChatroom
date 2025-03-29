package com.ly.chatroom.client.service;

import com.ly.chatroom.commom.Message;
import com.ly.chatroom.commom.Message_State;
import com.ly.chatroom.commom.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

//该类完成用户登陆验证和用户注册功能
public class UserClient_service {
    private User u = new User();//先要有一个准备验证的对象
    private Socket socket;//客户端所绑定的socket对象

    public boolean check_users(String id, String password) {
        boolean flag = false;
        u.setId(id);
        u.setPassword(password);
        //先检验一下客户端传进去成功的没有，成功了，这里应该是没错的
//        System.out.println("用户的id是"+u.getId());
//        System.out.println("用户的密码是"+u.getPassword());
//        把用户在登陆界面输入的id和密码设置成此服务中user的id和密码
//        再把这个user对象发送给服务端进行检验

        try {
            socket = new Socket(InetAddress.getByName("127.0.0.1"), 9999);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(u);
          //  把user对象发送过去之后，服务端回复message-state
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            Message message = (Message) objectInputStream.readObject();
            if ((message.getMessage_type()).equals(Message_State.Message_successful)) {
                flag = true;
//                此时代表用户登录成功
//                一旦成功，启用线程持有socket
                Client_connact_Server ccs = new Client_connact_Server(socket);
                ccs.start();//start方法用来启动这个线程，run方法用来定义线程需要执行的任务
//              客户端可能会启用多个线程，因此我们将线程放进管理线程的集合中
                Manage_Thread.add_thread(id, ccs);
            } else {
//               此时用户登录失败
                socket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }
    //此用法用来查询在线好友
    public void onlinefriendlist(){
        //发送一个message用来查询
        Message message = new Message();
        message.setSender(u.getId());
        message.setMessage_type("4");
        //得到当前对象的socket，把个message发送给服务端
//        当前对象的socket在线程中和id绑定在一起，线程又放在管理线程的集合中
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                    Manage_Thread.get_thread(u.getId()).getSocket().getOutputStream());
            objectOutputStream.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    public void log_out(){
        //退出客户端，并给服务端发送一个退出系统的MEssage对象
        Message message = new Message();
        message.setMessage_type(Message_State.Message_Client_Exit);
        message.setSender(u.getId());
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(message);
            System.out.println(u.getId()+" logs out");
            objectOutputStream.close();
            socket.close();
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
