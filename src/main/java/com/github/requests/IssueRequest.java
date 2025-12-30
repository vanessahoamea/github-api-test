package com.github.requests;

import com.github.constants.Endpoints;
import com.github.models.issues.IssueRequestModel;
import io.restassured.response.Response;

public class IssueRequest extends ApiRequest {
    public Response postIssue(String owner, String repoName, IssueRequestModel issue) {
        return setBasePath(Endpoints.REPO_ISSUES_ENDPOINT)
                .setPathParam("owner", owner)
                .setPathParam("repo", repoName)
                .post(issue);
    }

    public Response getIssue(String owner, String repoName, Integer issueNumber) {
        return setBasePath(Endpoints.ISSUE_ENDPOINT)
                .setPathParam("owner", owner)
                .setPathParam("repo", repoName)
                .setPathParam("number", issueNumber.toString())
                .get();
    }

    public Response patchIssue(String owner, String repoName, Integer issueNumber, IssueRequestModel issue) {
        return setBasePath(Endpoints.ISSUE_ENDPOINT)
                .setPathParam("owner", owner)
                .setPathParam("repo", repoName)
                .setPathParam("number", issueNumber.toString())
                .patch(issue);
    }

    public Response putIssue(String owner, String repoName, Integer issueNumber, IssueRequestModel issue) {
        return setBasePath(Endpoints.ISSUE_LOCK_ENDPOINT)
                .setPathParam("owner", owner)
                .setPathParam("repo", repoName)
                .setPathParam("number", issueNumber.toString())
                .put(issue);
    }
}
