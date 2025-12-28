package com.github.stepdefinitions;

import io.cucumber.java.en.Then;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CommonSteps {
    @Then("^the received status code is (.*)$")
    public void validateStatusCode(int statusCode) {
        int actualStatusCode = 200; // TODO
        assertEquals(statusCode, actualStatusCode);
    }
}
