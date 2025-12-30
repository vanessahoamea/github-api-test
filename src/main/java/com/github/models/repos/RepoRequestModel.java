package com.github.models.repos;

import io.cucumber.core.internal.com.fasterxml.jackson.annotation.JsonInclude;
import io.cucumber.core.internal.com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.Map;
import java.util.Objects;

import static com.github.utils.RandomValueUtils.generateRandomDomain;
import static com.github.utils.RandomValueUtils.generateRandomSentence;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RepoRequestModel {
    private final String name;
    private final String description;
    private final String homepage;
    @JsonProperty("private") private final Boolean isPrivate;
    @JsonProperty("has_issues") private final Boolean hasIssues;
    @JsonProperty("auto_init") private final Boolean autoInit;

    public RepoRequestModel(Map<String, String> repoDetails) {
        String providedDescription = repoDetails.get("description");
        String providedHomepage = repoDetails.get("homepage");
        String providedIsPrivate = repoDetails.get("private");
        String providedHasIssues = repoDetails.get("has_issues");
        String providedAutoInit = repoDetails.get("auto_init");

        name = repoDetails.get("name");
        description = Objects.equals(providedDescription, "@random") ? generateRandomSentence() : providedDescription;
        homepage = Objects.equals(providedHomepage, "@random") ? generateRandomDomain() : providedHomepage;
        isPrivate = providedIsPrivate != null ? providedIsPrivate.equals("true") : null;
        hasIssues = providedHasIssues != null ? providedHasIssues.equals("true") : null;
        autoInit = providedAutoInit != null ? providedAutoInit.equals("true") : null;
    }
}
