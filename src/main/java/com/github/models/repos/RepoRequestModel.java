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
    private String name;
    private String description;
    private String homepage;
    @JsonProperty("private") private Boolean isPrivate;
    @JsonProperty("has_issues") private Boolean hasIssues;
    @JsonProperty("auto_init") private Boolean autoInit;

    public RepoRequestModel(Map<String, String> repoDetails) {
        String providedDescription = repoDetails.get("description");
        String providedHomepage = repoDetails.get("homepage");
        String providedIsPrivate = repoDetails.get("private");
        String providedHasIssues = repoDetails.get("private");
        String providedAutoInit = repoDetails.get("private");

        name = repoDetails.get("name");
        description = Objects.equals(providedDescription, "@random") ? generateRandomSentence() : providedDescription;
        homepage = Objects.equals(providedHomepage, "@random") ? generateRandomDomain() : providedHomepage;
        isPrivate = providedIsPrivate != null ? providedIsPrivate.equals("true") : null;
        hasIssues = providedHasIssues != null ? providedHasIssues.equals("true") : null;
        autoInit = providedAutoInit != null ? providedAutoInit.equals("true") : null;
    }
}
