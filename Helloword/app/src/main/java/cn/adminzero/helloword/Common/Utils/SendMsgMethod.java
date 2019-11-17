package cn.adminzero.helloword.Common.Utils;

import android.util.Log;

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

    public static Message getObjectMessage(short CMD, Object obj){
        Message m = new Message(CMD);
        try {
            m.setData(SerializeUtils.serialize(obj));
        }catch (IOException e)
        {
            e.printStackTrace();
            Log.e("tag","序列化失败!");
        }
        return m;
    }

    public static Message getDataMessage(short CMD, byte[] bytes) {
        return new Message(CMD, bytes);
    }

    public static Message getStringMessage(short CMD, String str) {
        return new Message(CMD, str);
    }
}
