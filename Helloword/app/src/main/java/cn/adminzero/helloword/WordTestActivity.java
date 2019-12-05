package cn.adminzero.helloword;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import cn.adminzero.helloword.Common.CMDDef;
import cn.adminzero.helloword.Common.Utils.SendMsgMethod;
import cn.adminzero.helloword.Common.Utils.SerializeUtils;
import cn.adminzero.helloword.CommonClass.GameResult;
import cn.adminzero.helloword.CommonClass.OpponentInfo;
import cn.adminzero.helloword.NetWork.SessionManager;
import cn.adminzero.helloword.db.ServerDbUtil;
import cn.adminzero.helloword.util.MediaPlayUtil;
import cn.adminzero.helloword.util.RandomizeArrayList;
import cn.adminzero.helloword.util.Words;
import cn.adminzero.helloword.util.WordsLevelUtil;
import cn.adminzero.helloword.util.WordsUtil;

/*

  Author : Whl, Wx
  Content : 用于测试用户单词的活动，目前用于两个任务
              1. PK Game
              2. 称号升级


*/

public class WordTestActivity extends BaseActivity {
    // 用于PK Game的广播接收器
    class WordTestActivityBoradCastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // PK game 结束后接受到服务器反馈
            short cmd = intent.getShortExtra(CMDDef.INTENT_PUT_EXTRA_CMD, (short) -1);
            switch (cmd) {
                case CMDDef.REPLY_GAME_RESULT: {
                    byte[] data = intent.getByteArrayExtra(CMDDef.INTENT_PUT_EXTRA_DATA);
                    try {
                        GameResult gameResult = (GameResult) SerializeUtils.serializeToObject(data);
                        Log.e("tag", "PK结果:" + gameResult.isResult());
                        Log.e("tag", "加/减分数:" + gameResult.getAddScore());
                        Log.e("tag", "现在分数:" + gameResult.getNowScore());
                        // update userNoPassword
                        App.userNoPassword_global.setpKPoint(gameResult.getNowScore());
                        // 进入结果展示活动并杀死本活动
                        Intent intentToGameResultActivity = new Intent(WordTestActivity.this, GameResultActivity.class);
                        intentToGameResultActivity.putExtra("opponentInfo", opponentInfo);
                        intentToGameResultActivity.putExtra("gameResult", gameResult);
                        startActivity(intentToGameResultActivity);
                        finish();

                    } catch (IOException | ClassNotFoundException e) {
                        Log.e("tag","未知错误!");
                        e.printStackTrace();
                    }
                }
                break;
            }
        }
    }

    Boolean is_from_game;
    Boolean is_from_title_upgrade;
    // PK game 中存储对手信息的变量
    OpponentInfo opponentInfo;
    // 存储全部考核单词的队列
    ArrayList<Words> testWordsArrayList;
    TextView words_correct_in_test_textView;
    TextView time_left_in_test_textView;
    Words wordToTest;
    int theRightIndex; // 当前正确选项标号 0.1.2.3
    Integer rightWordsNumber; // 正确完成的单词数
    Integer finishedWordsNumber; // 已经完成的单词数
    ProgressBar progressBar_word_test;
    Integer maxWordsNumber;
    TextView word_content_test_textView;
    TextView phonemic_test_textView;
    ImageButton remember_pronounce_test_button;
    Button choice_1;
    Button choice_2;
    Button choice_3;
    Button choice_4;
    CountDownTimer timer; // 用于倒计时的变量

    Integer TestTime; // 用于测试的倒计时
    ArrayList<Words> preTestWordsList; // 用于称号升级的临时ArrayList

    private WordTestActivityBoradCastReceiver Receiver;
    private IntentFilter intentFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_test);
        intentFilter = new IntentFilter(CMDDef.MINABroadCast);
        Receiver = new WordTestActivityBoradCastReceiver();

        maxWordsNumber = 20; // 设成20避免没有设置出bug


        Intent intent = getIntent();
        is_from_game = intent.getBooleanExtra("is_from_game", false);
        is_from_title_upgrade = intent.getBooleanExtra("is_from_title_upgrade",false);
        // 在当前版本，如果上面的两个bool都为false一定存在问题。
        if(!is_from_title_upgrade&!is_from_game){
            Log.e("WordTestActivity","onCreate: I dont know who called me.");
            finish();
        }
        if (is_from_game) {
            opponentInfo = (OpponentInfo) intent.getSerializableExtra("test_words_id");
            testWordsArrayList = shortArrayToArrayList(opponentInfo.getPkWords());
            maxWordsNumber = CMDDef.PK_MAX_WORD_NUM;
            TestTime = 45000; // PK game 用时45s
            Toast.makeText(this, "你的对手是 " + opponentInfo.getNickName(), Toast.LENGTH_SHORT).show();
        }
        if (is_from_title_upgrade) {
            initFromTitle();
        }
        // 左上角正确单词数
        words_correct_in_test_textView = findViewById(R.id.words_correct_in_test_textView);
        // 上方进度条
        progressBar_word_test = findViewById(R.id.progressBar_word_test);
        // 右上角倒计时功能
        time_left_in_test_textView = findViewById(R.id.time_left_in_test_textView);
        // 初始化倒计时
        initTimerAndStart();
        // 为单词UI view赋值
        word_content_test_textView = findViewById(R.id.word_content_test_textView);
        phonemic_test_textView = findViewById(R.id.phonemic_test_textView);
        remember_pronounce_test_button = findViewById(R.id.remember_pronounce_test_button);
        choice_1 = findViewById(R.id.choice_1);
        choice_2 = findViewById(R.id.choice_2);
        choice_3 = findViewById(R.id.choice_3);
        choice_4 = findViewById(R.id.choice_4);
        // 初始化正确单词数以及已完成单词数 并更新UI
        rightWordsNumber = 0;
        finishedWordsNumber = 0;
        progressBar_word_test.setMax(maxWordsNumber);
        simpleUpdateUI();
        // 设置选项按钮点击监听
        choice_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickAnAnswer(0);
            }
        });
        choice_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickAnAnswer(1);
            }
        });
        choice_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickAnAnswer(2);
            }
        });
        choice_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickAnAnswer(3);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(Receiver,intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(Receiver);
    }

    private void onClickAnAnswer(int choice) {
        if (choice == theRightIndex) {
            // 用户选择了正确答案
            rightWordsNumber++;
        }
        finishedWordsNumber++;
        testWordsArrayList.remove(0);
        if (testWordsArrayList.size() == 0) {
            // 当用户提前完成了所有单词
            if(is_from_game){
                // 如果是game 是不可能的，因为考题在不随机选择的情况下是不可能做完的。所以直接重新开考
                testWordsArrayList = shortArrayToArrayList(opponentInfo.getPkWords());
                rightWordsNumber = 0;
                finishedWordsNumber = 0;
                simpleUpdateUI();
                Snackbar.make(findViewById(R.id.word_test_constraint_layout),"请不要胡乱选择！已经清空了您的答题记录。",Snackbar.LENGTH_SHORT).show();
            }
            if(is_from_title_upgrade){
                // 如果是升级称号，则是正确的,表示在给定时间内完成了考题,展示对话框显示结算结果
                showTitleUpgradeDialog();
            }

        }
        else{
            simpleUpdateUI();
        }

    }

    private void simpleUpdateUI() {
        wordToTest = testWordsArrayList.get(0);
        progressBar_word_test.setProgress(finishedWordsNumber);
        words_correct_in_test_textView.setText(rightWordsNumber.toString());
        // 更新四个选项
        Random r = new Random(System.currentTimeMillis());
        Words wrongWord1 = WordsUtil.getWordById(r.nextInt(10000)); // 这里设成一万因为单词库应该大于10000
        Words wrongWord2 = WordsUtil.getWordById(r.nextInt(10000));
        Words wrongWord3 = WordsUtil.getWordById(r.nextInt(10000));
        Words wrongWord4 = WordsUtil.getWordById(r.nextInt(10000));
        choice_1.setText(wrongWord1.getTranslation());
        choice_2.setText(wrongWord2.getTranslation());
        choice_3.setText(wrongWord3.getTranslation());
        choice_4.setText(wrongWord4.getTranslation());
        theRightIndex = r.nextInt(4);
        switch (theRightIndex) {
            case 0:
                choice_1.setText(wordToTest.getTranslation());
                break;
            case 1:
                choice_2.setText(wordToTest.getTranslation());
                break;
            case 2:
                choice_3.setText(wordToTest.getTranslation());
                break;
            case 3:
                choice_4.setText(wordToTest.getTranslation());
                break;
        }
        word_content_test_textView.setText(wordToTest.getWord());
        phonemic_test_textView.setText("/" + wordToTest.getPhonetic() + "/");
        remember_pronounce_test_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 播放声音
                MediaPlayUtil player = new MediaPlayUtil();
                player.playword(wordToTest.getWord());
            }
        });


    }

    // 将一个 short数组 通过 查询数据库转为 Words 的 ArrayList的函数
    ArrayList<Words> shortArrayToArrayList(short pkWords[]) {
        ArrayList<Words> result = new ArrayList<>();
        for (short pkWordsId : pkWords
        ) {
            result.add(WordsUtil.getWordById(pkWordsId));
        }
        return result;
    }

    // 在升级称号活动结束（提前完成或者时间截止时显示的UI)
    private void showTitleUpgradeDialog() {
        // 先将计时器关闭避免发生错误
        if(timer!=null){
            timer.cancel();
        }
        Double correctRatio = new Double(rightWordsNumber)/new Double(maxWordsNumber) * 100.0;
        String messageToShow;
        final AlertDialog.Builder builder;
        if(correctRatio>=84.9){
            messageToShow = "恭喜你！成功通过了测验！你的称号已经升级！\n";
            messageToShow = messageToShow + "共完成单词：" + finishedWordsNumber.toString() + "\n正确单词数:"+rightWordsNumber.toString() + "\n正确率"+correctRatio.toString()+"%";
            builder = new AlertDialog.Builder(this).setIcon(R.drawable.ic_sentiment_very_satisfied_blue_24dp).setTitle("Congratulations!")
                    .setMessage(messageToShow).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            finish();
                        }
                    });
            App.userNoPassword_global.setLevel(App.userNoPassword_global.getLevel()+1);
            // 网络同步
            ServerDbUtil.Upadte_UserNoPassword();
        }
        else{
            messageToShow = "Oops, 看来你没能达到85%的正确率要求，再试试吧\n";
            messageToShow = messageToShow + "共完成单词：" + finishedWordsNumber.toString() + "\n正确单词数:"+rightWordsNumber.toString() + "\n正确率"+correctRatio.toString()+"%";

            builder = new AlertDialog.Builder(this).setIcon(R.drawable.ic_sentiment_very_dissatisfied_blue_24dp).setTitle("Failed")
                    .setMessage(messageToShow).setPositiveButton("重试", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // 选择重新开始测试
                            initFromTitle();
                            // 初始化正确单词数以及已完成单词数 并更新UI
                            rightWordsNumber = 0;
                            finishedWordsNumber = 0;
                            progressBar_word_test.setMax(maxWordsNumber);
                            initTimerAndStart();
                            simpleUpdateUI();
                        }
                    }).setNegativeButton("取消（消极）", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            finish();
                        }
                    });
        }

        builder.setCancelable(false);
        builder.create().show();
    }

    private  void initFromTitle(){
        String levelToAppend = "";
        // First get all the words that user have recited (已背熟）
        preTestWordsList = WordsLevelUtil.getLevel7();
        switch(App.userNoPassword_global.getLevel()){
            case 0:
                levelToAppend = "无名菜鸟";
                TestTime = 60000;
                maxWordsNumber = 25;
                break;
            case 1:
                levelToAppend = "初出茅庐";
                TestTime = 180000;
                maxWordsNumber = 75;
                break;
            case 2:
                levelToAppend = "略有所得";
                TestTime = 240000;
                maxWordsNumber = 100;
                break;
            case 3:
                levelToAppend = "渐入佳境";
                TestTime = 360000;
                maxWordsNumber = 150;
                break;
            case 4:
                levelToAppend = "胸有成竹";
                TestTime = 480000;
                maxWordsNumber = 200;
                break;
            default:
                // Bug 才会出现这种情况
                levelToAppend = "扫地僧";
                TestTime = 3000;
                maxWordsNumber = 100;
        }
        // 检查用户已经背熟的单词中有多少 是否存在bug
        if(preTestWordsList.size()<maxWordsNumber){
            // 如果用户背熟单词少于应该考核数目，一定存在问题
            Log.e("WordTestActivity","onCreate: User actually has no enough words to test.");
            finish();
        }
        // 首先对用户已经背熟的单词ArrayList随机化（洗牌）
        testWordsArrayList = RandomizeArrayList.randomList(preTestWordsList);
        // 然后获取随机后的前 maxWordsNumber个 单词装进ArrayList
        testWordsArrayList = new ArrayList<Words>(testWordsArrayList.subList(0, maxWordsNumber)) ;
        Toast.makeText(this, "你现在的Level是 " + levelToAppend, Toast.LENGTH_SHORT).show();
    }

    private void initTimerAndStart(){
        timer = new CountDownTimer(TestTime, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Integer timeLeft = (int) millisUntilFinished / 1000;
                time_left_in_test_textView.setText(timeLeft.toString());
            }

            @Override
            public void onFinish() {
                // 时间到，如果是game则发送信息给服务器，如果是升级称号显示结算窗口
                if(is_from_game){
                    SessionManager.getInstance().writeToServer(
                            SendMsgMethod.getIntMessage(CMDDef.GAME_RESULT, rightWordsNumber));
                }
                if(is_from_title_upgrade){
                    showTitleUpgradeDialog();
                }
            }
        }.start();
    }

}
