package DB;

import cn.adminzero.helloword.CommonClass.*;
import org.apache.log4j.Logger;
import java.sql.*;
import java.util.ArrayList;

public class ServerDbutil {
    private static Logger logger = Logger.getLogger(ServerDbutil.class);
    /**
     * 服务器端输入用户User_id 得到用户除了密码之外的信息
     */
    public static UserNoPassword getUserNopassword(int user_id) throws SQLException {
        UserNoPassword user = new UserNoPassword("-1");
        PreparedStatement stmt = GlobalConn.getConn().prepareStatement("SELECT * FROM USER WHERE user_id=? ");
        stmt.setObject(1, user_id);
        ResultSet rs = null;
        try {
            rs = stmt.executeQuery();
            while (rs.next()) {
                user.UserNoPassword(
                        user_id,
                        rs.getString("user_name"),
                        rs.getString("email"),
                        rs.getInt("isPunch"),
                        rs.getInt("goal"),
                        rs.getInt("days"),
                        rs.getInt("group_id"),
                        rs.getInt("user_level"),
                        rs.getInt("points")
                );
            }//需要注意 这里rs已经跑完了，rs.next=null
        } catch (Exception e) {
            logger.info("查询出错!");
        }
        return user;
    }

    /**
     * 服务器端输入用户User_id 得到用户所有信息
     */
    public static UserInformation getUser(int user_id) throws SQLException {
        UserInformation user = new UserInformation(-1);
        PreparedStatement stmt = GlobalConn.getConn().prepareStatement("SELECT * FROM USER WHERE user_id=? ");
        stmt.setObject(1, user_id);
        ResultSet rs = null;
        try {
            rs = stmt.executeQuery();
            while (rs.next()) {
                UserInformation userInformation = new UserInformation(
                        user_id,
                        rs.getString("password"),
                        rs.getString("user_name"),
                        rs.getString("email"),
                        rs.getInt("isPunch"),
                        rs.getInt("goal"),
                        rs.getInt("days"),
                        rs.getInt("group_id"),
                        rs.getInt("user_level"),
                        rs.getInt("points")
                );
                user = userInformation;
            }//需要注意 这里rs已经跑完了，rs.next=null
        } catch (Exception e) {
            logger.info("查询出错!");
        }
        return user;
    }

