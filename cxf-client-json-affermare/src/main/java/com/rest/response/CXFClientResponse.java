package com.rest.response;

import org.apache.cxf.jaxrs.client.WebClient;

import javax.ws.rs.core.MediaType;

public class CXFClientResponse implements RestResponse {

    private final javax.ws.rs.core.Response response;
    private final String json;

    public CXFClientResponse(String baseUrl, String path) {
        WebClient webClient = WebClient.create(baseUrl);
        webClient.accept(MediaType.APPLICATION_JSON);
        response = webClient.path(path).get();
        json = null;//response.readEntity(String.class);
    }

    @Override
    public int status() {
        return response.getStatus();
    }

    @Override
    public String json() {
        return json;
    }
}
