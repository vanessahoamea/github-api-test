package com.github.models.comments;

import lombok.Getter;

import java.util.Map;

@Getter
public class CommentRequestModel {
    private final String body;

    public CommentRequestModel(Map<String, String> commentDetails) {
        body = commentDetails.get("body");
    }
}
