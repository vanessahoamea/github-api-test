package com.github.constants;

public class Endpoints {
    public static final String USER_ENDPOINT = "/user";
    public static final String USER_REPOS_ENDPOINT = "/user/repos";
    public static final String REPO_ENDPOINT = "/repos/{owner}/{repo}";
    public static final String REPO_ISSUES_ENDPOINT = "/repos/{owner}/{repo}/issues";
    public static final String ISSUE_ENDPOINT = "/repos/{owner}/{repo}/issues/{number}";
    public static final String ISSUE_LOCK_ENDPOINT = "/repos/{owner}/{repo}/issues/{number}/lock";
}
