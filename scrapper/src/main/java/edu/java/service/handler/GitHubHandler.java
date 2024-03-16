package edu.java.service.handler;

import edu.java.exception.exception.IncorrectParametersException;
import edu.java.model.GitHubPullRequestUriDTO;
import edu.java.model.UriDTO;
import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import edu.java.util.Utils;
import org.springframework.stereotype.Component;

@Component
public class GitHubHandler implements Handler {
    private static final Pattern GITHUB_PULL_REQUEST_PATTERN =
        Pattern.compile("https://github\\.com/([^/]+)/([^/]+)/pull/(\\d+)");
    private static final int GROUP_ID = 3;
    private static final int GROUP_REPO = 2;
    private static final int GROUP_OWNER = 1;


    @Override
    public int getId() {
        return Utils.GITHUB_SITE_TYPE_ID;
    }

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

            throw new IncorrectParametersException("Site: "+uri+" not supported");
        }
    }

}

