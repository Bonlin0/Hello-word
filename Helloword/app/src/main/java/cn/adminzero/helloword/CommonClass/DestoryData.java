package cn.adminzero.helloword.CommonClass;

import java.io.Serializable;

import cn.adminzero.helloword.Common.Utils.SerializeUtils;

public class DestoryData implements Serializable {
    private final long serialVersionUID = 1L;
    private int userID;

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }
}