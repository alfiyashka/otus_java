package json.converter.impl;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Map;

class JsonObjectWriter {
    public JsonObjectBuilder write(Object obj) {
        try {

            JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
            if (obj == null) {
                return jsonObjectBuilder;
            }
            var clazz = obj.getClass();
            var fields = clazz.getDeclaredFields();
            for (var field : fields) {
                if (Modifier.isStatic(field.getModifiers())) {//gson ignores static values
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
                else {
                    var jsonValue = JsonValueHelper.jsonValue(fieldData);
                    if (jsonValue.isPresent()) {
                        jsonObjectBuilder.add(field.getName(), jsonValue.get());
                    }
                    else {
                        jsonObjectBuilder.add(field.getName(), write(fieldData));
                    }
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
