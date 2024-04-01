package edu.java.service.updater;

import edu.java.configuration.ApplicationConfig;
import edu.java.domain.model.LinkDTO;
import edu.java.domain.repository.LinkRepository;
import edu.java.model.UriDTO;
import edu.java.model.scrapper.dto.request.LinkUpdateRequest;
import edu.java.service.handler.Handler;
import edu.java.service.processor.Processor;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LinkUpdateServiceImpl implements LinkUpdaterService {

    private final LinkRepository jdbcLinkRepository;
    private final ApplicationConfig applicationConfig;
    private final List<Handler> handlers;
    private final List<Processor> processors;
    private int oldLinksInSeconds;

    @Override
    @Transactional
    public List<LinkUpdateRequest> update() {
        List<LinkDTO> list = jdbcLinkRepository.findAllOldLinks(oldLinksInSeconds);

        List<LinkUpdateRequest> answer = new ArrayList<>();

        for (LinkDTO elem : list) {
            List<LinkUpdateRequest> response = null;

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
                    jdbcLinkRepository.findAllByLinkId(elem.getLinkId())
                        .stream()
                        .map(
                            LinkDTO::getTgChatId
                        ).toList();

                for (var responseElem : response) {
                    if (responseElem != null) {
                        responseElem.setTgChatIds(tgChatIds);
                        answer.add(responseElem);
                    }
                }

            }
            if (!answer.isEmpty()) {
                jdbcLinkRepository.updateLink(elem);
            }
        }

        return answer;
    }

    private List<LinkUpdateRequest> processDto(LinkDTO linkDTO, UriDTO uriDto) {
        for (var processor : processors) {
            var response = processor.processUriDTO(linkDTO, uriDto);
            if (response != null) {
                return response;
            }
        }
        return List.of();
    }

    @EventListener(ApplicationReadyEvent.class)
    protected void setTime() {
        oldLinksInSeconds = (int) applicationConfig.scheduler().forceCheckDelay().toSeconds();
    }

}
