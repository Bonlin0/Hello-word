package DB;

public class UserInformation {
    int userID;
    String password;
    String userNickName;
    String email;
    int goal;
    int days;
    int isPunch;
    int groupID;
    int level;
    int pKPoint;

    public  UserInformation(int userID){
        this.userID=userID;
    }


    public  UserInformation(int userID,String password,String userNickName,String email,
                               int isPunch,int goal,int days,int groupID, int level,int pKPoint){
        this.userID=userID;
        this.password=password;
        this.userNickName=userNickName;
        this.email=email;
        this.isPunch=isPunch;
        this.goal=goal;
        this.days=days;
        this.groupID=groupID;
        this.level=level;
        this.pKPoint=pKPoint;

    }
    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public int getIsPunch() {
        return isPunch;
    }

    public void setIsPunch(int isPunch) {
        this.isPunch = isPunch;
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
