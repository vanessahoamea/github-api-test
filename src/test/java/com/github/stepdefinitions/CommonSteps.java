package com.github.stepdefinitions;

import com.github.models.repos.RepoRequestModel;
import com.github.models.repos.RepoResponseModel;
import com.github.models.users.UserResponseModel;
import com.github.persistence.Context;
import com.github.requests.GenericRetryClient;
import com.github.requests.RepoRequest;
import com.github.requests.UserRequest;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.htmlunit.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CommonSteps {
    private final Context context = Context.getContext();
    private final GenericRetryClient<Response> retryClient = new GenericRetryClient<>();
    private static final Logger logger = LoggerFactory.getLogger(CommonSteps.class);

    @Given("I am authenticated as a GitHub user")
    @When("I fetch the current GitHub user")
    public void getCurrentUser() {
        Response response = new UserRequest().getAuthenticatedUser();
        UserResponseModel userResponseObject = new UserResponseModel(response);
        context.setResponse(response);
        context.setUsername(userResponseObject.getLogin());
    }

    @Given("I have access to a repository with the following properties")
    public void initializeRepo(DataTable repoTable) {
        Map<String, String> repoDetails = repoTable.asMaps().getFirst();
        RepoRequestModel repoRequestObject = new RepoRequestModel(repoDetails);
        Response response = new RepoRequest().postRepo(repoRequestObject);
        RepoResponseModel repoResponseObject = new RepoResponseModel(response);
        context.setRepoName(repoResponseObject.getName());
    }

    @Then("^the received status code is (.*)$")
    public void validateStatusCode(int expectedStatusCode) {
        int actualStatusCode = context.getResponse().getStatusCode();
        assertEquals(expectedStatusCode, actualStatusCode);
    }

    @Before("@wait")
    public void waitToExecute() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            logger.error("Error waiting before test starts", e);
        }
    }

    @After("@final")
    public void cleanup() {
        retryClient.waitForStatusCode(
                () -> new RepoRequest().deleteRepo(context.getUsername(), context.getRepoName()),
                HttpStatus.NO_CONTENT_204
        );
        context.setCommentID(null);
        context.setIssueNumber(null);
        context.setRepoName(null);
    }
}
