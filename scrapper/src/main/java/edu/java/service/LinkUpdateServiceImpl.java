package edu.java.service;

import edu.java.domain.model.LinkDTO;
import edu.java.model.GitHubPullRequestUriDTO;
import edu.java.model.StackOverFlowQuestionUriDTO;
import edu.java.model.github.PullRequestModelResponse;
import edu.java.model.scrapper.dto.request.LinkUpdateRequest;
import edu.java.model.stack_over_flow.StackOverFlowModel;
import edu.java.service.client.GitHubClient;
import edu.java.service.client.StackOverFlowClient;
import edu.java.service.parser.Handler;
import edu.java.service.process.LinkUpdaterService;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LinkUpdateServiceImpl implements LinkUpdaterService {
    public static final int MASK = 0xff;
    private final JdbcTemplate jdbcTemplate;
    private final GitHubClient gitHubClient;
    private final StackOverFlowClient stackOverFlowClient;

    private final Handler gitHubHandler;
    private final Handler stackOverFlowHandler;

    private List<LinkDTO> getLinkDTOList() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");

        return jdbcTemplate.query(
            "select lc.link_id,uri,created_at,last_update,hash,chat_id "
                + "from link left join link_chat lc on link.link_id = lc.link_id "
                + "where last_update is null or  last_update < now() - interval '10 minutes';",
            (rs, rowNum) -> {
                LinkDTO linkDTO = new LinkDTO();

                LocalDateTime localDateTimeCreatedAt = LocalDateTime.parse(rs.getString("created_at"), formatter);

                var lastUpdateString = rs.getString("last_update");
                LocalDateTime lastUpdate;
                if (lastUpdateString == null) {
                    lastUpdate = OffsetDateTime.now().toLocalDateTime();
                } else {
                    lastUpdate = LocalDateTime.parse(lastUpdateString, formatter);
                }
                linkDTO.setLastUpdate(lastUpdate.atOffset(ZoneOffset.UTC));
                linkDTO.setTgChatId(rs.getLong("chat_id"));
                linkDTO.setLinkId(rs.getLong("link_id"));
                linkDTO.setUri(URI.create(rs.getString("uri")));
                linkDTO.setCreatedAt(localDateTimeCreatedAt.atOffset(ZoneOffset.UTC));

                linkDTO.setHash(rs.getString("hash"));

                return linkDTO;
            }
        );
    }

    @Override
    public List<LinkUpdateRequest> update() throws NoSuchAlgorithmException {
        List<LinkDTO> list = getLinkDTOList();
        List<LinkDTO> listForUpdate = new ArrayList<>();
        GitHubPullRequestUriDTO gitHubPullRequestUriDTO;
        StackOverFlowQuestionUriDTO stackOverFlowQuestionUriDTO;
        String string = "";
        for (LinkDTO elem : list) {
            URI uri = elem.getUri();
            if (gitHubHandler.canHandle(uri)) {
                gitHubPullRequestUriDTO = (GitHubPullRequestUriDTO) gitHubHandler.handle(uri);
                PullRequestModelResponse response =
                    gitHubClient.fetchPullRequest(gitHubPullRequestUriDTO.getOwner(), gitHubPullRequestUriDTO.getRepo(),
                        gitHubPullRequestUriDTO.getPullNumber()
                    ).block();
                string = response.toString();
                string = process(string);
            } else if (stackOverFlowHandler.canHandle(uri)) {
                stackOverFlowQuestionUriDTO = (StackOverFlowQuestionUriDTO) stackOverFlowHandler.handle(uri);
                StackOverFlowModel response =
                    stackOverFlowClient.fetchQuestionData(stackOverFlowQuestionUriDTO.getQuestionId()).block();
                string = response.toString();
                string = process(string);
            }

            if (elem.getLastUpdate() == null || elem.getHash() == null) {
                elem.setHash(string);
                elem.setLastUpdate(OffsetDateTime.now());
                listForUpdate.add(elem);
            }
            if (!elem.getHash().equals(string)) {
                listForUpdate.add(elem);
            }

        }
        if (!listForUpdate.isEmpty()) {
            updateDatabase(listForUpdate);
            return converLinkDTOTOLinkUpdateRequest(listForUpdate);
        }

        return null;
    }

    private List<LinkUpdateRequest> converLinkDTOTOLinkUpdateRequest(List<LinkDTO> linkDTOList) {

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
            jdbcTemplate.update(
                "update link set uri = ?, last_update = now(), hash = ? where link_id = ? ",
                new Object[] {
                    elem.getUri().toString(),
                    elem.getHash(),
                    elem.getLinkId(),
                }
            );
        }
    }

    private String process(String string) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] encodedHash = digest.digest(
            string.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(encodedHash);
    }

    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(MASK & hash[i]);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
