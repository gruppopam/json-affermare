package com.rest.request;

import com.rest.response.CXFClientResponse;
import com.rest.response.ResponseStorage;
import com.rest.response.RestResponse;
import cucumber.api.java.en.Then;

public class CXFClient {

    public static String BASE_URL = null;

    @Then("^I make a GET to \"([^\"]*)\"$")
    public void I_make_a_Another_GET_to(String path) throws Throwable {
        RestResponse response = new CXFClientResponse(BASE_URL, path);
        ResponseStorage.initialize(response);
    }


    public static void initialize(String baseUrl){
        BASE_URL = baseUrl;
    }
}
