package edu.java.service.processor;

import edu.java.domain.model.GitHubCommitDTO;
import edu.java.domain.model.LinkDTO;
import edu.java.model.GitHubPullRequestUriDTO;
import edu.java.model.UriDTO;
import edu.java.model.github.PullRequestModelResponse;
import edu.java.model.scrapper.dto.request.LinkUpdateRequest;
import edu.java.service.client.GitHubClient;
import edu.java.service.database.GitHubService;
import edu.java.util.Utils;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class GitHubProcessor implements Processor {

    public static final int MAX_MESSAGE_SIZE = 5;
    public static final int END_SHA_INDEX = 7;
    public static final int START_SHA_INDEX = 0;
    private final GitHubClient gitHubClient;
    private final GitHubService gitHubService;
    private final static StringBuilder STRING_BUILDER = new StringBuilder();

    public Map<String, LinkUpdateRequest> processUriDTO(LinkDTO linkDTO, UriDTO uriDto) {
        if (!(uriDto instanceof GitHubPullRequestUriDTO)) {
            return null;
        }

        GitHubPullRequestUriDTO uriDto1 = (GitHubPullRequestUriDTO) uriDto;

        PullRequestModelResponse response =
            gitHubClient.fetchPullRequest(uriDto1.getOwner(), uriDto1.getRepo(),
                uriDto1.getPullNumber()
            );
        Map<String, LinkUpdateRequest> map = new HashMap<>();

        map.put(Utils.GH_COMMIT, processCommit(linkDTO, response));

        return map;
    }

    private LinkUpdateRequest processCommit(LinkDTO linkDTO, PullRequestModelResponse response) {
        var commitsFromAPI = response.getPullCommitDTOS().stream().map(
            pullCommitDTOResponse -> {
                GitHubCommitDTO commit = GitHubCommitDTO.create(pullCommitDTOResponse);
                commit.setLinkId(linkDTO.getLinkId());
                return commit;
            }).toList();
        var commitsFromDB = gitHubService.getCommits(linkDTO.getUri());
        List<GitHubCommitDTO> listForUpdate = new ArrayList<>();
        List<GitHubCommitDTO> listForInsertIntoDB = new ArrayList<>();
        for (var commit : commitsFromAPI) {

            if (!commitsFromDB.contains(commit)) {
                listForUpdate.add(commit);
                if (linkDTO.getLastUpdate() == null) {
                    listForInsertIntoDB.add(commit);
                }
            }
        }
        if (!listForInsertIntoDB.isEmpty()) {
            gitHubService.addCommits(listForInsertIntoDB);
            linkDTO.setLastUpdate(OffsetDateTime.now());
            return null;
        }
        gitHubService.addCommits(listForUpdate);

        STRING_BUILDER.setLength(0);
        if (listForUpdate.size() > MAX_MESSAGE_SIZE) {
            STRING_BUILDER.append("Появилось ").append(listForUpdate.size()).append(" коммитов в пулл реквесте: ")
                .append(linkDTO.getUri()).append(System.lineSeparator());
        } else {
            for (var commit : listForUpdate) {

                STRING_BUILDER.append("Пользователь ").append(commit.getAuthor())
                    .append(" оставил коммит ")
                    .append(commit.getSha(), START_SHA_INDEX, END_SHA_INDEX)
                    .append(".... с сообщением ")
                    .append(commit.getMessage())
                    .append(System.lineSeparator());
            }
        }
        if (STRING_BUILDER.isEmpty()) {
            return null;
        }
        return new LinkUpdateRequest(
            linkDTO.getLinkId(),
            linkDTO.getUri().toString(),
            STRING_BUILDER.toString(),
            null
        );
    }
}
