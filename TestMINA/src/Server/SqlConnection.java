package Server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SqlConnection {

    private static  String URL = "jdbc:mysql://localhost:3306/HelloWord";
    private static  String NAME = "root";
    private static  String PASSWORD = "123456";
    public  Connection conn = null;
    // public  Statement stmt = null;


    public void TheSqlConnection(){
        //加载驱动
        try{
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e){
            System.out.println("未能成功加载驱动程序,请检查是否导入驱动程序!");
            e.printStackTrace();
        }

        try{
            conn = DriverManager.getConnection(URL, NAME, PASSWORD);
            System.out.println("获取数据库链接成功");
        }catch (SQLException e){
            System.out.println("获取数据库连接失败");
            e.printStackTrace();
        }

        //数据库打开后要关闭
        if(conn != null){
            try {
                conn.close();
            }catch (SQLException e){
                e.printStackTrace();
                conn = null;
            }
        }
    }
}
