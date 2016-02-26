package com.rest.response;

public class ResponseStorage {

    public static RestResponse response;

    public static void initialize(RestResponse response) {
        ResponseStorage.response = response;
    }
}
