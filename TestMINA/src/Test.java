import Common.Decoder;
import Common.Encoder;
import Common.MessageCodecFactory;
import org.apache.commons.logging.Log;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import java.net.InetSocketAddress;

/**
 * @author: 王翔
 * @date: 2019/11/7-18:36
 * @description: <br>
 * <EndDescription>
 */
public class Test {
    public static void main(String []args)
    {
        IoConnector connector = new NioSocketConnector();  // 创建连接
        // 设置链接超时时间
        connector.setConnectTimeout(30000);
        // 添加过滤器
        connector.getFilterChain().addLast(   //添加消息过滤器
                "codec",
                new ProtocolCodecFilter(new MessageCodecFactory(new Decoder(),new Encoder())));
        // 添加业务逻辑处理器类
        connector.setHandler(new Demo1ClientHandler());// 添加业务处理
        IoSession session = null;
        try {
            ConnectFuture future = connector.connect(new InetSocketAddress(
                    "123.207.173.192", 3005));// 创建连接
            future.awaitUninterruptibly();// 等待连接创建完成
            session= future.getSession();// 获得session
            session.write("我爱你mina");// 发送消息
        }catch (Exception e) {
        }
    }
}
