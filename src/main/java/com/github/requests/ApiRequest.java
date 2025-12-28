package com.github.requests;

import com.github.config.Config;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import net.serenitybdd.rest.SerenityRest;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.reset;

public class ApiRequest {
    private final RequestSpecification request = SerenityRest.given();
    private Response response;

    protected ApiRequest() {
        reset();
        setDefaults();
    }

    private void setDefaults() {
        request.baseUri(Config.GITHUB_API_URL);
        request.accept(ContentType.JSON);
        setHeaders();
        if (Config.LOCAL) {
            request.log().all();
        }
    }

    private void setHeaders() {
        Map<String, Object> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + Config.ACCESS_TOKEN);
        headers.put("Content-Type", ContentType.JSON);
        headers.put("Cache-Control", "no-cache");
        request.headers(headers);
    }

    public Response get() {
        response = request.get();
        return response;
    }

    public Response post(Object payload) {
        response = request.body(payload).post();
        return response;
    }

    public Response patch(Object payload) {
        response = request.body(payload).patch();
        return response;
    }

    public Response put(Object payload) {
        response = request.body(payload).put();
        return response;
    }

    public Response delete() {
        response = request.delete();
        return response;
    }

    protected ApiRequest setBasePath(String path) {
        request.basePath(path);
        return this;
    }

    protected ApiRequest setPathParam(String param, String value) {
        request.pathParam(param, value);
        return this;
    }
}
