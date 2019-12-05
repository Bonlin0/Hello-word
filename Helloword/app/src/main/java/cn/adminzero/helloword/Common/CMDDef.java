package cn.adminzero.helloword.Common;

/**
 * @author: 王翔
 * @date: 2019/11/6-23:44
 * @description: <br>
 * 该类定义了所有的CMD控制命令，限制如下(闭区间)：
 * ①：CMD的类型为short类型，大小两个字节
 * ②：从0-0xFFF被预留为常量定义
 * ③：从0x1000-0x6FFF被预留为Server端使用的指令
 * ④：从0x7000-0xFFFF被预留位Client使用的指令
 * ⑤：指令为short类型，请勿错误定义
 * 为了防止四个人定义冲突，对四个人的区间限制如下：
 * 常量定义        服务端控制指令     客户端控制指令
 * 王翔:   0x0   - 0xFF   0x1000 - 0x1FFF   0x7000 - 0x7FFF
 * 吴昊林: 0x100 - 0x1FF  0x2000 - 0x2FFF   0x8000 - 0x8FFF
 * 赵君臣: 0x200 - 0x2FF  0x3000 - 0x3FFF   0x9000 - 0x9FFF
 * 陈渊:   0x300 - 0x3FF  0x4000 - 0x4FFF   0xA000 - 0xAFFF
 * 剩余区间预留
 * <EndDescription>
 */
public class CMDDef {
    //其他全局常量定义
    public static final int PORT = 3005;
    public static final int PK_MAX_WORD_NUM = 80;
    public static final String MINABroadCast = "cn.adminzero.helloword.MINABROADCAST";
    public static final String INTENT_PUT_EXTRA_CMD = "CMD";
    public static final String INTENT_PUT_EXTRA_DATA = "DATA";
    //  public static final String DBURL = "jdbc:mysql://123.207.173.192:3306/hello_word";
    public static final String DBURL = "jdbc:mysql://123.207.173.192:3306/hello_word?useUnicode=true&useSSL=false&serverTimezone=UTC&autoReconnect=true&failOverReadOnly=false";
    //  private static String URL = "jdbc:mysql://localhost:3306/helloword?useUnicode=true&useSSL=false&serverTimezone=UTC&autoReconnect=true&failOverReadOnly=false";
    // private static String URL = "jdbc:mysql://localhost:3306/helloword?useSSL=false&serverTimezone=UTC";
    public static final String DBDriver = "com.mysql.cj.jdbc.Driver";
    public static final String DBNAME = "root";
    public static final String DBPASSWD = "926b50985a";
    public static final String SuccessConnect = "SuccessConnect";
    public static final String ErrorConnect = "连接网络失败，请检查您的数据连接!";
    public static final String IP = "10.0.2.2";
   // public static final String IP = "123.207.173.192";
    //常量定义

    //通信协议消息传递的数据类型
    //只传递控制指令
    public static final byte PROTOCOL_MESSAGE_NULL = 0x2;
    //传递一个BYTE
    public static final byte PROTOCOL_MESSAGE_BYTE = 0x3;
    //传递一个SHORT
    public static final byte PROTOCOL_MESSAGE_SHORT = 0x4;
    //传递一个INT
    public static final byte PROTOCOL_MESSAGE_INT = 0x5;
    //传递一个String
    public static final byte PROTOCOL_MESSAGE_STRING = 0x6;
    //传递数据
    public static final byte PROTOCOL_MESSAGE_DATA = 0x7;

    //网络连接失败
    public static final short ERROR_CONNECT_NETWORK = 0x30;
    public static final short SUCCESS_CONNECT_NETWORK = 0x31;

    //协议标记
    public static final byte PROTOCOL_FLAG = (byte) 0xE8;


    //服务端控制指令定义区

    //回应注册请求结果
    public static final short REPLY_SIGN_UP_REQUEST = 0x2000;
    //回应登录请求结果
    public static final short REPLY_SIGN_IN_REQUEST = 0x2001;

    public static final short REPLY_UPDATE_USER_REQUESET = (short) 0x2002;

    //回应游戏玩家的对手信息
    public static final short REPLY_GAMER_IFNO = (short) 0x1000;

    //回应游戏结果
    public static final short REPLY_GAME_RESULT = (short) 0x1001;
    //强制下线
    public static final short FORCE_OFFLINE = (short) 0x1002;
    //客户端控制指令定义区


    //注册请求
    public static final short SIGN_UP_REQUESET = (short) 0x8000;

    //申请登录
    public static final short SIGN_IN_REQUESET = (short) 0x8001;
    //用户更新操作
    public static final short UPDATE_USER_REQUESET = (short) 0x8002;

    //被销毁时需要发送的信息
    public static final short DESTORY_SELF_SEND_DATA = (short) 0x7000;
    //请求加入pk游戏
    public static final short JOIN_PK_GAME_REQUEST = (short) 0x7001;
    //放弃匹配
    public static final short GIVE_UP_JOIN_GAME = (short) 0x7002;

    //游戏结果
    public static final short GAME_RESULT = (short) 0x7003;


    //创建小组请求
    public static final short CREATE_GROUP_REQUEST = (short) 0xA001;
    //更新小组请求
    public static final short UPDATE_GROUP_REQUEST = (short) 0xA002;
    //获取小组信息请求
    public static final short GET_GROUP_REQUEST = (short) 0xA003;
    //创建用户History 表
    public static final short CREATE_HISTORY_REQUEST = (short) 0xA004;
    //更新用户History 表
    public static final short UPDATE_HISTORY_REQUEST = (short) 0xA005;
    //获取用户History 表
    public static final short GET_HISTORY_REQUSEST = (short) 0xA006;
    //获取小组成员信息请求
    public static final short GET_GROUPMEMBER_REQUEST = (short) 0xA007;
    //更新词书 对History表的更改
    public static final short CHANGE_WORDBOOK_REQUEST = (short) 0xA008;


    //回应创建小组请求
    public static final short CREATE_GROUP_REPLY = (short) 0x4001;
    //回应更新小组请求
    public static final short UPDATE_GROUP_REPLY = (short) 0x4002;
    //回应获取小组信息请求
    public static final short GET_GROUP_REPLY = (short) 0x4003;
    //创建用户History 表
    public static final short CREATE_HISTORY_REPLY = (short) 0x4004;
    //更新用户History 表
    public static final short UPDATE_HISTORY_REPLY = (short) 0x4005;
    //获取用户History 表
    public static final short GET_HISTORY_REPLY = (short) 0x4006;
    //获取小组成员信息回应
    public static final short GET_GROUPMEMBER_REPLY = (short) 0x4007;
}

