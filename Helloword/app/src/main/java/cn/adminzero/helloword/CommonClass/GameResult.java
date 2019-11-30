package cn.adminzero.helloword.CommonClass;

import java.io.Serializable;

/**
 * @author: 王翔
 * @date: 2019/11/30-20:00
 * @description: <br>
 * <EndDescription>
 */
public class GameResult implements Serializable {
    public static final boolean win = true;
    public static final boolean lose = false;

    private final long serialVersionUID = 1L;
    boolean result;
    private int addScore;
    private int nowScore;

    public GameResult(boolean result, int addScore, int nowScore) {
        this.result = result;
        this.addScore = addScore;
        this.nowScore = nowScore;
    }

    public GameResult() {

    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public int getAddScore() {
        return addScore;
    }

    public void setAddScore(int addScore) {
        this.addScore = addScore;
    }

    public int getNowScore() {
        return nowScore;
    }

    public void setNowScore(int nowScore) {
        this.nowScore = nowScore;
    }
}
