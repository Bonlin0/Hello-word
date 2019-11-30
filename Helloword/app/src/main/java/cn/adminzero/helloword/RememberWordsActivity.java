package cn.adminzero.helloword;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cn.adminzero.helloword.CommonClass.WordsLevel;
import cn.adminzero.helloword.db.DbUtil;
import cn.adminzero.helloword.db.ServerDbUtil;
import cn.adminzero.helloword.util.MediaPlayUtil;
import cn.adminzero.helloword.util.MyStorage;
import cn.adminzero.helloword.util.Words;
import cn.adminzero.helloword.util.WordsLevelUtil;
import cn.adminzero.helloword.util.WordsUtil;

import static cn.adminzero.helloword.App.userNoPassword_global;
import static cn.adminzero.helloword.db.ServerDbUtil.UpdateHistory;
import static cn.adminzero.helloword.util.WordsLevelUtil.assignDailyWords;

public class RememberWordsActivity extends AppCompatActivity {

    private static final String TAG = "RememberWordsActivity";
    private Words wordsToShow;
    // wordsArrayList 会在背单词的过程中变动，最终变为空
    private ArrayList<Words> wordsArrayList;
    private ArrayList<WordsLevel> wordsLevelArrayList;
    // 这个ArrayList记录所有的需要更新的信息
    private ArrayList<WordsLevel> wordsIdToUpdate;

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
        if (dailyWordsNumber == null) {
            // 如果没有获取到每日单词 则进入设置活动

            Intent intent = new Intent(RememberWordsActivity.this, SettingsActivity.class);
            intent.putExtra("isFromRememberWords", true);
            startActivity(intent);
            finish();
        }
        dailyWordsNumber = null;
        wordsIdToUpdate = new ArrayList<>();
    }

    // 将部分检查打卡信息的代码扔到onStart，因为和后面的代码放到一起尽管finish函数调用，还是会执行完之后的代码
    @Override
    protected void onStart() {
        super.onStart();
        if (dailyWordsNumber == null) {
            dailyWordsNumber = defaultSharedPreferences.getString("dailyWordsNumber", null);
            dailyWordsNumber_int = Integer.valueOf(dailyWordsNumber);
        }
        // if there is an array exists, use it,
        if (App.wordsArrayToday != null) {
            wordsArrayList = App.wordsArrayToday;
            wordsLevelArrayList = App.wordsLevelArrayToday;
        }
        // else , create a new one.
        else {
            wordsArrayList = assignDailyWords(dailyWordsNumber_int);

            // 返回单词的数目可能不是dailyWordsNumber_int
            dailyWordsNumber_int = wordsArrayList.size();

            wordsLevelArrayList = WordsLevelUtil.wordsLevels;
            App.wordsArrayToday = wordsArrayList;
            App.wordsLevelArrayToday = wordsLevelArrayList;
        }

        // TODO 背完词书的特殊情况有待考虑
        // 背完今日单词
        if (wordsArrayList.size() <= 0) {
            // 更新用户今日打卡
            userNoPassword_global.setDays(userNoPassword_global.getDays() + 1);
            userNoPassword_global.setIsPunch(1);
            // 同步网络数据
            ServerDbUtil.Upadte_UserNoPassword();
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();


        wordsToShow = wordsArrayList.get(0);


        TextView word_content_textView = findViewById(R.id.word_content_textview);
/*        // 防止词书已经背完产生bug

        if(wordsToShow==null)
        {
            Snackbar.make(word_content_textView.getRootView(),"您似乎没有可以记忆的单词了，更换词书试一试？", Snackbar.LENGTH_LONG).setAction("离开", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            }).show();
            return;
        }*/

        // 使用wordsToShow 更新UI
        // TextView word_content_textView = findViewById(R.id.word_content_textview);
        word_content_textView.setText(wordsToShow.getWord());
        TextView phonemic_textView = findViewById(R.id.phonemic_textView);
        phonemic_textView.setText("/" + wordsToShow.getPhonetic() + "/");

        ImageButton remember_pronounce_button = findViewById(R.id.remember_pronounce_button);
        remember_pronounce_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayUtil player = new MediaPlayUtil();
                player.playword(wordsToShow.getWord());
            }
        });
        // 更新进度条UI
        ProgressBar remember_progress_bar = findViewById(R.id.remember_progress_bar);
        remember_progress_bar.setMax(dailyWordsNumber_int);
        remember_progress_bar.setProgress(dailyWordsNumber_int - wordsArrayList.size());

        // 播放声音
        MediaPlayUtil player = new MediaPlayUtil();
        player.playword(wordsToShow.getWord());

    }


    public void rememberButtonOnClicked(View view) {
        //  做相关处理
        Intent intent = new Intent(RememberWordsActivity.this, ShowWordActivity.class);
        intent.putExtra("word_to_show", wordsToShow);
        intent.putExtra("is_remembered", true);
        startActivityForResult(intent, 1);
        // 这个RequestCode 1 设置的没有任何意义，因为我之前已经通过is_remembered实现了相关功能了，但是必须传这个RequestCode没办法设了1
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1) {
            // 表示撤销，将该单词执行没记住的操作
            notRemembered();
        } else {
            // 表示不撤销，执行记住的操作
            reallyRemembered();
        }
    }

    public void notRememberButtonOnClicked(View view) {
        //  做相关处理
        notRemembered();

        Intent intent = new Intent(RememberWordsActivity.this, ShowWordActivity.class);
        intent.putExtra("word_to_show", wordsToShow);
        // intent.putExtra("is_remembered",true);
        startActivity(intent);

    }


    // 写的很不优雅，其实应该继承Words类新写一个类，同时包括words和level信息，这里每次要同步两个队列太不方便了
    private void reallyRemembered() {
        if (wordsToShow.leftTime == null || wordsToShow.leftTime == 0) {
            //今日首次遇见
            WordsLevel tmp = wordsLevelArrayList.get(0);
            // 如果这个单词是第一次见到，加上yesterday标签，明天重点安排
            if (tmp.getLevel() == 0) {
                tmp.setYesterday((byte) 1);
            }
            tmp.setLevel((short) (tmp.getLevel() + 1));
            wordsIdToUpdate.add(tmp);
            wordsArrayList.remove(0);
            wordsLevelArrayList.remove(0);
        } else if (wordsToShow.leftTime > 1) {
            // 今天遇见过且点过不会
            Words tmp = wordsArrayList.get(0);
            tmp.leftTime = tmp.leftTime - 1;
            // 如果还有几个单词就忘后插，插入到3个单词之后
            if (wordsArrayList.size() > 5) {
                wordsArrayList.add(4, tmp);
                wordsLevelArrayList.add(4, wordsLevelArrayList.get(0));
            } else {
                wordsArrayList.add(tmp);
                wordsLevelArrayList.add(wordsLevelArrayList.get(0));
            }
            wordsArrayList.remove(0);
            wordsLevelArrayList.remove(0);
        }
        //用户至少已经重认2遍了
        else if (wordsToShow.leftTime == 1) {
            WordsLevel tmp = wordsLevelArrayList.get(0);
            tmp.setLevel((short) (tmp.getLevel() + 1));
            wordsIdToUpdate.add(tmp);
            wordsArrayList.remove(0);
            wordsLevelArrayList.remove(0);
        }
    }


    private void notRemembered() {
        if (wordsToShow.leftTime == null || wordsToShow.leftTime == 0) {
            //今日首次遇见
            WordsLevel tmpLevel = wordsLevelArrayList.get(0);
            Words tmp = wordsArrayList.get(0);
            // 如果这个单词是第一次见到，加上yesterday标签，明天重点安排
            if (tmpLevel.getLevel() == 0) {
                tmpLevel.setYesterday((byte) 1);
            }
            tmpLevel.setLevel((short) (tmpLevel.getLevel() - 1));
            tmp.leftTime = 3;

            // 如果还有几个单词就忘后插，插入到3个单词之后
            if (wordsArrayList.size() > 5) {
                wordsArrayList.add(4, tmp);
                wordsLevelArrayList.add(4, tmpLevel);
            } else {
                wordsArrayList.add(tmp);
                wordsLevelArrayList.add(tmpLevel);
            }
            wordsArrayList.remove(0);
            wordsLevelArrayList.remove(0);
        } else if (wordsToShow.leftTime >= 1) {
            // 今天遇见过且点过不会
            WordsLevel tmpLevel = wordsLevelArrayList.get(0);
            Words tmp = wordsArrayList.get(0);
            tmp.leftTime = 3;

            // 如果还有几个单词就忘后插，插入到3个单词之后
            if (wordsArrayList.size() > 5) {
                wordsArrayList.add(4, tmp);
                wordsLevelArrayList.add(4, tmpLevel);
            } else {
                wordsArrayList.add(tmp);
                wordsLevelArrayList.add(tmpLevel);
            }
            wordsArrayList.remove(0);
            wordsLevelArrayList.remove(0);
        }
    }

    public void tooEasyButtonOnClicked(View view) {
        //  做相关处理

        WordsLevel temp = wordsLevelArrayList.get(0);
        temp.setLevel((byte) 7);
        // 将这个wordsLevel加入待同步队列
        wordsIdToUpdate.add(temp);
        wordsLevelArrayList.remove(0);
        wordsArrayList.remove(0);
        Intent intent = new Intent(RememberWordsActivity.this, ShowWordActivity.class);
        intent.putExtra("word_to_show", wordsToShow);
        intent.putExtra("is_too_easy", true);
        startActivity(intent);

    }

    private ArrayList<Words> allocwords(int assignDailyWordsNumbers) {
        /**
         * 分配今天的单词任务
         * */
        int userId = App.userNoPassword_global.getUserID();
        if (userId == -1) {
            ActivityCollector.finishAll();
        }

        MyStorage myStorage = new MyStorage();
        Cursor cursor = null;
        SQLiteDatabase db = DbUtil.getDatabase();
        String lastDate = myStorage.getString("lastDate");
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");//可以方便地修改日期格式
        String todayDate = dateFormat.format(now);
        if (!todayDate.equals(lastDate)) {//分配今天的任务
            /**
             *  新的日子使用分配函数分配今天的任务
             *  并且存在保存在数据库
             */
            myStorage.storeString("lastDate", todayDate);
            WordsLevelUtil.assignDailyWords(assignDailyWordsNumbers);
            /*db.execSQL("delete from TODAY_" + userId);
            ContentValues contentValues = new ContentValues();
            for (WordsLevel wordsLevel : WordsLevelUtil.wordsLevels) {
                contentValues.put("word_id", wordsLevel.getWord_id());
                contentValues.put("level", wordsLevel.getLevel());
                contentValues.put("yesterday", wordsLevel.getYestarday());
                db.insert("TODAY_" + userId, null, contentValues);
                contentValues.clear();
            }*/
        } else {
            // 从数据库读取今天的单词任务（任务存在就不读取任务）
            if (WordsLevelUtil.wordsLevels == null) {
                WordsLevelUtil.wordsLevels = new ArrayList<>();
                WordsLevelUtil.words = new ArrayList<>();
                // 数据库读取
                cursor = db.rawQuery("select * from TODAY_" + userId, null);
                int word_id = cursor.getColumnIndex("word_id");
                int level = cursor.getColumnIndex("level");
                int yesterday = cursor.getColumnIndex("yesterday");
                if (cursor.getCount() > 0 && cursor.moveToFirst()) {
                    do {
                        WordsLevel wordsLevel = new WordsLevel();
                        wordsLevel.setWord_id(cursor.getShort(word_id));
                        wordsLevel.setLevel((byte) cursor.getShort(level));
                        wordsLevel.setYesterday((byte) cursor.getShort(yesterday));
                        WordsLevelUtil.wordsLevels.add(wordsLevel);
                    } while (cursor.moveToNext());
                }
                WordsLevelUtil.words = WordsUtil.getWordArrayByIdArray(WordsLevelUtil.wordsLevels);
            }
        }
        return WordsLevelUtil.words;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // TODO 将这次背单词对WordLevel的修改同步到服务器 使用wordsIdToUpdate
        Log.e(TAG, "onDestroy: " + wordsIdToUpdate.toString());
        // 服務器更新History
        ServerDbUtil.UpdateHistory(wordsIdToUpdate);
        // 本地更新History
        /**
         * 1. 保存变化的单词分配队列（为今日的下次打开APP做备份）
         * 2. 写回更新的单词记录
         * */
        // 保存今日的单词分配任务
        SQLiteDatabase db = DbUtil.getDatabase();
        int userId = userNoPassword_global.getUserID();
        db.execSQL("delete from TODAY_" + userId);
        ContentValues contentValues = new ContentValues();
        if (wordsLevelArrayList != null) {
            for (WordsLevel wordsLevel : wordsLevelArrayList) {
                contentValues.put("word_id", wordsLevel.getWord_id());
                contentValues.put("level", wordsLevel.getLevel());
                contentValues.put("yesterday", wordsLevel.getYestarday());
                db.insert("TODAY_" + userId, null, contentValues);
                contentValues.clear();
            }
        }
        if (wordsIdToUpdate != null) {
            // 写回更新的单词
            for (WordsLevel wordsLevel : wordsIdToUpdate) {
                db.execSQL("update HISTORY_" + userId + " set level = ? , yesterday = ? where word_id = ?",
                        new String[]{
                                String.valueOf(wordsLevel.getLevel()),
                                String.valueOf(wordsLevel.getYestarday()),
                                String.valueOf(wordsLevel.getWord_id())
                        });

            }
        }


    }
}
