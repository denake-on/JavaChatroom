package com.ly.chatroom.commom;

public interface Message_State {
    String Message_successful = "1";
    String Message_failed = "2";
    String Message_common = "3";//普通信息包
    String Message_get_online_friend = "4";//要求返回在线用户列表
    String Message_return_online_friend = "5";//返回在线用户列表的结果
    String Message_Client_Exit = "6";//客户端请求退出
    String Message_group = "7";//客户端发送过来的消息是群发消息类型
    String Message_search = "8";//客户端请求搜索聊天记录
    String Message_path = "9";//客户端请求保存聊天记录到本地

}
