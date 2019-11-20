package cn.adminzero.helloword.CommonClass;

public class SignInRequest {
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
