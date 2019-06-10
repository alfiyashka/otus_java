package json.converter.impl;

import json.converter.JsonWriter;
import java.util.Collection;
import java.util.Map;

public class JsonWriterImpl implements JsonWriter {

    @Override
    public String write(Object object) {
        if (object == null) {
            return "null";
        }
        var objectClazz = object.getClass();
        if (Collection.class.isAssignableFrom(objectClazz)) {
            return new JsonArrayWriter().write(((Collection)object).toArray())
                    .build()
                    .toString();
        } else if (Map.class.isAssignableFrom(objectClazz)) {
            return new JsonMapWriter().write(object)
                    .build()
                    .toString();
        } else if (objectClazz.isArray()) {
            return new JsonArrayWriter().write(object)
                    .build()
                    .toString();
        } else if (String.class.isAssignableFrom(objectClazz)
                || Character.class.isAssignableFrom(objectClazz)
                || char.class.isAssignableFrom(objectClazz)) {
            return "\"" + object + "\"";
        } else if (objectClazz.isPrimitive()
                || Boolean.class.isAssignableFrom(objectClazz)
                || Byte.class.isAssignableFrom(objectClazz)
                || Character.class.isAssignableFrom(objectClazz)
                || Short.class.isAssignableFrom(objectClazz)
                || Integer.class.isAssignableFrom(objectClazz)
                || Long.class.isAssignableFrom(objectClazz)
                || Double.class.isAssignableFrom(objectClazz)
                || Float.class.isAssignableFrom(objectClazz)
        ) {
            return object.toString();
        } else {
            return new JsonObjectWriter().write(object)
                    .build()
                    .toString();
        }
    }
}
