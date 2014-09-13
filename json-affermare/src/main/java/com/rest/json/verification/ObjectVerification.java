package com.rest.json.verification;

import com.rest.model.CustomDataTable;
import cucumber.api.DataTable;
import cucumber.api.java.en.Then;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import static com.jayway.jsonpath.JsonPath.read;
import static com.rest.response.ResponseStorage.response;
import static junit.framework.Assert.fail;
import static junitx.framework.Assert.assertEquals;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ObjectVerification {

    @Then("^I verify that the following .+ is present$")
    public void jsonObjectAssert(DataTable table) throws Throwable {
        jsonObjectHasValues(table, response.json());
    }

    @Then("^I verify that .+ has a \"([^\"]*)\"")
    public void jsonObjectAssociatedObjectAssert(String selector, DataTable table) throws Throwable {
        String associatedJson = read(response.json(), "$." + selector).toString();
        jsonObjectHasValues(table,associatedJson);
    }

    private void jsonObjectHasValues(DataTable table, String json) {
        Map<String, String> tableMap = new CustomDataTable(table).asMap();
        for (Map.Entry<String, String> entries : tableMap.entrySet()) {
            try {
                String valueFromJson = read(json, "$." + entries.getKey()).toString();
                assertThat(valueFromJson, is(entries.getValue()));
            } catch (AssertionError error) {
                fail("Value does not match for key: " + entries.getKey() + " => " + error);
            }
        }
    }

    @Then("^I verify that .+ is empty$")
    public void I_verify_that_object_is_empty() throws Throwable {
        assertEquals(response.json(), "{}");
    }

    @Then("^I verify that book does not have \"([^\"]*)\"$")
    public void I_verify_that_object_does_not_have(String key) throws Throwable {
        JSONObject immediateParent = matchTreeNode("root");
        if (immediateParent.has(key))
            fail(String.format("Didn't expect to find %s in %s", key, immediateParent.toString()));
    }

    private JSONObject matchTreeNode(String selector) throws JSONException {
        JSONObject immediateParent = new JSONObject(response.json());
        if (!selector.equals("root")) {
            for (String sel : selector.split(".")) {
                immediateParent = immediateParent.optJSONObject(sel);
            }
        }
        return immediateParent;
    }
}