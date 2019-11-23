package cn.adminzero.helloword.util;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import cn.adminzero.helloword.App;
import cn.adminzero.helloword.db.DbUtil;


/**
 * author : zhaojunchen
 * date   : 2019/11/2016:14
 * desc   : none
 */
public class WordLevelUtil {
    private static final String TAG = "WordLevelUtil";

    // example initWorkBook(Words.tag_cet4) --> 0.13s ~ 0.37   most 1s per time
    public static boolean initWorkBook(int _tag) {
        // getGoal 获得 1-8的int
        // tag 是 0x01,到0x0080
        int tag_now = App.userNoPassword_global.getGoal();
        if(tag_now == _tag)
        {
            return true;
        }
        short tag = (short)(1 << _tag);
        long startTime = System.nanoTime();
        SQLiteDatabase db = DbUtil.getDatabase();
        List<WordsLevel> result = new ArrayList<WordsLevel>();
        try {
            // TODO 删除level = 0的单词！
            db.beginTransaction();
//            App.userNoPassword_global.getUserID();
//            db.execSQL("delete from HISTORY_" + App.getuserid() + " where level = ?", new String[]{"0"});
            Cursor cursor = db.rawQuery("select word_id,tag from WORDS ", null);

            if (cursor.moveToFirst()) {
                short id = -1;
                short classify = -1;
                do {
                    classify = cursor.getShort(cursor.getColumnIndex("tag"));
                    if ((classify & tag) == tag) {
                        WordsLevel wordsLevel = new WordsLevel();
                        id = cursor.getShort(cursor.getColumnIndex("word_id"));
                        wordsLevel.setWord_id(id);
                        result.add(wordsLevel);
                    }
                } while (cursor.moveToNext());
            }
            db.setTransactionSuccessful();
            db.endTransaction();
            cursor.close();

            db.beginTransaction();
            try {
                for (int i = 0; i < result.size(); i++) {
                    try {
                        db.execSQL("insert into HISTORY_" + App.getuserid() + " (word_id,level,yesterday) " +
                                "values(?,?,?)", new String[]{String.valueOf(result.get(i).getWord_id()), "0", "0"});
                    } catch (Exception e) {
                        continue;
                    }
                }
                db.setTransactionSuccessful();
                App.userNoPassword_global.setGoal(_tag);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                db.endTransaction();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (db != null) {
                db = null;
            }
        }
        Log.d(TAG, "initWorkBook: spend " + (System.nanoTime() - startTime)+" ns");;
        return true;
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
