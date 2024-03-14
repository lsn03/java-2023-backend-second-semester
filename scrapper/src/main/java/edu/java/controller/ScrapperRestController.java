package edu.java.controller;

import edu.java.model.scrapper.dto.request.AddLinkRequest;
import edu.java.model.scrapper.dto.request.RemoveLinkRequest;
import edu.java.model.scrapper.dto.response.LinkResponse;
import edu.java.model.scrapper.dto.response.ListLinksResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
public class ScrapperRestController {

    public static final String CHAT_SUCCESSFUL_SIGN_UP = "Чат зарегистрирован";
    public static final String CHAT_SUCCESSFUL_REMOVED = "Чат успешно удалён";
    public static final String HEADER_TG_CHAT_ID = "Tg-Chat-Id";
    public static final String GET_LINKS_SUCCESSFUL = "Ссылки успешно получены";
    public static final String LINK_SUCCESSFUL_ADDED = "Ссылка успешно добавлена";
    public static final String ERROR_CHAT_ALREADY_EXIST = "Чат уже существует";

    private static final String TG_CHAT_ID = "/tg-chat/{id}";
    private static final String LINKS = "/links";

    @PostMapping(value = TG_CHAT_ID, produces = {"application/json"})
    public ResponseEntity<?> signUpChat(@PathVariable Long id) {
        return ResponseEntity.ok(CHAT_SUCCESSFUL_SIGN_UP);
    }

    @DeleteMapping(value = TG_CHAT_ID, produces = {"application/json"})
    public ResponseEntity<?> deleteChat(@PathVariable Long id) {
        return ResponseEntity.ok(CHAT_SUCCESSFUL_REMOVED);
    }

    @GetMapping(value = LINKS, produces = {"application/json"})
    public ResponseEntity<?> getTrackedLinks(@RequestHeader(HEADER_TG_CHAT_ID) Long chatId) {
        ListLinksResponse listLinksResponse = new ListLinksResponse();
        return ResponseEntity.ok(listLinksResponse);
    }

    @PostMapping(value = LINKS, produces = {"application/json"})
    public ResponseEntity<?> trackLink(
        @RequestBody AddLinkRequest addLinkRequest,
        @RequestHeader(HEADER_TG_CHAT_ID) Long chatId
    ) {
        LinkResponse linkResponse = new LinkResponse();
        return ResponseEntity.ok(linkResponse);
    }

    @DeleteMapping(value = LINKS, produces = {"application/json"})
    public ResponseEntity<?> unTrackLink(
        @RequestBody RemoveLinkRequest removeLinkRequest,
        @RequestHeader(HEADER_TG_CHAT_ID) Long chatId
    ) {
        LinkResponse linkResponse = new LinkResponse();
        return ResponseEntity.ok(linkResponse);
    }
}
