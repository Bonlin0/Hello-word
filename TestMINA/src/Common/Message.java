package Common;

import Common.Utils.SerializeUtils;

import javax.print.attribute.standard.PrinterMessageFromOperator;
import java.io.IOException;
import java.io.Serializable;

/**
 * @author: 王翔
 * @date: 2019/11/7-14:38
 * @description: <br>
 *     用于在同端传递数据的类
 * <EndDescription>
 */
public class Message implements Serializable {
    private byte b;
    private short s;
    private int i;
    private String obj;
    private byte type;
    private short CMD;

    public Message(short cmd ,Object obj) throws IOException {
        String o = SerializeUtils.serialize(obj);
        this.obj = o;
        this.CMD = cmd;
        this.type = CMDDef.PROTOCOL_MESSAGE_OBJECT;
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
    public Message(short cmd)
    {
        CMD = cmd;
        this.type = CMDDef.PROTOCOL_MESSAGE_NULL;
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

    public void setObj(Object obj) throws IOException {
        this.type = CMDDef.PROTOCOL_MESSAGE_OBJECT;
        String o = SerializeUtils.serialize(obj);
        this.obj = o;
    }

    public void setCMD(short CMD) {
        this.CMD = CMD;
    }


    public short getCMD() {
        return CMD;
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

    public Object getObj() throws IOException, ClassNotFoundException {
        return SerializeUtils.serializeToObject(this.obj);
    }
    public int getObjSize()
    {
        return obj.length();
    }
    public String getStringObj()
    {
        return obj;
    }
    public void setStringObj(String object)
    {
        this.obj = object;
    }
    public byte getType() {
        return type;
    }
}
