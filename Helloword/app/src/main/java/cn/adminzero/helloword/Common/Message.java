package cn.adminzero.helloword.Common;

import cn.adminzero.helloword.Common.Utils.SerializeUtils;

import java.io.IOException;
import java.io.Serializable;

/**
 * @author: 王翔
 * @date: 2019/11/7-14:38
 * @description: <br>
 * 用于在同端传递数据的类
 * <EndDescription>
 */
public class Message implements Serializable {
    private byte b;
    private short s;
    private int i;
    private String str;
    private byte type;
    private short CMD;
    private byte[] data;

    public Message(short cmd, byte[] bytes) {
        data = bytes;
        this.CMD = cmd;
        this.type = CMDDef.PROTOCOL_MESSAGE_DATA;
    }

    public Message(short cmd, Object obj) throws IOException {
        data = SerializeUtils.serialize(obj);
        this.CMD = cmd;
        this.type = CMDDef.PROTOCOL_MESSAGE_DATA;
    }

    public Message(short cmd, String str) {
        this.CMD = cmd;
        this.str = str;
        this.type = CMDDef.PROTOCOL_MESSAGE_STRING;
    }

    public Message(short cmd, byte b) {
        this.b = b;
        this.CMD = cmd;
        this.type = CMDDef.PROTOCOL_MESSAGE_BYTE;
    }

    public Message(short cmd, short s) {
        this.s = s;
        CMD = cmd;
        this.type = CMDDef.PROTOCOL_MESSAGE_SHORT;
    }

    public Message(short cmd, int i) {
        this.i = i;
        CMD = cmd;
        this.type = CMDDef.PROTOCOL_MESSAGE_INT;
    }

    public Message(short cmd) {
        CMD = cmd;
        this.type = CMDDef.PROTOCOL_MESSAGE_NULL;
    }

    public void setData(byte[] bytes) {
        this.type = CMDDef.PROTOCOL_MESSAGE_DATA;
        data = bytes;
    }

    public void setB(byte b) {
        this.type = CMDDef.PROTOCOL_MESSAGE_BYTE;
        this.b = b;
    }

    public void setS(short s) {
        this.type = CMDDef.PROTOCOL_MESSAGE_SHORT;
        this.s = s;
    }

    public void setI(int i) {
        this.type = CMDDef.PROTOCOL_MESSAGE_INT;
        this.i = i;
    }

    //需要传递的是String
    public void setString(String str) {
        this.str = str;
        this.type = CMDDef.PROTOCOL_MESSAGE_STRING;
    }

    public void setObj(Object obj) throws IOException {
        this.data = SerializeUtils.serialize(obj);
        this.type = CMDDef.PROTOCOL_MESSAGE_DATA;
    }

    public void setCMD(short CMD) {
        this.CMD = CMD;
    }


    public short getCMD() {
        return CMD;
    }

    public byte[] getData() {
        return data;
    }

    public byte getB() {
        return b;
    }

    public short getS() {
        return s;
    }

    public int getI() {
        return i;
    }

    public String getString() {
        return str;
    }

    public int getStrSize() {
        return str.length();
    }

    public int getDataSize() {
        return data.length;
    }
    public Object getObj() throws IOException, ClassNotFoundException {
        return SerializeUtils.serializeToObject(this.data);
    }

    public byte getType() {
        return type;
    }
}
