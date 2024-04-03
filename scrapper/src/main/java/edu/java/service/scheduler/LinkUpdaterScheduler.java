package edu.java.service.scheduler;

import edu.java.configuration.ApplicationConfig;
import edu.java.model.scrapper.dto.request.LinkUpdateRequest;
import edu.java.model.scrapper.dto.response.ApiErrorResponse;
import edu.java.service.client.BotHttpClient;
import edu.java.service.sender.ScrapperHttpSender;
import edu.java.service.sender.ScrapperQueueProducer;
import edu.java.service.sender.Sender;
import edu.java.service.updater.LinkUpdaterService;
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
    private final LinkUpdaterService linkUpdaterService;
    private final Sender sender;
    @Scheduled(fixedDelayString = "#{@schedulerInterval}")
    public void update() {
        if (!applicationConfig.scheduler().enable()) {
            return;
        }

        List<LinkUpdateRequest> response = linkUpdaterService.update();

        if (response != null) {
            for (var elem : response) {
                sender.send(elem);
            }
        }
    }

}
