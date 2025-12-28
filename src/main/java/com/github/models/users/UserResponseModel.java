package com.github.models.users;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lombok.Getter;

@Getter
public class UserResponseModel {
    private String login;

    public UserResponseModel(Response response) {
        JsonPath jsonPath = response.jsonPath();
        login = jsonPath.getString("login");
    }
}
