package cn.adminzero.helloword;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import cn.adminzero.helloword.util.MediaPlayUtil;
import cn.adminzero.helloword.util.Words;
import cn.adminzero.helloword.util.WordsUtil;

import static cn.adminzero.helloword.App.Upadte_UserNoPassword;
import static cn.adminzero.helloword.App.userNoPassword_global;
import static cn.adminzero.helloword.util.WordsLevelUtil.assignDailyWords;

public class RememberWordsActivity extends AppCompatActivity {

    private Words wordsToShow;
    private ArrayList<Words> wordsArrayList;
    private SharedPreferences defaultSharedPreferences;
    private int dailyWordsNumber_int;
    private String dailyWordsNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remember_words);

        // 首先判断用户有没有设置每日单词
        defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        dailyWordsNumber = defaultSharedPreferences.getString("dailyWordsNumber", null);
        if(dailyWordsNumber == null)
        {
            // 如果没有获取到每日单词 则进入设置活动

            Intent intent = new Intent(RememberWordsActivity.this, SettingsActivity.class);
            intent.putExtra("isFromRememberWords",true);
            startActivity(intent);
            finish();
        }


    }

    @Override
    protected void onResume() {
        super.onResume();

        dailyWordsNumber_int = Integer.valueOf(dailyWordsNumber);
        // if there is an array exists, use it,
        if(App.wordsArrayToday!=null)
        {
            wordsArrayList = App.wordsArrayToday;
        }
        // else , create a new one.
        else{

            wordsArrayList = assignDailyWords(dailyWordsNumber_int);

        }
        // TODO 删去下面的部分 并且为Appd wordsArrayToday赋值
        /*wordsArrayList = new ArrayList<Words>();
        wordsArrayList.add(WordsUtil.getWordById(123));
        wordsArrayList.add(WordsUtil.getWordById(234));
        wordsArrayList.add(WordsUtil.getWordById(1234));
        wordsArrayList.add(WordsUtil.getWordById(12));
        wordsArrayList.add(WordsUtil.getWordById(673));*/

        wordsToShow = wordsArrayList.get(0);



        TextView word_content_textView = findViewById(R.id.word_content_textview);
        word_content_textView.setText(wordsToShow.getWord());
        TextView phonemic_textView = findViewById(R.id.phonemic_textView);
        phonemic_textView.setText("/"+wordsToShow.getPhonetic()+"/");

        ImageButton remember_pronounce_button = findViewById(R.id.remember_pronounce_button);
        remember_pronounce_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayUtil player = new MediaPlayUtil();
                player.playword(wordsToShow.getWord());
            }
        });
    }

    public void rememberButtonOnClicked(View view)
    {
        // TODO 做相关处理
        Intent intent = new Intent(RememberWordsActivity.this, ShowWordActivity.class);
        intent.putExtra("word_to_show",wordsToShow);
        intent.putExtra("is_remembered",true);
        startActivity(intent);
    }

    public void notRememberButtonOnClicked(View view)
    {
        // TODO 做相关处理
        Intent intent = new Intent(RememberWordsActivity.this, ShowWordActivity.class);
        intent.putExtra("word_to_show",wordsToShow);
        // intent.putExtra("is_remembered",true);
        startActivity(intent);
    }

    public void tooEasyButtonOnClicked(View view)
    {
        // TODO 做相关处理
        Intent intent = new Intent(RememberWordsActivity.this, ShowWordActivity.class);
        intent.putExtra("word_to_show",wordsToShow);
        intent.putExtra("is_too_easy",true);
        startActivity(intent);
    }

}
