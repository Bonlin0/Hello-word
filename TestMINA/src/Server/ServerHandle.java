package Server;

import Common.CMDDef;
import Common.Message;
import Common.Utils.SendMsgMethod;
import DB.UserInformation;
import cn.adminzero.helloword.CommonClass.Group;
import DB.ServerDbutil;
import Game.Gamer;
import cn.adminzero.helloword.CommonClass.*;
import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.FilterEvent;

import java.lang.management.OperatingSystemMXBean;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;

import static DB.ServerDbutil.*;

/**
 * @author: 王翔
 * @date: 2019/11/7-20:28
 * @description: <br>
 * 业务逻辑处理代码，也是后续添加交互需要重点关注的代码
 * <EndDescription>
 */
public class ServerHandle extends IoHandlerAdapter {
    private static Logger logger = Logger.getLogger(ServerHandle.class);

    public ServerHandle() {
        super();
    }

    @Override
    public void sessionCreated(IoSession session) throws Exception {
        super.sessionCreated(session);
        //     logger.info("用户连接至服务器");
    }

    @Override
    public void sessionOpened(IoSession session) throws Exception {
        super.sessionOpened(session);
        //    logger.info("连接被打开");


    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        super.sessionClosed(session);
        //    logger.info("连接被关闭");
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        super.sessionIdle(session, status);
        //    logger.info("连接被闲置");
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        super.exceptionCaught(session, cause);
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        super.messageReceived(session, message);
        //    logger.info("收到了数据");
        if (message instanceof Message) {
            Message mes = (Message) message;
            switch (mes.getCMD()) {
                case CMDDef.GAME_RESULT: {
                    Gamer.setConfrontatInfo(UserIDSession.getUserIDWithSessionID(session.getId()),
                            mes.getI());
                    break;
                }
                case CMDDef.SIGN_UP_REQUESET: {
                    SignUpRequest sur = (SignUpRequest) mes.getObj();
                    UserNoPassword userNoPassword = ServerDbutil.signup(sur);
                    session.write(SendMsgMethod.getObjectMessage(CMDDef.REPLY_SIGN_UP_REQUEST, userNoPassword));
                    if (userNoPassword.isValid())
                        CreateHistory(userNoPassword.getUserID());
                    if (userNoPassword.isValid()) {
                        UserIDSession.insertSessionUser(session.getId(), userNoPassword.getUserID());
                        logger.info("用户" + userNoPassword.getUserNickName() + "成功注册并与服务器建立了连接!");
                        logger.info("ID为" + userNoPassword.getUserID());
                    }
                }
                break;
                case CMDDef.SIGN_IN_REQUESET: {
                    SignInRequest sir = (SignInRequest) mes.getObj();
                    UserNoPassword userNoPassword = ServerDbutil.signin(sir);
                    userNoPassword.setCharacterEncoding("utf-8");
                    long anotherSession;
                    //如果用户检查通过
                    if (userNoPassword.isValid()) {
                        try {
                            //尝试在已有映射获取用户
                            anotherSession = UserIDSession.getSessionIDWithUserID(userNoPassword.getUserID());
                            IoSession session1 = session.getService().
                                    getManagedSessions().get(anotherSession);
                            //先发着吧，没啥卵用
                            session1.write(SendMsgMethod.getNullMessage(CMDDef.FORCE_OFFLINE));
                            UserIDSession.removeSessionWithUserID(userNoPassword.getUserID());
                            UserIDSession.insertSessionUser(session.getId(), userNoPassword.getUserID());
                            userNoPassword.setValid(false);
                            session.write(SendMsgMethod.getObjectMessage(CMDDef.REPLY_SIGN_IN_REQUEST, userNoPassword));
                        } catch (NullPointerException e) {
                            //说明未获取成功
                            //logger.info("未获取成功!");
                            session.write(SendMsgMethod.getObjectMessage(CMDDef.REPLY_SIGN_IN_REQUEST, userNoPassword));
                            UserIDSession.insertSessionUser(session.getId(), userNoPassword.getUserID());
                            break;
                        }
                    }
                }
                break;
                case CMDDef.DESTORY_SELF_SEND_DATA: {
                    DestoryData destoryData = (DestoryData) mes.getObj();
                    logger.info("ID为" + UserIDSession.getUserIDWithSessionID(session.getId()) + "的用户与服务器断开了连接!");
                    UserIDSession.removeSessionWithUserID(session.getId());
                }
                break;
                case CMDDef.UPDATE_USER_REQUESET: {
                    UserNoPassword unp = (UserNoPassword) mes.getObj();
                    UserNoPassword userNoPassword;
                    try {
                        userNoPassword = ServerDbutil.update_USER(unp);
                        session.write(SendMsgMethod.getObjectMessage(CMDDef.REPLY_UPDATE_USER_REQUESET, userNoPassword));

                    } catch (Exception e) {
                        logger.info("用户表更新插入失败!");
                    }
                }
                break;
                case CMDDef.JOIN_PK_GAME_REQUEST: {
                    if (Gamer.isEmpty()) {
                        Gamer.insertGamer(session.getId());
                        //   logger.info("此时一位靓仔加入了游戏!");
                        //     logger.info("还剩" + Gamer.getGamers() + "位靓仔");
                    } else {
                        long anotherID = Gamer.getGamer();
                        Gamer.removerGamer(anotherID);
                        //   logger.info("此时又一位靓仔加入了游戏!");
                        //  logger.info("还剩" + Gamer.getGamers() + "位靓仔");
                        IoSession anotherSession = session.getService().getManagedSessions().get(anotherID);

                        int userID2 = UserIDSession.getUserIDWithSessionID(anotherID);
                        int userID1 = UserIDSession.getUserIDWithSessionID(session.getId());
                        UserInformation userInformation1 = getUser(userID1);
                        UserInformation userInformation2 = getUser(userID2);
                        ArrayList<Short> wordsLevelArrayList1 = getHistoryWord(userID1);
                        ArrayList<Short> wordsLevelArrayList2 = getHistoryWord(userID2);
                        //求出并集
                        wordsLevelArrayList1.removeAll(wordsLevelArrayList2);
                        wordsLevelArrayList1.addAll(wordsLevelArrayList2);
                        int collectionSize = wordsLevelArrayList1.size() - CMDDef.PK_MAX_WORD_NUM;
                        short[] result = new short[CMDDef.PK_MAX_WORD_NUM];

                        //加锁
                        synchronized (Gamer.randomNumMute) {
                            for (int i = 0; i < CMDDef.PK_MAX_WORD_NUM; i++) {
                                result[i] = (short) (Gamer.getrandom()[i] + collectionSize);
                            }
                        }

                        OpponentInfo info1 = new OpponentInfo(userID1, userInformation1.getUserNickName(), result);
                        OpponentInfo info2 = new OpponentInfo(userID2, userInformation2.getUserNickName(), result);
                        anotherSession.write(SendMsgMethod.getObjectMessage(CMDDef.REPLY_GAMER_IFNO, info1));
                        session.write(SendMsgMethod.getObjectMessage(CMDDef.REPLY_GAMER_IFNO, info2));

                        Gamer.insertConfrontation(userID1, userID2);

                        //    logger.info("两位靓仔开始了游戏!");
                        //   logger.info("还剩" + Gamer.getGamers() + "位靓仔!");
                    }
                }
                break;
                case CMDDef.GIVE_UP_JOIN_GAME: {
                    Gamer.removerGamer(session.getId());
                    //  logger.info("此时一位靓仔不愿意玩游戏并悄悄的取消了匹配!");
                    //  logger.info("还剩" + Gamer.getGamers() + "位靓仔！");
                }
                break;
                case CMDDef.CREATE_GROUP_REQUEST: {
                    Group group = (Group) mes.getObj();
                    int user_id = group.getUser_id();
                    group = CreatGroup(group);
                    if (group == null) {
                        logger.info("创建小组失败");
                    }
                }
                break;
                case CMDDef.UPDATE_GROUP_REQUEST: {
                    Group group = (Group) mes.getObj();
                    group = updateGroup(group);
                }
                break;
                case CMDDef.GET_GROUP_REQUEST: {
                    int user_id = (int) mes.getI();
                    Group group = getGroup(user_id);
                    session.write(SendMsgMethod.getObjectMessage(CMDDef.GET_GROUP_REPLY, group));
                }
                break;
                case CMDDef.GET_GROUPMEMBER_REQUEST: {
                    int user_id = UserIDSession.getUserIDWithSessionID(session.getId());
                    GroupMember groupMember = getGroupMember(user_id);
                    session.write(SendMsgMethod.getObjectMessage(CMDDef.GET_GROUPMEMBER_REPLY, groupMember));
                }
                break;
                case CMDDef.UPDATE_HISTORY_REQUEST: {
                    ArrayList<WordsLevel> wordsToUpdate = (ArrayList<WordsLevel>) mes.getObj();
                    int user_id = UserIDSession.getUserIDWithSessionID(session.getId());
                    UpdateHistory_chen(user_id, wordsToUpdate);
                }
                break;
                case CMDDef.GET_HISTORY_REQUSEST: {
                    int user_id = UserIDSession.getUserIDWithSessionID(session.getId());
                    ArrayList<WordsLevel> wordlist = getHistory(user_id);
                    session.write(SendMsgMethod.getObjectMessage(CMDDef.GET_HISTORY_REPLY, wordlist));
                }
                break;
                case CMDDef.CHANGE_WORDBOOK_REQUEST: {
                    int user_id = UserIDSession.getUserIDWithSessionID(session.getId());
                    int tag= mes.getI();
                    if(changeHistory_zjc((short)tag,user_id))
                        logger.info("词书更新成功！");
                    else
                        logger.info("词书更新失败!");
                }
                break;

            }
        } else {
            logger.info("收到了未知请求！");
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
