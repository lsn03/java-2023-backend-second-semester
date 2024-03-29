package edu.java.configuration.rate;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

//@ConditionalOnProperty(prefix = "app.rate")
public record BucketProperties
    (Integer count,
     Integer seconds) {
}
