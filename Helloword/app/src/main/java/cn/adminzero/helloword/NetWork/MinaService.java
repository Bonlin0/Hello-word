package cn.adminzero.helloword.NetWork;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.HandlerThread;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import cn.adminzero.helloword.Common.CMDDef;
import cn.adminzero.helloword.Common.Utils.SendMsgMethod;

public class MinaService extends Service {
    private ConnectionThread thread;
    private final String Ip = CMDDef.IP;
    private final int port = CMDDef.PORT;
    @Override
    public void onCreate() {
        super.onCreate();
        thread = new ConnectionThread("mina", getApplicationContext());
        thread.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        thread.disConnect();
        thread=null;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    class ConnectionThread extends HandlerThread {
        private Context context;
        boolean isConnection;
        ConnectionManager mManager;
        public ConnectionThread(String name, Context context){
            super(name);
            this.context = context;
            ConnectionConfig config = new ConnectionConfig.Builder(context)
                    .setIp(Ip)
                    .setPort(port)
                    .setConnectionTimeout(10000).setReadBufferSize(2048*5000).builder();
            mManager = new ConnectionManager(config);
        }

        @Override
        protected void onLooperPrepared() {
            while(true){
                isConnection = mManager.connnect();
                if(isConnection){
                    Log.e("tag", "连接成功");
                    SessionManager.getInstance().writeToServer(SendMsgMethod.getStringMessage(CMDDef.REQUEST_FILE_TEST,"测试String传输和大文件传输!"));
                    break;
                }
                try {
                    Log.e("tag", "尝试重新连接");
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public void disConnect(){
            mManager.disContect();
        }
    }
}
