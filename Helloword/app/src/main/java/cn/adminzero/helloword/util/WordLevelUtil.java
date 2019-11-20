package cn.adminzero.helloword.util;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import cn.adminzero.helloword.App;
import cn.adminzero.helloword.db.DbUtil;


/**
 * author : zhaojunchen
 * date   : 2019/11/2016:14
 * desc   : none
 */
public class WordLevelUtil {
    private static final String TAG = "WordLevelUtil";

    /**
     * public static final String CREATE_HISTORY =
     *             "create table " + "HISTORY_" + getuserid() + "(" +
     *                     "word_id integer primary key," +
     *                     "level int default(0)," +
     *                     "yesterday integer default(0))";*/

    /**
     * 词书初始化  0x0001 zk.......
     */
    // example initWorkBook(Words.tag_cet4)
    public static boolean initWorkBook(short tag) {
        SQLiteDatabase db = DbUtil.getDatabase();
        db.beginTransaction();
        try {
            db.execSQL("delete from HISTORY_" + App.getuserid() + " where level = ?;", new String[]{String.valueOf(0)});


            /**设置事务*/
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "initWorkBook: " + "数据库更改失败");
        } finally {
            db.endTransaction();
            if (db != null) {
                db.close();
            }
        }
        return false;
    }

    public static int getLevel7Count() {
        SQLiteDatabase db = DbUtil.getDatabase();
        db.beginTransaction();
        int result = 0;
        try {
            db.setTransactionSuccessful();
            Cursor cursor = db.rawQuery("select from HISTORY_" + App.getuserid() + " where level = ?", new String[]{String.valueOf(7)});
            if (cursor != null) {
                result = cursor.getCount();
            } else {
                Log.d(TAG, "getLevel7Count: error!");
                result = 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            return result;
        }

    }
}