    /**
     * 注册
     */
    public static UserNoPassword signup(SignUpRequest sur) {
        UserNoPassword userNoPassword = new UserNoPassword(-1, sur.getNickName(), sur.getEmail());
        String email = sur.getEmail();
        String user_name = sur.getNickName();
        String password = sur.getPassword();
        int user_id = -1;
        //  userNoPassword.setValid(false);
        Statement stmt = null;
        try {
            stmt = GlobalConn.getConn().createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String sql_qurey = "SELECT user_id,email FROM USER";
        ResultSet rs = null;
        try {
            rs = stmt.executeQuery(sql_qurey);
            // 展开结果集数据库
            while (rs.next()) {
                // 通过字段检索,搜索到最后一个字段
                user_id = rs.getInt("user_id");
                if (email.equals(rs.getString("email"))) {
                    //已经存在这个邮箱名了
                    userNoPassword.setValid(false);
                    logger.info("该邮箱已经已经存在");
                }
            }
            if (user_id == -1) {
                user_id = 10000;
            } else {
                user_id = user_id + 1;
                userNoPassword.setUserID(user_id);
            }
            if (userNoPassword.isValid()) {
                //如果合法 就插入该用户数据
                // stmt.execute("insert into user(user_id,user_name,password,email) values(?,?,?,?)", new String[]{user_id + "", user_name, password, email});
                PreparedStatement statement = GlobalConn.getConn().prepareStatement("insert into USER(user_id,user_name,password,email) values(?,?,?,?)");
                statement.setObject(1, user_id);
                statement.setObject(2, user_name);
                statement.setObject(3, password);
                statement.setObject(4, email);
                statement.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // 完成后关闭数据库链接
        try {
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userNoPassword;
    }

    /**
     * 登录函数
     */
    public static UserNoPassword signin(SignInRequest sur) throws SQLException {
        UserNoPassword userNoPassword = new UserNoPassword(sur.getEmail());
        //TODO：数据库处理注册
        String email = sur.getEmail();
        String password = sur.getPassword();
        int user_id = -1;
        //  userNoPassword.setValid(false);
        PreparedStatement stmt = GlobalConn.getConn().prepareStatement("SELECT * FROM USER WHERE email=? ");
        stmt.setObject(1, email);
        ResultSet rs = null;
        try {
            rs = stmt.executeQuery();
            String password_q = "";
            int days_q = 0;
            // 展开结果集数据库
            while (rs.next()) {
                // 通过字段检索,搜索到最后一个字段
                if (password.equals(rs.getString("password"))) {
                    //密码正确
                    // password=rs.getString("password");
                    user_id = rs.getInt("user_id");
                    password_q = rs.getString("password");
                    userNoPassword.UserNoPassword(
                            rs.getInt("user_id"),
                            rs.getString("user_name"),
                            rs.getString("email"),
                            rs.getInt("isPunch"),
                            rs.getInt("goal"),
                            rs.getInt("days"),
                            rs.getInt("group_id"),
                            rs.getInt("user_level"),
                            rs.getInt("points")
                    );
                    days_q = rs.getInt("days");
//                    int isPunch=rs.getInt("isPunch");
//                    if(isPunch==0)
//                    //TODO 查询上次登录时间距离这次的时间差，看是否需要登陆天数+1
//                    {
//                        PreparedStatement statement = GlobalConn.getConn().prepareStatement("update USER set days=? where user_id=?");
//                        statement.setObject(1, days_q + 1);
//                        statement.setObject(2, user_id);
//                        statement.execute();
//                        userNoPassword.setDays(days_q+1);//返回结果天数也+1
//                    }
                }

            }//需要注意 这里rs已经跑完了，rs.next=null
            if (user_id == -1) {
                //没有此邮箱地址
                logger.info("邮箱错误！");
                userNoPassword.setValid(false);
            } else if (!password.equals(password_q)) {
                //密码错误
                logger.info("密码错误！");
                userNoPassword.setValid(false);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userNoPassword;
    }

    /**
     * 传回UserNopassword更新所有的用户数据,适用于除修改密码外所有的用户更新操作
     */
    public static UserNoPassword update_USER(UserNoPassword unp) {
        try {
            PreparedStatement stmt = GlobalConn.getConn().prepareStatement("" +
                    "update USER set user_name=?, " +
                    " email=?, " +
                    " days=?, " +
                    " group_id=?, " +
                    " user_level=?, " +
                    " points=?, " +
                    " isPunch=?, " +
                    " goal=? " +
                    " where user_id =?");
            stmt.setObject(1, unp.getUserNickName());
            stmt.setObject(2, unp.getEmail());
            stmt.setObject(3, unp.getDays());
            stmt.setObject(4, unp.getGroupID());
            stmt.setObject(5, unp.getLevel());
            stmt.setObject(6, unp.getpKPoint());
            stmt.setObject(7, unp.getIsPunch());
            stmt.setObject(8, unp.getGoal());
            stmt.setObject(9, unp.getUserID());
            stmt.execute();
            //   logger.info(stmt);
        } catch (Exception e) {
            logger.info("数据库插入失败！");
        }
        UserNoPassword userNoPassword = unp;
        return userNoPassword;
    }

    /**
     *
     * @param grp 传入的创建组的信息（只用有user_id和最大人数就好了）
     * @return
     * @throws SQLException
     */
    public static Group CreatGroup(Group grp) throws SQLException {
        Group group = grp;
        int user_id = grp.getUser_id();
        int max_member = grp.getMax_member();
        int group_id=0;
        int isExist=0;
        PreparedStatement stmt = GlobalConn.getConn().prepareStatement("select * from GROUP_USER");
        ResultSet rs=null;
        try{
            rs=stmt.executeQuery();
            while (rs.next()){
                group_id=rs.getInt("group_id");
                if(user_id==rs.getInt("user_id")){
                    //该用户已经属于某一组了，不能再创建组
                    isExist=1;
                    break;
                }
            }
        }catch (Exception e){

        }
        //当前用户不属于任何一组
        if(isExist==0){
            group_id++;
            stmt = GlobalConn.getConn().prepareStatement("insert into GROUP_USER(user_id,group_id,contribution,master,max_member) values(?,?,?,?,?)");
            stmt.setObject(1,user_id);
            stmt.setObject(2,group_id);
            stmt.setObject(3,0);
            stmt.setObject(4,user_id);
            stmt.setObject(5,max_member);
            stmt.execute();
            group.setGroup_id(group_id);
            group.setMaster(user_id);

        }
        else {
            return null;
        }
        return group;
    }

    /**
     * 获取用户的组信息
     * @param user_id
     * @return
     * @throws SQLException
     */
    public static Group getGroup(int user_id) throws SQLException {
        Group group=new Group(user_id);
        group.setGroup_id(-1);
        PreparedStatement statement=GlobalConn.getConn().prepareStatement("select * from GROUP_USER where user_id=?");
        statement.setObject(1,user_id);
        ResultSet resultSet=null;
        try {
            resultSet=statement.executeQuery();
           while (resultSet.next()){
               if(user_id==resultSet.getInt("user_id")){
                   group.setUser_id(resultSet.getInt("user_id"));
                   group.setGroup_id(resultSet.getInt("group_id"));
                   group.setContribution(resultSet.getInt("contribution"));
                   group.setMaster(resultSet.getInt("master"));
                   group.setMax_member(resultSet.getInt("max_member"));

               }
           }
        }catch (Exception e){
            //出错
        }
        int a=group.getUser_id();
        int b=group.getGroup_id();
        return group;
    }

    /**
     * 更新用户的组信息
     * @param grp
     * @return
     */
    public static  Group updateGroup(Group grp) throws SQLException {
        Group group=grp;
        int user_id=group.getUser_id();

        PreparedStatement statement=GlobalConn.getConn().prepareStatement("update GROUP_USER set group_id=?, contribution=?,master=?,max_member=? where user_id=?");
        statement.setObject(1,group.getGroup_id());
        statement.setObject(2,group.getContribution());
        statement.setObject(3,group.getMaster());
        statement.setObject(4,group.getMax_member());
        statement.setObject(5,user_id);
        statement.execute();
       // System.out.println(statement);
        return  group;
    }

    public  static  void CreateHistory(int user_id) throws SQLException {
        String tabelName="HISTORY_"+user_id;
        PreparedStatement statement=GlobalConn.getConn().prepareStatement("create table "+tabelName+"(word_id smallint primary key,level smallint default 0,yesterday tinyint default 0)");
       // statement.setString(1,tabelName);
        logger.info(statement);
        statement.execute();
    }

    public static void UpdateHistory(int user_id,ArrayList<WordsLevel> wordsIdToUpdate) throws SQLException {
        int i=0;
        String tabelName="HISTORY_"+user_id;
        for(i=0;i<wordsIdToUpdate.size();i++){
            WordsLevel wordsLevel=wordsIdToUpdate.get(i);
            int word_id=wordsLevel.getWord_id();
            //检查H表里有没有该单词，有的话更新，没有的话插入
            PreparedStatement stmt=GlobalConn.getConn().prepareStatement("select * from "+tabelName+" where word_id=?");
            stmt.setObject(1,word_id);
            ResultSet rs=stmt.executeQuery();
            if(rs.next()){
                if(word_id==rs.getInt(word_id)) {
                    PreparedStatement statement = GlobalConn.getConn().prepareStatement("" +
                            "update  " + tabelName + " set level=?,yesterday=?  where word_id=?");
                    statement.setObject(1, wordsLevel.getLevel());
                    statement.setObject(2, wordsLevel.getYestarday());
                    statement.setObject(3, wordsLevel.getWord_id());
                    statement.execute();
                    continue;
                }
            }
            PreparedStatement statement=GlobalConn.getConn().prepareStatement("" +
                    "insert into "+tabelName+"(word_id,level,yesterday) values(?,?,?)");
            statement.setObject(1,wordsLevel.getWord_id());
            statement.setObject(2,wordsLevel.getLevel());
            statement.setObject(3,wordsLevel.getYestarday());
            statement.execute();
        }
    }

    /**
     * 获取用户表
     *
     */
    public static ArrayList<WordsLevel> getHistory(int user_id) throws SQLException {
        String tabelName="HISTORY_"+user_id;
        ArrayList<WordsLevel> wordlist=new ArrayList<WordsLevel>();
        PreparedStatement stmt=GlobalConn.getConn().prepareStatement("select * from "+tabelName+" ");
        ResultSet rs=stmt.executeQuery();
        try{
            while (rs.next()){
                WordsLevel word=new WordsLevel();
                word.setWord_id(rs.getShort("word_id"));
                word.setLevel(rs.getShort("level"));
                word.setYesterday(rs.getByte("yesterday"));
                wordlist.add(word);
            }
        }catch (Exception e){
         //
        }
        return wordlist;


    }
    public static ArrayList<Short> getHistoryWord(int user_id) throws SQLException {
        String tabelName= "HISTORY_" +user_id;
        ArrayList<Short> wordlist=new ArrayList<>();
        PreparedStatement stmt=GlobalConn.getConn().prepareStatement("select * from "+tabelName+" ");
        ResultSet rs=stmt.executeQuery();
        try{
            while (rs.next()){
                WordsLevel word=new WordsLevel();
                word.setWord_id(rs.getShort("word_id"));
                word.setLevel(rs.getShort("level"));
                word.setYesterday(rs.getByte("yesterday"));
                wordlist.add(word.getWord_id());
            }
        }catch (Exception e){
            logger.info("查询单词异常!");
        }
        return wordlist;
    }
//            // 获取除了密码外的所有信息
//            UserNoPassword userNoPassword=getUserNopassword(10062);
//            //修改用户信息
//            userNoPassword.setUserNickName("test修改");
//            update_USER(userNoPassword);
//            //获取用户所有信息（包括密码）
//            UserInformation userInformation=getUser(10062);
//            logger.info("user_name:"+userInformation.getUserNickName()+"\n"+"email:"+userInformation.getEmail());

//            //小组功能的实现
//            //构建一个小组，包括创建的用户 和最大成员数量
//            Group group=new Group(10001,20);
//            //创建小组
//            CreatGroup(group);
//            //获取用户小组信息
//            Group group0=getGroup(10001);
//            //修改小组信息再更新
//            group0.setContribution(1000);
//            updateGroup(group0);

//            //创建用户背单词历史数据库
//            CreateHistory(10006);
//            //测试更新单词历史数据库
//             ArrayList<WordsLevel> wordsIdToUpdate=new ArrayList<WordsLevel>();
//             WordsLevel wordsLevel1=new WordsLevel((short)1);
//             wordsLevel1.setLevel((short)3);
//            WordsLevel wordsLevel2=new WordsLevel((short)4);
//            wordsIdToUpdate.add(wordsLevel1);
//            wordsIdToUpdate.add(wordsLevel2);
//            UpdateHistory(10006,wordsIdToUpdate);


    //           ArrayList<WordsLevel> wordlist= ServerDbutil.getHistory(10067);
//           WordsLevel wordsLevel =wordlist.get(1);


}
