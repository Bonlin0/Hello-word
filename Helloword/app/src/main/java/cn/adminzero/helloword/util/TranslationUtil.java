package cn.adminzero.helloword.util;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * https://www.songshizhao.com/blog/blogPage/996.html src detail
 * https://www.jianshu.com/p/12c2fb8cab66
 * 根据单词 获取在线的翻译资源
 */
public class TranslationUtil {

    private static final String TAG = "TranslationUtil";

    GoogleTranslation googleTranslation = null;

    public TranslationUtil() {

    }

    public void getTranslation(final String word) {
        final String myword = word.trim().toLowerCase();
//        final String url = "https://translate.google.cn/translate_a/single?client=gtx&dt=t&dj=1&ie=UTF-8&sl=auto&tl=zh_TW&q=calculate";
        final String url = "https://dict-co.iciba.com/search.php?word=test&submit=%E6%9F%A5%E8%AF%A2";
        final String result = "none";
        try {
            HttpUtil.sendHttpRequest(url, new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Log.d(TAG, "onFailure: requests error-->" + e.getMessage());
                    e.printStackTrace();

                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    Log.d(TAG, "onResponse: success！");
                    String json = response.body().string();
                    parseGSON(json);// google translation为json  爱词霸 为html解析
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "getTranslation: failed-->" + e.getMessage());
        }
       /* new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient okHttpClient = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(url)
                            .build();
                    Response response = okHttpClient.newCall(request).execute();
                    String result = response.body().string();
                    parseGSON(result);
                    Log.d(TAG, "run: " + result);
                } catch (Exception e) {
                    Log.d(TAG, "run: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }).start();*/
    }

    void parseGSON(String json) {
        Gson gson = new Gson();
        googleTranslation = gson.fromJson(json, GoogleTranslation.class);
        Log.d(TAG, "parseGSON: end");
    }


    /**
     * 下面的三个类为google翻译返回的数据json格式
     * 给出一个链接及其具体的json数据：
     * <p>
     * http://translate.google.cn/translate_a/single?client=gtx&dt=t&dj=1&ie=UTF-8&sl=auto&tl=zh_CN&q=calculate
     * {
     * "sentences":[{"trans":"計算","orig":"calculate","backend":1}]
     * ,"src":"en",
     * "confidence":0.9609375,
     * "ld_result":{"srclangs":["en"],"srclangs_confidences":[0.9609375],"extended_srclangs":["en"]}
     * }
     */

    class GoogleTranslation {
        @SerializedName("sentences")
        public List<Sentences> sentences;
        public String src;
        public float confidence;
        @SerializedName("ld_result")
        public Result result;
    }

    class Sentences {
        public String trans;
        public String orig;
        public int backend;
    }

    class Result {
        List<String> srclangs;
        List<Float> srclangs_confidences;
        List<String> extended_srclangs;
    }

}

