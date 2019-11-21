package cn.adminzero.helloword.CommonClass;

import java.io.Serializable;


public class UserNoPassword implements Serializable {
    private static final long serialVersionUID = 1L;
    int userID;
    String userNickName;
    String email;
    int goal;
    // 打卡天数
    int days;
    int isPunch;
    int groupID;
    int level;
    int pKPoint;
    boolean isValid;

    public UserNoPassword(int userID, String userNickName, String email)
    {
        this.userID = userID;
        this.userNickName = userNickName;
        this.email = email;
        goal = -1;
        days = 0;
        groupID = -1;
        level = 0;
        pKPoint = 1000;
        isPunch=0;
        this.isValid = true;
    }
    public UserNoPassword(String email){
        this.userID =-1;
        this.userNickName = "nickname";
        this.email = email;
        goal = -1;
        days = 0;
        groupID = -1;
        level = 0;
        pKPoint = 0;
        this.isValid = true;
    }

    public void UserNoPassword(int userID,String userNickName,String email,
                               int isPunch,int goal,int days,int groupID, int level,int pKPoint){
        this.userID=userID;
        this.userNickName=userNickName;
        this.email=email;
        this.goal=goal;
        this.days=days;
        this.groupID=groupID;
        this.level=level;
        this.pKPoint=pKPoint;
        this.isPunch=isPunch;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }
    public long getSerialVersionUID() {
        return serialVersionUID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUserNickName() {
        return userNickName;
    }

    public void setUserNickName(String userNickName) {
        this.userNickName = userNickName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getIsPunch() {
        return isPunch;
    }

    public void setIsPunch(int isPunch) {
        this.isPunch = isPunch;
    }

    public int getGoal() {
        return goal;
    }

    public void setGoal(int goal) {
        this.goal = goal;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public int getGroupID() {
        return groupID;
    }

    public void setGroupID(int groupID) {
        this.groupID = groupID;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getpKPoint() {
        return pKPoint;
    }

    public void setpKPoint(int pKPoint) {
        this.pKPoint = pKPoint;
    }
}
