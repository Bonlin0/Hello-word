package cn.adminzero.helloword.CommonClass;

import java.io.Serializable;

// Author : whl
// Date : 2019/11/17

public class UserNoPassword implements Serializable {
    int userID;
    String userNickName;
    String email;
    String avatarPath;
    int goal;
    // 打卡天数
    int days;
    int groupID;
    int level;
    int pKPoint;

    public UserNoPassword(int userID,String userNickName, String email)
    {
        this.userID = userID;
        this.userNickName = userNickName;
        this.email = email;
        avatarPath = ""; // TODO 将这里路径改为默认头像路径
        goal = -1;
        days = 0;
        groupID = -1;
        level = 0;
        pKPoint = 1000;
    }
}
