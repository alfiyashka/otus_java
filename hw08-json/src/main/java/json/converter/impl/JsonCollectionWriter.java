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
                }
                var jsonValue = JsonValueHelper.jsonValue(data);
                if (jsonValue.isPresent()) {
                    arrayBuilder.add(jsonValue.get());
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
