package json.converter.impl;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import java.util.Collection;
import java.util.Map;

class JsonCollectionWriter {
    public JsonArrayBuilder write(Object collection) {
        try {
            Collection collectionData = (Collection) collection;
            JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

            for (var data: collectionData) {
                var dataClazz = data.getClass();

                if (Collection.class.isAssignableFrom(dataClazz)) {
                    arrayBuilder.add(write(data));
                } else if (Map.class.isAssignableFrom(dataClazz)) {
                    arrayBuilder.add(new JsonMapWriter().write(data));
                } else if (dataClazz.isArray()) {
                    arrayBuilder.add(new JsonArrayWriter().write(data));
                } else if (Integer.class.isAssignableFrom(dataClazz)){
                    arrayBuilder.add((Integer) data);
                }
                else if (Boolean.class.isAssignableFrom(dataClazz)){
                    arrayBuilder.add((Boolean) data);
                }
                else if (String.class.isAssignableFrom(dataClazz)) {
                    arrayBuilder.add((String) data);
                }
                else if (Character.class.isAssignableFrom(dataClazz)) {
                    arrayBuilder.add(data.toString());
                }
                else if (Float.class.isAssignableFrom(dataClazz)) {
                    arrayBuilder.add(Double.valueOf(((Float)data).toString()).doubleValue());
                }
                else if (Long.class.isAssignableFrom(dataClazz)) {
                    arrayBuilder.add((Long) data);
                }
                else if (Double.class.isAssignableFrom(dataClazz)) {
                    arrayBuilder.add((Double) data);
                }
                else if (Short.class.isAssignableFrom(dataClazz)) {
                    arrayBuilder.add((Short) data);
                }
                else if (Byte.class.isAssignableFrom(dataClazz)) {
                    arrayBuilder.add((Byte) data);
                }
                else {
                    arrayBuilder.add(new JsonObjectWriter().write(data));
                }
            }
            return arrayBuilder;
        }
        catch (Exception e) {
            throw new RuntimeException("Cannot convert collection to json because of error: " + e.getMessage());
        }
    }
}
