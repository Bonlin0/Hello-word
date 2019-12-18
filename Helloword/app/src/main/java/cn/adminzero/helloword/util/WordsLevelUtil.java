package cn.adminzero.helloword.util;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;


import cn.adminzero.helloword.App;
import cn.adminzero.helloword.CommonClass.WordsLevel;
import cn.adminzero.helloword.db.DbUtil;
import cn.adminzero.helloword.db.ServerDbUtil;


/**
 * author : zhaojunchen
 * date   : 2019/11/20 16:14
 * desc   : 单词历史记录表的一些操作
 */
public class WordsLevelUtil {
    private static final String TAG = "WordsLevelUtil";

    public static ArrayList<WordsLevel> wordsLevels = null;

    public static ArrayList<Words> words = null;

    public static void updateWordLevelByArraylist(ArrayList<WordsLevel> wordsLevels) {
        SQLiteDatabase db = DbUtil.getDatabase();
        int userId = App.userNoPassword_global.getUserID();
        db.beginTransaction();
        try {
            db.execSQL("delete from HISTORY_" + userId);
            for (WordsLevel wordsLevel : wordsLevels) {
                db.execSQL("insert into HISTORY_" + userId + " (word_id,level,yesterday) " +
                        "values(?,?,?)", new String[]{
                        String.valueOf(wordsLevel.getWord_id()),
                        String.valueOf(wordsLevel.getLevel()),
                        String.valueOf(wordsLevel.getYestarday())
                });
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "uodateWordLevelByArraylist: " + e.getMessage());
        } finally {
            db.endTransaction();
            if (db != null) {
                db = null;
            }
        }

    }

    /**
     * @param _tag [0,7] 代表词书的编号
     * @return 操作是否成功
     * @desc 初始化词书 在词书选择后调用
     * 关于时间：0.13s ~ 0.37(or large but can not exceed 1s!)
     */

