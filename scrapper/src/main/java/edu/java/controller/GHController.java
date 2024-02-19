package edu.java.controller;

import edu.java.model.github.PullRequestModel;
import edu.java.model.github.dto.IssueCommentDTO;
import edu.java.model.github.dto.PullCommentDTO;
import edu.java.model.github.dto.PullCommitDTO;
import edu.java.model.github.dto.PullReviewDTO;
import edu.java.service.client.GitHubClient;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@RestController
@RequestMapping("/scrapper/gh")
@Slf4j
public class GHController {
    private GitHubClient client;
    private final String owner = "lsn03";
    private final String repo = "java-2023-backend-first-semester";
    private final int pullNumber = 1;

    @GetMapping("/1")
    public Mono<PullRequestModel> do1() {
        return client.fetchPullRequest(owner, repo, pullNumber);
    }

    @GetMapping("/2")
    public Mono<List<PullCommitDTO>> do2() {
        return client.getAllCommitsInPullRequest(owner, repo, pullNumber);
    }

    @GetMapping("/3")
    public Mono<List<IssueCommentDTO>> do3() {
        return client.getIssueComments(owner, repo, pullNumber);
    }

    @GetMapping("/4")
    public Mono<List<PullCommentDTO>> do4() {
        return client.getPullComments(owner, repo, pullNumber);
    }

    @GetMapping("/5")
    public Mono<List<PullReviewDTO>> do5() {
        return client.getPullReviews(owner, repo, pullNumber);
    }
}
