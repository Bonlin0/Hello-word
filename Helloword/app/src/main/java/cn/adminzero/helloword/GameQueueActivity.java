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

import android.os.CountDownTimer;
import android.widget.TextView;


public class GameQueueActivity extends AppCompatActivity {

    private GameQueueActivityBroadCastReceiver gameQueueActivityBroadCastReceiver = new GameQueueActivityBroadCastReceiver();
    private final IntentFilter intentFilter = new IntentFilter(CMDDef.MINABroadCast);
    TextView time_to_start_game_textView;
    ProgressBar progressBar2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_queue);
        SessionManager.getInstance().writeToServer(SendMsgMethod.getNullMessage(CMDDef.JOIN_PK_GAME_REQUEST));

        // TODO 打开单词考察活动应该在收到网络反馈后开始而不是点击进度条
        progressBar2 = findViewById(R.id.progressBar2);

        Button exit_game_queue_button = findViewById(R.id.exit_game_queue_button);
        exit_game_queue_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 退出需要做的工作
                SessionManager.getInstance().writeToServer(SendMsgMethod.getNullMessage(CMDDef.GIVE_UP_JOIN_GAME));
                finish();
            }
        });

        // 展示倒计时的TextView
        time_to_start_game_textView = findViewById(R.id.time_to_start_game_textView);

    }

    private void readyAndUpdateUi() {
        progressBar2.setVisibility(View.INVISIBLE);
        time_to_start_game_textView.setVisibility(View.VISIBLE);
        TextView please_wait_game_queue_textView = findViewById(R.id.please_wait_game_queue_textView);
        please_wait_game_queue_textView.setText("已经为您匹配到对手，比赛即将开始……");

        // 倒计时 4s
        CountDownTimer timer = new CountDownTimer(4000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Integer timeToShow = (int) millisUntilFinished / 1000;
                time_to_start_game_textView.setText("00:0" + timeToShow.toString());
            }

            @Override
            public void onFinish() {
                //  启动 单词测试活动
                Intent intent = new Intent(GameQueueActivity.this, WordTestActivity.class);
                startActivity(intent);
                finish();
            }
        }.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(GameQueueActivity.this).
                registerReceiver(gameQueueActivityBroadCastReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(GameQueueActivity.this).
                unregisterReceiver(gameQueueActivityBroadCastReceiver);
    }


    class GameQueueActivityBroadCastReceiver extends BroadcastReceiver {
        @SuppressLint("ShowToast")
        @Override
        public void onReceive(Context context, Intent intent) {
            short cmd = intent.getShortExtra(CMDDef.INTENT_PUT_EXTRA_CMD, (short) -1);
            switch (cmd) {
                case CMDDef.REPLY_GAMER_IFNO: {
                    try {
                        OpponentInfo opponentInfo = (OpponentInfo) SerializeUtils.serializeToObject(
                                intent.getByteArrayExtra(CMDDef.INTENT_PUT_EXTRA_DATA));
                        readyAndUpdateUi();
                    } catch (IOException | ClassNotFoundException e) {
                        Log.e("tag", "反序列化失败!");
                        e.printStackTrace();
                    }
                }
                break;
            }
        }
    }
}
