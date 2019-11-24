package cn.adminzero.helloword.util;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;

import cn.adminzero.helloword.db.DbUtil;

import static androidx.constraintlayout.widget.Constraints.TAG;

/**
 * author : zhaojunchen
 * date   : 2019/11/2015:09
 * desc   : none
 */
public class WordsUtil {


    /**
     * @param _word_id 单词ID
     * @return 返回查询到的单词对象 可为NULL
     */
    public static @Nullable
    Words getWordById(int _word_id) {
        short word_id = (short) _word_id;
        SQLiteDatabase db = DbUtil.getDatabase();
        Words words = new Words();
        String word;
        String phonetic;
        String definition;
        String translation;
        String exchange;
        short tag;
        String sentence;
        try {
            Cursor cursor = db.rawQuery("select * from WORDS where word_id = ?", new String[]{String.valueOf(word_id)});
            if (cursor.getCount() == 1 && cursor.moveToFirst()) {
                /** 得到数据*/
                word = cursor.getString(cursor.getColumnIndex("word"));
                phonetic = cursor.getString(cursor.getColumnIndex("phonetic"));
                definition = cursor.getString(cursor.getColumnIndex("definition"));
                translation = cursor.getString(cursor.getColumnIndex("translation"));
                exchange = cursor.getString(cursor.getColumnIndex("exchange"));
                tag = cursor.getShort(cursor.getColumnIndex("tag"));
                sentence = cursor.getString(cursor.getColumnIndex("sentence"));
                /** 赋值*/
                words.setWord_id(word_id);
                words.setWord(word);
                words.setPhonetic(phonetic);
                words.setDefinition(definition);
                words.setTranslation(translation);
                words.setExchange(exchange);
                words.setTag(tag);
                words.setSentence(sentence);
            } else {
                Log.d(TAG, "getWordById: " + "查询失败");
                words = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "getWordById: " + e.getMessage());
        }
        if (db != null) {
            db.close();
        }
        return words;
    }

    /**
     * @param wordsLevels
     * @return 返回单词对象数组 ID未查询到的对应位置对象为NULL
     */
    public static ArrayList<Words> getWordArrayByIdArray(ArrayList<WordsLevel> wordsLevels) {
        long startTime = System.nanoTime();
        short word_id[] = new short[wordsLevels.size()];
        for (int i = 0; i < wordsLevels.size(); i++) {
            word_id[i] = wordsLevels.get(i).getWord_id();
        }
        SQLiteDatabase db = DbUtil.getDatabase();
        String word;
        String phonetic;
        String definition;
        String translation;
        String exchange;
        short tag;
        String sentence;
        ArrayList<Words> result = new ArrayList<Words>();
        try {
            db.beginTransaction();
            for (int i = 0; i < word_id.length; i++) {
                Cursor cursor = db.rawQuery("select * from WORDS where word_id = ?", new String[]{String.valueOf(word_id[i])});
                @Nullable Words words = new Words();
                try {
                    if (cursor.getCount() == 0) {
                        throw new Exception("此条ID数据不存在");
                    } else if (cursor.getCount() != 1) {
                        throw new Exception("查询到多条ID数据");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d(TAG, "getWordArrayByIdArray: " + e.getMessage());
                    words = null;
                    result.add(words);
                    continue;
                }
                if (cursor.getCount() == 1 && cursor.moveToFirst()) {
                    /** 得到数据*/
                    word = cursor.getString(cursor.getColumnIndex("word"));
                    phonetic = cursor.getString(cursor.getColumnIndex("phonetic"));
                    definition = cursor.getString(cursor.getColumnIndex("definition"));
                    translation = cursor.getString(cursor.getColumnIndex("translation"));
                    exchange = cursor.getString(cursor.getColumnIndex("exchange"));
                    tag = cursor.getShort(cursor.getColumnIndex("tag"));
                    sentence = cursor.getString(cursor.getColumnIndex("sentence"));

                    words.setWord_id(word_id[i]);
                    words.setWord(word);
                    words.setPhonetic(phonetic);
                    words.setDefinition(definition);
                    words.setTranslation(translation);
                    words.setExchange(exchange);
                    words.setTag(tag);
                    words.setSentence(sentence);
                    result.add(words);//
                } else {
                    words = null;
                    cursor = null;
                    result.add(words);
                }
                if (cursor != null) {
                    cursor.close();
                }
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "getWordArrayByIdArray: " + e.getMessage());
            Log.d(TAG, "getWordArrayByIdArray: " + "查询失败");
        } finally {
            db.endTransaction();
            if (db != null) {
                db.close();
            }
        }
        Log.d(TAG, "getWordArrayByIdArray: spend time " + (System.nanoTime() - startTime) / 1000);
        return result;
    }

    /**
     * 输入单词，返回单词的对象
     * 查询不到单词 对应的对象
     * 返回值为NULL
     */
    public static @Nullable
    Words getWordByWord(String word) {
        SQLiteDatabase db = DbUtil.getDatabase();
        Words words = new Words();
        short word_id;
        String phonetic;
        String definition;
        String translation;
        String exchange;
        short tag;
        String sentence;
        try {
            Cursor cursor = db.rawQuery("select * from WORDS where word = ?", new String[]{word});
            final int indexword_id = cursor.getColumnIndex("word_id");
            final int indexword = cursor.getColumnIndex("word");
            final int indexphonetic = cursor.getColumnIndex("phonetic");
            final int indexdefinition = cursor.getColumnIndex("definition");
            final int indextranslation = cursor.getColumnIndex("translation");
            final int indexexchange = cursor.getColumnIndex("exchange");
            final int indextag = cursor.getColumnIndex("tag");
            final int indexsentence = cursor.getColumnIndex("sentence");
            if (cursor.getCount() == 1 && cursor.moveToFirst()) {
                /** 得到数据*/
                word_id = cursor.getShort(indexword_id);
                word = cursor.getString(indexword);
                phonetic = cursor.getString(indexphonetic);
                definition = cursor.getString(indexdefinition);
                translation = cursor.getString(indextranslation);
                exchange = cursor.getString(indexexchange);
                tag = cursor.getShort(indextag);
                sentence = cursor.getString(indexsentence);

                words.setWord_id(word_id);
                words.setWord(word);
                words.setPhonetic(phonetic);
                words.setDefinition(definition);
                words.setTranslation(translation);
                words.setExchange(exchange);
                words.setTag(tag);
                words.setSentence(sentence);
            } else {
                Log.d(TAG, "getWordById: " + "查询失败");
                words = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "getWordByWord: " + e.getMessage());
        }

        if (db != null) {
            db.close();
        }
        return words;
    }

}
