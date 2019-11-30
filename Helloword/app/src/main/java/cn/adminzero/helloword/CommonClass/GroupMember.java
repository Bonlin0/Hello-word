package cn.adminzero.helloword.CommonClass;


import java.io.Serializable;
import java.util.ArrayList;

public class GroupMember implements Serializable {
    private final long serialVersionUID = 1L;
    public ArrayList<MemberItem> memberlist=new ArrayList<MemberItem>();
    public Group master=new Group(-1);

    public Group getMaster() {
        return master;
    }

    public void setMaster(Group master) {
        this.master = master;
    }

    public ArrayList<MemberItem> getMemberlist() {
        return memberlist;
    }

    public void setMemberlist(ArrayList<MemberItem> memberlist) {
        this.memberlist = memberlist;
    }
    public  void addMember(int user_id,String user_name, int contribution){
        MemberItem memberItem=new MemberItem(user_id,user_name,contribution);
        memberlist.add(memberItem);
    }
    public long getSerialVersionUID() {
        return serialVersionUID;
    }

}
