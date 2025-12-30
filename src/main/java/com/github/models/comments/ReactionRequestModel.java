package com.github.models.comments;

import lombok.Getter;

import java.util.Map;

@Getter
public class ReactionRequestModel {
    private String content;

    public ReactionRequestModel(Map<String, String> commentDetails) {
        content = commentDetails.get("content");
    }
}
