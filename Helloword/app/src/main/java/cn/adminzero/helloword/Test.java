package cn.adminzero.helloword;

import android.util.Log;

import cn.adminzero.helloword.util.TranslationUtil;
import cn.adminzero.helloword.util.WordLevelUtil;
import cn.adminzero.helloword.util.Words;

/**
 * 测试类  包含一些测试函数
 */
public class Test {
    private static final String TAG = "Test";

    private TranslationUtil translationUtil = new TranslationUtil();

    public void test() {
        Log.d(TAG, "test: start test moudle");
        WordLevelUtil.initWorkBook(Words.tag_zk);
        WordLevelUtil.initWorkBook(Words.tag_gk);
        Log.d(TAG, "test: end test moudle");
    }
}
