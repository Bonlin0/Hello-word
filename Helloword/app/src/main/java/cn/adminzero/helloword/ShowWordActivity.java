package cn.adminzero.helloword;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import cn.adminzero.helloword.util.MediaPlayUtil;
import cn.adminzero.helloword.util.Words;

public class ShowWordActivity extends AppCompatActivity {

    private SharedPreferences defaultSharedPreferences;

    private Words wordsActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_word);

        defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);


        Intent intent =getIntent();
        Words words = (Words)intent.getSerializableExtra("word_to_show");
        wordsActivity = words;

        Boolean from_my_words = intent.getBooleanExtra("from_my_words",false);
        Button next_word_button  = findViewById(R.id.next_word_button);
        if(from_my_words)
        {
            next_word_button.setVisibility(View.GONE);
        }
        else
        {
            next_word_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }

        // 根据设置决定是否自动播放声音
        Boolean ifAutoPlay = defaultSharedPreferences.getBoolean("auto_play_sound", true);
        // 自动播放声音
        if(ifAutoPlay)
        {
            MediaPlayUtil player = new MediaPlayUtil();
            player.playword(words.getWord());
        }


        // 使用word 更新UI
        TextView word_content_textview = findViewById(R.id.word_content_textview);
        word_content_textview.setText(words.getWord());
        TextView show_word_phonetic = findViewById(R.id.show_word_phonetic);
        show_word_phonetic.setText("/"+words.getPhonetic()+"/");
        TextView translation_textView = findViewById(R.id.translation_textView);
        translation_textView.setText(words.getTranslation());
        TextView definition_textView = findViewById(R.id.definition_textView);
        definition_textView.setText(words.getDefinition());
        TextView exchange_textView = findViewById(R.id.exchange_textView);
        exchange_textView.setText(words.getExchange());
        TextView example_sentence_content_text = findViewById(R.id.example_sentence_content_text);
        // TODO 由于暂时没有例句 如果是空 就附上通用临时例句
        String sentenceToShow = null;
        if((sentenceToShow = words.getSentence())==null)
        {
            sentenceToShow = getString(R.string.place_holder_sentence);
        }
        example_sentence_content_text.setText(sentenceToShow);

        boolean isRemembered = intent.getBooleanExtra("is_remembered",false);
        final TextView cancel_remember_textView = findViewById(R.id.cancel_remember_textView);
        setResult(0);
        if(!isRemembered)
        {
            cancel_remember_textView.setVisibility(View.GONE);
        }
        else
        {
            cancel_remember_textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cancel_remember_textView.setVisibility(View.GONE);
                    setResult(1);
                }
            });
        }

    }

    public void onClickPronounceButton(View view){
        if(wordsActivity==null)
            return;
        MediaPlayUtil player = new MediaPlayUtil();
        player.playword(wordsActivity.getWord());
    }
    /** MediaPlayUtil player = new MediaPlayUtil();
     * player.playword(String word); */


}
