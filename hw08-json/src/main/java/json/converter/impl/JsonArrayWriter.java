package json.converter.impl;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

class JsonArrayWriter {

    private JsonArrayBuilder writePrimitiveType(Object array) {
        var arrayClazz = array.getClass();
        if (!arrayClazz.getComponentType().isPrimitive()) {
            throw new RuntimeException("Internal error: cannot convert to json array,"
                    + " because array have data with non-primitive type "
                    + arrayClazz.getComponentType().getName());
        }
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

        for (int i = 0; i < Array.getLength(array); i++) {
            arrayBuilder.add(JsonValueHelper.jsonValue(Array.get(array, i)).get());
        }
        return arrayBuilder;
    }

    private JsonArrayBuilder writeNonPrimitiveType(Object array) {
        var componentType = array.getClass().getComponentType();
        if (componentType.isPrimitive()) {
            throw new RuntimeException("Internal error: cannot convert to json array,"
                    + " because array have data with primitive type " + componentType.getName());
        }
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for (var data : (Object[]) array) {
            var dataClazz = data.getClass();

            if (Collection.class.isAssignableFrom(dataClazz)) {
                arrayBuilder.add(new JsonCollectionWriter().write(data));
            } else if (Map.class.isAssignableFrom(dataClazz)) {
                arrayBuilder.add(new JsonMapWriter().write(data));
            } else if (dataClazz.isArray()) {
                arrayBuilder.add(write(data));
            }
            else {
                var jsonValue = JsonValueHelper.jsonValue(data);
                if (jsonValue.isPresent()) {
                    arrayBuilder.add(jsonValue.get());
                }
                else {
                    arrayBuilder.add(new JsonObjectWriter().write(data));
                }
            }
        }
        return arrayBuilder;
    }


    public JsonArrayBuilder write(Object array) {
        try {
            var arrayClazz = array.getClass();

            if (arrayClazz.getComponentType().isPrimitive()) {
                return writePrimitiveType(array);
            }
            else {
                return writeNonPrimitiveType(array);
            }
        }
        catch (Exception e) {
            throw new RuntimeException("Cannot convert array to json because of error: " + e.getMessage());
        }
    }
}
