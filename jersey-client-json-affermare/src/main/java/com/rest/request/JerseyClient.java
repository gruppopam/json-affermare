package com.rest.request;

import com.rest.response.JerseyClientResponse;
import com.rest.response.ResponseStorage;
import com.rest.response.RestResponse;
import cucumber.api.DataTable;
import cucumber.api.java.en.Then;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;

public class JerseyClient {

    public static String BASE_URL = null;

    @Then("^I make a GET to \"([^\"]*)\"$")
    public void I_make_a_GET_to(String path) throws Throwable {
        Response response = ClientBuilder.newClient().target(BASE_URL).path(path)
                .request().get();
        JerseyClientResponse jerseyClientResponse = new JerseyClientResponse(response);
        ResponseStorage.initialize(jerseyClientResponse);
    }

    @Then("^I make a GET to \"([^\"]*)\" with params$")
    public void I_make_a_GET_to_with_params(String path, DataTable table) throws Throwable {
        WebTarget webTarget = ClientBuilder.newClient().target(BASE_URL).path(path);
        map(table).entrySet().stream().forEach(entry -> webTarget.queryParam(entry.getKey(), entry.getValue()));
        RestResponse response = new JerseyClientResponse(webTarget.request().get());
        ResponseStorage.initialize(response);
    }

    @Then("^I make a PUT to \"([^\"]*)\"$")
    public void I_make_a_PUT_to(String path) throws Throwable {
        RestResponse response = new JerseyClientResponse(ClientBuilder.newClient().target(BASE_URL).path(path).request().put(null));
        ResponseStorage.initialize(response);
    }

    @Then("^I make a PUT to \"([^\"]*)\" with body$")
    public void I_make_a_PUT_to_with_body(String path, DataTable table) throws Throwable {
        RestResponse response = new JerseyClientResponse(ClientBuilder.newClient().target(BASE_URL).path(path)
                .request(APPLICATION_JSON_TYPE)
                .put(payload(table)));
        ResponseStorage.initialize(response);
    }

    @Then("^I make a PUT to \"([^\"]*)\" with header \"([^\"]*)\" with body$")
    public void I_make_a_PUT_to_with_body_and_headers(String path, String headers, DataTable table) throws Throwable {
        String[] headerTokens = headers.split(",");
        Map<String, String> headersMap = new HashMap<>();
        for (String token : headerTokens) {
            String[] headerKeyValue = token.split("=");
            if (headerKeyValue.length == 2) {
                headersMap.put(headerKeyValue[0], headerKeyValue[1]);
            }
        }
        Invocation.Builder putResourceBuilder = ClientBuilder.newClient().target(BASE_URL).path(path)
                .request(APPLICATION_JSON_TYPE);

        for (Map.Entry<String, String> entry : headersMap.entrySet()) {
            putResourceBuilder.header(entry.getKey(), entry.getValue());
        }

        RestResponse response = new JerseyClientResponse(putResourceBuilder.put(Entity.entity(payload(table), APPLICATION_JSON_TYPE)));
        ResponseStorage.initialize(response);
    }

    @Then("^I make a POST to \"([^\"]*)\"$")
    public void I_make_a_POST_to(String path) throws Throwable {
        RestResponse response = new JerseyClientResponse(ClientBuilder.newClient().target(BASE_URL).path(path).request().post(null));
        ResponseStorage.initialize(response);
    }

    @Then("^I make a POST to \"([^\"]*)\" with body$")
    public void I_make_a_POST_to_with_body(String path, DataTable table) throws Throwable {
        RestResponse response = new JerseyClientResponse(ClientBuilder.newClient().target(BASE_URL).path(path)
                .request(APPLICATION_JSON_TYPE)
                .post(Entity.entity(payload(table), APPLICATION_JSON_TYPE)));
        ResponseStorage.initialize(response);
    }

    @Then("^I make a POST to \"([^\"]*)\" with header \"([^\"]*)\" with body$")
    public void I_make_a_POST_to_with_body_and_headers(String path, String headers, DataTable table) throws Throwable {
        String[] headerTokens = headers.split(",");
        Map<String, String> headersMap = new HashMap<>();
        for (String token : headerTokens) {
            String[] headerKeyValue = token.split("=");
            if (headerKeyValue.length == 2) {
                headersMap.put(headerKeyValue[0], headerKeyValue[1]);
            }
        }
        Invocation.Builder postResourceBuilder = ClientBuilder.newClient().target(BASE_URL).path(path).request(APPLICATION_JSON_TYPE);

        for (Map.Entry<String, String> entry : headersMap.entrySet()) {
            postResourceBuilder.header(entry.getKey(), entry.getValue());
        }
        RestResponse response = new JerseyClientResponse(postResourceBuilder.post(Entity.entity(payload(table), APPLICATION_JSON_TYPE)));
        ResponseStorage.initialize(response);
    }

    @Then("^I make a DELETE to \"([^\"]*)\"$")
    public void I_make_a_DELETE_to(String path) throws Throwable {
        RestResponse response = new JerseyClientResponse(ClientBuilder.newClient().target(BASE_URL).path(path).request().delete());
        ResponseStorage.initialize(response);
    }

    private Entity<String> payload(DataTable table) {
        String collect = table.raw().stream().flatMap(Collection::stream).collect(Collectors.joining(""));
        return Entity.entity(collect, APPLICATION_JSON_TYPE);
    }

    private MultivaluedMap<String, String> map(DataTable table) {
        MultivaluedMap<String, String> result = new MultivaluedHashMap<>();
        Map<String, String> stringStringMap = table.asMaps(String.class, String.class).get(0);
        for (Map.Entry<String, String> entry : stringStringMap.entrySet()) {
            result.add(entry.getKey(), entry.getValue());
        }
        return result;
    }


    public static void initialize(String baseUrl) {
        BASE_URL = baseUrl;
    }
}
