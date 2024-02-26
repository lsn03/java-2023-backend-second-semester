package edu.java.bot.controller;

import edu.java.bot.model.dto.request.LinkUpdateRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BotRestController {

    @PostMapping("/updates")
    public ResponseEntity<?> getUpdates(@RequestBody LinkUpdateRequest body) {
//        throw new IncorrectParametersException("default");
        return ResponseEntity.ok(body);
    }
}

