package com.github.utils;

import io.cucumber.core.internal.com.fasterxml.jackson.core.JsonProcessingException;
import io.cucumber.core.internal.com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.core.internal.com.fasterxml.jackson.databind.ObjectWriter;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;

public class JsonUtils {
    private static final Logger logger = LoggerFactory.getLogger(JsonUtils.class);

    public static String convertToJson(Object object) {
        try {
            ObjectWriter writer = new ObjectMapper().writer().withDefaultPrettyPrinter();
            return writer.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            logger.error("Error serializing JSON. Object: {}", object, e);
            return "{}";
        }
    }

    public static void validateJsonSchema(Response response, String fileName) {
        File jsonFile = FileUtils.getJsonFile(fileName);
        response.then().body(matchesJsonSchema(jsonFile));
    }
}
