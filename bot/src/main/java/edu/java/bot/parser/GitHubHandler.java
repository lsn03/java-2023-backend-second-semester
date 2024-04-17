package edu.java.bot.parser;

import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;

@Component
public class GitHubHandler implements ResourceHandler {
    private static final Pattern GITHUB_PULL_REQUEST_PATTERN =
        Pattern.compile("https://github\\.com/([^/]+)/([^/]+)/pull/(\\d+)");

    @Override
    public boolean canHandle(URI uri) {
        Matcher matcher = GITHUB_PULL_REQUEST_PATTERN.matcher(uri.toString());
        return matcher.matches();
    }

}

