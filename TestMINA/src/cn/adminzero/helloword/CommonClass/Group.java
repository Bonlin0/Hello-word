package cn.adminzero.helloword.CommonClass;

public class Group {
    public  static  int user_id=-1;
    public  static  int group_id=-1;
    public  static  int contribution=0;
    public  static  int master=-1;
    public  static  int max_member=0;

    public  void Group(int user_id){
        this.user_id=user_id;
        this.master=user_id;
        this.max_member=10;
    }

    public  void Group(int user_id,int max_member){
        this.user_id=user_id;
        this.master=user_id;
        this.max_member=max_member;
    }

    public  void Group(int user_id,int group_id, int contribution,int master, int max_member){
        this.user_id=user_id;
        this.master=master;
        this.max_member=max_member;
        this.group_id=group_id;
        this.contribution=contribution;
    }

    public static int getUser_id() {
        return user_id;
    }

    public static void setUser_id(int user_id) {
        Group.user_id = user_id;
    }

    public static int getGroup_id() {
        return group_id;
    }

    public static void setGroup_id(int group_id) {
        Group.group_id = group_id;
    }

    public static int getContribution() {
        return contribution;
    }

    public static void setContribution(int contribution) {
        Group.contribution = contribution;
    }

    public static int getMaster() {
        return master;
    }

    public static void setMaster(int master) {
        Group.master = master;
    }

    public static int getMax_member() {
        return max_member;
    }

    public static void setMax_member(int max_member) {
        Group.max_member = max_member;
    }
}
