package cn.adminzero.helloword.util;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * 请求url 返回数据
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
