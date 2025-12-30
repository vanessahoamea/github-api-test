package com.github.models.repos;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lombok.Getter;

@Getter
public class RepoResponseModel {
    private final Long id;
    private final String name;
    private final String description;
    private final String homepage;
    private final Boolean isPrivate;
    private final Boolean hasIssues;

    public RepoResponseModel(Response response) {
        JsonPath jsonPath = response.jsonPath();
        id = jsonPath.getLong("id");
        name = jsonPath.getString("name");
        description = jsonPath.getString("description");
        homepage = jsonPath.getString("homepage");
        isPrivate = jsonPath.getBoolean("private");
        hasIssues = jsonPath.getBoolean("has_issues");
    }
}
