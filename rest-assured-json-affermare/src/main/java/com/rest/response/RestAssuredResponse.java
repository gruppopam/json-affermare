package com.rest.response;


public class RestAssuredResponse implements RestResponse {

    private final com.jayway.restassured.response.Response clientResponse;
    private final String json;

    public RestAssuredResponse(com.jayway.restassured.response.Response clientResponse) {
        this.clientResponse = clientResponse;
        this.json = clientResponse.getBody().asString();
    }

    @Override
    public int status() {
        return clientResponse.getStatusCode();
    }

    @Override
    public String json() {
        return json;
    }
}
