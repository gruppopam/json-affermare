package com.rest.verification;

import com.rest.response.ResponseStorage;
import cucumber.api.java.en.Then;
import org.junit.Assert;

import javax.ws.rs.core.Response;

import static org.hamcrest.CoreMatchers.is;

public class ResponseVerification {

    @Then("^I verify that the status is \"([^\"]*)\"$")
    public void I_verify_that_the_status_is(String responseStatus) throws Throwable {
        Assert.assertThat(ResponseStorage.response.status(), is(Response.Status.valueOf(responseStatus).getStatusCode()));
    }
}
