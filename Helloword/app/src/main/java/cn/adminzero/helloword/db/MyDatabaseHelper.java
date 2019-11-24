package cn.adminzero.helloword.db;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import cn.adminzero.helloword.App;


/**
 * https://github.com/amitshekhariitbhu/Android-Debug-Database
 * debugImplementation 'com.amitshekhar.android:debug-db:1.0.6'
 * debugImplementation 'com.amitshekhar.android:debug-db-encrypt:1.0.6'
 * adb forward tcp:8080 tcp:8080
 * http://localhost:8080 in emulator
 * <p>
 * SQLciper: https://blog.csdn.net/top_code/article/details/41178607
 * https://www.zetetic.net/sqlcipher/sqlcipher-for-android/    official document
 * The call to SQLiteDatabase.loadLibs(this) must occur before any other database operation.
 */
public class MyDatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "MyDatabaseHelper";


    public static final String CREATE_USER =
            "create table USER(" +
                    "user_id int primary key ," +
                    "user_name text NOT NULL," +
                    "password text NOT NULL," +
                    "email text ," +
                    "avatar blob ," +
                    "goal int default(-1) ," +
                    "days int default(0)," +
                    "group_id int default(-1)," +
                    "user_level int default(0)," +
                    "points int default(0) )";


    public static final String CREATE_USER_HISTORY = "" +
            "create table USER_HISTORY(" +
            "user_id integer primary key," +
            "history_name text)";

    public static final String CREATE_GROUP_USER = "" +
            "create table GROUP_USER(" +
            //  "id INTEGER primary key AUTOINCREMENT," +//这里只能用INITEGER  不能用i
            "user_id int primary key," +
            "group_id int default(-1)," +
            "contribution int default(0))";


    public static final String CREATE_WORDS =
            "create table WORDS(" +
                    "word_id integer primary key," +
                    "word text NOT NULL," +
                    "phonetic text ," +
                    "definition text ," +
                    "translation text," +
                    "exchange text," +
                    "tag integer," +
                    "sentence text)";


    private Context context;
    private String name;

    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory cursorFactory, int version) {
        super(context, name, cursorFactory, version);
        this.context = context;
        this.name = name;
    }

    /**
     * 初始化创建数据表
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        /**
         * 创建数据库 在数据库第一次被创建时调用
         * */
        db.beginTransaction();
        try {
            db.execSQL(CREATE_USER);
            db.execSQL(CREATE_WORDS);
            db.execSQL(CREATE_USER_HISTORY);
            db.execSQL(CREATE_GROUP_USER);
            db.execSQL("create index word_idindex on WORDS(word_id)");
            db.execSQL("create index wordindex on WORDS(word)");

            Log.d(TAG, "start create databases");
            Toast.makeText(App.getContext(), "Databases Create succeed!", Toast.LENGTH_LONG).show();
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "onCreate: databases create failed!");
            Toast.makeText(App.getContext(), "Databases Create failed!", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        /**
         * drop table if exists table_name;
         * */

        db.beginTransaction();
        try {
            Log.d(TAG, "onUpgrade: update database!");
            db.execSQL("drop table if exists USER");
            db.execSQL("drop table if exists WORDS");
            db.execSQL("drop table if exists HISTORY");
            db.execSQL("drop table if exists " + "HISTORY_" + App.userNoPassword_global.getUserID());
            db.execSQL("drop table if exists GROUP_USER");
            Log.d(TAG, "start create databases");
            Toast.makeText(App.getContext(), "Databases drop succeed!", Toast.LENGTH_LONG).show();
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "onCreate: databases drop failed!");
            Toast.makeText(App.getContext(), "Databases Create failed!", Toast.LENGTH_LONG).show();
            e.printStackTrace();

        } finally {
            db.endTransaction();
        }
        onCreate(db);
    }


}