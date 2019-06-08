package json;

import java.util.List;

public class JsonInput2 {
    public JsonInput2(long aLong) {
        this.aLong = aLong;
    }
    public List<Integer> ints;
    public int anInt;
    private String string = "bbb";
    private final long aLong;
    public final static int STATIC_INT = 100;
    private static String STATIC_STRING = "String";
    private char[] chars;

    public void setChars(char[] chars) {
        this.chars = chars;
    }
}
