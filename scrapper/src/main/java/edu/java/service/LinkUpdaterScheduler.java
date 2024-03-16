package edu.java.service;

import edu.java.configuration.ApplicationConfig;
import edu.java.model.scrapper.dto.request.LinkUpdateRequest;
import edu.java.model.scrapper.dto.response.ApiErrorResponse;
import edu.java.service.process.LinkUpdaterService;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@EnableScheduling
@AllArgsConstructor
public class LinkUpdaterScheduler {
    private final ApplicationConfig applicationConfig;
    private final BotHttpClient botHttpClient;
    private final LinkUpdaterService linkUpdaterService;

    @Scheduled(fixedDelayString = "#{@schedulerInterval}")
    public void update() {
        if(!applicationConfig.scheduler().enable()){
            return;
        }
        try {

            List<LinkUpdateRequest> response = linkUpdaterService.update();
            log.info("update {}", response);
            if (response != null) {
                for (var elem : response) {
                    ApiErrorResponse ans = botHttpClient.sendUpdates(elem).block();
                    if (ans != null) {
                        log.error("{} error while Sending update {}", ans, elem);
                    }
                }

            }
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

    }

}
