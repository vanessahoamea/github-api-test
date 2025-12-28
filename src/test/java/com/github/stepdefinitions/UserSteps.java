package com.github.stepdefinitions;

import com.github.config.Config;
import com.github.models.users.UserResponseModel;
import com.github.persistence.Context;
import com.github.requests.UserRequest;
import com.github.utils.JsonSchemaUtils;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UserSteps {
    private final Context context = Context.getContext();

    @Given("I have a valid access token")
    public void getAccessToken() {
        assertNotNull(Config.ACCESS_TOKEN);
    }

    @When("I fetch the current GitHub user")
    public void getCurrentUser() {
        Response response = new UserRequest().getAuthenticatedUser();
        UserResponseModel userResponse = new UserResponseModel(response);
        context.setResponse(response);
        context.setUsername(userResponse.getLogin());
    }

    @Then("the user response JSON schema matches the one expected")
    public void validateUserResponseSchema() {
        Response response = context.getResponse();
        String schema = "userSchema.json";
        JsonSchemaUtils.validateJsonSchema(response, schema);
    }
}
