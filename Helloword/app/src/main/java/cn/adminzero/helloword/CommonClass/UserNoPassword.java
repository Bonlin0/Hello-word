package cn.adminzero.helloword.CommonClass;

import java.io.Serializable;

// Author : whl
// Date : 2019/11/17

public class UserNoPassword implements Serializable {
    private static final long serialVersionUID = 1L;
    int user_id;
    String user_name;
    String email;
    String avatar;
    int goal;
    // 打卡天数
    int days;
    int group_id;
    int level;
    int points;

    public UserNoPassword(int user_id,String user_name, String email)
    {
        this.user_id = user_id;
        this.user_name = user_name;
        this.email = email;
        avatar = ""; // TODO 将这里路径改为默认头像路径
        goal = -1;
        days = 0;
        group_id = -1;
        level = 0;
        points = 1000;
    }

    public long getSerialVersionUID() {
        return serialVersionUID;
    }

    public int getUserID() {
        return user_id;
    }

    public void setUserID(int user_id) {
        this.user_id = user_id;
    }

    public String getUserNickName() {
        return user_name;
    }

    public void setUserNickName(String user_name) {
        this.user_name = user_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatarPath() {
        return avatar;
    }

    public void setAvatarPath(String avatar) {
        this.avatar = avatar;
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
        return group_id;
    }

    public void setGroupID(int group_id) {
        this.group_id = group_id;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getpoints() {
        return points;
    }

    public void setpoints(int points) {
        this.points = points;
    }
}
