package edu.java.controller;

import edu.java.domain.model.LinkDTO;
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

    public static final String CHAT_SUCCESSFUL_SIGN_UP = "Чат зарегистрирован";
    public static final String CHAT_SUCCESSFUL_REMOVED = "Чат успешно удалён";
    public static final String HEADER_TG_CHAT_ID = "Tg-Chat-Id";


    private final LinkService linkService;
    private final TgChatService chatService;

    @PostMapping(value = "/tg-chat/{id}", produces = {"application/json"})
    public ResponseEntity<?> signUpChat(@PathVariable Long id) {

        chatService.add(id);

        return ResponseEntity.ok(CHAT_SUCCESSFUL_SIGN_UP);
    }

    @DeleteMapping("/tg-chat/{id}")
    public ResponseEntity<?> deleteChat(@PathVariable Long id) {
        chatService.remove(id);
        return ResponseEntity.ok(CHAT_SUCCESSFUL_REMOVED);
    }

    @GetMapping("/links")
    public ResponseEntity<?> getTrackedLinks(@RequestHeader(HEADER_TG_CHAT_ID) Long chatId) {
        List<LinkDTO> list = (List<LinkDTO>) linkService.findAll(chatId);

        var response = new ListLinksResponse();

        response.setLists(list.stream().map(linkDTO -> new LinkResponse(linkDTO.getLinkId(), linkDTO.getUri()))
            .collect(Collectors.toList()));

        response.setSize(list.size());
        return ResponseEntity.ok(response);

    }

    @PostMapping("/links")
    public ResponseEntity<?> trackLink(
        @RequestBody AddLinkRequest addLinkRequest,
        @RequestHeader(HEADER_TG_CHAT_ID) Long chatId
    ) {
        LinkDTO linkDTO = new LinkDTO();
        linkDTO.setTgChatId(chatId);
        linkDTO.setUri(URI.create(addLinkRequest.getLink()));

        linkService.add(linkDTO);

        LinkResponse linkResponse = new LinkResponse(linkDTO.getLinkId(), linkDTO.getUri());

        return ResponseEntity.ok(linkResponse);
    }

    @DeleteMapping("/links")
    public ResponseEntity<?> unTrackLink(
        @RequestBody RemoveLinkRequest removeLinkRequest,
        @RequestHeader(HEADER_TG_CHAT_ID) Long chatId
    ) {
        LinkDTO linkDTO = new LinkDTO();
        linkDTO.setTgChatId(chatId);
        linkDTO.setUri(URI.create(removeLinkRequest.getLink()));
        linkService.remove(linkDTO);
        LinkResponse linkResponse = new LinkResponse(linkDTO.getLinkId(), linkDTO.getUri());

        return ResponseEntity.ok(linkResponse);
    }
}
