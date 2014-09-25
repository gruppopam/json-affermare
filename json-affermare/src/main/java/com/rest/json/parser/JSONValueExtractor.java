package com.rest.json.parser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import static com.rest.model.JsonProcessor.getValueFromJsonArray;
import static com.rest.model.JsonProcessor.getValueFromJsonObject;

public class JSONValueExtractor {
    private final JSONObject jsonObject;
    private final String key;


    public JSONValueExtractor(JSONObject jsonObject, String key) {
        this.jsonObject = jsonObject;
        this.key = key;
    }


    public boolean directAccess() {
        return !key.contains(".");
    }

    public Object directValue() {
        return getValueFromJsonObject(jsonObject, key);
    }

    public boolean objectGraphAccess() {
        String[] keys = key.split("\\.");
        Object actual = jsonObject;

        for (String nestedKey : keys) {
            if (actual instanceof JSONObject)
                actual = getValueFromJsonObject((JSONObject) actual, nestedKey);
            else
                return false;
        }
        return true;
    }

    public Object objectGraphValue() {
        Object actual = jsonObject;
        String[] keys = key.split("\\.");
        for (String nestedKey : keys) {
            actual = getValueFromJsonObject((JSONObject) actual, nestedKey);
        }
        return actual;
    }

    public List<Object> objectArrayValue() {
        String[] keys = key.split("\\.");
        Object actual = jsonObject;

        for (String nestedKey : keys) {
            if (actual instanceof JSONObject)
                actual = getValueFromJsonObject((JSONObject) actual, nestedKey);
            if(actual instanceof JSONArray)
                actual = getValueFromJsonArray((JSONArray) actual, nestedKey);
        }
        return (List<Object>) actual;
    }
}
