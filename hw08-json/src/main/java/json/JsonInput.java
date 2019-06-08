package json;


import java.util.List;
import java.util.Map;
import java.util.Set;

public class JsonInput {
    private int privateVal = 0;
    public List<String> stringList;
    public int anInt;
    public String string;
    public float aFloat = 0.8F;
    public Map<String, Integer> map;
    public Set<String> stringSet;
    public String[] stringArray;
    public Integer[] integerArray;
    public Integer integer;
    public Boolean aBoolean;
    public Character character;
    public JsonInput2 jsonInput2;
    public Set<JsonInput2> jsonInput2Set;
    public Map<String, JsonInput2> jsonInput2Map;
    public List<JsonInput2> jsonInput2List;
    public JsonInput2[] jsonInput2Array;
}
