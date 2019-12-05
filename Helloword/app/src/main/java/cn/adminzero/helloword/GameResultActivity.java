package cn.adminzero.helloword;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import cn.adminzero.helloword.CommonClass.GameResult;
import cn.adminzero.helloword.CommonClass.OpponentInfo;

public class GameResultActivity extends AppCompatActivity {


    private static final String TAG = "ShowGameResult";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_result);
        // 获取传来的信息
        Intent intent = getIntent();
        OpponentInfo opponentInfo = (OpponentInfo) intent.getSerializableExtra("opponentInfo");
        GameResult gameResult = (GameResult)intent.getSerializableExtra("gameResult");
        // 异常情况 出现空
        if(opponentInfo == null || gameResult == null){
            finish();
            Log.e(TAG, "onCreate: Intent Extra message are null.");
            return;
        }
        // 使用传来的消息更新UI
        TextView win_or_lose_big_game_result_textView = findViewById(R.id.win_or_lose_big_game_result_textView);
        TextView info_about_opponent_game_result_textView = findViewById(R.id.info_about_opponent_game_result_textView);
        TextView pk_point_info_game_result_textView = findViewById(R.id.pk_point_info_game_result_textView);
        String string_pk_point_info_game_result_textView= null;


        if(gameResult.isResult()){
            // 我方胜利
            win_or_lose_big_game_result_textView.setText("YOU WIN!");
            info_about_opponent_game_result_textView.setText("你击败了 "+opponentInfo.getNickName()+" ！");
            string_pk_point_info_game_result_textView= "PK 评分： "+gameResult.getNowScore()+" ( + "+gameResult.getAddScore()+" )";
        }
        else{
            win_or_lose_big_game_result_textView.setText("YOU LOSE...");
            info_about_opponent_game_result_textView.setText("你惜败于 "+opponentInfo.getNickName()+" 。");
            string_pk_point_info_game_result_textView = "PK 评分： "+gameResult.getNowScore()+" ( - "+gameResult.getAddScore()+" )";
        }
        pk_point_info_game_result_textView.setText(string_pk_point_info_game_result_textView);
    }
}
