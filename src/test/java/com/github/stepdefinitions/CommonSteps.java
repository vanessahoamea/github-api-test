package com.github.stepdefinitions;

import com.github.persistence.Context;
import io.cucumber.java.en.Then;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CommonSteps {
    private final Context context = Context.getContext();

    @Then("^the received status code is (.*)$")
    public void validateStatusCode(int expectedStatusCode) {
        int actualStatusCode = context.getResponse().getStatusCode();
        assertEquals(expectedStatusCode, actualStatusCode);
    }
}
