package com.stub.request;

import cucumber.api.DataTable;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import org.apache.commons.lang.StringUtils;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.stub.StubServerDriver.getServer;

public class StubServerRequest {

    @Then("^I stub a POST to \"([^\"]*)\" \"([^\"]*)\" with body$")
    public void I_stub_a_POST_to_with_body(String server, String url, DataTable table) throws Throwable {
        getServer(server).
                stubFor(
                        post(urlEqualTo(url))
                                .withHeader("Content-Type", equalTo(("application/json")))
                                .withRequestBody(equalToJson(json(table)))
                                .willReturn(aResponse().withStatus(200))
                );
    }

    @Given("^I stub a POST to \"([^\"]*)\" \"([^\"]*)\"$")
    public void I_stub_a_POST_to(String server, String url) throws Throwable {
        getServer(server).
                stubFor(post(urlEqualTo(url))
                                .withHeader("Content-Type", equalTo(("application/json")))
                                .willReturn(aResponse().withStatus(200))
                );
    }

    @Then("^I stub a GET to \"([^\"]*)\" \"([^\"]*)\" with body$")
    public void I_stub_a_GET_to_with_body(String server, String url, DataTable table) throws Throwable {
        getServer(server).
                stubFor(
                        get(urlEqualTo(url))
                                .willReturn(aResponse().withBody(json(table)).withStatus(200))
                );
    }


    private String json(DataTable table) {
        return StringUtils.join(table.flatten().toArray(), "");
    }
}
