package cn.adminzero.helloword.CommonClass;

import java.util.ArrayList;

public class GroupMember {
    public ArrayList<MemberItem> memberlist=new ArrayList<MemberItem>();

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


}
