package com.github.models.comments;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lombok.Getter;

@Getter
public class ReactionResponseModel {
    private final Long id;
    private final String content;

    public ReactionResponseModel(Response response) {
        JsonPath jsonPath = response.jsonPath();
        id = jsonPath.getLong("id");
        content = jsonPath.getString("content");
    }
}
