package cn.adminzero.helloword.util;

/**
 * author : zhaojunchen
 * date   : 2019/11/2014:58
 * desc   : 单词类
 */
public class Words {
    public static short tag_zk = 0x0001;//中考 0
    public static short tag_gk = 0x0002;//高考 1
    public static short tag_cet4 = 0x0004;//四级 2
    public static short tag_cet6 = 0x0008;//六级 3
    public static short tag_toefl = 0x0010;//托福 4
    public static short tag_ielts = 0x0020;//雅思 5
    public static short tag_gre = 0x0040;// gre 6
    public static short tag_ky = 0x0080;//ky 7

    private short word_id;
    private String word;
    private String phonetic;
    private String definition;
    private String translation;
    private String exchange;
    private short tag;
    private String sentence;

    public Words() {
        sentence = "";
    }

    public short getWord_id() {
        return word_id;
    }

    public void setWord_id(short word_id) {
        this.word_id = word_id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getPhonetic() {
        return phonetic;
    }

    public void setPhonetic(String phonetic) {
        this.phonetic = phonetic;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public short getTag() {
        return tag;
    }

    public void setTag(short tag) {
        this.tag = tag;
    }

    public String getSentence() {
        return sentence;
    }

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }

}
