package com.rest.verification;

import com.rest.response.ResponseStorage;
import cucumber.api.DataTable;
import cucumber.api.java.en.Then;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.List;
import java.util.Set;

public class JsonVerification {


    @Then("^I verify that the json .+ is empty$")
    public void I_verify_that_json_is_empty() throws Throwable {
        String json = ResponseStorage.response.json();
        if(json.equals("{}")){
            new ObjectVerification().I_verify_that_object_is_empty();
        }else{
            new ArrayVerification().I_verify_that_collection_is_empty();
        }
    }

    @Then("^I verify that the json has the following \"([^\"]*)\"$")
    public void I_verify_that_the_json_has_the_following(String info, DataTable table) throws Throwable {
        if(isJsonAnObject()){
            new ObjectVerification().jsonObjectAssert(table);
            return;
        }

        if(info.endsWith("in the same order") && isJsonPrimitiveArray()){
            new ArrayVerification().I_verify_the_primitive_collection_with_order(table);
            return;
        }else if(info.endsWith("in the same order")){
            new ArrayVerification().I_verify_that_the_following_list_are_present_in_the_same_order(table);
            return;
        }

        if(isJsonPrimitiveArray()){
            new ArrayVerification().I_verify_the_primitive_collection(table);
            return;
        }

        new ArrayVerification().I_verify_the_root_collection(table);
    }

    @Then("^I verify that the json does not have the following \"([^\"]*)\"$")
    public void I_verify_that_the_json_does_not_have_the_following(String filler, DataTable table) throws Throwable {
        if(isJsonAnObject()){
            List<String> keys = table.topCells();
            for (String key : keys) {
                new ObjectVerification().I_verify_that_object_does_not_have(key);
            }
            return;
        }

        new ArrayVerification().I_verify_the_root_collection_does_not_contain(table);
    }

    @Then("^I verify that the json has (\\d+) .+$")
    public void I_verify_that_the_json_array_count(int count) throws Throwable {
        new ArrayVerification().I_verify_that_the_number_of_root_collection_are(count);
    }

    private boolean isJsonPrimitiveArray() throws JSONException {
        String json = ResponseStorage.response.json();
        Object value = new JSONTokener(json).nextValue();
        Object data = ((JSONArray) value).get(0);
        return !(data instanceof JSONObject);
    }

    private boolean isJsonAnObject() {
        String json = ResponseStorage.response.json();
        return json.trim().startsWith("{");
    }
}
