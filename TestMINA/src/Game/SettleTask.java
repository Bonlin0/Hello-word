package Game;

import Common.CMDDef;
import Common.Utils.SendMsgMethod;
import DB.ServerDbutil;
import Server.UserIDSession;
import cn.adminzero.helloword.CommonClass.GameResult;
import cn.adminzero.helloword.CommonClass.UserNoPassword;
import org.apache.log4j.Logger;
import org.apache.mina.core.session.IoSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;
import java.util.TimerTask;

/**
 * @author: 王翔
 * @date: 2019/11/30-17:19
 * @description: <br>
 * <EndDescription>
 */
public class SettleTask extends TimerTask {
    private static Logger logger = Logger.getLogger(SettleTask.class);
    public static Map<Integer, SettleTask> all = null;

    public static Map<Long, IoSession> sessionMap = null;

    private int player1_ID = -1;
    private int player2_ID = -1;
    private int player1RightCount = -1;
    private int player2RightCount = -1;

    public SettleTask(int player1_ID, int player2_ID) {
        this.player1_ID = player1_ID;
        this.player2_ID = player2_ID;
    }

    public void setPlayerRightCount(int userID, int count) {
        if (userID == player1_ID) {
            player1RightCount = count;
        } else if (userID == player2_ID) {
            player2RightCount = count;
        }
    }

    public boolean isReady() {
        return (player1RightCount != -1) && (player2RightCount != -1);
    }

    //-1 为1赢，0为平，1为2赢
    public int result() {
        return Integer.compare(player2RightCount, player1RightCount);
    }

    public int getScore() {
        //未准备就绪说明一位玩家强退了游戏
        if (!isReady()) {
            if (result() == -1)
                return player1RightCount;
            else if (result() == 1)
                return player2RightCount;
            else
                return 0;
        } else {
            return (player2RightCount > player1RightCount)?(player2RightCount - player1RightCount) :
                    (player1RightCount - player2RightCount);
        }
    }

    public int getPlayer1_ID() {
        return player1_ID;
    }

    public int getPlayer2_ID() {
        return player2_ID;
    }

    public int getPlayer1RightCount() {
        return player1RightCount;
    }

    public int getPlayer2RightCount() {
        return player2RightCount;
    }

    @Override
    public void run() {
        IoSession session1 = null;
        IoSession session2 = null;

        GameResult gameResult1 = new GameResult();
        GameResult gameResult2 = new GameResult();
        try {
            session1 = sessionMap.get(
                    UserIDSession.getSessionIDWithUserID(getPlayer1_ID()));
            session2 = sessionMap.get(
                    UserIDSession.getSessionIDWithUserID(getPlayer2_ID()));

            UserNoPassword user1 = ServerDbutil.getUserNopassword(getPlayer1_ID());
            UserNoPassword user2 = ServerDbutil.getUserNopassword(getPlayer2_ID());

            gameResult1.setAddScore(10 + getScore());
            gameResult2.setAddScore(gameResult1.getAddScore());

            //user1赢了
            if (result() == -1) {
                gameResult1.setResult(GameResult.win);
                gameResult2.setResult(GameResult.lose);
                user1.setpKPoint(user1.getpKPoint() + getScore() + 10);
                user2.setpKPoint(user2.getpKPoint() - 10 - getScore());
            } else if (result() == 1) {
                gameResult1.setResult(GameResult.lose);
                gameResult2.setResult(GameResult.win);
                user1.setpKPoint(user1.getpKPoint() - getScore() - 10);
                user2.setpKPoint(user2.getpKPoint() + 10 + getScore());
            }

            gameResult1.setNowScore(user1.getpKPoint());
            gameResult2.setNowScore(user2.getpKPoint());

            ServerDbutil.update_USER(user1);
            ServerDbutil.update_USER(user2);

        } catch (SQLException e) {
            logger.info("分数数据库更新错误!");
            e.printStackTrace();
        }
        try{
            if (session1 != null) {
                session1.write(SendMsgMethod.getObjectMessage(CMDDef.REPLY_GAME_RESULT, gameResult1));
            }
            if (session2 != null) {
                session2.write(SendMsgMethod.getObjectMessage(CMDDef.REPLY_GAME_RESULT, gameResult2));
            }
        } catch (IOException e) {
            logger.info("分数数据库序列化错误!");
        }
        this.cancel();
    }
}
