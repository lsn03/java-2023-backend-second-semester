package edu.java.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
@ConfigurationProperties(prefix = "client")
public class ClientProperties {
    private String githubBaseUrl;
    private String sofBaseUrl;
    private String botBaseUrl;
}
