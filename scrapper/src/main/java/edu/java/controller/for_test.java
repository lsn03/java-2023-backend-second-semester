package edu.java.controller;

import edu.java.model.scrapper.dto.request.LinkUpdateRequest;
import edu.java.model.scrapper.dto.response.ApiErrorResponse;
import edu.java.service.BotHttpClient;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
public class for_test {
    private BotHttpClient client;

    @GetMapping("/1")
    public Mono<ApiErrorResponse> t1() {
        return client.sendUpdates(new LinkUpdateRequest(1l, "http://example.com", "desc", List.of(1, 2, 3)));
    }
}
