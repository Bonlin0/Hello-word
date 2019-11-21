package cn.adminzero.helloword.NetWork;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.util.Log;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.FilterEvent;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.LogLevel;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import java.lang.ref.WeakReference;
import java.net.InetSocketAddress;

import cn.adminzero.helloword.Common.CMDDef;
import cn.adminzero.helloword.Common.Decoder;
import cn.adminzero.helloword.Common.Encoder;
import cn.adminzero.helloword.Common.Message;
import cn.adminzero.helloword.Common.MessageCodecFactory;
import cn.adminzero.helloword.CommonClass.UserNoPassword;

public class ConnectionManager {
    private ConnectionConfig mConfig;
    private WeakReference<Context> mContext;
    private NioSocketConnector mConnection;
    private IoSession mSession;
    private InetSocketAddress mAddress;

    public ConnectionManager(ConnectionConfig config) {
        this.mConfig = config;
        this.mContext = new WeakReference<>(config.getContext());
        init();
    }

    public WeakReference<Context> getmContext() {
        return mContext;
    }

    private void init() {
        mAddress = new InetSocketAddress(mConfig.getIp(), mConfig.getPort());
        mConnection = new NioSocketConnector();
        SessionManager.getInstance().setmContext(mContext);
        //   mConnection.getSessionConfig().setReadBufferSize(mConfig.getReadBufferSize());
        //设置过滤链
        LoggingFilter lf = new LoggingFilter();
        lf.setMessageReceivedLogLevel(LogLevel.DEBUG);
        mConnection.getFilterChain().addLast("logger", lf);
        mConnection.getFilterChain().addLast("codec", new ProtocolCodecFilter(new MessageCodecFactory(new Decoder(), new Encoder())));
        //业务逻辑
        DefaultHandler defaultHandler = new DefaultHandler(mContext.get());
        mConnection.setHandler(defaultHandler);
        mConnection.setDefaultRemoteAddress(mAddress);
    }

    /**
     * 与服务器连接
     *
     * @return
     */
    public boolean connnect() {
        Log.e("tag", "准备连接");
        try {
            ConnectFuture future = mConnection.connect();
            future.awaitUninterruptibly();
            mSession = future.getSession();
            SessionManager.getInstance().setmSessionsion(mSession);
            Log.e("tag", "连接成功!");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return mSession != null;
    }

    /**
     * 断开连接
     */
    public void disContect() {
        mConnection.dispose();
        mConnection = null;
        mSession = null;
        mAddress = null;
        mContext = null;
        Log.e("tag", "断开连接");
    }

    private static class DefaultHandler extends IoHandlerAdapter {
        private Context mContext;

        void DataArriveBroadcast(short cmd, byte[] data) {
            Intent intent = new Intent(CMDDef.MINABroadCast);
            intent.putExtra(CMDDef.INTENT_PUT_EXTRA_CMD, cmd);
            intent.putExtra(CMDDef.INTENT_PUT_EXTRA_DATA, data);
            LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
        }

        void DataArriveBroadcast(short cmd) {
            Intent intent = new Intent(CMDDef.MINABroadCast);
            intent.putExtra(CMDDef.INTENT_PUT_EXTRA_CMD, cmd);
            LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
        }

        void DataArriveBroadcast(short cmd, byte b) {
            Intent intent = new Intent(CMDDef.MINABroadCast);
            intent.putExtra(CMDDef.INTENT_PUT_EXTRA_CMD, cmd);
            intent.putExtra(CMDDef.INTENT_PUT_EXTRA_DATA, b);
            LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
        }

        void DataArriveBroadcast(short cmd, int i) {
            Intent intent = new Intent(CMDDef.MINABroadCast);
            intent.putExtra(CMDDef.INTENT_PUT_EXTRA_CMD, cmd);
            intent.putExtra(CMDDef.INTENT_PUT_EXTRA_DATA, i);
            LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
        }

        void DataArriveBroadcast(short cmd, String str) {
            Intent intent = new Intent(CMDDef.MINABroadCast);
            intent.putExtra(CMDDef.INTENT_PUT_EXTRA_CMD, cmd);
            intent.putExtra(CMDDef.INTENT_PUT_EXTRA_DATA, str);
            LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
        }

        void DataArriveBroadcast(short cmd, short s) {
            Intent intent = new Intent(CMDDef.MINABroadCast);
            intent.putExtra(CMDDef.INTENT_PUT_EXTRA_CMD, cmd);
            intent.putExtra(CMDDef.INTENT_PUT_EXTRA_DATA, s);
            LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
        }

        @Override
        public void sessionCreated(IoSession session) throws Exception {
            super.sessionCreated(session);
        }

        @Override
        public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
            super.sessionIdle(session, status);
        }

        @Override
        public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
            super.exceptionCaught(session, cause);
        }

        @Override
        public void messageSent(IoSession session, Object message) throws Exception {
            super.messageSent(session, message);
        }

        @Override
        public void inputClosed(IoSession session) throws Exception {
            super.inputClosed(session);
        }

        @Override
        public void event(IoSession session, FilterEvent event) throws Exception {
            super.event(session, event);
        }

        private DefaultHandler(Context context) {
            this.mContext = context;
        }

        @Override
        public void sessionClosed(IoSession session) throws Exception {
            super.sessionClosed(session);
        }

        @Override
        public void sessionOpened(IoSession session) throws Exception {
            super.sessionOpened(session);
        }

        @Override
        public void messageReceived(IoSession session, Object message) throws Exception {
            super.messageReceived(session, message);
            Message mes = (Message) message;
            switch (mes.getCMD()) {
                case CMDDef.REPLY_SIGN_UP_REQUEST:
                case CMDDef.REPLY_SIGN_IN_REQUEST: {
                    DataArriveBroadcast(mes.getCMD(), mes.getData());
                }
                break;
                default:
            }
        }
    }

}