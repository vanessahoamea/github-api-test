package com.github.utils;

import io.restassured.response.Response;

import java.io.File;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;

public class JsonSchemaUtils {
    public static void validateJsonSchema(Response response, String fileName) {
        File jsonFile = FileUtils.getJsonFile(fileName);
        response.then().body(matchesJsonSchema(jsonFile));
    }
}
