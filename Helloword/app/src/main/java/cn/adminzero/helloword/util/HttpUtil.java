package cn.adminzero.helloword.util;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * author : zhaojunchen
 * date   : 2019/11/18/11:43
 * desc   : 发出Http请求,编写回调函数
 * 参见TranslationUtil返回google翻译
 */
public class HttpUtil {

    public static void sendHttpRequest(final String url, final okhttp3.Callback callback) {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        okHttpClient.newCall(request).enqueue(callback);
    }

}
