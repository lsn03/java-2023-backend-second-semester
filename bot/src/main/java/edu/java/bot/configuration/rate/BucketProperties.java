package edu.java.bot.configuration.rate;

//@ConditionalOnProperty(prefix = "app.rate")
public record BucketProperties
    (Integer count,
     Integer seconds) {
}
