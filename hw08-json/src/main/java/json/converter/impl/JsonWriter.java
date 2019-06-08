package json.converter.impl;

import json.converter.IJsonWriter;

public class JsonWriter implements IJsonWriter {

    @Override
    public String write(Object object) {
        return new JsonObjectWriter().write(object).build().toString();
    }
}
