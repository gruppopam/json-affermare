package com.rest.response;

import com.sun.jersey.api.client.ClientResponse;

public class JerseyClientResponse implements Response{

    private final ClientResponse clientResponse;
    private final String json;

    public JerseyClientResponse(ClientResponse clientResponse) {
        this.clientResponse = clientResponse;
        this.json = clientResponse.getEntity(String.class);
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
