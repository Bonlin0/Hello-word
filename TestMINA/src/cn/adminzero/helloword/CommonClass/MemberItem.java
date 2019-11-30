package cn.adminzero.helloword.CommonClass;

public   class MemberItem{
    int user_id;
    String user_name;
    int contribution;
    public  MemberItem(int user_id){
        this.user_id=user_id;
    }
    public  MemberItem(int user_id,String user_name, int contribution){
        this.user_id=user_id;
        this.user_name=user_name;
        this.contribution=contribution;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public int getContribution() {
        return contribution;
    }

    public void setContribution(int contribution) {
        this.contribution = contribution;
    }
}