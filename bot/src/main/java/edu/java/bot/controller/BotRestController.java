package edu.java.bot.controller;

import edu.java.bot.model.dto.request.LinkUpdateRequest;
import edu.java.bot.service.UpdateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BotRestController {
    private final UpdateService updateService;

    @PostMapping("/updates")
    public ResponseEntity<?> getUpdates(@RequestBody LinkUpdateRequest body) {
        updateService.sendUpdate(body);

        return ResponseEntity.ok(body);
    }
}

