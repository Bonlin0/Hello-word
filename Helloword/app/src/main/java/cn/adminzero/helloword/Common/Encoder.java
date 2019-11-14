package cn.adminzero.helloword.Common;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.demux.MessageEncoder;

import java.io.IOException;

/**
 * @author: 王翔
 * @date: 2019/11/7-15:06
 * @description: <br>
 * <EndDescription>
 */
public class Encoder implements MessageEncoder<Message> {
    @Override
    public void encode(IoSession ioSession, Message message, ProtocolEncoderOutput protocolEncoderOutput) throws Exception {
        IoBuffer ioBuffer = null;
        switch (message.getType()) {
            case CMDDef.PROTOCOL_MESSAGE_NULL:
                ioBuffer = IoBuffer.allocate(8).setAutoExpand(true);
                //放置标记，1
                ioBuffer.put((byte) 0xE8);
                //放置长度，4
                ioBuffer.putInt(0);
                //放置类型，1
                ioBuffer.put(CMDDef.PROTOCOL_MESSAGE_NULL);
                //放置指令，2
                ioBuffer.putShort(message.getCMD());
                ioBuffer.flip();
                protocolEncoderOutput.write(ioBuffer);
                break;
            case CMDDef.PROTOCOL_MESSAGE_BYTE:
                ioBuffer = IoBuffer.allocate(9).setAutoExpand(true);
                //放置标记，1
                ioBuffer.put((byte) 0xE8);
                //放置长度，4
                ioBuffer.putInt(1);
                //放置类型，1
                ioBuffer.put(CMDDef.PROTOCOL_MESSAGE_BYTE);
                //放置指令，2
                ioBuffer.putShort(message.getCMD());
                //放置数据，1
                ioBuffer.put(message.getB());
                ioBuffer.flip();
                protocolEncoderOutput.write(ioBuffer);
                break;
            case CMDDef.PROTOCOL_MESSAGE_INT:
                ioBuffer = IoBuffer.allocate(12).setAutoExpand(true);
                //放置标记，1
                ioBuffer.put((byte) 0xE8);
                //放置长度，4
                ioBuffer.putInt(4);
                //放置类型，1
                ioBuffer.put(CMDDef.PROTOCOL_MESSAGE_INT);
                //放置指令，2
                ioBuffer.putShort(message.getCMD());
                //放置数据
                ioBuffer.putInt(message.getI());
                ioBuffer.flip();
                protocolEncoderOutput.write(ioBuffer);
                break;
            case CMDDef.PROTOCOL_MESSAGE_SHORT:
                ioBuffer = IoBuffer.allocate(10).setAutoExpand(true);
                //放置标记，1
                ioBuffer.put((byte) 0xE8);
                //放置长度，4
                ioBuffer.putInt(2);
                //放置类型，1
                ioBuffer.put(CMDDef.PROTOCOL_MESSAGE_SHORT);
                //放置指令，2
                ioBuffer.putShort(message.getCMD());
                //放置数据
                ioBuffer.putShort(message.getS());
                ioBuffer.flip();
                protocolEncoderOutput.write(ioBuffer);
                break;
            case CMDDef.PROTOCOL_MESSAGE_STRING:
                int strsize = message.getStrSize();
                ioBuffer = IoBuffer.allocate(strsize + 8).setAutoExpand(true);
                //放置标记，1
                ioBuffer.put((byte) 0xE8);
                //放置长度，4
                ioBuffer.putInt(strsize);
                //放置类型，1
                ioBuffer.put(CMDDef.PROTOCOL_MESSAGE_STRING);
                //放置指令，2
                ioBuffer.putShort(message.getCMD());
                //放置数据
                ioBuffer.putObject(message.getString());
                ioBuffer.flip();
                protocolEncoderOutput.write(ioBuffer);
                break;
            case CMDDef.PROTOCOL_MESSAGE_DATA:
                int dataSize = message.getDataSize();
                ioBuffer = IoBuffer.allocate(dataSize + 8).setAutoExpand(true);
                //放置标记，1
                ioBuffer.put((byte) 0xE8);
                //放置长度，4
                ioBuffer.putInt(dataSize);
                //放置类型，1
                ioBuffer.put(CMDDef.PROTOCOL_MESSAGE_DATA);
                //放置指令，2
                ioBuffer.putShort(message.getCMD());
                //放置数据
                ioBuffer.put(message.getData());
                ioBuffer.flip();
                protocolEncoderOutput.write(ioBuffer);
                break;
        }
    }
}
