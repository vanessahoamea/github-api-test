package com.github.models.issues;

import com.github.persistence.Context;
import io.cucumber.core.internal.com.fasterxml.jackson.annotation.JsonInclude;
import io.cucumber.core.internal.com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.*;

import static com.github.utils.RandomValueUtils.*;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IssueRequestModel {
    private String title;
    private String body;
    private String assignee;
    private List<String> labels;
    private String state;
    @JsonProperty("state_reason") private String stateReason;
    @JsonProperty("lock_reason") private String lockReason;

    public IssueRequestModel(Map<String, String> issueDetails) {
        Context context = Context.getContext();

        String providedTitle = issueDetails.get("title");
        String providedBody = issueDetails.get("body");
        String providedAssignee = issueDetails.get("assignee");
        String providedLabels = issueDetails.get("labels");

        title = Objects.equals(providedTitle, "@random") ? generateRandomSentence() : providedTitle;
        body = Objects.equals(providedBody, "@random") ? generateRandomParagraph() : providedBody;
        assignee = Objects.equals(providedAssignee, "@owner") ? context.getUsername() : providedAssignee;
        labels = providedLabels != null ? Arrays.asList(providedLabels.split("\\s*,\\s*")) : null;
        state = issueDetails.get("state");
        stateReason = issueDetails.get("state_reason");
        lockReason = issueDetails.get("lock_reason");
    }
}
