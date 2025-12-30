package com.github.requests;

import com.github.constants.Endpoints;
import com.github.models.repos.RepoRequestModel;
import io.restassured.response.Response;

public class RepoRequest extends ApiRequest {
    public Response postRepo(RepoRequestModel repo) {
        return setBasePath(Endpoints.USER_REPOS_ENDPOINT).post(repo);
    }

    public Response getRepo(String owner, String repoName) {
        return setBasePath(Endpoints.REPO_ENDPOINT)
                .setPathParam("owner", owner)
                .setPathParam("repo", repoName)
                .get();
    }

    public Response patchRepo(String owner, String repoName, RepoRequestModel repo) {
        return setBasePath(Endpoints.REPO_ENDPOINT)
                .setPathParam("owner", owner)
                .setPathParam("repo", repoName)
                .patch(repo);
    }

    public Response deleteRepo(String owner, String repoName) {
        return setBasePath(Endpoints.REPO_ENDPOINT)
                .setPathParam("owner", owner)
                .setPathParam("repo", repoName)
                .delete();
    }
}