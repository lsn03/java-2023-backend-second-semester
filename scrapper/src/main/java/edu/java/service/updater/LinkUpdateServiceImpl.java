package edu.java.service.updater;

import edu.java.domain.model.LinkDTO;
import edu.java.domain.repository.LinkRepository;
import edu.java.model.UriDTO;
import edu.java.model.scrapper.dto.request.LinkUpdateRequest;
import edu.java.service.client.GitHubClient;
import edu.java.service.client.StackOverFlowClient;
import edu.java.service.handler.Handler;
import edu.java.service.processor.Processor;
import java.net.URI;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LinkUpdateServiceImpl implements LinkUpdaterService {

    private static final int TIME_TO_OLD_LINK_IN_SECONDS = 20;


    private final LinkRepository linkRepository;

    private final List<Handler> handlers;
    private final List<Processor> processors;

    @Override
    public List<LinkUpdateRequest> update() {
        List<LinkDTO> list = linkRepository.findAllOldLinks(TIME_TO_OLD_LINK_IN_SECONDS);

        List<LinkUpdateRequest> answer = new ArrayList<>();

        for (LinkDTO elem : list) {
            Map<String, LinkUpdateRequest> response = null;

            URI uri = elem.getUri();
            for (var handler : handlers) {
                if (handler.canHandle(uri)) {
                    var uriDto = handler.handle(uri);
                    response = processDto(elem, uriDto);
                    if (response != null) {
                        break;
                    }
                }
            }
            if (response != null) {
                var tgChatIds =
                    linkRepository.findAllByLinkId(elem.getLinkId())
                        .stream()
                        .map(
                            LinkDTO::getTgChatId
                        ).toList();
                var keySet = response.keySet();
                for (var key : keySet) {
                    var collectionElem = response.get(key);
                    if (collectionElem == null) {
                        break;
                    }
                    collectionElem.setTgChatIds(tgChatIds);
                    answer.add(collectionElem);

                }
            }
            if (!answer.isEmpty()) {
                linkRepository.updateLink(elem);
            }
        }

        return answer;
    }

    private Map<String, LinkUpdateRequest> processDto(LinkDTO linkDTO, UriDTO uriDto) {
        for (var processor : processors) {
            var response = processor.processUriDTO(linkDTO, uriDto);
            if (response != null) {
                return response;
            }
        }
        return null;
    }

}
