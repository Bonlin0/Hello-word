package cn.adminzero.helloword.CommonClass;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author: 王翔
 * @date: 2019/11/25-19:56
 * @description: <br>
 * <EndDescription>
 */
public class OpponentInfo implements Serializable {
    private final long serialVersionUID = 1L;

    private int userID;
    private String nickName;
    private short[] pkWords = null;

    public OpponentInfo(int userID, String nickName, short[] pkWords) {
        this.userID = userID;
        this.nickName = nickName;
        this.pkWords = pkWords;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public short[] getPkWords() {
        return pkWords;
    }

    public void setPkWords(short[] pkWords) {
        this.pkWords = pkWords;
    }
}
