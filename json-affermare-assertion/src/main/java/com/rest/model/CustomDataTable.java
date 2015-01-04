package com.rest.model;

import cucumber.api.DataTable;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.rest.model.JsonProcessor.getActualObject;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class CustomDataTable {

    private final List<Map<String, String>> maps;
    private final DataTable dataTable;

    public CustomDataTable(DataTable dataTable) {
        this.dataTable = dataTable;
        maps = removeIgnoredValues(dataTable.asMaps());
    }

    private List<Map<String, String>> removeIgnoredValues(List<Map<String, String>> maps) {
        List<Map<String, String>> toBeReturned = new ArrayList<>();
        for (Map<String, String> map : maps) {
            HashMap<String, String> newMap = new HashMap<>(map);
            map.keySet().stream().filter(key -> map.get(key).equals("-")).forEach(newMap::remove);
            toBeReturned.add(newMap);
        }
        return toBeReturned;
    }

    public List<Map<String, String>> asMaps() {
        return maps;
    }

    public List<Object> tableAsPrimitiveList(Class<?> aClass){
        return dataTable.asList(aClass);
    }

    public Map<String, String> asMap() {
        return maps.get(0);
    }

    public void matches(List<JSONObject> actualJsonObjects) {
        List<Map<String, String>> resultTable = populateResultTable(actualJsonObjects);
        for (Map<String, String> actual : resultTable) {
            try{
                assertTrue(maps.contains(actual));
            }catch (AssertionError e){
                fail("Data "+ actual + " is not found in " + maps);
            }
        }
    }

    public void matchesPrimitive(List<Object> actualObjects) throws JSONException {
        List<Object> expectedList = this.tableAsPrimitiveList(actualObjects.get(0).getClass());
        for (Object actualObject : actualObjects) {
            try{
                assertTrue(expectedList.contains(actualObject));
            }catch (AssertionError e){
                fail("Data "+ actualObject + " is not found in " + expectedList);
            }
        }
    }

    public void matchesPrimitiveWithOrder(List<Object> actualObjects) throws JSONException {
        assertThat(actualObjects, is(this.tableAsPrimitiveList(actualObjects.get(0).getClass())));
    }

    public void matchesOrder(List<JSONObject> actualJsonObjects) {
        List<Map<String, String>> resultTable = populateResultTable(actualJsonObjects);
        assertThat(resultTable, is(maps));
    }

    private List<Map<String, String>> populateResultTable(List<JSONObject> jsonObjects) {
        List<Map<String, String>> resultTable = new ArrayList<>();

        for (JSONObject jsonObject : jsonObjects) {
            Map<String, String> resultMap = new HashMap<>();
            for (String key : this.getKeys()) {
                Object actual;
                actual = getActualObject(jsonObject, key);
                resultMap.put(key, actual.toString());
            }
            resultTable.add(resultMap);
        }

        return resultTable;
    }

    private  List<String> getKeys() {
        ArrayList<String> keys = new ArrayList<String>();
        for (Map<String, String> map : maps) {
            keys.addAll(map.keySet());
        }
        return keys;
    }


}