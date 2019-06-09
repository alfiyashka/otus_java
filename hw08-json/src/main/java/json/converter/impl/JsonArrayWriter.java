package json.converter.impl;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
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
        if (boolean[].class.isAssignableFrom(arrayClazz)) {
            for (var booleanData: (boolean[]) array) {
                arrayBuilder.add(booleanData);
            }
        }
        else if (byte[].class.isAssignableFrom(arrayClazz)) {
            for (var byteData: (byte[]) array) {
                arrayBuilder.add(byteData);
            }
        }
        else if (char[].class.isAssignableFrom(arrayClazz)) {
            for (char charData: (char[]) array) {
                arrayBuilder.add(String.valueOf(charData));
            }
        }
        else if (short[].class.isAssignableFrom(arrayClazz)) {
            for (var shortData: (short[]) array) {
                arrayBuilder.add(shortData);
            }
        }
        else if (int[].class.isAssignableFrom(arrayClazz)) {
            for (var intData: (int[]) array) {
                arrayBuilder.add(intData);
            }
        }
        else if (long[].class.isAssignableFrom(arrayClazz)) {
            for (var longData: (long[]) array) {
                arrayBuilder.add(longData);
            }
        }
        else if (float[].class.isAssignableFrom(arrayClazz)) {
            for (var floatData: (float[]) array) {
                arrayBuilder.add(Double.valueOf(((Float)floatData).toString()).doubleValue());
            }
        }
        else if (double[].class.isAssignableFrom(arrayClazz)) {
            for (var doubleData: (double[]) array) {
                arrayBuilder.add(doubleData);
            }
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
