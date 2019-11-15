package cn.adminzero.helloword.util;

import android.media.MediaPlayer;
import android.text.TextUtils;
import android.util.Log;

/**
 * 音频播放器类
 * 多线程适用-->在子线程播放音频  避免卡死
 * MediaPlayUtil player = new MediaPlayUtil(); // 最好设置在全局
 * player.playword(String word); // 播放单词
 */
public class MediaPlayUtil {
    // 音频链接获取
    private static final String TAG = "MediaPlayUtil";
    private MediaPlayer mediaPlayer = null;

    private static String getUrl(String word) {
        return "https://ssl.gstatic.com/dictionary/static/sounds/oxford/" + word + "--_gb_1.mp3";
    }

    public MediaPlayUtil() {
        mediaPlayer = new MediaPlayer();
    }

    // 单词发音
    public boolean playword(String word) {
        if (TextUtils.isEmpty(word)) {
            Log.d(TAG, "playword: " + "单词确缺失");
            return false;
        }
        final String url = getUrl(word.trim().toLowerCase());
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
                mediaPlayer.reset();
                try {
                    mediaPlayer.setDataSource(url);
                    mediaPlayer.prepare();
                    mediaPlayer.start();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        return true;
    }


}
