package edu.java.bot.parser;

import org.springframework.stereotype.Component;

@Component
public class GitHubHandler implements ResourceHandler {
    @Override
    public boolean canHandle(String url) {
        return url.startsWith("https://github.com/");
    }

}
