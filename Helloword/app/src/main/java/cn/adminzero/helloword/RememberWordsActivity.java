package cn.adminzero.helloword;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import cn.adminzero.helloword.util.MediaPlayUtil;
import cn.adminzero.helloword.util.Words;
import cn.adminzero.helloword.util.WordsUtil;

import static cn.adminzero.helloword.App.Upadte_UserNoPassword;
import static cn.adminzero.helloword.App.userNoPassword_global;

public class RememberWordsActivity extends AppCompatActivity {

    private Words wordsToShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remember_words);
        // TODO 删去下面这行 从今日单词队列获取单词
        wordsToShow = WordsUtil.getWordByWord("prime");

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



}
