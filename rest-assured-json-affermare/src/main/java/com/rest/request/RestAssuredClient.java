package com.rest.request;

import com.jayway.restassured.RestAssured;
import com.rest.response.Response;
import com.rest.response.ResponseStorage;
import com.rest.response.RestAssuredResponse;
import cucumber.api.DataTable;
import cucumber.api.java.en.Then;
import org.apache.commons.lang.StringUtils;

import java.util.Map;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.http.ContentType.JSON;

public class RestAssuredClient {

    //public static String BASE_URL = null;
    public static Integer PORT = null;

    @Then("^I make a GET to \"([^\"]*)\"$")
    public void I_make_a_GET_to(String path) throws Throwable {
        Response response = new RestAssuredResponse(given().get(path));
        ResponseStorage.initialize(response);
    }

    @Then("^I make a GET to \"([^\"]*)\" with params$")
    public void I_make_a_GET_to_with_params(String path, DataTable table) throws Throwable {
        Response response = new RestAssuredResponse(given().parameters(params(table)).get(path));
        ResponseStorage.initialize(response);
    }

    @Then("^I make a PUT to \"([^\"]*)\"$")
    public void I_make_a_PUT_to(String path) throws Throwable {
        Response response = new RestAssuredResponse(given().put(path));
        ResponseStorage.initialize(response);
    }

    @Then("^I make a PUT to \"([^\"]*)\" with params$")
    public void I_make_a_PUT_to_with_params(String path, DataTable table) throws Throwable {
        Response response = new RestAssuredResponse(given().parameters(params(table)).put(path));
        ResponseStorage.initialize(response);
    }

    @Then("^I make a PUT to \"([^\"]*)\" with body$")
    public void I_make_a_PUT_to_with_body(String path, DataTable table) throws Throwable {
        Response response = new RestAssuredResponse(given().contentType(JSON).body(payload(table)).put(path));
        ResponseStorage.initialize(response);
    }

    @Then("^I make a POST to \"([^\"]*)\"$")
    public void I_make_a_POST_to(String path) throws Throwable {
        Response response = new RestAssuredResponse(given().post(path));
        ResponseStorage.initialize(response);
    }

    @Then("^I make a POST to \"([^\"]*)\" with params$")
    public void I_make_a_POST_to_with_params(String path, DataTable table) throws Throwable {
        Response response = new RestAssuredResponse(given().parameters(params(table)).post(path));
        ResponseStorage.initialize(response);
    }

    @Then("^I make a POST to \"([^\"]*)\" with body$")
    public void I_make_a_POST_to_with_body(String path, DataTable table) throws Throwable {
        Response response = new RestAssuredResponse(given().contentType(JSON).body(payload(table)).post(path));
        ResponseStorage.initialize(response);
    }

    @Then("^I make a DELETE to \"([^\"]*)\"$")
    public void I_make_a_DELETE_to(String path) throws Throwable {
        Response response = new RestAssuredResponse(given().delete(path));
        ResponseStorage.initialize(response);
    }

    private String payload(DataTable table) {
        return StringUtils.join(table.flatten().toArray(), "");
    }

    private Map<String, String> params(DataTable table) {
        return table.asMaps().get(0);
    }


    public static void initialize(Integer port) {
        PORT = port;
        RestAssured.port = port;
    }
}
