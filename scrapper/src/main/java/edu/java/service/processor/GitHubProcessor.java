package edu.java.service.processor;

import edu.java.domain.model.GitHubCommitDto;
import edu.java.domain.model.LinkDto;
import edu.java.domain.repository.LinkRepository;
import edu.java.model.GitHubPullRequestUriDto;
import edu.java.model.UriDto;
import edu.java.model.github.PullRequestModelResponse;
import edu.java.model.scrapper.dto.request.LinkUpdateRequest;
import edu.java.service.client.GitHubClient;
import edu.java.service.database.GitHubService;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class GitHubProcessor implements Processor {

    private static final int MAX_MESSAGE_SIZE = 5;
    private static final int END_SHA_INDEX = 7;
    private static final int START_SHA_INDEX = 0;
    private final static StringBuilder STRING_BUILDER = new StringBuilder();

    private final GitHubClient gitHubClient;
    private final GitHubService gitHubService;
    private final LinkRepository jooqLinkRepository;

    public List<LinkUpdateRequest> processUriDTO(LinkDto linkDTO, UriDto uriDto) {
        if (!(uriDto instanceof GitHubPullRequestUriDto)) {
            return null;
        }

        GitHubPullRequestUriDto uriDto1 = (GitHubPullRequestUriDto) uriDto;

        PullRequestModelResponse response =
            gitHubClient.fetchPullRequest(uriDto1.getOwner(), uriDto1.getRepo(),
                uriDto1.getPullNumber()
            );
        List<LinkUpdateRequest> list = new ArrayList<>();

        list.add(processCommit(linkDTO, response));

        return list;
    }

    private LinkUpdateRequest processCommit(LinkDto linkDTO, PullRequestModelResponse response) {
        List<GitHubCommitDto> commitsFromAPI = response.getPullCommitDTOS().stream().map(
            pullCommitDTOResponse -> {
                GitHubCommitDto commit = GitHubCommitDto.create(pullCommitDTOResponse);
                commit.setLinkId(linkDTO.getLinkId());
                return commit;
            }).toList();
        log.info(commitsFromAPI.toString());
        if (linkDTO.getLastUpdate() == null) {
            log.info(linkDTO.toString());
            gitHubService.addCommits(commitsFromAPI);
            linkDTO.setLastUpdate(OffsetDateTime.now());
            jooqLinkRepository.updateLink(linkDTO);
            return null;
        }

        var commitsFromDB = gitHubService.getCommits(linkDTO.getUri());
        log.info(commitsFromDB.toString());
        List<GitHubCommitDto> listForUpdate = new ArrayList<>();

        for (var commit : commitsFromAPI) {
            if (!commitsFromDB.contains(commit)) {
                listForUpdate.add(commit);
            }

        }
        gitHubService.addCommits(listForUpdate);
        log.info(listForUpdate.toString());
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
