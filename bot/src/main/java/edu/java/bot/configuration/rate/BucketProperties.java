package edu.java.bot.configuration.rate;


public record BucketProperties(
    Integer count,
    Integer seconds
) {
}
