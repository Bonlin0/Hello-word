package Server;

import Common.CMDDef;
import Common.Message;
import Common.Utils.SendMsgMethod;
import cn.adminzero.helloword.CommonClass.SignUpRequest;
import cn.adminzero.helloword.CommonClass.UserNoPassword;
import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.FilterEvent;

import java.sql.ResultSet;
import java.sql.Statement;

/**
 * @author: 王翔
 * @date: 2019/11/7-20:28
 * @description: <br>
 * 业务逻辑处理代码，也是后续添加交互需要重点关注的代码
 * <EndDescription>
 */
public class ServerHandle extends IoHandlerAdapter {
    public static Logger logger = Logger.getLogger(ServerHandle.class);

    public ServerHandle() {
        super();
    }

    @Override
    public void sessionCreated(IoSession session) throws Exception {
        super.sessionCreated(session);
        logger.info("用户连接至服务器");
    }

    @Override
    public void sessionOpened(IoSession session) throws Exception {
        super.sessionOpened(session);
        logger.info("连接被打开");
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        super.sessionClosed(session);
        logger.info("连接被关闭");
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        super.sessionIdle(session, status);
        logger.info("连接被闲置");
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        super.exceptionCaught(session, cause);
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        super.messageReceived(session, message);
        logger.info("收到了数据");
        if (message instanceof Message) {
            Message mes = (Message) message;
            switch (mes.getCMD()) {
                case CMDDef.SIGN_UP_REQUESET:

                    SignUpRequest sur = (SignUpRequest) mes.getObj();
                    UserNoPassword userNoPassword = new UserNoPassword(-1,sur.getNickName(),sur.getEmail());
                    //TODO：数据库处理注册
                    String email=sur.getEmail();
                    String user_name=sur.getNickName();
                    String password= sur.getPassword();
                    int user_id=-1;
                  //  userNoPassword.setValid(false);
                     SqlConnection sqlConnection=new SqlConnection();
                     sqlConnection.TheSqlConnection();
                    Statement stmt = sqlConnection.conn.createStatement();
                    String sql_qurey= "SELECT user_id，email FROM user";
                    ResultSet rs = stmt.executeQuery(sql_qurey);
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
                 /*   String sql_insert="INSERT INTO USER (" +
                            "user_id,user_name,password,email)" +
                            " values(" +
                            user_id+"" +
                            user_name +
                            password +
                            email +
                            ")";

                  */
                 if(userNoPassword.isValid()) {
                     //如果合法 就插入该用户数据
                     stmt.execute("insert into user(user_id,user_name,password,email) values(?,?,?,?)", new String[]{user_id + "", user_name, password, email});
                 }
                 // 完成后关闭数据库链接
                    rs.close();
                    stmt.close();
                    sqlConnection.conn.close();
                    
                    session.write(SendMsgMethod.getObjectMessage(CMDDef.REPLY_SIGN_UP_REQUEST, userNoPassword));
                    break;
            }
        } else {
            logger.info("未知请求！");
        }
    }

    @Override
    public void messageSent(IoSession session, Object message) throws Exception {
        super.messageSent(session, message);
    }

    @Override
    public void inputClosed(IoSession session) throws Exception {
        super.inputClosed(session);
    }

    @Override
    public void event(IoSession session, FilterEvent event) throws Exception {
        super.event(session, event);
    }
}
