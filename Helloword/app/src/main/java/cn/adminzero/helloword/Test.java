package cn.adminzero.helloword;

import android.util.Log;

import cn.adminzero.helloword.util.TranslationUtil;

/**
 * 测试类  包含一些测试函数
 */
public class Test {
    private static final String TAG = "Test";

    private TranslationUtil translationUtil = new TranslationUtil();

    public void test() {
        Log.d(TAG, "test: start test moudle");
        translationUtil.getTranslation
                ("https://raw.githubusercontent.com/square/okhttp/master/samples/guide/src/main/java/okhttp3/guide/GetExample.java");

    }
}
