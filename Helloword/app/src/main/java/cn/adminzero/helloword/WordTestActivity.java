package cn.adminzero.helloword;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

import cn.adminzero.helloword.CommonClass.OpponentInfo;
import cn.adminzero.helloword.util.MediaPlayUtil;
import cn.adminzero.helloword.util.Words;
import cn.adminzero.helloword.util.WordsUtil;

public class WordTestActivity extends AppCompatActivity {

    Boolean is_from_game;
    OpponentInfo opponentInfo;
    ArrayList<Words> testWordsArrayList;
    TextView words_correct_in_test_textView;
    Words wordToTest;
    int theRightIndex; // 当前正确选项标号 0.1.2.3
    Integer rightWordsNumber;
    ProgressBar progressBar_word_test;
    Integer maxWordsNumber;
    TextView word_content_test_textView;
    TextView phonemic_test_textView;
    ImageButton remember_pronounce_test_button;
    Button choice_1;
    Button choice_2;
    Button choice_3;
    Button choice_4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_test);

        maxWordsNumber = 20;

        Intent intent = getIntent();
        is_from_game = intent.getBooleanExtra("is_from_game",false);
        if(is_from_game){
            opponentInfo = (OpponentInfo) intent.getSerializableExtra("test_words_id");
            testWordsArrayList = shortArrayToArrayList(opponentInfo.getPkWords());
            maxWordsNumber = 80; // 如果Game的单词数有更改在这里更改
            Toast.makeText(this, "你的对手是 "+opponentInfo.getNickName(), Toast.LENGTH_SHORT).show();
        }
        // 左上角正确单词数
        words_correct_in_test_textView = findViewById(R.id.words_correct_in_test_textView);
        // 上方进度条
        progressBar_word_test = findViewById(R.id.progressBar_word_test);
        // 右上角倒计时功能
        final TextView time_left_in_test_textView = findViewById(R.id.time_left_in_test_textView);
        CountDownTimer timer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Integer timeLeft = (int)millisUntilFinished/1000;
                time_left_in_test_textView.setText(timeLeft.toString());
            }
            @Override
            public void onFinish() {
                //TODO 计时结束发送结果
            }
        }.start();
        // 为单词UI view赋值
        word_content_test_textView = findViewById(R.id.word_content_test_textView);
        phonemic_test_textView = findViewById(R.id.phonemic_test_textView);
        remember_pronounce_test_button = findViewById(R.id.remember_pronounce_test_button);
        choice_1 = findViewById(R.id.choice_1);
        choice_2 = findViewById(R.id.choice_2);
        choice_3 = findViewById(R.id.choice_3);
        choice_4 = findViewById(R.id.choice_4);
        // 初始化正确单词数 并更新UI
        rightWordsNumber = 0;
        progressBar_word_test.setMax(maxWordsNumber);
        wordToTest = testWordsArrayList.get(0);
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

    private void onClickAnAnswer(int choice){
        if(choice == theRightIndex){
            // 用户选择了正确答案
            rightWordsNumber++;
        }
        testWordsArrayList.remove(0);
        if(testWordsArrayList.size()==0){
            //TODO 当用户提前完成了所有单词
            finish();
        }
        wordToTest = testWordsArrayList.get(0);
        simpleUpdateUI();
    }

    private void simpleUpdateUI(){
        progressBar_word_test.setProgress(rightWordsNumber);
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
        switch (theRightIndex){
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
        phonemic_test_textView.setText("/"+wordToTest.getPhonetic()+"/");
        remember_pronounce_test_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 播放声音
                MediaPlayUtil player = new MediaPlayUtil();
                player.playword(wordToTest.getWord());
            }
        });


    }

    ArrayList<Words> shortArrayToArrayList(short pkWords[])
    {
        ArrayList<Words> result = new ArrayList<>();
        for (short pkWordsId:pkWords
             ) {
            result.add(WordsUtil.getWordById(pkWordsId));
        }
        return result;
    }

}
