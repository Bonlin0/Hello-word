package cn.adminzero.helloword;

import android.util.Log;

import java.util.ArrayList;

import cn.adminzero.helloword.util.TranslationUtil;
import cn.adminzero.helloword.util.WordLevelUtil;
import cn.adminzero.helloword.util.Words;
import cn.adminzero.helloword.util.WordsUtil;

/**
 * 测试类  包含一些测试函数
 */
public class Test {
    private static final String TAG = "Test";

    private TranslationUtil translationUtil = new TranslationUtil();

    public void test() {
        Log.d(TAG, "test: start test moudle");
        ArrayList<Words> words = new ArrayList<Words>();
        Words word = new Words();
        short[] you = {1, 2, 234, 34, 5, 45, 4, 56, 45, 765, 7, 567, 56, 88, 766, 7967};
        word = WordsUtil.getWordById((short) 2);
        words = WordsUtil.getWordArrayByIdArray(you);


        Log.d(TAG, "test: end test moudle");
    }
}
