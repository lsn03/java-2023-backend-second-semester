package edu.java.controller;

import edu.java.domain.model.LinkDto;
import edu.java.model.scrapper.dto.request.AddLinkRequest;
import edu.java.model.scrapper.dto.request.RemoveLinkRequest;
import edu.java.model.scrapper.dto.response.LinkResponse;
import edu.java.model.scrapper.dto.response.ListLinksResponse;
import edu.java.service.process.LinkService;
import edu.java.service.process.TgChatService;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class ScrapperRestController {

    private static final String CHAT_SUCCESSFUL_SIGN_UP = "Чат зарегистрирован";
    private static final String CHAT_SUCCESSFUL_REMOVED = "Чат успешно удалён";
    private static final String HEADER_TG_CHAT_ID = "Tg-Chat-Id";
    private static final String TG_CHAT_ID = "/tg-chat/{id}";
    private static final String LINKS = "/links";

    private final LinkService linkService;
    private final TgChatService chatService;



    @PostMapping(value = TG_CHAT_ID, produces = {"application/json"})
    public ResponseEntity<?> signUpChat(@PathVariable Long id) {

        chatService.add(id);

        return ResponseEntity.ok(CHAT_SUCCESSFUL_SIGN_UP);
    }

    @DeleteMapping(value = TG_CHAT_ID, produces = {"application/json"})
    public ResponseEntity<?> deleteChat(@PathVariable Long id) {
        chatService.remove(id);
        return ResponseEntity.ok(CHAT_SUCCESSFUL_REMOVED);
    }

    @GetMapping(value = LINKS, produces = {"application/json"})
    public ResponseEntity<?> getTrackedLinks(@RequestHeader(HEADER_TG_CHAT_ID) Long chatId) {
        List<LinkDto> list = (List<LinkDto>) linkService.findAll(chatId);

        var response = new ListLinksResponse();

        response.setLists(list.stream().map(linkDTO -> new LinkResponse(linkDTO.getLinkId(), linkDTO.getUri()))
            .collect(Collectors.toList()));

        response.setSize(list.size());
        return ResponseEntity.ok(response);

    }

    @PostMapping(value = LINKS, produces = {"application/json"})
    public ResponseEntity<?> trackLink(
        @RequestBody AddLinkRequest addLinkRequest,
        @RequestHeader(HEADER_TG_CHAT_ID) Long chatId
    ) {
        LinkDto linkDTO = new LinkDto();
        linkDTO.setTgChatId(chatId);
        linkDTO.setUri(URI.create(addLinkRequest.getLink()));

        linkService.add(linkDTO);

        LinkResponse linkResponse = new LinkResponse(linkDTO.getLinkId(), linkDTO.getUri());

        return ResponseEntity.ok(linkResponse);
    }

    @DeleteMapping(value = LINKS, produces = {"application/json"})
    public ResponseEntity<?> unTrackLink(
        @RequestBody RemoveLinkRequest removeLinkRequest,
        @RequestHeader(HEADER_TG_CHAT_ID) Long chatId
    ) {
        LinkDto linkDTO = new LinkDto();
        linkDTO.setTgChatId(chatId);
        linkDTO.setUri(URI.create(removeLinkRequest.getLink()));
        linkService.remove(linkDTO);
        LinkResponse linkResponse = new LinkResponse(linkDTO.getLinkId(), linkDTO.getUri());

        return ResponseEntity.ok(linkResponse);
    }
}
