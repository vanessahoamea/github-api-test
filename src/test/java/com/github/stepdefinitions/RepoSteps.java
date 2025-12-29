package com.github.stepdefinitions;

import com.github.models.repos.RepoRequestModel;
import com.github.models.repos.RepoResponseModel;
import com.github.persistence.Context;
import com.github.requests.GenericRetryClient;
import com.github.requests.RepoRequest;
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

public class RepoSteps {
    private final Context context = Context.getContext();
    private final SoftAssertions softly = new SoftAssertions();
    private final GenericRetryClient<Response> retryClient = new GenericRetryClient<>();

    private RepoRequestModel repoRequestObject;
    private RepoResponseModel repoResponseObject;

    @Given("I have successfully created a new repository")
    public void validateRepoName() {
        assertNotNull(context.getRepoName());
    }

    @When("I create a new repository for my user with the following details")
    public void createRepo(DataTable repoTable) {
        Map<String, String> repoDetails = repoTable.asMaps().getFirst();
        repoRequestObject = new RepoRequestModel(repoDetails);
        Response response = new RepoRequest().postRepo(repoRequestObject);
        repoResponseObject = new RepoResponseModel(response);
        context.setResponse(response);
        context.setRepoName(repoResponseObject.getName());
    }

    @When("I update the newly created repository with the following details")
    public void updateRepo(DataTable repoTable) {
        Map<String, String> repoDetails = repoTable.asMaps().getFirst();
        repoRequestObject = new RepoRequestModel(repoDetails);
        Response response = retryClient.waitForStatusCode(
                () -> new RepoRequest().patchRepo(context.getUsername(), context.getRepoName(), repoRequestObject),
                HttpStatus.OK_200
        );
        repoResponseObject = new RepoResponseModel(response);
        context.setResponse(response);
    }

    @When("I delete the newly created repository")
    public void deleteRepo() {
        Response response = retryClient.waitForStatusCode(
                        () -> new RepoRequest().deleteRepo(context.getUsername(), context.getRepoName()),
                        HttpStatus.NO_CONTENT_204
        );
        context.setResponse(response);
    }

    @Then("the repo response JSON schema matches the one expected")
    public void validateRepoResponseSchema() {
        Response response = context.getResponse();
        String schema = "repoSchema.json";
        JsonUtils.validateJsonSchema(response, schema);
    }

    @Then("the create repo response contains the correct data")
    public void validateCreateRepoResponse() {
        softly.assertThat(Objects.equals(repoResponseObject.getName(), repoRequestObject.getName()))
                .as("The received repo name should match the data sent with the request")
                .isTrue();
        softly.assertThat(Objects.equals(repoResponseObject.getDescription(), repoRequestObject.getDescription()))
                .as("The received repo description should match the data sent with the request")
                .isTrue();
        softly.assertThat(Objects.equals(repoResponseObject.getHomepage(), repoRequestObject.getHomepage()))
                .as("The received repo homepage should match the data sent with the request")
                .isTrue();
        softly.assertThat(Objects.equals(repoResponseObject.getIsPrivate(), repoRequestObject.getIsPrivate()))
                .as("The received repo private status should match the data sent with the request")
                .isTrue();
        softly.assertThat(Objects.equals(repoResponseObject.getHasIssues(), repoRequestObject.getHasIssues()))
                .as("The received repo issues status should match the data sent with the request")
                .isTrue();

        softly.assertAll();
    }

    @Then("the update repo response contains the correct data")
    public void validateUpdateRepoResponse() {
        softly.assertThat(Objects.equals(repoResponseObject.getDescription(), repoRequestObject.getDescription()))
                .as("The received repo description should match the data sent with the request")
                .isTrue();
        softly.assertThat(Objects.equals(repoResponseObject.getIsPrivate(), repoRequestObject.getIsPrivate()))
                .as("The received repo private status should match the data sent with the request")
                .isTrue();

        softly.assertAll();
    }

    @Then("the get repo endpoint response reflects the changes")
    public void validateGetRepoResponse() {
        Response getRepoResponse = new RepoRequest().getRepo(context.getUsername(), context.getRepoName());
        RepoResponseModel getRepoResponseObject = new RepoResponseModel(getRepoResponse);

        assertThat(EqualsBuilder.reflectionEquals(getRepoResponseObject, repoResponseObject))
                .as("The create/update repo response and the get repo response must be the same")
                .isTrue();
    }

    @Then("^the get repo endpoint returns (.*) after the repo deletion$")
    public void validateGetRepoResponseAfterDelete(int expectedStatusCode) {
        Response getRepoResponse = new RepoRequest().getRepo(context.getUsername(), context.getRepoName());
        context.setResponse(getRepoResponse);
        context.setRepoName(null);
        new CommonSteps().validateStatusCode(expectedStatusCode);
    }
}
