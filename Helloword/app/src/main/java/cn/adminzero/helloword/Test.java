package cn.adminzero.helloword;

import android.util.Log;

import java.util.ArrayList;

import cn.adminzero.helloword.util.TranslationUtil;
import cn.adminzero.helloword.util.Words;

import cn.adminzero.helloword.util.WordsLevelUtil;
import cn.adminzero.helloword.util.WordsUtil;

/**
 * 测试类  包含一些测试函数
 */
public class Test {
    private static final String TAG = "Test";

    private TranslationUtil translationUtil = new TranslationUtil();

    public void test() {
        Log.d(TAG, "test: start test moudle");
        ArrayList<Words> words0 = new ArrayList<Words>();
        ArrayList<Words> words1to6 = new ArrayList<Words>();
        ArrayList<Words> words7 = new ArrayList<Words>();
        words0 = WordsLevelUtil.getLevel0();
        words1to6 = WordsLevelUtil.getLevel1to6();
        words7 = WordsLevelUtil.getLevel7();


        Log.d(TAG, "test: end test moudle");
    }
}
