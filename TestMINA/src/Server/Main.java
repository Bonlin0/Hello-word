package Server;

import Common.*;

import DB.GlobalConn;
import DB.ServerDbutil;
import Game.Gamer;
import cn.adminzero.helloword.CommonClass.GroupMember;
import cn.adminzero.helloword.CommonClass.MemberItem;
import cn.adminzero.helloword.CommonClass.UserNoPassword;
import cn.adminzero.helloword.CommonClass.WordsLevel;
import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.LogLevel;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import java.net.InetSocketAddress;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static DB.ServerDbutil.*;


public class Main {
    private static Logger logger = Logger.getLogger(Main.class);
    private static int PORT = CMDDef.PORT;
    private static DateFormat dayFormat = new SimpleDateFormat("yy-MM-dd");
    private static DateFormat dateFormat = new SimpleDateFormat("yy-MM-dd HH:mm:ss");

    public static void main(String[] args) {
        //定时器
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
            Gamer.initGamer(acceptor.getManagedSessions());
            //开启随机数产生器的线程
            ScheduledExecutorService service = Executors
                    .newSingleThreadScheduledExecutor();

            //执行10s，每隔50s更新一次随机数表
            service.scheduleAtFixedRate(Gamer.runnable,10,50, TimeUnit.SECONDS);

            long oneDay = 24 * 60 * 60 * 1000;
            long initDelay  = getTimeMillis("23:59:59") - System.currentTimeMillis();
            initDelay = initDelay > 0 ? initDelay : oneDay + initDelay;
            logger.info(initDelay);
            service.scheduleAtFixedRate(ServerDbutil.clearPunchTask,initDelay, oneDay, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static long getTimeMillis(String time) {
        try {
            DateFormat dateFormat = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
            DateFormat dayFormat = new SimpleDateFormat("yy-MM-dd");
            Date curDate = dateFormat.parse(dayFormat.format(new Date()) + " " + time);
            return curDate.getTime();
        } catch (ParseException e) {
            logger.info("时间解析失败!");
            e.printStackTrace();
        }

        return 0;
    }
}