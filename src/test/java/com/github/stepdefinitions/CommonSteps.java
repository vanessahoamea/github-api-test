package com.github.stepdefinitions;

import com.github.models.users.UserResponseModel;
import com.github.persistence.Context;
import com.github.requests.UserRequest;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CommonSteps {
    private final Context context = Context.getContext();

    @Given("I am authenticated as a GitHub user")
    @When("I fetch the current GitHub user")
    public void getCurrentUser() {
        Response response = new UserRequest().getAuthenticatedUser();
        UserResponseModel userResponseObject = new UserResponseModel(response);
        context.setResponse(response);
        context.setUsername(userResponseObject.getLogin());
    }

    @Then("^the received status code is (.*)$")
    public void validateStatusCode(int expectedStatusCode) {
        int actualStatusCode = context.getResponse().getStatusCode();
        assertEquals(expectedStatusCode, actualStatusCode);
    }
}
