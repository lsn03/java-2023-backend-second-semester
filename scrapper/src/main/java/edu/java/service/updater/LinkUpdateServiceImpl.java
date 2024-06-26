package edu.java.service.updater;

import edu.java.configuration.ApplicationConfig;
import edu.java.domain.model.LinkDto;
import edu.java.domain.repository.LinkRepository;
import edu.java.model.UriDto;
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

    private final LinkRepository jpaLinkRepository;
    private final ApplicationConfig applicationConfig;
    private final List<Handler> handlers;
    private final List<Processor> processors;
    private int oldLinksInSeconds;

    @Override
    @Transactional
    public List<LinkUpdateRequest> update() {
        List<LinkDto> list = jpaLinkRepository.findAllOldLinks(oldLinksInSeconds);

        List<LinkUpdateRequest> answer = new ArrayList<>();

        for (LinkDto elem : list) {
            List<LinkUpdateRequest> response = null;

            URI uri = elem.getUri();
            for (var handler : handlers) {
                if (handler.canHandle(uri)) {
                    var uriDto = handler.handle(uri);
                    response = processDto(elem, uriDto);
                    if (response.isEmpty()) {
                        break;
                    }
                }
            }

            var tgChatIds =
                jpaLinkRepository.findAllByLinkId(elem.getLinkId())
                    .stream()
                    .map(
                        LinkDto::getTgChatId
                    ).toList();

            for (var responseElem : response) {
                if (responseElem != null) {
                    responseElem.setTgChatIds(tgChatIds);
                    answer.add(responseElem);
                }
            }

            if (!answer.isEmpty()) {
                jpaLinkRepository.updateLink(elem);
            }
        }

        return answer;
    }

    private List<LinkUpdateRequest> processDto(LinkDto linkDTO, UriDto uriDto) {
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
