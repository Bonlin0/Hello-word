package DB;

import Common.CMDDef;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author: 王翔
 * @date: 2019/11/20-19:14
 * @description: <br>
 * <EndDescription>
 */
public class GlobalConn {
    private static Connection conn = null;
    private static Logger logger = Logger.getLogger(GlobalConn.class);

    public static synchronized void closeconn() throws SQLException {
        if (conn == null) {
            logger.info("数据库连接已被关闭!");
        } else {
            conn.close();
            conn = null;
        }
    }

    public static synchronized void initDBConnection() throws ClassNotFoundException {
        /**
         1、MySQL 8.0 以上版本驱动包版本 mysql-connector-java-8.0.16.jar。
         2、com.mysql.jdbc.Driver 更换为 com.mysql.cj.jdbc.Driver。
         MySQL 8.0 以上版本不需要建立 SSL 连接的，需要显示关闭。
         最后还需要设置 CST。
         */
        //
        try {
            // Class.forName("com.mysql.cj.jdbc.Driver");
            new com.mysql.cj.jdbc.Driver();
          //  Class.forName(CMDDef.DBDriver);
            conn = DriverManager.getConnection(CMDDef.DBURL, CMDDef.DBNAME, CMDDef.DBPASSWD);
        } catch (SQLException e) {
            logger.info("数据库连接失败!");
        }

    }

    public static Connection getConn() {
        return conn;
    }
}
