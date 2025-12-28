package com.github.requests;

import com.github.constants.Endpoints;
import io.restassured.response.Response;

public class UserRequest extends ApiRequest {
    public Response getAuthenticatedUser() {
        return setBasePath(Endpoints.USER_ENDPOINT).get();
    }
}