package com.github.models.repos;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lombok.Getter;

@Getter
public class RepoResponseModel {
    private Integer id;
    private String name;
    private String description;
    private String homepage;
    private Boolean isPrivate;
    private Boolean hasIssues;

    public RepoResponseModel(Response response) {
        JsonPath jsonPath = response.jsonPath();
        id = jsonPath.getInt("id");
        name = jsonPath.getString("name");
        description = jsonPath.getString("description");
        homepage = jsonPath.getString("homepage");
        isPrivate = jsonPath.getBoolean("private");
        hasIssues = jsonPath.getBoolean("has_issues");
    }
}
