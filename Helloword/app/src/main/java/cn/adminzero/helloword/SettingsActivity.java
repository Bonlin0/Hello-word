package cn.adminzero.helloword;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Set;

import cn.adminzero.helloword.util.MyStorage;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // 如果是从背单词活动因为没有配置单词量转移过来，弹出Toast 提示用户
        Intent intent = getIntent();
        Boolean isFromRememberWords = intent.getBooleanExtra("isFromRememberWords",false);
        if(isFromRememberWords){
            Toast.makeText(SettingsActivity.this,"您还没有配置每日所需单词量，请配置后再开始学习。", android.widget.Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 左上角返回上一级
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        private Preference remindme;
        private Preference remindtime;
        private Preference wordbooks;
        private Preference dailyWordsNumber;
        private Preference sync;
        private Preference exit;


        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            remindme = findPreference("remindme");
            remindtime = findPreference("remindtime");
            wordbooks = findPreference("wordbooks");
            dailyWordsNumber = findPreference("dailyWordsNumber");
            sync = findPreference("sync");
            exit = findPreference("exit");


            remindme.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    Log.d("test", "onPreferenceChange: " + "是否提醒");
                    Log.d("test", "onPreferenceChange: " + newValue.toString());
                    MyStorage myStorage = new MyStorage("SettingActivity");
                    myStorage.storeBoolean("remindme", (boolean) newValue);
                    Log.d("test", "onPreferenceChange: " + "设置OK！");
                    return true;
                }
            });
            remindtime.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    Log.d("test", "onPreferenceChange: " + "提醒时间");
                    Log.d("test", "onPreferenceChange: " + newValue.toString());
                    MyStorage myStorage = new MyStorage("SettingActivity");
                    myStorage.storeString("remindtime", newValue.toString());
                    Log.d("test", "onPreferenceChange: " + "设置OK！");
                    return true;
                }
            });
            wordbooks.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    Log.d("test", "onPreferenceChange: " + "词书选择");
                    Log.d("test", "onPreferenceChange: " + newValue.toString());
                    MyStorage myStorage = new MyStorage("SettingActivity");
                    myStorage.storeInt("wordbooks", Integer.valueOf(newValue.toString()));
                    Log.d("test", "onPreferenceChange: " + "设置OK！");
                    return true;
                }
            });
            dailyWordsNumber.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    Log.d("test", "onPreferenceChange: " + "背词数");
                    Log.d("test", "onPreferenceChange: " + newValue.toString());
                    MyStorage myStorage = new MyStorage("SettingActivity");
                    myStorage.storeInt("dailyWordsNumber", Integer.valueOf(newValue.toString()));
                    Log.d("test", "onPreferenceChange: " + "设置OK！");
                    return true;
                }
            });


            sync.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    // TODO 网络同步词书！ 设置圈圈dialog
                    Log.d("test", "onPreferenceChange: 词库同步");
                    return true;
                }
            });

            exit.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    SharedPreferences sharedPreferences = App.getContext().getSharedPreferences("LoginSharedPreference", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("isLogin", false);
                    editor.commit();

                    Log.d("test", "onPreferenceClick: Exit");
                    ActivityCollector.finishAll();
                    android.os.Process.killProcess(android.os.Process.myPid());
                    return true;

                }
            });


        }
    }
}