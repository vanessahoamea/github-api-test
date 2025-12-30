package com.github.stepdefinitions;

import com.github.models.comments.CommentRequestModel;
import com.github.models.comments.CommentResponseModel;
import com.github.models.comments.ReactionRequestModel;
import com.github.models.comments.ReactionResponseModel;
import com.github.models.issues.IssueRequestModel;
import com.github.models.issues.IssueResponseModel;
import com.github.persistence.Context;
import com.github.requests.CommentRequest;
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CommentSteps {
    private final Context context = Context.getContext();
    private final SoftAssertions softly = new SoftAssertions();
    private final GenericRetryClient<Response> retryClient = new GenericRetryClient<>();

    private CommentRequestModel commentRequestObject;
    private CommentResponseModel commentResponseObject;
    private ReactionRequestModel reactionRequestObject;
    private ReactionResponseModel reactionResponseObject;

    @Given("I have successfully created a new comment")
    public void validateCommentID() {
        assertNotNull(context.getCommentID());
    }

    @Given("the repository has an issue with the following details")
    public void initializeIssue(DataTable issueTable) {
        Map<String, String> issueDetails = issueTable.asMaps().getFirst();
        IssueRequestModel issueRequestObject = new IssueRequestModel(issueDetails);
        Response response = new IssueRequest().postIssue(context.getUsername(), context.getRepoName(), issueRequestObject);
        IssueResponseModel issueResponseObject = new IssueResponseModel(response);
        context.setIssueNumber(issueResponseObject.getNumber());
    }

    @When("I create a comment for the issue with the following body")
    public void createComment(DataTable commentTable) {
        Map<String, String> commentDetails = commentTable.asMaps().getFirst();
        commentRequestObject = new CommentRequestModel(commentDetails);
        Response response = new CommentRequest().postComment(context.getUsername(), context.getRepoName(), context.getIssueNumber(), commentRequestObject);
        commentResponseObject = new CommentResponseModel(response);
        context.setResponse(response);
        context.setCommentID(commentResponseObject.getId());
    }

    @When("I create a reaction to the comment with the following content")
    public void createReaction(DataTable reactionTable) {
        Map<String, String> reactionDetails = reactionTable.asMaps().getFirst();
        reactionRequestObject = new ReactionRequestModel(reactionDetails);
        Response response = new CommentRequest().postReaction(context.getUsername(), context.getRepoName(), context.getCommentID(), reactionRequestObject);
        reactionResponseObject = new ReactionResponseModel(response);
        context.setResponse(response);
    }

    @When("I delete the newly created comment")
    public void deleteComment() {
        Response response = retryClient.waitForStatusCode(
                () -> new CommentRequest().deleteComment(context.getUsername(), context.getRepoName(), context.getCommentID()),
                HttpStatus.NO_CONTENT_204
        );
        context.setResponse(response);
    }

    @Then("the comment response JSON schema matches the one expected")
    public void validateCommentResponseSchema() {
        Response response = context.getResponse();
        String schema = "comments/commentSchema.json";
        JsonUtils.validateJsonSchema(response, schema);
    }

    @Then("the reaction response JSON schema matches the one expected")
    public void validateReactionResponseSchema() {
        Response response = context.getResponse();
        String schema = "comments/reactionSchema.json";
        JsonUtils.validateJsonSchema(response, schema);
    }

    @Then("the create comment response contains the correct data")
    public void validateCreateCommentResponse() {
        assertEquals(commentResponseObject.getBody(), commentRequestObject.getBody(), "The received comment body should match the data sent with the request");
    }

    @Then("the create reaction response contains the correct data")
    public void validateCreateReactionResponse() {
        assertEquals(reactionResponseObject.getContent(), reactionRequestObject.getContent(), "The received reaction content should match the data sent with the request");
    }

    @Then("the get comment endpoint response reflects the changes")
    public void validateGetCommentResponseAfterCreateComment() {
        Response getCommentResponse = new CommentRequest().getComment(context.getUsername(), context.getRepoName(), context.getCommentID());
        CommentResponseModel getCommentResponseObject = new CommentResponseModel(getCommentResponse);

        assertThat(EqualsBuilder.reflectionEquals(getCommentResponseObject, commentResponseObject))
                .as("The create comment response and the get comment response must be the same")
                .isTrue();
    }

    @Then("the get comment endpoint response reflects the updated reactions")
    public void validateGetCommentResponseAfterCreateReaction() {
        Response getCommentResponse = new CommentRequest().getComment(context.getUsername(), context.getRepoName(), context.getCommentID());
        CommentResponseModel getCommentResponseObject = new CommentResponseModel(getCommentResponse);
        Map<String, Object> reactions = getCommentResponseObject.getReactions();
        String reactionKey = reactionResponseObject.getContent();

        softly.assertThat(reactions.containsKey(reactionKey))
                .as("The get comment response reactions should contain the reaction sent with the request")
                .isTrue();
        softly.assertThat(Objects.equals(reactions.get(reactionKey), 1))
                .as("The get comment response should contain only one reaction")
                .isTrue();

        softly.assertAll();
    }

    @Then("^the get comment endpoint returns (.*) after the comment deletion$")
    public void validateGetCommentResponseAfterDelete(int expectedStatusCode) {
        Response getCommentResponse = new CommentRequest().getComment(context.getUsername(), context.getRepoName(), context.getCommentID());
        context.setResponse(getCommentResponse);
        context.setCommentID(null);
        new CommonSteps().validateStatusCode(expectedStatusCode);
    }
}