    public static boolean initWorkBook(int _tag) {
        if (_tag > 7 || _tag < 0) {
            return false;
        }

        short tag = (short) (1 << _tag);
        long startTime = System.nanoTime();
        SQLiteDatabase db = DbUtil.getDatabase();
        List<WordsLevel> result = new ArrayList<WordsLevel>();
        try {
            // TODO 删除level = 0的单词！
            db.beginTransaction();
            db.execSQL("delete from HISTORY_" + App.userNoPassword_global.getUserID() + " where level = ?", new String[]{"0"});
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
                        db.execSQL("insert into HISTORY_" + App.userNoPassword_global.getUserID() + " (word_id,level,yesterday) " +
                                "values(?,?,?)", new String[]{String.valueOf(result.get(i).getWord_id()), "0", "0"});
                    } catch (Exception e) {
                        continue;
                    }
                }
                db.setTransactionSuccessful();
                // 通知服务器切换词书  服务器不切换词书
                // 服务器端H表只是在单词背完之后完成单词的上传（删除和更新）
                // ServerDbUtil.ChangeBook(_tag);
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
        Log.d(TAG, "initWorkBook: spend " + (System.nanoTime() - startTime) + " ns");
        return true;
    }

    /**
     * @param choose
     * @return WordsLevel数组
     * @desc 0:0    1:1..6  2: 7    3: <7 4: all
     */

    public static ArrayList<WordsLevel> getLevelEq7(int choose) {
        ArrayList<WordsLevel> arrayList = new ArrayList<>();
        SQLiteDatabase db = DbUtil.getDatabase();
        Cursor cursor = null;
        ArrayList<Short> targetId = new ArrayList<>();
        int userId = App.userNoPassword_global.getUserID();
        int indexword_id, indexlevel, indexyesterday;
        try {
            db.beginTransaction();
            if (choose == 0) {
                cursor = db.rawQuery("select * from HISTORY_" + userId + " where level = ?", new String[]{"0"});
            } else if (choose == 1) {
                cursor = db.rawQuery("select * from HISTORY_" + userId + " where level < ? and level > ?", new String[]{"7", "0"});
            } else if (choose == 2) {
                cursor = db.rawQuery("select * from HISTORY_" + userId + " where level = ? ", new String[]{"7"});
            } else if (choose == 3) {
                cursor = db.rawQuery("select * from HISTORY_" + userId + " where level < ? ", new String[]{"7"});
            } else {
                cursor = db.rawQuery("select * from HISTORY_" + userId, null);
            }

            if (cursor.getCount() > 0 && cursor.moveToFirst()) {
                /** 将单词同步数据一次性读取到内存对象 空间换时间*/
                indexword_id = cursor.getColumnIndex("word_id");
                indexyesterday = cursor.getColumnIndex("yesterday");
                indexlevel = cursor.getColumnIndex("level");
                do {
                    WordsLevel wordsLevel = new WordsLevel();
                    wordsLevel.setWord_id(cursor.getShort(indexword_id));
                    wordsLevel.setLevel((byte) cursor.getShort(indexlevel));
                    wordsLevel.setYesterday((byte) cursor.getShort(indexyesterday));
                    arrayList.add(wordsLevel);
                } while (cursor.moveToNext());
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "assignDailyWords: " + e.getMessage());
        } finally {
            db.endTransaction();
            if (cursor != null) {
                cursor.close();
            }
        }
        return arrayList;
    }

    /**
     * @param number 每日分配单词书数目 --->从配置文件获得
     */
    public static ArrayList<WordsLevel> helpAssignDailyWords(int number) {
        // TODO 存储今天的任务
        ArrayList<WordsLevel> arrayList = getLevelEq7(3);//获取<7
        if (arrayList.size() <= number) {
            // 今天的任务是最后的任务
            return arrayList;
        }
        ArrayList<WordsLevel> Array0 = new ArrayList<>();
        ArrayList<WordsLevel> ArrayNot0 = new ArrayList<>();
        final double ratio = 0.3;
        final int bound = 1;
        for (WordsLevel wordsLevel : arrayList) {
            if (wordsLevel.getLevel() < bound) {
                Array0.add(wordsLevel);
            } else {
                ArrayNot0.add(wordsLevel);
            }
        }

        ArrayList<WordsLevel> result = new ArrayList<>();
        int size0 = Array0.size();
        int sizeNot0 = ArrayNot0.size();

        int alloc0, allocNot0;
        allocNot0 = (int) (number * ratio);
        alloc0 = number - allocNot0;
        /**
         * 最多只有1种情况发生
         * */

        if (allocNot0 >= sizeNot0) {
            for (WordsLevel wordsLevel : ArrayNot0) {
                result.add(wordsLevel);
            }
            allocNot0 = sizeNot0;
            alloc0 = number - allocNot0;
            /**
             * 分配 为0的代码即可
             * */
            ArrayList<Short> short0 = randomSet(size0, alloc0);
            for (Short i : short0) {
                result.add(Array0.get(i));
            }


        } else if (alloc0 >= size0) {
            for (WordsLevel wordsLevel : Array0) {
                result.add(wordsLevel);
            }
            alloc0 = size0;
            allocNot0 = number - alloc0;
            /**
             * 分配 不为0的代码即可
             * */
            ArrayList<Short> shortNot0 = randomSet(sizeNot0, alloc0);
            for (Short i : shortNot0) {
                result.add(ArrayNot0.get(i));
            }
        } else {
            /**
             * 分配为0的代码
             * 分配不为0的代码
             * */
            ArrayList<Short> short0 = randomSet(size0, alloc0);
            for (Short i : short0) {
                result.add(Array0.get(i));
            }
            ArrayList<Short> shortNot0 = randomSet(sizeNot0, alloc0);
            for (Short i : shortNot0) {
                result.add(ArrayNot0.get(i));
            }
        }
        return result;
    }

    public static ArrayList<Words> assignDailyWords(int number) {
        // MyStorage myStorage = new MyStorage();
        // TODO 当日词书的分配
        wordsLevels = helpAssignDailyWords(number);
        words = WordsUtil.getWordArrayByIdArray(wordsLevels);
        return words;
    }

    /**
     * 获取词书中level = 0的单词列表
     */
    public static ArrayList<Words> getLevel0() {
        ArrayList<WordsLevel> wordsLevels = getLevelEq7(0);
        ArrayList<Words> words = WordsUtil.getWordArrayByIdArray(wordsLevels);
        return words;
    }

    /**
     * 获取词书中level>0&&level<7 的单词列表
     */
    public static ArrayList<Words> getLevel1to6() {
        ArrayList<WordsLevel> wordsLevels = getLevelEq7(1);
        ArrayList<Words> words = WordsUtil.getWordArrayByIdArray(wordsLevels);
        return words;
    }


    /**
     * 获取词书中level=7 的单词列表
     */
    public static ArrayList<Words> getLevel7() {
        ArrayList<WordsLevel> wordsLevels = getLevelEq7(2);
        ArrayList<Words> words = WordsUtil.getWordArrayByIdArray(wordsLevels);
        return words;
    }

    /**
     * 获取level= 7 的单词数量
     */
    public static int getLevel7Count() {
        SQLiteDatabase db = DbUtil.getDatabase();
        Cursor cursor = null;
        int result = -1;
        int userId = App.userNoPassword_global.getUserID();
        int indexword_id, indexlevel, indexyesterday;
        try {
            db.beginTransaction();
            cursor = db.rawQuery("select word_id from HISTORY_" + userId + " where level = ? ", new String[]{"7"});
            db.setTransactionSuccessful();
            result = cursor.getCount();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "assignDailyWords: " + e.getMessage());
        } finally {
            db.endTransaction();
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db = null;
            }
        }
        return result;
    }


    /**
     * @param number
     * @return 无重复的随机数[0..number]
     */
    public static ArrayList<Short> randomSet(int max, int number) {
        ArrayList<Short> result = new ArrayList<>(number);
        LinkedHashSet<Short> r = new LinkedHashSet<Short>(number);
        Random random = new Random();
        short i;
        while (r.size() < number) {
            i = (short) random.nextInt(max);
            r.add(i);
        }
        for (Short it : r) {
            result.add(it);
        }
        return result;
    }

}
