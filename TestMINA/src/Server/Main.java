package Server;

import Common.*;

import DB.GlobalConn;
import DB.UserInformation;
import cn.adminzero.helloword.CommonClass.UserNoPassword;
import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.LogLevel;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import java.net.InetSocketAddress;

import static DB.ServerDbutil.*;


public class Main {
    private static Logger logger = Logger.getLogger(Main.class);
    private static int PORT = CMDDef.PORT;
    public static void main(String[] args) {
        IoAcceptor acceptor = null;
        try {
            acceptor = new NioSocketAcceptor();
            LoggingFilter lf = new LoggingFilter();
            lf.setMessageReceivedLogLevel(LogLevel.DEBUG);
            acceptor.getFilterChain().addLast("logger", lf);
            acceptor.getFilterChain().addLast(
                    "codec",
                    new ProtocolCodecFilter(
                            new MessageCodecFactory(new Decoder(), new Encoder())
                    )
            );
            // 读写通道10秒内无操作进入空闲状态
            acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 50);
            // 绑定逻辑处理器
            acceptor.setHandler(new ServerHandle()); // 添加业务处理
            acceptor.bind(new InetSocketAddress(PORT));
            logger.info("服务端启动成功...     端口号为：" + PORT);
            GlobalConn.initDBConnection();

//            // 获取除了密码外的所有信息
//            UserNoPassword userNoPassword=getUserNopassword(10062);
//            //修改用户信息
//            userNoPassword.setUserNickName("test修改");
//            update_USER(userNoPassword);
//            //获取用户所有信息（包括密码）
//            UserInformation userInformation=getUser(10062);
//            System.out.println("user_name:"+userInformation.getUserNickName()+"\n"+"email:"+userInformation.getEmail());

         // initDataBase();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}