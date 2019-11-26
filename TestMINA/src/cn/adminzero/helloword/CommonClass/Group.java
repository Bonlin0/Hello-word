package cn.adminzero.helloword.CommonClass;

import java.io.Serializable;

public class Group implements Serializable {
    private final long serialVersionUID = 1L;
    public  int user_id=-1;
    public  int group_id=-1;
    public  int contribution=0;
    public  int master=-1;
    public  int max_member=10;

    public  Group(int user_id){
        this.user_id=user_id;
        this.master=user_id;
        this.max_member=10;
    }

    public long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Group(int user_id, int max_member){
        this.user_id=user_id;
        this.master=user_id;
        this.max_member=max_member;
    }

    public  Group(int user_id,int group_id, int contribution,int master, int max_member){
        this.user_id=user_id;
        this.master=master;
        this.max_member=max_member;
        this.group_id=group_id;
        this.contribution=contribution;
    }

    public int getUser_id() {
        return user_id;
    }

    public  void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public  int getGroup_id() {
        return group_id;
    }

    public  void setGroup_id(int group_id) {
        this.group_id = group_id;
    }

    public  int getContribution() {
        return contribution;
    }

    public  void setContribution(int contribution) {
        this.contribution = contribution;
    }

    public  int getMaster() {
        return master;
    }

    public  void setMaster(int master) {
        this.master = master;
    }

    public  int getMax_member() {
        return max_member;
    }

    public  void setMax_member(int max_member) {
        this.max_member = max_member;
    }
}
