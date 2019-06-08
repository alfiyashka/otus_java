package json.converter.impl;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

class JsonMapWriter {

    private String stringKeyValue(Object key) {
        if (!String.class.isAssignableFrom(key.getClass())
                && !Character.class.isAssignableFrom(key.getClass())
                && !char.class.isAssignableFrom(key.getClass())) {
            throw new RuntimeException("Cannot convert map to json, because map key is not string");
        }

        if (Character.class.isAssignableFrom(key.getClass())
                || char.class.isAssignableFrom(key.getClass())){
            return ((Character) key).toString();
        } else {
            return (String)key;
        }
    }

    public JsonObjectBuilder write(Object map) {
        try {
            Map mapData = (Map) map;
            JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
            for (var data: mapData.entrySet()) {
                HashMap.Entry entry = (Map.Entry) data;

                var valueClazz = entry.getValue().getClass();

                String keyValue = stringKeyValue(entry.getKey());

                if (Collection.class.isAssignableFrom(valueClazz)) {
                    objectBuilder.add(keyValue, new JsonCollectionWriter().write(entry.getValue()));
                }
                else if (Map.class.isAssignableFrom(valueClazz)){
                    objectBuilder.add(keyValue, new JsonMapWriter().write(entry.getValue()));
                }
                else if (valueClazz.isArray()) {
                    objectBuilder.add(keyValue, new JsonArrayWriter().write(entry.getValue()));
                }
                else if (Integer.class.isAssignableFrom(valueClazz)){
                    objectBuilder.add(keyValue, (Integer) entry.getValue());
                }
                else if (Boolean.class.isAssignableFrom(valueClazz)){
                    objectBuilder.add(keyValue, (Boolean) entry.getValue());
                }
                else if (String.class.isAssignableFrom(valueClazz)) {
                    objectBuilder.add(keyValue, (String) entry.getValue());
                }
                else if (Character.class.isAssignableFrom(valueClazz)) {
                    objectBuilder.add(keyValue, entry.getValue().toString());
                }
                else if (Float.class.isAssignableFrom(valueClazz)) {
                    objectBuilder.add(keyValue,
                            Double.valueOf(((Float)entry.getValue()).toString()).doubleValue());
                }
                else if (Long.class.isAssignableFrom(valueClazz)) {
                    objectBuilder.add(keyValue, (Long) entry.getValue());
                }
                else if (Double.class.isAssignableFrom(valueClazz)) {
                    objectBuilder.add(keyValue, (Double) entry.getValue());
                }
                else if (Short.class.isAssignableFrom(valueClazz)) {
                    objectBuilder.add(keyValue, (Short) entry.getValue());
                }
                else if (Byte.class.isAssignableFrom(valueClazz)) {
                    objectBuilder.add(keyValue, (Byte) entry.getValue());
                }
                else {
                    objectBuilder.add(keyValue, new JsonObjectWriter().write(entry.getValue()));
                }

            }
            return objectBuilder;
        }
        catch (Exception e) {
            throw new RuntimeException("Cannot convert map to json because of error: " + e.getMessage());
        }
    }

}
