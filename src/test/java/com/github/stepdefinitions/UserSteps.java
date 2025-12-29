package com.github.stepdefinitions;

import com.github.config.Config;
import com.github.persistence.Context;
import com.github.utils.JsonUtils;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.restassured.response.Response;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UserSteps {
    private final Context context = Context.getContext();

    @Given("I have a valid access token")
    public void validateAccessToken() {
        assertNotNull(Config.ACCESS_TOKEN);
    }

    @Then("the user response JSON schema matches the one expected")
    public void validateUserResponseSchema() {
        Response response = context.getResponse();
        String schema = "userSchema.json";
        JsonUtils.validateJsonSchema(response, schema);
    }
}
