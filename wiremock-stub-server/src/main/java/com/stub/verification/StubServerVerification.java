package com.stub.verification;

import cucumber.api.DataTable;
import cucumber.api.java.en.Then;
import org.apache.commons.lang.StringUtils;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.stub.StubServerDriver.getServer;

public class StubServerVerification {

    @Then("^I verify POST requested for \"([^\"]*)\" \"([^\"]*)\" with body$")
    public void I_verify_POST_requested_for_with_body(String server, String url, DataTable table) throws Throwable {
        getServer(server).verify(postRequestedFor(urlMatching(url))
                        .withRequestBody(equalToJson(json(table)))
                        .withHeader("Content-Type", matching("application/json"))
        );
    }

    @Then("^I verify no POST requested for \"([^\"]*)\" \"([^\"]*)\"$")
    public void I_verify_no_POST_requested_for(String server, String url) throws Throwable {
        getServer(server).verify(0, postRequestedFor(urlEqualTo(url)));
    }

    private String json(DataTable table) {
        return StringUtils.join(table.flatten().toArray(), "");
    }

}
