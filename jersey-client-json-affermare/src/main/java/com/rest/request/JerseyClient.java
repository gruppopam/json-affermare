package com.rest.request;

import com.rest.response.JerseyClientResponse;
import com.rest.response.Response;
import com.rest.response.ResponseStorage;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import cucumber.api.DataTable;
import cucumber.api.java.en.Then;
import org.apache.commons.lang.StringUtils;

import javax.ws.rs.core.MultivaluedMap;
import java.util.Map;
import java.util.HashMap;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;

public class JerseyClient {

    public static String BASE_URL = null;

    @Then("^I make a GET to \"([^\"]*)\"$")
    public void I_make_a_GET_to(String path) throws Throwable {
        Response response = new JerseyClientResponse(new Client().resource(BASE_URL + path).get(ClientResponse.class));
        ResponseStorage.initialize(response);
    }

    @Then("^I make a GET to \"([^\"]*)\" with params$")
    public void I_make_a_GET_to_with_params(String path, DataTable table) throws Throwable {
        Response response = new JerseyClientResponse(
                new Client().resource(BASE_URL + path)
                        .queryParams(map(table))
                        .get(ClientResponse.class));
        ResponseStorage.initialize(response);
    }

    @Then("^I make a PUT to \"([^\"]*)\"$")
    public void I_make_a_PUT_to(String path) throws Throwable {
        Response response = new JerseyClientResponse(new Client().resource(BASE_URL + path).put(ClientResponse.class));
        ResponseStorage.initialize(response);
    }

    @Then("^I make a PUT to \"([^\"]*)\" with body$")
    public void I_make_a_PUT_to_with_body(String path, DataTable table) throws Throwable {
        Response response = new JerseyClientResponse(new Client().resource(BASE_URL + path)
                .type(APPLICATION_JSON_TYPE)
                .put(ClientResponse.class, payload(table)));
        ResponseStorage.initialize(response);
    }

    @Then("^I make a PUT to \"([^\"]*)\" with header \"([^\"]*)\" with body$")
    public void I_make_a_PUT_to_with_body_and_headers(String path, String headers, DataTable table) throws Throwable {
        String[] headerTokens = headers.split(",");
        Map<String, String> headersMap = new HashMap<>();
        for (String token : headerTokens) {
            String[] headerKeyValue = token.split("=");
            if(headerKeyValue.length == 2) {
                headersMap.put(headerKeyValue[0], headerKeyValue[1]);
            }
        }
        WebResource.Builder putResourceBuilder = new Client().resource(BASE_URL + path)
                .type(APPLICATION_JSON_TYPE);

        for (Map.Entry<String, String> entry : headersMap.entrySet()) {
            putResourceBuilder.header(entry.getKey(),entry.getValue());
        }

        Response response = new JerseyClientResponse(putResourceBuilder.put(ClientResponse.class, payload(table)));
        ResponseStorage.initialize(response);
    }

    @Then("^I make a POST to \"([^\"]*)\"$")
    public void I_make_a_POST_to(String path) throws Throwable {
        Response response = new JerseyClientResponse(new Client().resource(BASE_URL + path).post(ClientResponse.class));
        ResponseStorage.initialize(response);
    }

    @Then("^I make a POST to \"([^\"]*)\" with body$")
    public void I_make_a_POST_to_with_body(String path, DataTable table) throws Throwable {
        Response response = new JerseyClientResponse(new Client().resource(BASE_URL + path)
                .type(APPLICATION_JSON_TYPE)
                .post(ClientResponse.class, payload(table)));
        ResponseStorage.initialize(response);
    }

    @Then("^I make a POST to \"([^\"]*)\" with header \"([^\"]*)\" with body$")
    public void I_make_a_POST_to_with_body_and_headers(String path, String headers, DataTable table) throws Throwable {
        String[] headerTokens = headers.split(",");
        Map<String, String> headersMap = new HashMap<>();
        for (String token : headerTokens) {
            String[] headerKeyValue = token.split("=");
            if(headerKeyValue.length == 2) {
                headersMap.put(headerKeyValue[0], headerKeyValue[1]);
            }
        }
        WebResource.Builder postResourceBuilder = new Client().resource(BASE_URL + path)
                .type(APPLICATION_JSON_TYPE);

        for (Map.Entry<String, String> entry : headersMap.entrySet()) {
            postResourceBuilder.header(entry.getKey(),entry.getValue());
        }
        Response response = new JerseyClientResponse(postResourceBuilder.post(ClientResponse.class, payload(table)));
        ResponseStorage.initialize(response);
    }

    @Then("^I make a DELETE to \"([^\"]*)\"$")
    public void I_make_a_DELETE_to(String path) throws Throwable {
        Response response = new JerseyClientResponse(new Client().resource(BASE_URL + path).delete(ClientResponse.class));
        ResponseStorage.initialize(response);
    }

    private String payload(DataTable table) {
        return StringUtils.join(table.flatten().toArray(), "");
    }

    private MultivaluedMap<String, String> map(DataTable table) {
        MultivaluedMap<String, String> result = new MultivaluedMapImpl();
        Map<String, String> stringStringMap = table.asMaps().get(0);
        for (Map.Entry<String, String> entry : stringStringMap.entrySet()) {
            result.add(entry.getKey(), entry.getValue());
        }
        return result;
    }


    public static void initialize(String baseUrl) {
        BASE_URL = baseUrl;
    }
}
