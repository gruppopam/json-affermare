package com.rest.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

public class JsonParser {

    private final String json;

    public JsonParser(String json) {
        this.json = json;
    }

    public List<JSONObject> parse() throws JSONException {
        Object json = new JSONTokener(this.json).nextValue();
        if (json instanceof JSONArray) {
            return getArray((JSONArray) json);
        }else {
            return asList((JSONObject) json);
        }
    }

    private List<JSONObject> getArray(JSONArray json) throws JSONException {
        List<JSONObject> jsonObjects = new ArrayList<>();
        for (int i = 0; i < json.length(); i++) {
            jsonObjects.add(json.getJSONObject(i));
        }
        return jsonObjects;
    }
}
