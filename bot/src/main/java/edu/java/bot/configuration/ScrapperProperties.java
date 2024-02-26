package edu.java.bot.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "scrapper")
@Getter
@Setter
public class ScrapperProperties {

    private String baseUrl;

}
