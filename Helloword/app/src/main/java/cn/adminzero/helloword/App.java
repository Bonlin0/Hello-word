package cn.adminzero.helloword;

import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import cn.adminzero.helloword.CommonClass.UserNoPassword;
import cn.adminzero.helloword.db.DbUtil;

import cn.adminzero.helloword.util.MyStorage;
import cn.adminzero.helloword.util.Words;
import cn.adminzero.helloword.util.WordsLevel;

public class App extends Application {

    private static final String TAG = "Helloword";
    private static String Dbname = "HelloWord";
    //全局用户信息
    public static UserNoPassword userNoPassword_global;
    /**
     * 是否第一次运行APP  数据保存在同名字段"isFirstRunApp"
     */
    private static int isFirstRunApp = 0;
    private static int dbversion = 1;
    private static Context context = null;
    private static boolean isDailyFirstLogin = false;
    private static int lastLoginAccount = -1;// 记录上次APP的登录ID
    // 记录是否用户退出登录，当为true时 MainActivity检测到自动退出
    //public static boolean isLoggingOut = false;
    // 记录用户是否满足挑战下一级称号的条件
    public static boolean isAbleToUpgradeTitle= false;
    // 记录今日单词队列and Level
    public static ArrayList<Words> wordsArrayToday;
    public static ArrayList<WordsLevel> wordsLevelArrayToday;


    public static String getTAG() {
        return TAG;
    }

    public static int getDbversion() {
        return dbversion;
    }

    public static String getDbname() {
        return Dbname;
    }


    public static Context getContext() {
        return context;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        /**
         * 判断数据库初始化
         * */
        MyStorage myStorage = new MyStorage();
        /**
         * 初始化判断 Assets文件是只读的！
         * */
        int isFirstRunApp = myStorage.getInt("isFirstRunApp");
        lastLoginAccount = myStorage.getInt("lastLoginAccount");

        //TODO  提取上次登录时间，并且保存当前登陆时间，用以比对 是否是今日第一次登录

        if (isFirstRunApp == 0) {
            /** 初始化 word数据库
             *  添加 csv单词数据
             *  */
            initword();
            myStorage.storeInt("isFirstRunApp", 1);
        }


    }

    /**
     * 初始化本地单词数据库
     */
    private static void initword() {
        SQLiteDatabase db = DbUtil.getDatabase();
        AssetManager assetManager = context.getAssets();

        InputStream inputStream = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        String[] buffer;
        /*"word_id integer primary key," +
                "word text NOT NULL," +
                "phonetic text ," +
                "definition text ," +
                "translation text," +
                "exchange text," +
                "tag integer," +
                "sentence text)";*/
        try {
            inputStream = assetManager.open("target.csv");
            isr = new InputStreamReader(inputStream);
            br = new BufferedReader(isr);
            String line = null;
            ContentValues contentValues = new ContentValues();

            short word_id = 0;
            String word = null;
            String phonetic = null;
            String definition = null;
            String translation = null;
            String exchange = null;
            Short tag = 0;
            /**
             * 主动开启事务
             * */
            db.beginTransaction();
            while ((line = br.readLine()) != null) {
                buffer = line.split("#", -1);
                assert (buffer.length == 7);
                word_id = Short.valueOf(buffer[0]);
                word = buffer[1];
                phonetic = buffer[2];
                definition = buffer[3].replace("\\n", "\n");
                translation = buffer[4].replace("\\n", "\n");
                exchange = buffer[5];
                tag = Short.valueOf(buffer[6]);
                /** 插入单词信息数据库！*/
                contentValues.put("word_id", word_id);
                contentValues.put("word", word);
                contentValues.put("phonetic", phonetic);
                contentValues.put("definition", definition);
                contentValues.put("translation", translation);
                contentValues.put("exchange", exchange);
                contentValues.put("tag", tag);
                db.insert("WORDS", null, contentValues);
                contentValues.clear();
            }
            db.setTransactionSuccessful();
            br.close();
            isr.close();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
            try {
                if (br != null) {
                    br.close();
                }
                if (isr != null) {
                    isr.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}