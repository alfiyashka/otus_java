package json.converter.impl;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import java.util.Collection;
import java.util.Map;

class JsonArrayWriter {

    public JsonArrayBuilder write(Object array) {
        try {
            JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
            var arrayClazz = array.getClass();
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
            else {

                for (var data : (Object[]) array) {
                    var dataClazz = data.getClass();

                    if (Collection.class.isAssignableFrom(dataClazz)) {
                        arrayBuilder.add(new JsonCollectionWriter().write(data));
                    } else if (Map.class.isAssignableFrom(dataClazz)) {
                        arrayBuilder.add(new JsonMapWriter().write(data));
                    } else if (dataClazz.isArray()) {
                        arrayBuilder.add(write(data));
                    } else if (Integer.class.isAssignableFrom(dataClazz)) {
                        arrayBuilder.add((Integer) data);
                    } else if (Boolean.class.isAssignableFrom(dataClazz)) {
                        arrayBuilder.add((Boolean) data);
                    } else if (String.class.isAssignableFrom(dataClazz)) {
                        arrayBuilder.add((String) data);
                    } else if (Character.class.isAssignableFrom(dataClazz)) {
                        arrayBuilder.add(data.toString());
                    } else if (Float.class.isAssignableFrom(dataClazz)) {
                        arrayBuilder.add(Double.valueOf(((Float)data).toString()).doubleValue());
                    } else if (Long.class.isAssignableFrom(dataClazz)) {
                        arrayBuilder.add((Long) data);
                    } else if (Double.class.isAssignableFrom(dataClazz)) {
                        arrayBuilder.add((Double) data);
                    } else if (Short.class.isAssignableFrom(dataClazz)) {
                        arrayBuilder.add((Short) data);
                    } else if (Byte.class.isAssignableFrom(dataClazz)) {
                        arrayBuilder.add((Byte) data);
                    } else {
                        arrayBuilder.add(new JsonObjectWriter().write(data));
                    }
                }
            }
            return arrayBuilder;
        }
        catch (Exception e) {
            throw new RuntimeException("Cannot convert array to json because of error: " + e.getMessage());
        }
    }

}
