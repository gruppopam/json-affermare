package com.rest.model;

import com.rest.json.parser.JSONValueExtractor;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.apache.commons.lang.StringUtils.EMPTY;

public class JsonProcessor {


    public JsonProcessor() {

    }

    public  List<JSONObject> selectJsonObjects(String selector, Object json) throws JSONException {
        List<JSONObject> toBeReturned = new ArrayList<>();
        String restOfSelectors = restOfSelectors(selector);
        selector = firstSelector(selector);
        if (!selector.isEmpty()) {
            if (json instanceof JSONArray)
                for (JSONObject object : asList((JSONArray) json)) {
                    toBeReturned.addAll(selectJsonObjects(restOfSelectors, object.get(selector)));
                }
            else
                toBeReturned.addAll(selectJsonObjects(restOfSelectors, ((JSONObject) json).get(selector)));
        } else {
            if (json instanceof JSONArray)
                toBeReturned.addAll(asList((JSONArray) json));
            else
                toBeReturned.add((JSONObject) json);
        }

        return toBeReturned;
    }

    public List<Object> getJsonPrimitiveCollection(String selector, Object json) throws JSONException {
        List<Object> toBeReturned = new ArrayList<Object>();
        String restOfSelectors = restOfSelectors(selector);
        selector = firstSelector(selector);
        if (!selector.isEmpty()) {
            if (json instanceof JSONArray)
                for (JSONObject object : asList((JSONArray) json)) {
                    toBeReturned.addAll(getJsonPrimitiveCollection(restOfSelectors, object.get(selector)));
                }
            else
                toBeReturned.addAll(getJsonPrimitiveCollection(restOfSelectors, ((JSONObject) json).get(selector)));
        } else {
            toBeReturned.addAll(Arrays.asList(json));
        }

        return getPrimitiveCollection(toBeReturned);
    }

    private List<Object> getPrimitiveCollection(List<Object> primitiveCollectionJsonArray) throws JSONException {
        List<Object> result = new ArrayList<>();
        for (Object jsonArray : primitiveCollectionJsonArray) {
            for (int i = 0; i < ((JSONArray) jsonArray).length(); i++)
                result.add(((JSONArray) jsonArray).get(i));
        }
        return result;
    }

    public List<JSONObject> filterAndSelectJsonObjects(String selector, Object json, String key, String value) throws JSONException {
        return filter(selectJsonObjects(selector,json), d -> getValueFromJsonObject(d,key).toString().equals(value));
    }

    public List<JSONObject> inFilterAndSelectJsonObjects(String selector, Object json, String key, String values) throws JSONException {
        List<String> valuesList = Arrays.asList(values.split(","));
        return filter(selectJsonObjects(selector,json), d -> valuesList.contains(getValueFromJsonObject(d,key).toString()));
    }

    private  String restOfSelectors(String selector) {
        selector = formatSelector(selector);

        int indexOfDot = selector.indexOf(".");
        if (indexOfDot == -1) return EMPTY;

        return selector.substring(indexOfDot);
    }

    private String firstSelector(String selector) {
        selector = formatSelector(selector);

        int indexOfDot = selector.indexOf(".");
        if (indexOfDot == -1) return selector;

        return selector.substring(0, indexOfDot);
    }

    private String formatSelector(String selector) {
        if (selector.contains("root"))
            selector = selector.replace("root", "");
        if (selector.startsWith("."))
            selector = selector.substring(1);
        return selector;
    }

    private List<JSONObject> asList(JSONArray json) throws JSONException {
        List<JSONObject> result = new ArrayList<>();
        for (int i = 0; i < json.length(); i++) {
            result.add((JSONObject) json.get(i));
        }
        return result;
    }


    public static Object getActualObject(JSONObject jsonObject, String key)  {
        JSONValueExtractor extractor = new JSONValueExtractor(jsonObject, key);
        if(extractor.directAccess()){
            return extractor.directValue();
        }
        if(extractor.objectGraphAccess()){
            return extractor.objectGraphValue();
        }
        return extractor.objectArrayValue();
    }

    public static Object getValueFromJsonObject(JSONObject actual, String key) {
        try {
            return actual.get(key);
        } catch (JSONException e) {
            throw new RuntimeException("Key not found in json " + key);
        }
    }

    public static List<Object> getValueFromJsonArray(JSONArray actual, String key) {
        List<Object> result = new ArrayList<Object>();
        try {
            for (int i = 0; i < actual.length(); i++) {
                result.add(((JSONObject) actual.get(i)).get(key));
            }
        } catch (JSONException e) {
            throw new RuntimeException("Key not found in json " + key);
        }
        return result;
    }


    //Duplicated logic from Hyphen to avoid including one more dependency. Till Hyphen goes to maven.
    public static <O, T extends Collection<O>> T filter(T collection, Predicate<? super O> predicate) {
        return collect(collection.stream().filter(predicate), collection);
    }

    static <I,O, C extends Collection<I>, R extends Collection<O>> R collect(Stream<O> stream, C collection) {
        return (R) (collection instanceof List ? stream.collect(toList()) : stream.collect(toSet()));
    }
}
