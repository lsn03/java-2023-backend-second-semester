package edu.java.configuration;

import java.util.Map;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Setter
@ConfigurationProperties(prefix = "client")
public class ClientProperties {
    private Map<String, String> properties;

    public String getValue(String key) {
        return properties.getOrDefault(key, null);
    }
}
