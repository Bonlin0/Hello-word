package Game;

import Common.CMDDef;
import Common.Utils.SendMsgMethod;
import DB.ServerDbutil;
import Server.UserIDSession;
import cn.adminzero.helloword.CommonClass.GameResult;
import cn.adminzero.helloword.CommonClass.UserNoPassword;
import com.mysql.cj.Session;
import org.apache.log4j.Logger;
import org.apache.mina.core.filterchain.IoFilterLifeCycleException;
import org.apache.mina.core.session.IoSession;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

/**
 * @author: 王翔
 * @date: 2019/11/25-19:46
 * @description: <br>
 * <EndDescription>
 */
public class Gamer {
    private static Logger logger = Logger.getLogger(Gamer.class);

    private static Map<Long, IoSession> sessionMap = null;

    private static ArrayList<Long> gamer = new ArrayList<>();
    private static final Object gamerMute = new Object();

    private static Timer settleTimer = new Timer();

    private static Map<Integer, SettleTask> confrontation = new HashMap<>();
    private static final Object confrontationMute = new Object();

    private static int[] randomNum = new int[CMDDef.PK_MAX_WORD_NUM];
    public static final Object randomNumMute = new Object();

    public static Random rd = new Random();

    public static void insertConfrontation(int user1ID, int user2ID) {
        synchronized (confrontationMute) {
            SettleTask settleTask = new SettleTask(user1ID, user2ID);
            confrontation.put(user1ID, settleTask);
            confrontation.put(user2ID, settleTask);
            settleTimer.schedule(settleTask, 50000);
            logger.info("两位玩家开始了游戏，ID分别为" + user1ID + "、" + user2ID);
        }
    }

    public static void setConfrontatInfo(int playerID, int count) {
        synchronized (confrontationMute) {
            SettleTask settleTask = confrontation.get(playerID);
            if (settleTask == null) {
                logger.info("寻找对局失败!");
                return;
            }
            settleTask.setPlayerRightCount(playerID, count);
            if (settleTask.isReady()) {
                GameResult gameResult1 = new GameResult();
                GameResult gameResult2 = new GameResult();

                settleTask.cancel();
                IoSession session1 = sessionMap.get(
                        UserIDSession.getSessionIDWithUserID(settleTask.getPlayer1_ID()));
                IoSession session2 = sessionMap.get(
                        UserIDSession.getSessionIDWithUserID(settleTask.getPlayer2_ID()));
                //TODO: 结算
                try {
                    UserNoPassword user1 = ServerDbutil.getUserNopassword(settleTask.getPlayer1_ID());
                    UserNoPassword user2 = ServerDbutil.getUserNopassword(settleTask.getPlayer2_ID());

                    gameResult1.setAddScore(10 + settleTask.getScore());
                    gameResult2.setAddScore(gameResult1.getAddScore());

                    if (settleTask.result() == -1) {
                        gameResult1.setResult(GameResult.win);
                        gameResult2.setResult(GameResult.lose);

                        user1.setpKPoint(user1.getpKPoint() + gameResult1.getAddScore());
                        user2.setpKPoint(user2.getpKPoint() - gameResult2.getAddScore());

                    } else if (settleTask.result() == 1) {
                        gameResult1.setResult(GameResult.lose);
                        gameResult2.setResult(GameResult.win);

                        user1.setpKPoint(user1.getpKPoint() - settleTask.getScore() - 10);
                        user2.setpKPoint(user2.getpKPoint() + 10 + settleTask.getScore());
                    }
                    gameResult1.setNowScore(user1.getpKPoint());
                    gameResult2.setNowScore(user2.getpKPoint());
                    ServerDbutil.update_USER(user1);
                    ServerDbutil.update_USER(user2);
                } catch (SQLException e) {
                    logger.info("分数数据库更新错误!");
                    e.printStackTrace();
                    confrontation.remove(settleTask.getPlayer1_ID());
                    confrontation.remove(settleTask.getPlayer2_ID());
                    return;
                }
                try {
                    if (session1 != null) {
                        session1.write(SendMsgMethod.getObjectMessage(CMDDef.REPLY_GAME_RESULT, gameResult1));
                    }
                    if (session2 != null) {
                        session2.write(SendMsgMethod.getObjectMessage(CMDDef.REPLY_GAME_RESULT, gameResult2));
                    }
                } catch (IOException e) {
                    logger.info("分数数据库序列化错误!");
                }
                confrontation.remove(settleTask.getPlayer1_ID());
                confrontation.remove(settleTask.getPlayer2_ID());
            }
        }
    }

    public static void initGamer(Map<Long, IoSession> s) {
        sessionMap = s;
        rd.setSeed(System.currentTimeMillis());
        List<Integer> lst = new ArrayList<>();
        int index = 0;//随机索引
        for (int i = 0; i < CMDDef.PK_MAX_WORD_NUM; i++) {
            lst.add(i);
        }
        synchronized (randomNumMute) {
            for (int i = 0; i < CMDDef.PK_MAX_WORD_NUM; i++) {
                index = rd.nextInt(CMDDef.PK_MAX_WORD_NUM - i);
                randomNum[i] = lst.get(index);
                lst.remove(index);
            }
        }
        SettleTask.all = confrontation;
        SettleTask.sessionMap = sessionMap;
    }

    public static void insertGamer(long UserID) {
        synchronized (gamerMute) {
            gamer.add(UserID);
        }
    }

    public static boolean removerGamer(long UserID) {
        synchronized (gamerMute) {
            return gamer.remove(UserID);
        }
    }

    public static boolean isEmpty() {
        synchronized (gamerMute) {
            return gamer.isEmpty();
        }
    }

    public static long getGamer() {
        synchronized (gamerMute) {
            if (gamer.isEmpty())
                return -1;
            return gamer.get(0);
        }
    }

    public static int getGamers() {
        synchronized (gamerMute) {
            return gamer.size();
        }
    }

    public static int[] getrandom() {
        return randomNum;
    }

    public static Runnable runnable = new Runnable() {
        @Override
        public void run() {
            rd.setSeed(System.currentTimeMillis() + rd.nextInt());
            List<Integer> lst = new ArrayList<Integer>();
            //清空已经安排的任务
            settleTimer.purge();
            int index = 0;//随机索引
            for (int i = 0; i < CMDDef.PK_MAX_WORD_NUM; i++) {
                lst.add(i);
            }
            synchronized (randomNumMute) {
                for (int i = 0; i < CMDDef.PK_MAX_WORD_NUM; i++) {
                    index = rd.nextInt(CMDDef.PK_MAX_WORD_NUM - i);
                    randomNum[i] = lst.get(index);
                    lst.remove(index);
                }
            }
        }
    };
}
