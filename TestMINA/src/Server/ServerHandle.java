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
                    //TODO：数据库处理注册
                    //user_id 查询数据库有没有user_id如果，没有就 创建第一个为10000，如果有就取最后一个user_id +1


                    session.write(SendMsgMethod.getObjectMessage(CMDDef.REPLY_SIGN_UP_REQUEST,
                            new UserNoPassword(10085,sur.getNickName(),sur.getEmail())));
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
