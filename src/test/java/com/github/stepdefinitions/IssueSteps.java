package com.github.stepdefinitions;

import com.github.models.issues.IssueRequestModel;
import com.github.models.issues.IssueResponseModel;
import com.github.persistence.Context;
import com.github.requests.GenericRetryClient;
import com.github.requests.IssueRequest;
import com.github.utils.JsonUtils;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.assertj.core.api.SoftAssertions;
import org.htmlunit.jetty.http.HttpStatus;

import java.util.Map;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class IssueSteps {
    private final Context context = Context.getContext();
    private final SoftAssertions softly = new SoftAssertions();
    private final GenericRetryClient<Response> retryClient = new GenericRetryClient<>();

    private IssueRequestModel issueRequestObject;
    private IssueResponseModel issueResponseObject;

    @Given("I have successfully created a new issue")
    public void validateIssueNumber() {
        assertNotNull(context.getIssueNumber());
    }

    @When("I create a new issue for my repository with the following details")
    public void createIssue(DataTable issueTable) {
        Map<String, String> issueDetails = issueTable.asMaps().getFirst();
        issueRequestObject = new IssueRequestModel(issueDetails);
        Response response = new IssueRequest().postIssue(context.getUsername(), context.getRepoName(), issueRequestObject);
        issueResponseObject = new IssueResponseModel(response);
        context.setResponse(response);
        context.setIssueNumber(issueResponseObject.getNumber());
    }

    @When("I update the newly created issue with the following details")
    public void updateIssue(DataTable issueTable) {
        Map<String, String> issueDetails = issueTable.asMaps().getFirst();
        issueRequestObject = new IssueRequestModel(issueDetails);
        Response response = retryClient.waitForStatusCode(
                () -> new IssueRequest().patchIssue(context.getUsername(), context.getRepoName(), context.getIssueNumber(), issueRequestObject),
                HttpStatus.OK_200
        );
        issueResponseObject = new IssueResponseModel(response);
        context.setResponse(response);
    }

    @When("I lock the newly created issue with the following reason")
    public void lockIssue(DataTable issueTable) {
        Map<String, String> issueDetails = issueTable.asMaps().getFirst();
        issueRequestObject = new IssueRequestModel(issueDetails);
        Response response = retryClient.waitForStatusCode(
                () -> new IssueRequest().putIssue(context.getUsername(), context.getRepoName(), context.getIssueNumber(), issueRequestObject),
                HttpStatus.NO_CONTENT_204
        );
        context.setResponse(response);
    }

    @Then("the issue response JSON schema matches the one expected")
    public void validateIssueResponseSchema() {
        Response response = context.getResponse();
        String schema = "issueSchema.json";
        JsonUtils.validateJsonSchema(response, schema);
    }

    @Then("the create issue response contains the correct data")
    public void validateCreateIssueResponse() {
        softly.assertThat(Objects.equals(issueResponseObject.getTitle(), issueRequestObject.getTitle()))
                .as("The received issue title should match the data sent with the request")
                .isTrue();
        softly.assertThat(Objects.equals(issueResponseObject.getBody(), issueRequestObject.getBody()))
                .as("The received issue body should match the data sent with the request")
                .isTrue();
        softly.assertThat(Objects.equals(issueResponseObject.getAssignee(), issueRequestObject.getAssignee()))
                .as("The received issue assignee should match the data sent with the request")
                .isTrue();
        softly.assertThat(Objects.equals(issueResponseObject.getLabels(), issueRequestObject.getLabels()))
                .as("The received issue labels should match the data sent with the request")
                .isTrue();

        softly.assertAll();
    }

    @Then("the update issue response contains the correct data")
    public void validateUpdateIssueResponse() {
        softly.assertThat(Objects.equals(issueResponseObject.getState(), issueRequestObject.getState()))
                .as("The received issue state should match the data sent with the request")
                .isTrue();
        softly.assertThat(Objects.equals(issueResponseObject.getStateReason(), issueRequestObject.getStateReason()))
                .as("The received issue state reason should match the data sent with the request")
                .isTrue();

        softly.assertAll();
    }

    @Then("the get issue endpoint response reflects the changes")
    public void validateGetIssueResponse() {
        Response getIssueResponse = new IssueRequest().getIssue(context.getUsername(), context.getRepoName(), context.getIssueNumber());
        IssueResponseModel getIssueResponseObject = new IssueResponseModel(getIssueResponse);

        assertThat(EqualsBuilder.reflectionEquals(getIssueResponseObject, issueResponseObject))
                .as("The create/update issue response and the get issue response must be the same")
                .isTrue();
    }

    @Then("the get issue endpoint response reflects the updated lock status")
    public void validateGetIssueResponseAfterLock() {
        Response getIssueResponse = new IssueRequest().getIssue(context.getUsername(), context.getRepoName(), context.getIssueNumber());
        IssueResponseModel getIssueResponseObject = new IssueResponseModel(getIssueResponse);

        softly.assertThat(Objects.equals(getIssueResponseObject.getLocked(), true))
                .as("The received issue lock status should be true")
                .isTrue();
        softly.assertThat(Objects.equals(getIssueResponseObject.getActiveLockReason(), issueRequestObject.getLockReason()))
                .as("The received issue lock reason should match the data sent with the request")
                .isTrue();

        softly.assertAll();
    }
}
