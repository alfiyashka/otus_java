import com.google.gson.Gson;
import json.JsonInput2;
import json.JsonInput;
import json.converter.impl.JsonWriter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Main {

    public static void main(String[] args) {

        JsonInput jsonInput = new JsonInput();
        jsonInput.anInt = 1;
        jsonInput.stringList = new ArrayList<>();
        jsonInput.stringList.add("String");
        jsonInput.stringList.add("String1");
        jsonInput.stringSet = new HashSet<>();
        jsonInput.stringSet.add("data");
        jsonInput.map = new HashMap<>();
        jsonInput.map.put("data1", 1);
        jsonInput.map.put("data2", 2);
        jsonInput.stringArray = new String[] {"data2", "data1"};
        jsonInput.integerArray = new Integer[] {1, 2, 8, 9999};
        jsonInput.string = "blala";
        jsonInput.integer = 120;
        jsonInput.aBoolean = true;
        jsonInput.character = 'a';
        jsonInput.jsonInput2 = new JsonInput2(12L);
        jsonInput.jsonInput2.ints = new ArrayList<>();
        jsonInput.jsonInput2.ints.add(23);

        JsonInput2 inputClass = new JsonInput2(130L);
        inputClass.ints = new ArrayList<>();
        inputClass.ints.add(23);
        inputClass.anInt = 12345;
        inputClass.setChars(new char[]{'r', '4', 'g'});

        jsonInput.jsonInput2Set = new HashSet<>();
        jsonInput.jsonInput2Set.add(inputClass);

        jsonInput.jsonInput2Map = new HashMap<>();
        jsonInput.jsonInput2Map.put("entry 1", inputClass);
        jsonInput.jsonInput2Map.put("entry 2", inputClass);

        jsonInput.jsonInput2List = new ArrayList<>();

        jsonInput.jsonInput2Array = new JsonInput2[]{inputClass};

        var jsonWriter = new JsonWriter();

        var json =  jsonWriter.write(jsonInput);
        Gson gson = new Gson();
        gson.fromJson(json, JsonInput.class);
        String jsonFromGson = gson.toJson(jsonInput);
        boolean equal = jsonFromGson.equals(json);

        System.out.println("Jsons are " + (equal ? "equal" : "not equal"));
    }
}
