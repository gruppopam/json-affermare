package com.rest.verification;

import com.rest.response.Response;
import com.rest.response.ResponseStorage;
import cucumber.api.DataTable;
import org.junit.Test;

import static java.util.Arrays.asList;

public class JsonVerificationTest {

    @Test
    public void shouldCheckJsonForEmptyValues() throws Throwable {
        ResponseStorage.initialize(getTestResponse("{}"));
        new JsonVerification().I_verify_that_json_is_empty();

        ResponseStorage.initialize(getTestResponse("[]"));
        new JsonVerification().I_verify_that_json_is_empty();
    }

    @Test
    public void shouldVerifyIfJsonObjectHasEntries() throws Throwable {
        String json="{ \"id\":1000, \"name\":\"Kent Beck\" }";

        DataTable table = DataTable.create(asList(
                asList("id", "name"),
                asList("1000", "Kent Beck")
        ));

        ResponseStorage.initialize(getTestResponse(json));
        new JsonVerification().I_verify_that_the_json_has_the_following("author", table);

    }

    @Test
    public void shouldVerifyIfJsonObjectDoesNotHaveKeys() throws Throwable {
        String json="{ \"id\":1000, \"name\":\"Kent Beck\" }";

        DataTable table = DataTable.create(asList(
                asList("age", "phone")
        ));

        ResponseStorage.initialize(getTestResponse(json));
        new JsonVerification().I_verify_that_the_json_does_not_have_the_following("age", table);

    }

    @Test
    public void shouldVerifyIfJsonObjectHasEntriesWithOneToOneAssociation() throws Throwable {
        String json="{ \"isbn\": \"isbn123\", \"name\":\"Test driven development\", \"author\":{ \"id\":1000, \"name\":\"Kent Beck\" } }";


        DataTable table = DataTable.create(asList(
                asList("isbn", "name", "author.id", "author.name"),
                asList("isbn123", "Test driven development", "1000", "Kent Beck")
        ));

        ResponseStorage.initialize(getTestResponse(json));
        new JsonVerification().I_verify_that_the_json_has_the_following("author", table);

    }

    @Test
    public void shouldVerifyIfJsonObjectHasEntriesWithOneToManyAssociation() throws Throwable {
        String json="{\"phone_numbers\":[{\"type\":\"office\",\"number\":987654321},{\"type\":\"home\",\"number\":123456789}],\"id\":123,\"name\":\"Fowler\"}";


        DataTable table = DataTable.create(asList(
                asList("id", "name", "phone_numbers.type", "phone_numbers.number"),
                asList("123", "Fowler", "office", "987654321"),
                asList("123", "Fowler", "home", "123456789")
        ));

        ResponseStorage.initialize(getTestResponse(json));
        new JsonVerification().I_verify_that_the_json_has_the_following("author", table);

    }

    @Test
    public void shouldVerifyIfJsonPrimitiveHasEntries() throws Throwable {
        String json="[100,200]";

        DataTable table = DataTable.create(asList(
                asList("200"),
                asList("100")
        ));

        ResponseStorage.initialize(getTestResponse(json));
        new JsonVerification().I_verify_that_the_json_has_the_following("author", table);
    }

    @Test
    public void shouldVerifyIfJsonPrimitiveHasEntriesInSameOrder() throws Throwable {
        String json="[100,200]";

        DataTable table = DataTable.create(asList(
                asList("100"),
                asList("200")
        ));

        ResponseStorage.initialize(getTestResponse(json));
        new JsonVerification().I_verify_that_the_json_has_the_following("id in the same order", table);
    }

    @Test
    public void shouldVerifyIfJsonArrayHasEntries() throws Throwable {
        String json="[{ \"id\":1000, \"name\":\"Kent Beck\" }, { \"id\":1001, \"name\":\"Fowler\" }]";

        DataTable table = DataTable.create(asList(
                asList("id", "name"),
                asList("1001", "Fowler"),
                asList("1000", "Kent Beck")
        ));

        ResponseStorage.initialize(getTestResponse(json));
        new JsonVerification().I_verify_that_the_json_has_the_following("author", table);

    }

    @Test
    public void shouldVerifyIfJsonArrayDoesNotHaveEntries() throws Throwable {
        String json="[{ \"id\":1000, \"name\":\"Kent Beck\" }, { \"id\":1001, \"name\":\"Fowler\" }]";

        DataTable table = DataTable.create(asList(
                asList("id", "name"),
                asList("1002", "Sadalge")
        ));

        ResponseStorage.initialize(getTestResponse(json));
        new JsonVerification().I_verify_that_the_json_does_not_have_the_following("authors", table);

    }

    @Test
    public void shouldVerifyArrayCount() throws Throwable {
        String json="[{ \"id\":1000, \"name\":\"Kent Beck\" }, { \"id\":1001, \"name\":\"Fowler\" }]";

        ResponseStorage.initialize(getTestResponse(json));
        new JsonVerification().I_verify_that_the_json_array_count(2);

    }

    @Test
    public void shouldVerifyIfJsonArrayHasEntriesInSameOrder() throws Throwable {
        String json="[{ \"id\":1000, \"name\":\"Kent Beck\" }, { \"id\":1001, \"name\":\"Fowler\" }]";

        DataTable table = DataTable.create(asList(
                asList("id", "name"),
                asList("1000", "Kent Beck"),
                asList("1001", "Fowler")
        ));

        ResponseStorage.initialize(getTestResponse(json));
        new JsonVerification().I_verify_that_the_json_has_the_following("author in the same order", table);

    }

    @Test
    public void shouldVerifyIfJsonArrayHasEntriesWithOneToOneAssociation() throws Throwable {
        String json="[ { \"isbn\": \"isbn123\", \"name\":\"Test driven development\", \"author\":{ \"id\":1000, \"name\":\"Kent Beck\" } } , { \"isbn\": \"isbn124\", \"name\": \"Refactoring\", \"author\": { \"id\":1001, \"name\": \"Martin Fowler\" } }]";

        DataTable table = DataTable.create(asList(
                asList("isbn", "name", "author.id", "author.name"),
                asList("isbn123", "Test driven development", "1000", "Kent Beck"),
                asList("isbn124", "Refactoring", "1001", "Martin Fowler")
        ));

        ResponseStorage.initialize(getTestResponse(json));
        new JsonVerification().I_verify_that_the_json_has_the_following("author", table);

    }

    private Response getTestResponse(final String json) {
        return new Response() {
            @Override
            public int status() {
                return 0;
            }

            @Override
            public String json() {
                return json;
            }
        };
    }


}