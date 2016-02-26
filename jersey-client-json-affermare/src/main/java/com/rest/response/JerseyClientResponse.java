package com.rest.response;


import javax.ws.rs.core.Response;

public class JerseyClientResponse implements RestResponse {

    private final Response clientResponse;
    private final String json;

    public JerseyClientResponse(Response clientResponse) {
        this.clientResponse = clientResponse;
        this.json = clientResponse.readEntity(String.class);
    }

    @Override
    public int status() {
        return clientResponse.getStatus();
    }

    @Override
    public String json() {
        return json;
    }
}
