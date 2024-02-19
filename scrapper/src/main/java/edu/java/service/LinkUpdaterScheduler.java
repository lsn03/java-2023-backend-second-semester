package edu.java.service;

import edu.java.configuration.ApplicationConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class LinkUpdaterScheduler {
    private final ApplicationConfig applicationConfig;

    @Autowired
    public LinkUpdaterScheduler(ApplicationConfig applicationConfig) {
        this.applicationConfig = applicationConfig;
    }

    @Scheduled(fixedDelayString = "#{applicationConfig.scheduler.interval.toMillis()}")
    public void update() {
        log.info("update");
    }

}
