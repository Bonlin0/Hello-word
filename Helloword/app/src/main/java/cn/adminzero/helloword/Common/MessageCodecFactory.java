package cn.adminzero.helloword.Common;

import org.apache.mina.filter.codec.demux.DemuxingProtocolCodecFactory;
import org.apache.mina.filter.codec.demux.MessageDecoder;
import org.apache.mina.filter.codec.demux.MessageEncoder;

/**
 * @author: 王翔
 * @date: 2019/11/7-15:43
 * @description: <br>
 * <EndDescription>
 */
public class MessageCodecFactory extends DemuxingProtocolCodecFactory {
    private MessageEncoder<Message> encoder;
    private MessageDecoder decoder;
    public MessageCodecFactory(MessageDecoder decoder,MessageEncoder<Message> encoder){
        this.decoder = decoder;
        this.encoder = encoder;
        addMessageDecoder(this.decoder);
        addMessageEncoder(Message.class,this.encoder);
    }
}