package cn.adminzero.helloword.CommonClass;

import java.io.Serializable;

public class SignInRequest implements Serializable {
    private final long serialVersionUID = 1L;
    String email="";
    String password="";
    public  SignInRequest(String email,String password){
        this.email=email;
        this.password=password;
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
}
