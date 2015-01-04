package com.rest.model;

import cucumber.api.DataTable;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.apache.commons.lang.StringUtils.countMatches;
import static org.junit.Assert.fail;

public class JsonFlattener {

    private final CustomDataTable table;
    private final List<JSONObject> jsonObjects;

    public JsonFlattener(List<JSONObject> jsonObjects, DataTable table) {
        this.jsonObjects = jsonObjects;
        this.table = new CustomDataTable(table);
    }

    public List<JSONObject> flatten() throws JSONException {
        return flattenAssociations(jsonObjects);
    }

    private boolean isNestedKey(String key) {
        return key.contains(".");
    }

    private List<JSONObject> flattenAssociations(List<JSONObject> jsonObjects) throws JSONException {
        List<JSONObject> flattenedObjects = jsonObjects;
        int maxLevelOfNesting = getMaxLevelOfNesting();

        for (int level = 0; level <= maxLevelOfNesting; level++) {
            flattenedObjects = flattenToLevel(flattenedObjects, level);
        }

        return flattenedObjects;
    }

    private List<JSONObject> flattenToLevel(List<JSONObject> objectsToFlatten, int level) throws JSONException {
        List<JSONObject> result = new ArrayList<>();
        Set<String> arrayKeys = new HashSet<>();
        for (JSONObject jsonObject : objectsToFlatten) {

            Set<String> keysAtLevel = getAllKeysAtLevel(level);
            for (String key : keysAtLevel) {
                if (isJsonArray(jsonObject, key)) {
                    arrayKeys.add(key);
                    continue;
                }
                if (arrayKeys.size() > 1) {
                    fail("Asserting more than one parallel arrays in the same test; Not supported and test has to be separated into two assertions");
                }
            }
            JSONObject flattened = new JSONObject(jsonObject, JSONObject.getNames(jsonObject));
            if (arrayKeys.isEmpty()) {
                result.add(flattened);
            } else {
                for (String arrayKey : arrayKeys) {
                    JSONArray array = getJsonArrayAtKey(jsonObject, arrayKey);
                    for (int i = 0; i < array.length(); i++) {
                        //TODO: find a better way to clone the json; this is super inefficient
                        JSONObject cloneCopy = new JSONObject(flattened.toString());
                        replace(cloneCopy, array.get(i), arrayKey);
                        result.add(cloneCopy);
                    }
                }
            }

        }

        return result;

    }

    private void replace(JSONObject cloneCopy, Object objectToReplace, String arrayKey) throws JSONException {
        String[] nestedKeys = arrayKey.split("\\.");
        for (int i = 0; i < nestedKeys.length - 1; i++) {
            cloneCopy = (JSONObject) cloneCopy.get(nestedKeys[i]);
        }
        cloneCopy.put(nestedKeys[nestedKeys.length - 1], objectToReplace);
    }

    private JSONArray getJsonArrayAtKey(JSONObject jsonObject, String arrayKey) throws JSONException {
        String[] nestedKeys = arrayKey.split("\\.");
        for (int i = 0; i < nestedKeys.length - 1; i++) {
            jsonObject = (JSONObject) jsonObject.get(nestedKeys[i]);
        }
        return (JSONArray) jsonObject.get(nestedKeys[nestedKeys.length - 1]);
    }

    private boolean isJsonArray(JSONObject jsonObject, String key) throws JSONException {
        if (!isNestedKey(key)) {
            return jsonObject.get(key) instanceof JSONArray;
        } else {
            String[] nestedKeys = key.split("\\.");
            for (int i = 0; i < nestedKeys.length - 1; i++) {
                jsonObject = (JSONObject) jsonObject.get(nestedKeys[i]);
            }

            return jsonObject.get(nestedKeys[nestedKeys.length - 1]) instanceof JSONArray;
        }
    }

    private Set<String> getAllKeysAtLevel(int level) {
        Set<String> result = new HashSet<>();
        for (String key : table.getKeys()) {
            if (countMatches(key, ".") <= level) {
                result.add(key);
            }

            if (countMatches(key, ".") > level) {
                String[] split = key.split("\\.");
                List<String> restrictToLevel = new ArrayList<>();
                for (int i = 0; i <= level; i++) {
                    restrictToLevel.add(split[i]);
                }
                result.add(StringUtils.join(restrictToLevel, "."));
            }

        }

        return result;
    }

    private int getMaxLevelOfNesting() {
        int level = 0;
        for (String key : table.getKeys()) {
            if (countMatches(key, ".") > level) {
                level = countMatches(key, ".");
            }
        }
        return level;
    }


}
