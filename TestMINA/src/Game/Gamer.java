package Game;

import Common.CMDDef;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * @author: 王翔
 * @date: 2019/11/25-19:46
 * @description: <br>
 * <EndDescription>
 */
public class Gamer {
    private static Logger logger = Logger.getLogger(Gamer.class);
    private static ArrayList<Long> gamer = new ArrayList<>();
    private static int[] randomNum = new int[CMDDef.PK_MAX_WORD_NUM];
    private static final Object randomNumMute = new Object();

    public static Random rd = new Random();

    public static void initGamer() {
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
    }

    public static synchronized void insertGamer(long UserID) {
        gamer.add(UserID);
    }

    public static synchronized boolean removerGamer(long UserID) {
        return gamer.remove(UserID);
    }

    public static synchronized boolean isEmpty() {
        return gamer.isEmpty();
    }

    public static synchronized long getGamer() {
        if (gamer.isEmpty())
            return -1;
        return gamer.get(0);
    }

    public static int getGamers() {
        return gamer.size();
    }

    public static int[] getrandom() {
        synchronized (randomNumMute) {
            return randomNum;
        }
    }

    public static Runnable runnable = new Runnable() {
        @Override
        public void run() {
            rd.setSeed(System.currentTimeMillis() + rd.nextInt());
            List<Integer> lst = new ArrayList<Integer>();

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
