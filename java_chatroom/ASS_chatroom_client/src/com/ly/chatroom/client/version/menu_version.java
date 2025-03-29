package com.ly.chatroom.client.version;

import com.ly.chatroom.client.service.Client_connact_Server;
import com.ly.chatroom.client.service.UserClient_service;
import com.ly.chatroom.client.service.private_group_Message;

public class menu_version {
    private boolean choice = true;
    private private_group_Message pgm = new private_group_Message();
    private UserClient_service ucs = new UserClient_service();//对象用于判断用户登录是否成功

    public static void main(String[] args) throws Exception {
        menu_version menuVersion = new menu_version();
        menuVersion.menu_version();
        System.out.println("Client logs out");
        //但其实客户端并没有真正的退出
//        因为主线程退出了，但是和服务器通信的线程并没有退出
//        解决方法：在主线程中调用方法，给服务器端发送一个退出系统的Message，调用System.exit（0）正常人退出
//        为什么要给服务器发送message:服务器其实也是在循环读取数据————读到message时关闭socket并退出线程
    }

    private void menu_version() throws Exception {
        while (choice) {
            //登陆界面如下
            System.out.println("————welcome to chatroom :) ————");
            System.out.println("please enter your choice!");
            System.out.println("1,Log in to the Chatroom");
            System.out.println("0,Log out of the Chatroom");
            String ans = InputUtility.getString();
//            判断用户选择
            switch (ans) {
                case "1":
                    System.out.print("please enter your id:");
                    String id = InputUtility.getString();
                    System.out.print("please enter your password(default password:00000000):");
                    String password = InputUtility.getString();
//                    这里应该到服务端验证此用户是否合法
                    if (ucs.check_users(id, password)) {
//                        登陆成功，此时已经创建一个与id绑定的线程
                        System.out.println("hello," + id + " :) " + new java.util.Date().toString());
                        System.out.println();
                        while (choice) {
                            System.out.println("0,Log out of the Chatroom");
                            System.out.println("1,Check who is online");
                            System.out.println("2,send message in a group");
                            System.out.println("3,send private message");
                            System.out.println("4,search conversation history");
                            System.out.println("5,Save all previous chatting history to the local");
                            System.out.println("please enter your choice!");
                            String ans2 = InputUtility.getString();
                            switch (ans2) {
                                case "0":
                                    System.out.println("bye:)");
//                                    此处调用无异常退出方法
//                                    现象服务端发送一个message
                                    ucs.log_out();
                                    choice = false;
                                    break;
                                case "1":
                                    //在这里调用一个UserClient_service中的方法，用来查询在线列表好友
                                    ucs.onlinefriendlist();
                                    break;
                                case "2":
                                    System.out.println("enter what you want to say in the group:");
                                    String group_content = InputUtility.getString();
                                    //调用一个方法，将消息封装成message对象
                                    pgm.group_talk(id,group_content);
                                    break;
                                case "3":
                                    System.out.println("choose who you want to talk to(online user):");
                                    String receiver_id = InputUtility.getString();
                                    //这里需要完善id必须为在线用户的业务逻辑
                                    System.out.println("enter your message content:");
                                    String message_content = InputUtility.getString();
                                    //编写一个方法把message发送给服务端
                                    pgm.private_talk(id,receiver_id,message_content);
                                    break;
                                case "4":
                                    System.out.println("enter a keyword:");
                                    String keyword = InputUtility.getString();
                                    //调用一个方法，将消息封装成message对象
                                    pgm.search(id,keyword);
                                    break;
                                case"5":
                                    System.out.println("enter the saving path(e.g:/Users/lanjieping/Desktop/chatting_history.txt):");
                                    String path = InputUtility.getString();
                                    //调用一个方法，将消息封装成message对象
                                    pgm.save_chatting_history(id,path);
                                    break;
                                default:
                                    System.out.println("please enter the correct number!");
                            }
                        }
                    } else {
                        System.out.println("login failed :( ");
                    }
                    break;

                case "0":
                    System.out.println("bye:)");
                    choice = false;
                    break;
                default:
                    System.out.println("please enter the correct number!");
            }
        }
    }
}
