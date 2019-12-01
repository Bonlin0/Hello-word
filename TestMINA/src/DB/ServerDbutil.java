package DB;

import Server.UserIDSession;
import cn.adminzero.helloword.CommonClass.*;
import org.apache.log4j.Logger;

import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.Date;

public class ServerDbutil {
    private static Logger logger = Logger.getLogger(ServerDbutil.class);

    public static void initWordBook(String filename) throws IOException, SQLException {
        File file = new File(filename);//C:\Users\Sairen\Documents\GitHub\Hello-word\TestMINA\src\target.csv
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line = null;
        String[] buffer;

        short word_id = 0;
        String word = null;
        String phonetic = null;
        String definition = null;
        String translation = null;
        String exchange = null;
        Short tag = 0;
        String sentence = "";
        //创建数据总表

        PreparedStatement statement=GlobalConn.getConn().prepareStatement(" create table  if not exists WORDS(" +
                "word_id int primary key, " +
                "word TEXT," +
                "phonetic TEXT ," +
                "definition TEXT ," +
                "translation TEXT," +
                "exchange TEXT," +
                "tag int," +
                "sentence TEXT)");
        statement.execute();
//        }catch ( Exception e){
//           // logger.info("创建WORDS失败");
//        }
//
      try{
          //开启事务
            GlobalConn.getConn().setAutoCommit(false);
            int i=0;
        while ((line = br.readLine()) != null) {
            buffer = line.split("#", -1);
            word_id = Short.valueOf(buffer[0]);
            word = buffer[1];
            phonetic = buffer[2];
            definition = buffer[3];
            translation = buffer[4];
            exchange = buffer[5];
            tag = Short.valueOf(buffer[6]);
            i++;
            if(i%100==0){
                logger.info(i);
            }
            // 提取CSV 文件插入数据库
          try {
              PreparedStatement statement1 = GlobalConn.getConn().prepareStatement("" +
                      "insert into WORDS (word_id,word,phonetic,definition,translation,exchange,tag,sentence) values(?,?,?,?,?,?,?,?)");
              statement1.setObject(1, word_id);
              statement1.setObject(2, word);
              statement1.setObject(3, phonetic);
              statement1.setObject(4, definition);
              statement1.setObject(5, translation);
              statement1.setObject(6, exchange);
              statement1.setObject(7, tag);
              statement1.setObject(8, sentence);
              statement1.execute();
          }catch (Exception e){
              //logger.info("插入失败");
              continue;
          }
        }
          //提交事务
          GlobalConn.getConn().commit();
        logger.info("初始化WORDS表 事务提交成功");
        }catch (Exception e){
            //开启的事务有异常就回滚
            GlobalConn.getConn().rollback();
          logger.info("初始化WORDS表异常 事务回滚");
        }
    }

    /**
     * 更新用户的History表
     * @param _tag
     * @param user_id
     * @return
     * @throws SQLException
     */
    // _tag range is [0,7]
    public static boolean changeHistory(int _tag,int user_id) throws SQLException {
        if (_tag > 7 || _tag < 0) {
            logger.info("tag 大小不符合规范");
            return false;
        }
        //传进来3 就是00001000
        //记录剩余word_id
        int[] word=new  int[5000];
        ArrayList<WordsLevel> newHistoryList = new ArrayList<WordsLevel>();
            // db.beginTransaction()
            //开启事务
        try {
            GlobalConn.getConn().setAutoCommit(false);
            String table_name = "HISTORY_" + user_id;

            //删除原来表中 level=0的单词
            PreparedStatement preparedStatement = GlobalConn.getConn().prepareStatement("delete from " + table_name + " where level=0");
            preparedStatement.execute();
            logger.info("删除原来Hitory表的level=0的单词");
            //记录剩下H表的所有word_id
            PreparedStatement statement1 = GlobalConn.getConn().prepareStatement("select word_id from " + table_name);
            ResultSet resultSet0 = statement1.executeQuery();
            int i = 0;
            while (resultSet0.next()) {
                word[i] = resultSet0.getInt("word_id");
                i++;
            }

            // 选出WORDS所有的符合的tag行
            PreparedStatement statement = GlobalConn.getConn().prepareStatement("select word_id,tag from WORDS");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                short word_id = -1;
                WordsLevel wordsLevel = new WordsLevel();
                word_id = (short) resultSet.getInt("word_id");
                int tag = resultSet.getInt("tag");
                //  &
                if ((tag & _tag) == _tag) {
                    //遍历剩余的H表，不覆盖原来的记录
                    int isExist = 0;
                    for (int j = 0; j < word.length; j++) {
                        if (word_id == word[j])
                            isExist = 1;
                    }
                    if (isExist == 1) {
                        continue;
                    }
                    wordsLevel.setWord_id(word_id);
                    newHistoryList.add(wordsLevel);
                    // logger.info(word_id);
                }
            }
            //插入新的单词
            UpdateHistory(user_id, newHistoryList);
            //提交事务
            GlobalConn.getConn().commit();
            logger.info("提交改变History表的事务");
        }catch (Exception e){
            GlobalConn.getConn().rollback();
            logger.info("更新词书History表失败，回滚！");
            return  false;
        }


