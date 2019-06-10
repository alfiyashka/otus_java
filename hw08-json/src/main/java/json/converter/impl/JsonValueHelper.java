package json.converter.impl;

import javax.json.Json;
import javax.json.JsonValue;
import java.util.Optional;

public class JsonValueHelper {
    
    public static Optional<JsonValue> jsonValue(Object obj){
        var clazz = obj.getClass();
        if (Integer.class.isAssignableFrom(clazz)
                || int.class.isAssignableFrom(clazz)){
            return Optional.of(Json.createValue((Integer) obj));
        }
        else if (String.class.isAssignableFrom(clazz)) {
            return Optional.of(Json.createValue((String) obj));
        }
        else if (char.class.isAssignableFrom(clazz)
                || Character.class.isAssignableFrom(clazz)) {
            return Optional.of(Json.createValue(obj.toString()));
        }
        else if (Float.class.isAssignableFrom(clazz)
                || float.class.isAssignableFrom(clazz)) {
            return Optional.of(
                    Json.createValue(Double.valueOf(((Float) obj).toString()).doubleValue()));
        }
        else if (Long.class.isAssignableFrom(clazz)
                || long.class.isAssignableFrom(clazz)) {
            return Optional.of(Json.createValue((Long) obj));
        }
        else if (Double.class.isAssignableFrom(clazz)
                || double.class.isAssignableFrom(clazz)) {
            return Optional.of(Json.createValue((Double) obj));
        }
        else if (Short.class.isAssignableFrom(clazz)
                || short.class.isAssignableFrom(clazz)) {
            return Optional.of(Json.createValue((Short) obj));
        }
        else if (Byte.class.isAssignableFrom(clazz)
                || byte.class.isAssignableFrom(clazz)) {
            return Optional.of(Json.createValue((Byte) obj));
        }
        else if (Boolean.class.isAssignableFrom(clazz)
                || boolean.class.isAssignableFrom(clazz)) {
            return Optional.of((boolean) obj ? JsonValue.TRUE : JsonValue.FALSE);
        }
        return Optional.ofNullable(null);
    }
}
