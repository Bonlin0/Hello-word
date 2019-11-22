package Server;

import java.awt.desktop.UserSessionEvent;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: 王翔
 * @date: 2019/11/20-21:30
 * @description: <br>
 * <EndDescription>
 */
public class UserIDSession {
    private static Map<Long, Integer> SessionUser = new HashMap<>();
    private static Map<Integer, Long> UserSession = new HashMap<>();

    public static synchronized void insertSessionUser(long SessionID, int userID) {
        SessionUser.put(SessionID, userID);
        UserSession.put(userID, SessionID);
    }

    public static synchronized void removeSessionWithUserID(long SessionID) {
        if(SessionUser.isEmpty() || UserSession.isEmpty())
            return;
        UserSession.remove(SessionUser.get(SessionID));
        SessionUser.remove(SessionID);
    }

    public static synchronized void removeUserWithSessionID(int userID) {
        if(SessionUser.isEmpty() || UserSession.isEmpty())
            return;
        SessionUser.remove(UserSession.get(userID));
        UserSession.remove(userID);
    }

    public static int getUserIDWithSessionID(long SessionID) {
        if(SessionUser.isEmpty() || UserSession.isEmpty())
            return -1;
        return SessionUser.get(SessionID);
    }

    public static long getSessionIDWithUserID(int userID) {
        if(SessionUser.isEmpty() || UserSession.isEmpty())
            return -1;
        return UserSession.get(userID);
    }
}
