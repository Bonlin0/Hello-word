package cn.adminzero.helloword;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.IOException;

import cn.adminzero.helloword.Common.CMDDef;
import cn.adminzero.helloword.Common.Utils.SendMsgMethod;
import cn.adminzero.helloword.Common.Utils.SerializeUtils;
import cn.adminzero.helloword.CommonClass.OpponentInfo;
import cn.adminzero.helloword.NetWork.SessionManager;

public class GameQueueActivity extends AppCompatActivity {

    private GameQueueActivityBroadCastReceiver gameQueueActivityBroadCastReceiver = new GameQueueActivityBroadCastReceiver();
    private final IntentFilter intentFilter = new IntentFilter(CMDDef.MINABroadCast);

    class GameQueueActivityBroadCastReceiver extends BroadcastReceiver{
        @SuppressLint("ShowToast")
        @Override
        public void onReceive(Context context, Intent intent) {
            short cmd = intent.getShortExtra(CMDDef.INTENT_PUT_EXTRA_CMD, (short) -1);
            switch (cmd){
                case CMDDef.REPLY_GAMER_IFNO:{
                    try {
                        OpponentInfo opponentInfo = (OpponentInfo) SerializeUtils.serializeToObject(
                                intent.getByteArrayExtra(CMDDef.INTENT_PUT_EXTRA_DATA));
                        Intent intent1 = new Intent(GameQueueActivity.this,WordTestActivity.class);
                        startActivity(intent1);
                        Toast.makeText(GameQueueActivity.this,"ID为"+opponentInfo.getUserID(),Toast.LENGTH_LONG);
                        finish();
                    } catch (IOException | ClassNotFoundException e) {
                        Log.e("tag","反序列化失败!");
                        e.printStackTrace();
                    }
                }
                break;
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_queue);
        SessionManager.getInstance().writeToServer(SendMsgMethod.getNullMessage(CMDDef.JOIN_PK_GAME_REQUEST));

        // TODO 打开单词考察活动应该在收到网络反馈后开始而不是点击进度条
        ProgressBar progressBar2 = findViewById(R.id.progressBar2);
        progressBar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        Button exit_game_queue_button = findViewById(R.id.exit_game_queue_button);
        exit_game_queue_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 退出需要做的工作
                SessionManager.getInstance().writeToServer(SendMsgMethod.getNullMessage(CMDDef.GIVE_UP_JOIN_GAME));
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(GameQueueActivity.this).
                registerReceiver(gameQueueActivityBroadCastReceiver,intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(GameQueueActivity.this).
                unregisterReceiver(gameQueueActivityBroadCastReceiver);
    }
}
