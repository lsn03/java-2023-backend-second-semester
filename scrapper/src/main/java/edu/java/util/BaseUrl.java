package edu.java.util;

import lombok.Getter;

@Getter
public enum BaseUrl {
    GITHUB_BASE_URL("https://api.github.com"),
    STACK_OVER_FLOW_BASE_URL("https://api.stackexchange.com/2.3");
    private final String url;

    BaseUrl(String url) {
        this.url = url;
    }

}
