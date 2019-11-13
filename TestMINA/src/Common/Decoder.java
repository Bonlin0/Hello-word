package Common;

import Server.ServerHandle;
import org.apache.log4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.demux.MessageDecoder;
import org.apache.mina.filter.codec.demux.MessageDecoderResult;

import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author: 王翔
 * @date: 2019/11/7-15:07
 * @description: <br>
 * <EndDescription>
 */
public class Decoder implements MessageDecoder {
    public static org.apache.log4j.Logger logger = Logger.getLogger(Decoder.class);

    @Override
    public MessageDecoderResult decodable(IoSession ioSession, IoBuffer ioBuffer) {
        //少于包头长度
        if (ioBuffer.remaining() < 8)
            return MessageDecoderResult.NEED_DATA;
        //获取标志
        byte tag = ioBuffer.get();
        //标志不合法
        if (tag != CMDDef.PROTOCOL_FLAG) {
            logger.info("协议标记不支持，为" + tag);
            return MessageDecoderResult.NOT_OK;
        }
        //判断数据区长度
        int len = ioBuffer.getInt();
        //如果数据还不够
        if (ioBuffer.remaining() < 3 + len)
            return MessageDecoderResult.NEED_DATA;
        return MessageDecoderResult.OK;
    }

    @Override
    public MessageDecoderResult decode(IoSession ioSession, IoBuffer ioBuffer, ProtocolDecoderOutput protocolDecoderOutput) throws Exception {
        byte tag = ioBuffer.get();
        int len = ioBuffer.getInt();
        byte type = ioBuffer.get();
        short cmd = ioBuffer.getShort();
        Message mes = new Message(cmd);
        switch (type) {
            case CMDDef.PROTOCOL_MESSAGE_NULL:
                break;
            case CMDDef.PROTOCOL_MESSAGE_BYTE:
                byte b = (byte) ioBuffer.getChar();
                mes.setB(b);
                break;
            case CMDDef.PROTOCOL_MESSAGE_INT:
                int i = ioBuffer.getInt();
                mes.setI(i);
                break;
            case CMDDef.PROTOCOL_MESSAGE_SHORT:
                short s = ioBuffer.getShort();
                mes.setS(s);
                break;
            case CMDDef.PROTOCOL_MESSAGE_OBJECT:
                String str = (String) ioBuffer.getObject();
                mes.setStringObj(str);
                break;
        }
        protocolDecoderOutput.write(mes);
        return MessageDecoderResult.OK;
    }

    @Override
    public void finishDecode(IoSession ioSession, ProtocolDecoderOutput protocolDecoderOutput) throws Exception {

    }
}
