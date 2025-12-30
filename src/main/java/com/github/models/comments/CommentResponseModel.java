package com.github.models.comments;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lombok.Getter;

import java.util.Map;

@Getter
public class CommentResponseModel {
    private Long id;
    private String body;
    private Map<String, Object> reactions;

    public CommentResponseModel(Response response) {
        JsonPath jsonPath = response.jsonPath();
        id = jsonPath.getLong("id");
        body = jsonPath.getString("body");
        reactions = jsonPath.getMap("reactions");
    }
}
