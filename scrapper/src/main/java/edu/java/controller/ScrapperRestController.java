package edu.java.controller;

import edu.java.model.scrapper.dto.request.AddLinkRequest;
import edu.java.model.scrapper.dto.response.LinkResponse;
import edu.java.model.scrapper.dto.response.ListLinksResponse;
import edu.java.model.scrapper.dto.request.RemoveLinkRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ScrapperRestController {

    public static final String CHAT_SUCCESSFUL_SIGN_UP = "Чат зарегистрирован";
    public static final String CHAT_SUCCESSFUL_REMOVED = "Чат успешно удалён";
    public static final String HEADER_TG_CHAT_ID = "Tg-Chat-Id";
    public static final String GET_LINKS_SUCCESSFUL = "Ссылки успешно получены";
    public static final String LINK_SUCCESSFUL_ADDED = "Ссылка успешно добавлена";
    public static final String ERROR_CHAT_ALREADY_EXIST = "Чат уже существует";

    @PostMapping(value = "/tg-chat/{id}", produces = {"application/json"})
    public ResponseEntity<?> signUpChat(@PathVariable Long id) {
        return ResponseEntity.ok(CHAT_SUCCESSFUL_SIGN_UP);
    }

    @DeleteMapping("/tg-chat/{id}")
    public ResponseEntity<?> deleteChat(@PathVariable Long id) {
        return ResponseEntity.ok(CHAT_SUCCESSFUL_REMOVED);
    }

    @GetMapping("/links")
    public ResponseEntity<?> getTrackedLinks(@RequestHeader(HEADER_TG_CHAT_ID) Long chatId) {
        ListLinksResponse listLinksResponse = new ListLinksResponse();
        return ResponseEntity.ok(listLinksResponse);
    }

    @PostMapping("/links")
    public ResponseEntity<?> trackLink(
        @RequestBody AddLinkRequest addLinkRequest,
        @RequestHeader(HEADER_TG_CHAT_ID) Long chatId
    ) {
        LinkResponse linkResponse = new LinkResponse();
        return ResponseEntity.ok(linkResponse);
    }

    @DeleteMapping("/links")
    public ResponseEntity<?> unTrackLink(
        @RequestBody RemoveLinkRequest removeLinkRequest,
        @RequestHeader(HEADER_TG_CHAT_ID) Long chatId
    ) {
        LinkResponse linkResponse = new LinkResponse();
        return ResponseEntity.ok(linkResponse);
    }
}
