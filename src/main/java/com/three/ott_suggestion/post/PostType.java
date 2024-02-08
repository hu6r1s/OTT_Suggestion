package com.three.ott_suggestion.post;

public enum PostType {
    TITLE("title"),
    NICKNAME("nickname");

    private final String type;

    PostType(String type){
        this.type = type;
    }

    public String type(){
        return this.type;
    }
}
