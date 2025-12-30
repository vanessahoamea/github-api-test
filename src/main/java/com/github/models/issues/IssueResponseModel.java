package com.github.models.issues;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public class IssueResponseModel {
    private final Long id;
    private final Integer number;
    private final String title;
    private final String body;
    private final String assignee;
    private final List<String> labels;
    private final String state;
    private final String stateReason;
    private final Boolean locked;
    private final String activeLockReason;

    public IssueResponseModel(Response response) {
        JsonPath jsonPath = response.jsonPath();
        id = jsonPath.getLong("id");
        number = jsonPath.getInt("number");
        title = jsonPath.getString("title");
        body = jsonPath.getString("body");
        state = jsonPath.getString("state");
        stateReason = jsonPath.getString("state_reason");
        locked = jsonPath.getBoolean("locked");
        activeLockReason = jsonPath.getString("active_lock_reason");

        Map<String, String> assigneeMap = jsonPath.getMap("assignee");
        assignee = assigneeMap != null && assigneeMap.containsKey("login") ? assigneeMap.get("login") : null;

        List<Map<String, String>> labelsArray = jsonPath.getList("labels");
        if (labelsArray != null) {
            labels = labelsArray.stream()
                    .filter(label -> label != null && label.containsKey("name"))
                    .map(label -> label.get("name"))
                    .collect(Collectors.toList());
        } else {
            labels = null;
        }
    }
}
