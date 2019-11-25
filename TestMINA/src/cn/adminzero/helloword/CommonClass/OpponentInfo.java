package cn.adminzero.helloword.CommonClass;

import java.io.Serializable;

/**
 * @author: 王翔
 * @date: 2019/11/25-19:56
 * @description: <br>
 * <EndDescription>
 */
public class OpponentInfo implements Serializable {
    private final long serialVersionUID = 1L;

    //TODO：暂时只发ID，等陈渊回复
    int userID;

    public OpponentInfo(int userID) {
        this.userID = userID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }
}
