package cn.adminzero.helloword.util;

/**
 * author : zhaojunchen
 * date   : 2019/11/2015:02
 * desc   : none
 */
public class WordsLevel {

    private short word_id;
    private byte level;
    private byte yesterday;

    public WordsLevel() {
        this.level = 0;
        this.yesterday = 0;
    }

    public byte getYestarday() {
        return yesterday;
    }

    public void setYesterday(byte yesterday) {
        this.yesterday = yesterday;
    }

    public short getWord_id() {
        return word_id;
    }

    public void setWord_id(short word_id) {
        this.word_id = word_id;
    }

    public short getLevel() {
        return level;
    }

    public void setLevel(byte level) {
        this.level = level;
    }

}
