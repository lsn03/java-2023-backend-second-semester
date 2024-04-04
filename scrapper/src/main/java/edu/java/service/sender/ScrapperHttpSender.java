package edu.java.service.sender;

import edu.java.model.scrapper.dto.request.LinkUpdateRequest;
import edu.java.model.scrapper.dto.response.ApiErrorResponse;
import edu.java.service.client.BotHttpClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class ScrapperHttpSender implements Sender {
    private final BotHttpClient client;

    @Override
    public void send(LinkUpdateRequest update) {
        ApiErrorResponse ans = client.sendUpdates(update).block();
        if (ans != null) {
            log.error("{} error while Sending update {}", ans, update);
        } else {
            log.info("send update {}", update);
        }
    }
}
