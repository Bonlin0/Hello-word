package cn.adminzero.helloword.util;

/**
 * author : zhaojunchen
 * date   : 2019/11/2015:02
 * desc   : none
 */
public class WordsLevel {
    /*public static final String CREATE_HISTORY =
            "create table " + "HISTORY_" + getuserid() + "(" +
                    "word_id integer primary key," +
                    "level int default(0)," +
                    "yesterday integer default(0))";*/
    private short word_id;
    private short level;
    private boolean isyestarday;

    public WordsLevel() {
        this.level = 0;
        this.isyestarday = false;
    }

    public boolean isIsyestarday() {
        return isyestarday;
    }

    public void setIsyestarday(boolean isyestarday) {
        this.isyestarday = isyestarday;
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

    public void setLevel(short level) {
        this.level = level;
    }

}
