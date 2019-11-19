package Server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SqlConnection {

    private static String URL = "jdbc:mysql://123.207.173.192:3306/hello_word?useUnicode=true&useSSL=false&serverTimezone=UTC&autoReconnect=true&failOverReadOnly=false";
 //  private static String URL = "jdbc:mysql://localhost:3306/helloword?useUnicode=true&useSSL=false&serverTimezone=UTC&autoReconnect=true&failOverReadOnly=false";
 // private static String URL = "jdbc:mysql://localhost:3306/helloword?useSSL=false&serverTimezone=UTC";

    private static String NAME = "root";
    private static String PASSWORD = "926b50985a";
  // private static String PASSWORD = "123456";
    public static Connection conn = null;
    // public  Statement stmt = null;


    public void TheSqlConnection() {
        //加载驱动
        try {
            /**
             1、MySQL 8.0 以上版本驱动包版本 mysql-connector-java-8.0.16.jar。
             2、com.mysql.jdbc.Driver 更换为 com.mysql.cj.jdbc.Driver。
             MySQL 8.0 以上版本不需要建立 SSL 连接的，需要显示关闭。
             最后还需要设置 CST。
             */
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("加载驱动成功！");
        } catch (ClassNotFoundException e) {
            System.out.println("未能成功加载驱动程序,请检查是否导入驱动程序!");
            e.printStackTrace();
        }
        try {
            conn = DriverManager.getConnection(URL, NAME, PASSWORD);
            System.out.println("获取数据库链接成功");
        } catch (SQLException e) {
            System.out.println("获取数据库连接失败");
            e.printStackTrace();
        }
    }

    public void SqlConnectionClose() {
        //数据库打开后要关闭
        if (conn != null) {
            try {
                conn.close();
                System.out.println("数据库连接关闭");
            } catch (SQLException e) {
                e.printStackTrace();
                conn = null;
            }
        }

    }

}

