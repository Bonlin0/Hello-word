package cn.adminzero.helloword.CommonClass;

import java.io.Serializable;

// Author : whl
// Date : 2019/11/17
// 用于存储注册请求的类，由用户在注册时构建并发往服务器

public class SignUpRequest implements Serializable {
    private final long serialVersionUID = 1L;
    String email;
    String password;
    String user_name;

    public SignUpRequest(String eamil, String password, String user_name)
    {
        this.email = eamil;
        this.user_name = user_name;
        this.password = password;
    }

    public long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickName() {
        return user_name;
    }

    public void setNickName(String user_name) {
        this.user_name = user_name;
    }
}
