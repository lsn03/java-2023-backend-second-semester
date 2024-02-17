package edu.java.controller;

import edu.java.repository.GitHubStorage;
import edu.java.request.RequestToGitHub;
import edu.java.service.DataComparisonService;
import edu.java.service.GitHubService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/scrapper/github")
@Slf4j
public class BotGitHubController {

    private GitHubService gitHubService;
    private final GitHubStorage storage;
    private final DataComparisonService dataComparisonService;

    @PostMapping("/track")
    public ResponseEntity<String> trackGitHubUrl(@RequestBody RequestToGitHub body) {
        String owner = body.getOwner();
        String repo = body.getRepo();
        String url = body.getUrl();
        return ResponseEntity.ok("");
    }

}
