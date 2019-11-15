package cn.adminzero.helloword.util;

import android.app.Activity;
import android.content.SharedPreferences;
import android.text.TextUtils;

import cn.adminzero.helloword.App;

public class MyStorage {


    private SharedPreferences preferences;

    //PasswordSheild保存于PasswordSheild
    public MyStorage() {
        preferences = App.getContext().getSharedPreferences(App.getTAG(), Activity.MODE_PRIVATE);
    }

    //没有String 默认 ""
    public String getString(String key) {
        //同样，在读取SharedPreferences数据前要实例化出一个SharedPreferences对象
        return preferences.getString(key, "");
    }

    public int getInt(String key) {
        //同样，在读取SharedPreferences数据前要实例化出一个SharedPreferences对象
        return preferences.getInt(key, 0);
    }

    //保存data
    public boolean storeString(String key, String data) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, data);
        return editor.commit();
    }

    //保存data
    public boolean storeInt(String key, int data) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, data);
        return editor.commit();
    }

}
