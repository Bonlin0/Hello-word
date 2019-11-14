package cn.adminzero.helloword.Common.Utils;
import java.io.IOException;

import cn.adminzero.helloword.Common.Message;

/**
 * @author: 王翔
 * @date: 2019/11/7-14:31
 * @description: <br>
 * 该类为方法集合，用于获取需要传递的Message
 * <EndDescription>
 */
public class SendMsgMethod {
    public static Message getNullMessage(short CMD) {
        return new Message(CMD);
    }
    public static Message getByteMessage(short CMD, byte b) {
        return new Message(CMD, b);
    }
    public static Message getIntMessage(short CMD, int i) {
        return new Message(CMD, i);
    }
    public static Message getShortMessage(short CMD, short s) {
        return new Message(CMD, s);
    }
    public static Message getObjectMessage(short CMD, Object obj) throws IOException {
        return new Message(CMD, obj);
    }
}
