package edu.java.service.parser;

import edu.java.model.GitHubPullRequestUriDTO;
import edu.java.model.UriDTO;
import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;

@Component
public class GitHubHandler implements Handler {
    private static final Pattern GITHUB_PULL_REQUEST_PATTERN =
        Pattern.compile("https://github\\.com/([^/]+)/([^/]+)/pull/(\\d+)");
    public static final int GROUP_ID = 3;
    public static final int GROUP_REPO = 2;
    public static final int GROUP_OWNER = 1;

    @Override
    public boolean canHandle(URI uri) {
        Matcher matcher = GITHUB_PULL_REQUEST_PATTERN.matcher(uri.toString());
        return matcher.matches();
    }

    @Override
    public UriDTO handle(URI uri) {
        Matcher matcher = GITHUB_PULL_REQUEST_PATTERN.matcher(uri.toString());
        if (matcher.matches()) {
            String owner = matcher.group(GROUP_OWNER);
            String repo = matcher.group(GROUP_REPO);
            Integer id = Integer.parseInt(matcher.group(GROUP_ID));

            return new GitHubPullRequestUriDTO(owner, repo, id);
        } else {

            throw new IllegalArgumentException();
        }
    }

}

