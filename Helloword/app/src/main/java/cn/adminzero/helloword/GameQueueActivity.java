package cn.adminzero.helloword;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class GameQueueActivity extends AppCompatActivity {

    TextView time_to_start_game_textView;
    ProgressBar progressBar2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_queue);

        // TODO 打开单词考察活动应该在收到网络反馈后开始而不是点击进度条
        progressBar2 = findViewById(R.id.progressBar2);
        progressBar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readyAndUpdateUi();
            }
        });

        Button exit_game_queue_button = findViewById(R.id.exit_game_queue_button);
        exit_game_queue_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 退出需要做的工作
                finish();
            }
        });

        // 展示倒计时的TextView
        time_to_start_game_textView = findViewById(R.id.time_to_start_game_textView);

    }

    private void readyAndUpdateUi()
    {
        progressBar2.setVisibility(View.INVISIBLE);
        time_to_start_game_textView.setVisibility(View.VISIBLE);
        TextView please_wait_game_queue_textView = findViewById(R.id.please_wait_game_queue_textView);
        please_wait_game_queue_textView.setText("已经为您匹配到对手，比赛即将开始……");

        // 倒计时 4s
        CountDownTimer timer = new CountDownTimer(4000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Integer timeToShow = (int)millisUntilFinished/1000;
                time_to_start_game_textView.setText("00:0"+timeToShow.toString());
            }

            @Override
            public void onFinish() {
                //  启动 单词测试活动
                Intent intent = new Intent(GameQueueActivity.this,WordTestActivity.class);
                startActivity(intent);
                finish();
            }
        }.start();
    }
}
