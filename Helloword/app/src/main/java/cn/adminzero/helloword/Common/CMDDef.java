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
    public static final String MINABroadCast = "cn.adminzero.helloword.MINABROADCAST";
    public static final String INTENT_PUT_EXTRA_CMD = "CMD";
    public static final String INTENT_PUT_EXTRA_DATA = "DATA";
   // public static final String IP = "192.168.37.1";//陈渊测试的本机IP
   // public static final int PORT = 3006;//陈渊测试的本机端口
   // public static final String IP = "10.0.2.2";
    public static final String IP = "123.207.173.192";
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

    //协议标记
    public static final byte PROTOCOL_FLAG = (byte) 0xE8;


    //服务端控制指令定义区
    public static final short REPLY_FILE_TEST = 0x1000;

    //回应注册请求结果
    public static final short REPLY_SIGN_UP_REQUEST = 0x2000;


    //客户端控制指令定义区
    public static final short REQUEST_FILE_TEST = 0x7000;

    //注册请求
    public static final short SIGN_UP_REQUESET = (short) 0x8000;
}
