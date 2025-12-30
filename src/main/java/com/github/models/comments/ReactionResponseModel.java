package com.github.models.comments;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lombok.Getter;

@Getter
public class ReactionResponseModel {
    private Integer id;
    private String content;

    public ReactionResponseModel(Response response) {
        JsonPath jsonPath = response.jsonPath();
        id = jsonPath.getInt("id");
        content = jsonPath.getString("content");
    }
}
