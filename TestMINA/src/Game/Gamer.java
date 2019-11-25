package Game;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * @author: 王翔
 * @date: 2019/11/25-19:46
 * @description: <br>
 * <EndDescription>
 */
public class Gamer {
    private static ArrayList<Long> gamer = new ArrayList<>();

    public static synchronized void insertGamer(long UserID)
    {
        gamer.add(UserID);
    }
    public static synchronized boolean removerGamer(long UserID)
    {
        return gamer.remove(UserID);
    }
    public static synchronized boolean isEmpty()
    {
        return gamer.isEmpty();
    }
    public static synchronized long getGamer()
    {
        if(gamer.isEmpty())
            return -1;
        return gamer.get(0);
    }
    public static int getGamers()
    {
        return gamer.size();
    }

}
