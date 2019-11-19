package Server;

import cn.adminzero.helloword.CommonClass.SignInRequest;
import cn.adminzero.helloword.CommonClass.SignUpRequest;
import cn.adminzero.helloword.CommonClass.UserNoPassword;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ServerDbutil {
    static SqlConnection sqlConnection=new SqlConnection();
    static Connection conn=sqlConnection.conn;

    public static UserNoPassword signup(SignUpRequest sur){
        UserNoPassword userNoPassword = new UserNoPassword(-1,sur.getNickName(),sur.getEmail());
        //TODO：数据库处理注册
        String email=sur.getEmail();
        String user_name=sur.getNickName();
        String password= sur.getPassword();
        int user_id=-1;
        //  userNoPassword.setValid(false);

        Statement stmt = null;
        try {
            stmt = conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String sql_qurey= "SELECT user_id，email FROM user";
        ResultSet rs = null;
        try {
            rs = stmt.executeQuery(sql_qurey);
            // 展开结果集数据库
            while(rs.next()){
                // 通过字段检索,搜索到最后一个字段
                user_id  = rs.getInt("user_id");
                if(email.equals(rs.getString("email"))){
                    //已经存在这个邮箱名了
                    userNoPassword.setValid(false);
                }
            }
            if(user_id==-1){
                user_id=10000;
            }
            else{
                user_id=user_id+1;
                userNoPassword.setUserID(user_id);
            }
            if(userNoPassword.isValid()) {
                //如果合法 就插入该用户数据
                stmt.execute("insert into user(user_id,user_name,password,email) values(?,?,?,?)", new String[]{user_id + "", user_name, password, email});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // 完成后关闭数据库链接
        try {
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        sqlConnection.SqlConnectionClose();
        return userNoPassword;
    }

    public UserNoPassword signup(SignInRequest sur){
        UserNoPassword userNoPassword = new UserNoPassword(sur.getEmail());
        //TODO：数据库处理注册
        String email=sur.getEmail();
        String password= sur.getPassword();
        int user_id=-1;
        //  userNoPassword.setValid(false);

        Statement stmt = null;
        try {
            stmt =conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String sql_qurey= "SELECT * FROM user WHERE email="+email;
        ResultSet rs = null;
        try {
            rs = stmt.executeQuery(sql_qurey);
            // 展开结果集数据库
            while(rs.next()){
                // 通过字段检索,搜索到最后一个字段
                if(password.equals(rs.getString("password"))){
                    //密码正确
                   // password=rs.getString("password");
                    user_id= rs.getInt("user_id");
                    userNoPassword.UserNoPassword(
                            rs.getInt("user_id"),
                            rs.getString("user_name"),
                            rs.getString("email"),
                            rs.getString("avatar"),
                            rs.getInt("goal"),
                            rs.getInt("days"),
                            rs.getInt("group_id"),
                            rs.getInt("level"),
                            rs.getInt("points")
                            );

                }

            }
            if(user_id==-1){
                //没有此邮箱地址
                userNoPassword.setValid(false);
                System.out.println("没有此用户！");
            }
            else if(!password.equals(rs.getString("password"))){
                //密码错误
                userNoPassword.setValid(false);
                System.out.println("密码错误！");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // 完成后关闭数据库链接
        try {
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        sqlConnection.SqlConnectionClose();
        return userNoPassword;
    }



}