        return  true;
    }




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
     * @param grp 传入的创建组的信息（只用有user_id和最大人数就好了）
     * @return
     * @throws SQLException
     */
    public static Group CreatGroup(Group grp) throws SQLException {
        Group group = grp;
        int user_id = grp.getUser_id();
        int max_member = grp.getMax_member();
        int group_id = 0;
        int isExist = 0;
        PreparedStatement stmt = GlobalConn.getConn().prepareStatement("select * from GROUP_USER");
        ResultSet rs = null;
        try {
            rs = stmt.executeQuery();
            while (rs.next()) {
                group_id = rs.getInt("group_id");
                if (user_id == rs.getInt("user_id")) {
                    //该用户已经属于某一组了，不能再创建组
                    isExist = 1;
                    break;
                }
            }
        } catch (Exception e) {

        }
        //当前用户不属于任何一组
        if (isExist == 0) {
            group_id++;
            stmt = GlobalConn.getConn().prepareStatement("insert into GROUP_USER(user_id,group_id,contribution,master,max_member) values(?,?,?,?,?)");
            stmt.setObject(1, user_id);
            stmt.setObject(2, group_id);
            stmt.setObject(3, 0);
            stmt.setObject(4, user_id);
            stmt.setObject(5, max_member);
            stmt.execute();
            group.setGroup_id(group_id);
            group.setMaster(user_id);

        } else {
            return null;
        }
        return group;
    }

    /**
     * 获取用户的组信息
     *
     * @param user_id
     * @return
     * @throws SQLException
     */
    public static Group getGroup(int user_id) throws SQLException {
        Group group = new Group(user_id);
        group.setGroup_id(-1);
        PreparedStatement statement = GlobalConn.getConn().prepareStatement("select * from GROUP_USER where user_id=?");
        statement.setObject(1, user_id);
        ResultSet resultSet = null;
        try {
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                if (user_id == resultSet.getInt("user_id")) {
                    group.setUser_id(resultSet.getInt("user_id"));
                    group.setGroup_id(resultSet.getInt("group_id"));
                    group.setContribution(resultSet.getInt("contribution"));
                    group.setMaster(resultSet.getInt("master"));
                    group.setMax_member(resultSet.getInt("max_member"));

                }
            }
        } catch (Exception e) {
            //出错
        }
        int a = group.getUser_id();
        int b = group.getGroup_id();
        return group;
    }

    /**
     * 获取小组成员信息
     */
    public static GroupMember getGroupMember(int user_id) throws SQLException {
        GroupMember groupMember = new GroupMember();

        //获取该用户的小组信息
        Group group = getGroup(user_id);
        int group_id = group.getGroup_id();
        //如果没有加入小组  就直接返回
        if (group_id == -1) {
            return null;
        }
        //查询出同一组的成员id
        PreparedStatement statement = GlobalConn.getConn().prepareStatement("select * from GROUP_USER where group_id=?");
        statement.setObject(1, group_id);
        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            // 获取组长信息
            groupMember.master = getGroup(rs.getInt("master"));
            int memberId = rs.getInt("user_id");
            int memberContribution = rs.getInt("contribution");
            //查出用户名
            UserInformation memberinfo = getUser(memberId);
            String memberName = memberinfo.getUserNickName();
            //添加用户到列表
            groupMember.addMember(memberId, memberName, memberContribution);
        }

        return groupMember;
    }

    /**
     * 更新用户的组信息
     *
     * @param grp
     * @return
     */
    public static Group updateGroup(Group grp) throws SQLException {
        Group group = grp;
        int user_id = group.getUser_id();
        //如果未加入小组 就加组
        if (getGroup(user_id).getGroup_id() == -1) {
            PreparedStatement statement = GlobalConn.getConn().prepareStatement("insert into GROUP_USER(user_id,group_id,contribution,master,max_member) values(?,?,?,?,?)");
            statement.setObject(1, user_id);
            statement.setObject(2, group.getGroup_id());
            statement.setObject(3, group.getContribution());
            statement.setObject(4, group.getMaster());
            statement.setObject(5, group.getMax_member());
            statement.execute();
            return  group;
        }
        PreparedStatement statement = GlobalConn.getConn().prepareStatement("update GROUP_USER set group_id=?, contribution=?,master=?,max_member=? where user_id=?");
        statement.setObject(1, group.getGroup_id());
        statement.setObject(2, group.getContribution());
        statement.setObject(3, group.getMaster());
        statement.setObject(4, group.getMax_member());
        statement.setObject(5, user_id);
        statement.execute();
        // System.out.println(statement);
        return group;
    }

    public static void CreateHistory(int user_id) throws SQLException {
        String tabelName = "HISTORY_" + user_id;
        PreparedStatement statement = GlobalConn.getConn().prepareStatement("create  table if not exists  " + tabelName + "(word_id smallint primary key,level smallint default 0,yesterday tinyint default 0)");
        // statement.setString(1,tabelName);
        logger.info(statement);
        statement.execute();
    }

    public static void UpdateHistory(int user_id, ArrayList<WordsLevel> wordsIdToUpdate) throws SQLException {
        int i = 0;
        String tabelName = "HISTORY_" + user_id;
        for (i = 0; i < wordsIdToUpdate.size(); i++) {
            WordsLevel wordsLevel = wordsIdToUpdate.get(i);
            int word_id = wordsLevel.getWord_id();
            //检查H表里有没有该单词，有的话更新，没有的话插入
            PreparedStatement stmt = GlobalConn.getConn().prepareStatement("select * from " + tabelName + " where word_id=?");
            stmt.setObject(1, word_id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                if (word_id == rs.getInt(word_id)) {
                    PreparedStatement statement = GlobalConn.getConn().prepareStatement("" +
                            "update  " + tabelName + " set level=?,yesterday=?  where word_id=?");
                    statement.setObject(1, wordsLevel.getLevel());
                    statement.setObject(2, wordsLevel.getYestarday());
                    statement.setObject(3, wordsLevel.getWord_id());
                    statement.execute();
                    continue;
                }
            }
            PreparedStatement statement = GlobalConn.getConn().prepareStatement("" +
                    "insert into " + tabelName + "(word_id,level,yesterday) values(?,?,?)");
            statement.setObject(1, wordsLevel.getWord_id());
            statement.setObject(2, wordsLevel.getLevel());
            statement.setObject(3, wordsLevel.getYestarday());
            statement.execute();

        }
    }

    /**
     * 获取用户表
     */
    public static ArrayList<WordsLevel> getHistory(int user_id) throws SQLException {
        String tabelName = "HISTORY_" + user_id;
        ArrayList<WordsLevel> wordlist = new ArrayList<WordsLevel>();
        PreparedStatement stmt = GlobalConn.getConn().prepareStatement("select * from " + tabelName + " ");
        ResultSet rs = stmt.executeQuery();
        try {
            while (rs.next()) {
                WordsLevel word = new WordsLevel();
                word.setWord_id(rs.getShort("word_id"));
                word.setLevel(rs.getShort("level"));
                word.setYesterday(rs.getByte("yesterday"));
                wordlist.add(word);
            }
        } catch (Exception e) {
            //
        }
        return wordlist;

    }

    public static ArrayList<Short> getHistoryWord(int user_id) throws SQLException {
        String tabelName = "HISTORY_" + user_id;
        ArrayList<Short> wordlist = new ArrayList<>();
        PreparedStatement stmt = GlobalConn.getConn().prepareStatement("select * from " + tabelName + " ");
        ResultSet rs = stmt.executeQuery();
        try {
            while (rs.next()) {
                WordsLevel word = new WordsLevel();
                word.setWord_id(rs.getShort("word_id"));
                word.setLevel(rs.getShort("level"));
                word.setYesterday(rs.getByte("yesterday"));
                wordlist.add(word.getWord_id());
            }
        } catch (Exception e) {
            logger.info("查询单词异常!");
        }
        return wordlist;
    }

    //产生一些有关组的测试数据
    public static void test_group() throws SQLException {
        int i = 10000;
        for (i = 10048; i <= 10067; i++) {
            //更改用戶表的group_id
            UserInformation user = getUser(i);
            UserNoPassword userNoPassword = new UserNoPassword(user);
            userNoPassword.setGroupID(5);
            update_USER(userNoPassword);
            //更改group表的信息
            Group group = new Group(i, 5, 1000 + i * 10, 10048, 20);
            updateGroup(group);
        }
    }

    //产生一些History的测试数据
    public static void test_History() throws SQLException {
        int user_id1 = 10001;//1-100
        int user_id2 = 10002;//50-150
        CreateHistory(user_id1);
        CreateHistory(user_id2);
        ArrayList<WordsLevel> wordlist1 = new ArrayList<WordsLevel>();
        ArrayList<WordsLevel> wordlist2 = new ArrayList<WordsLevel>();
        for (int i = 1; i < 100; i++) {
            WordsLevel word = new WordsLevel((short) i);
            word.setLevel((short) ((i + 3) % 8));
            word.setYesterday((byte) (i % 2));
            wordlist1.add(word);
        }
        for (int i = 50; i < 150; i++) {
            WordsLevel word = new WordsLevel((short) i);
            word.setLevel((short) ((i + 3) % 8));
            word.setYesterday((byte) (i % 2));
            wordlist2.add(word);
        }
        UpdateHistory(user_id1,wordlist1);
        UpdateHistory(user_id2,wordlist2);

    }

    /**
     * 清空打卡
     * @throws SQLException
     */
    public  static void ClearPunch() throws SQLException {
        java.util.Date date =new Date();

        long time=date.getTime();
     //   logger.info("time:"+time);

        Calendar calendar=Calendar.getInstance();
        int year=calendar.get(calendar.YEAR);
        int month=calendar.get(calendar.MONTH);
        int days=calendar.get(calendar.DATE);
        int hours=calendar.get(calendar.HOUR_OF_DAY);
        int minute=calendar.get(calendar.MINUTE);
        int second=calendar.get(calendar.SECOND);
        //测试
//        int hours=0;
//        int minute=0;
//        int second=3;
        //   logger.info("year:"+year+"month:"+month+"date:"+days+"hour:"+hours+"minute:"+minute+"second:"+second);

        if (hours == 0 && minute == 0 && second <= 5) {
            PreparedStatement statement = GlobalConn.getConn().prepareStatement("update USER set isPunch=0");
            statement.execute();
            logger.info("date:" + date);
            logger.info("打卡状态清空");
        }
    }

    public static Runnable clearPunchTask = new Runnable() {
        @Override
        public void run() {
            try {
                ClearPunch();
                logger.info("清空打卡表!");
            } catch (SQLException e) {
                e.printStackTrace();
                logger.info("打卡表清空失败！");
            }
        }
    };

//    public static  void Timer(){
//        Timer timer = new Timer();
//        timer.schedule(new TimerTask() {
//            // 在run方法中的语句就是定时任务执行时运行的语句。
//            public void run() {
//
//            }
//            // 表示在0秒之后开始执行，并且每3秒执行一次
//        }, 0, 3000);
//    }
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

    // initWordBook("C:\\Users\\Sairen\\Documents\\GitHub\\Hello-word\\TestMINA\\src\\target.csv");
    // changeHistory(6,10074);

}
