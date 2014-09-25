package com.rest.verification;

import com.rest.json.parser.RowIterator;
import com.rest.json.parser.TableIterator;
import cucumber.api.DataTable;
import cucumber.api.java.en.Then;

import static com.rest.json.parser.AssertHelper.*;

public class ArrayVerification {

    /*@Then("^I verify that the collection \"([^\"]*)\" contains$")
    public void I_verify_that_the_collection(String selector, DataTable table) throws Throwable {
        assertCollection(selector, table);
    }*/

    @Then("^I verify the list of .+ in the same order$")
    public void I_verify_the_primitive_collection_with_order(DataTable table) throws Throwable {
        assertCollectionAsPrimitiveWithOrder("root", table);
    }

    @Then("^I verify the list of .+ are present$")
    public void I_verify_the_primitive_collection(DataTable table) throws Throwable {
        assertCollectionAsPrimitive("root", table);
    }

    @Then("^I verify that the following .+ are present$")
    public void I_verify_the_root_collection(DataTable table) throws Throwable {
        assertCollection("root", table);
    }

    @Then("^I verify that the following .+ are not present$")
    public void I_verify_the_root_collection_does_not_contain(DataTable table) throws Throwable {
        TableIterator tableIterator = new TableIterator(table.asMaps());
        while (tableIterator.hasNext()) {
            RowIterator rowIterator = tableIterator.next();
            assertCollectionToBeUndefined("", rowIterator);
        }
    }

    @Then("^I verify that no .+ are present$")
    public void I_verify_that_collection_is_empty() throws Throwable {
        assertEmpty("root");
    }

    @Then("^I verify that the number of .+ are \"([^\"]*)\"$")
    public void I_verify_that_the_number_of_root_collection_are(Integer count) throws Throwable {
        assertCount("root", count);

    }

    @Then("^I verify that .+ has the following \"([^\"]*)\"$")
    public void I_verify_that_object_has_many_objects(String selector, DataTable table) throws Throwable {
        assertCollection("root." + selector, table);
    }

    @Then("^I filter the .+ with \"([^\"]*)\" is \"([^\"]*)\"$")
    public void I_filter_the_collection_with_specific_condition(String key, String value, DataTable table) throws Throwable {
        assertCollectionWithFilter("root", table, key, value);
    }

    @Then("^I filter the .+ with \"([^\"]*)\" is \"([^\"]*)\" and has the following \"([^\"]*)\"$")
    public void I_filter_the_collection_with_specific_condition(String key, String value, String selector, DataTable table) throws Throwable {
        assertCollectionWithFilterAndVerifyAssociatedList("root", table, key, value, selector);
    }


    @Then("^I filter the .+ with \"([^\"]*)\" in \"([^\"]*)\"$")
    public void I_filter_the_authors_with_in(String key, String values, DataTable table) throws Throwable {
        assertCollectionWithInFilter("root", table, key, values);
    }

    @Then("^I verify that the following .+ are present in the same order$")
    public void I_verify_that_the_following_list_are_present_in_the_same_order(DataTable table) throws Throwable {
        assertCollectionWithOrder("root", table);
    }
}
