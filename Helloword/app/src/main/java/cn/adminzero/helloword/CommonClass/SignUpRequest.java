package cn.adminzero.helloword.CommonClass;

import java.io.Serializable;

// Author : whl
// Date : 2019/11/17
// 用于存储注册请求的类，由用户在注册时构建并发往服务器

public class SignUpRequest implements Serializable {
    String email;
    String password;
    String nickName;

    SignUpRequest(String eamil, String password, String nickName)
    {
        this.email = eamil;
        this.nickName = nickName;
        this.password = password;
    }
}
