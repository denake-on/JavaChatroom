package com.ly.chatroom.commom;

import java.io.Serializable;

public class User implements Serializable{
    private static final long serialVersionUID = 1L;
    //    基本属性是用户id和密码
//    被读取时希望通过对象流的方式来读取，因此要进行序列化
    private String id;
    private String password;
    public User(){}
    public User(String id,String password){
        this.id = id;
        this.password = password;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
