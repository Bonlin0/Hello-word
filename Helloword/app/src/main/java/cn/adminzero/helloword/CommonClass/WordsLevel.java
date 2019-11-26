package cn.adminzero.helloword.CommonClass;
import java.io.Serializable;
public class WordsLevel implements Serializable {
    private final long serialVersionUID = 1L;
    private short word_id;
    private short level;
    private byte yesterday;

    public WordsLevel() {
        this.level = 0;
        this.yesterday = 0;
    }
    public WordsLevel(short word_id) {
        this.word_id=word_id;
        this.level = 0;
        this.yesterday = 0;
    }

    public long getSerialVersionUID() {
        return serialVersionUID;
    }

    public byte getYesterday() {
        return yesterday;
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

    public void setLevel(short level) {
        this.level = level;
    }

}

