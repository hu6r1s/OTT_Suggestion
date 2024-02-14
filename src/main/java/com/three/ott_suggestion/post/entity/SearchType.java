package com.three.ott_suggestion.post.entity;

public enum SearchType {
    TITLE("title"),
    NICKNAME("nickname");

    private final String type;

    SearchType(String type) {
        this.type = type;
    }

    public String type() {
        return this.type;
    }
}
