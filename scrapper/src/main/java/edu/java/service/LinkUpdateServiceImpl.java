package edu.java.service;

import edu.java.domain.model.LinkDTO;
import edu.java.domain.repository.LinkRepository;
import edu.java.model.GitHubPullRequestUriDTO;
import edu.java.model.StackOverFlowQuestionUriDTO;
import edu.java.model.UriDTO;
import edu.java.model.github.PullRequestModelResponse;
import edu.java.model.scrapper.dto.request.LinkUpdateRequest;
import edu.java.model.stack_over_flow.StackOverFlowModel;
import edu.java.service.client.GitHubClient;
import edu.java.service.client.StackOverFlowClient;
import edu.java.service.handler.Handler;
import edu.java.service.process.LinkUpdaterService;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LinkUpdateServiceImpl implements LinkUpdaterService {
    private static final int MASK = 0xff;
    private static final int TIME_TO_OLD_LINK_IN_SECONDS = 10;
    private static final MessageDigest MESSAGE_DIGEST;


    private final LinkRepository jdbcLinkRepository;
    private final GitHubClient gitHubClient;
    private final StackOverFlowClient stackOverFlowClient;
    private final List<Handler> handlers;


    static {
        try {
            MESSAGE_DIGEST = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<LinkUpdateRequest> update() throws NoSuchAlgorithmException {
        List<LinkDTO> list = jdbcLinkRepository.findAllOldLinks(TIME_TO_OLD_LINK_IN_SECONDS);
        List<LinkDTO> listForUpdate = new ArrayList<>();

        for (LinkDTO elem : list) {

            URI uri = elem.getUri();
            for (var handler : handlers) {
                if (handler.canHandle(uri)) {
                    var uriDto = handler.handle(uri);

                    String string = processDto(uriDto);
                    if (elem.getLastUpdate() == null || elem.getHash() == null) {
                        elem.setHash(string);
                        elem.setLastUpdate(OffsetDateTime.now());
                        updateDatabase(List.of(elem));
                    }
                    if (!elem.getHash().equals(string)) {
                        elem.setLastUpdate(OffsetDateTime.now());
                        elem.setHash(string);
                        listForUpdate.add(elem);
                    }
                }

            }

        }
        if (!listForUpdate.isEmpty()) {
            updateDatabase(listForUpdate);
            return convertLinkDtoToLinkUpdateRequest(listForUpdate);
        }

        return List.of();
    }

    private String processDto(UriDTO uriDto) throws NoSuchAlgorithmException {
        String answer = "";
        if (uriDto instanceof GitHubPullRequestUriDTO) {
            answer = processGitHubUriDTO((GitHubPullRequestUriDTO) uriDto);
        } else if (uriDto instanceof StackOverFlowQuestionUriDTO) {
            answer = processStackOverFlowUriDTO((StackOverFlowQuestionUriDTO) uriDto);
        }
        return answer;
    }

    private String processStackOverFlowUriDTO(StackOverFlowQuestionUriDTO uriDto) throws NoSuchAlgorithmException {
        String string;

        StackOverFlowModel response =
            stackOverFlowClient.fetchQuestionData(uriDto.getQuestionId());
        string = response.toString();
        return getHashOfResponse(string);

    }

    private String processGitHubUriDTO(GitHubPullRequestUriDTO uriDto) throws NoSuchAlgorithmException {
        String string;

        PullRequestModelResponse response =
            gitHubClient.fetchPullRequest(uriDto.getOwner(), uriDto.getRepo(),
                uriDto.getPullNumber()
            );
        string = response.toString();
        return getHashOfResponse(string);

    }

    private List<LinkUpdateRequest> convertLinkDtoToLinkUpdateRequest(List<LinkDTO> linkDTOList) {

        Map<Long, Map<URI, List<LinkDTO>>> groupedByLinkIdAndUri = linkDTOList.stream()
            .collect(Collectors.groupingBy(
                LinkDTO::getLinkId,
                Collectors.groupingBy(LinkDTO::getUri)
            ));

        List<LinkUpdateRequest> linkUpdateRequests = new ArrayList<>();

        groupedByLinkIdAndUri.forEach((linkId, uriMap) -> uriMap.forEach((uri, dtos) -> {
            List<Long> tgChatIds = dtos.stream()
                .map(LinkDTO::getTgChatId)
                .distinct()
                .collect(Collectors.toList());

            linkUpdateRequests.add(new LinkUpdateRequest(linkId, uri.toString(), "", tgChatIds));
        }));

        return linkUpdateRequests;
    }

    private void updateDatabase(List<LinkDTO> list) {
        for (var elem : list) {
            jdbcLinkRepository.updateLink(elem);
        }
    }

    private String getHashOfResponse(String string) throws NoSuchAlgorithmException {
        byte[] encodedHash = MESSAGE_DIGEST.digest(
            string.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(encodedHash);
    }

    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            String hex = Integer.toHexString(MASK & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
