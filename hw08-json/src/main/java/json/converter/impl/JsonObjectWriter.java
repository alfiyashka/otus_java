package json.converter.impl;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Map;

class JsonObjectWriter {
    public JsonObjectBuilder write(Object obj) {
        try {
            var clazz = obj.getClass();
            var fields = clazz.getDeclaredFields();
            JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
            for (var field : fields) {
                if (Modifier.isStatic(field.getModifiers())) {//gson ignores static values)
                    continue;
                }
                if (!field.canAccess(obj)) {
                    field.setAccessible(true);
                }
                var fieldData = field.get(obj);
                if (fieldData == null) {
                    continue;
                }

                var type = field.getType();

                if (Collection.class.isAssignableFrom(type))
                {
                    jsonObjectBuilder.add(field.getName(),
                            new JsonCollectionWriter().write(fieldData));
                }
                else if (Map.class.isAssignableFrom(type)){
                    jsonObjectBuilder.add(field.getName(), new JsonMapWriter().write(fieldData));
                }
                else if (type.isArray()) {
                    jsonObjectBuilder.add(field.getName(),
                            new JsonArrayWriter().write(fieldData));
                }
                else if (Integer.class.isAssignableFrom(type)
                        || int.class.isAssignableFrom(type)){
                    jsonObjectBuilder.add(field.getName(), (Integer) fieldData);
                }
                else if (Boolean.class.isAssignableFrom(type)
                        || boolean.class.isAssignableFrom(type)){
                    jsonObjectBuilder.add(field.getName(), (Boolean) fieldData);
                }
                else if (String.class.isAssignableFrom(type)) {
                    jsonObjectBuilder.add(field.getName(), (String) fieldData);
                }
                else if (char.class.isAssignableFrom(type)
                        || Character.class.isAssignableFrom(type)) {
                    jsonObjectBuilder.add(field.getName(), fieldData.toString());
                }
                else if (Float.class.isAssignableFrom(type)
                        || float.class.isAssignableFrom(type)) {
                    jsonObjectBuilder.add(field.getName(),
                            Double.valueOf(((Float) fieldData).toString()).doubleValue());
                }
                else if (Long.class.isAssignableFrom(type)
                        || long.class.isAssignableFrom(type)) {
                    jsonObjectBuilder.add(field.getName(), (Long) fieldData);
                }
                else if (Double.class.isAssignableFrom(type)
                        || double.class.isAssignableFrom(type)) {
                    jsonObjectBuilder.add(field.getName(), (Double) fieldData);
                }
                else if (Short.class.isAssignableFrom(type)
                        || short.class.isAssignableFrom(type)) {
                    jsonObjectBuilder.add(field.getName(), (Short) fieldData);
                }
                else if (Byte.class.isAssignableFrom(type)
                        || byte.class.isAssignableFrom(type)) {
                    jsonObjectBuilder.add(field.getName(), (Byte) fieldData);
                }
                else {
                    jsonObjectBuilder.add(field.getName(), write(fieldData));
                }
            }
            return jsonObjectBuilder;
        }
        catch (Exception e) {
            throw new RuntimeException("Cannot convert object '" + obj.getClass().getName()
                    + "' to json because of error: " + e.getMessage());
        }
    }

}
