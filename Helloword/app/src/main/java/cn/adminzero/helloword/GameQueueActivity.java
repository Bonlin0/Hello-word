package cn.adminzero.helloword;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

public class GameQueueActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_queue);

        // TODO 打开单词考察活动应该在收到网络反馈后开始而不是点击进度条
        ProgressBar progressBar2 = findViewById(R.id.progressBar2);
        progressBar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GameQueueActivity.this,WordTestActivity.class);
                startActivity(intent);
                // TODO 不知道是否需要在打开新活动后结束自己？
                finish();
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
    }
}
