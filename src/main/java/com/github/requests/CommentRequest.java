package com.github.requests;

import com.github.constants.Endpoints;
import com.github.models.comments.CommentRequestModel;
import com.github.models.comments.ReactionRequestModel;
import io.restassured.response.Response;

public class CommentRequest extends ApiRequest {
    public Response postComment(String owner, String repoName, Integer issueNumber, CommentRequestModel comment) {
        return setBasePath(Endpoints.ISSUE_COMMENTS_ENDPOINT)
                .setPathParam("owner", owner)
                .setPathParam("repo", repoName)
                .setPathParam("number", issueNumber.toString())
                .post(comment);
    }

    public Response getComment(String owner, String repoName, Long commentID) {
        return setBasePath(Endpoints.COMMENT_ENDPOINT)
                .setPathParam("owner", owner)
                .setPathParam("repo", repoName)
                .setPathParam("id", commentID.toString())
                .get();
    }

    public Response postReaction(String owner, String repoName, Long commentID, ReactionRequestModel reaction) {
        return setBasePath(Endpoints.COMMENT_REACTIONS_ENDPOINT)
                .setPathParam("owner", owner)
                .setPathParam("repo", repoName)
                .setPathParam("id", commentID.toString())
                .post(reaction);
    }

    public Response deleteComment(String owner, String repoName, Long commentID) {
        return setBasePath(Endpoints.COMMENT_ENDPOINT)
                .setPathParam("owner", owner)
                .setPathParam("repo", repoName)
                .setPathParam("id", commentID.toString())
                .delete();
    }
}
