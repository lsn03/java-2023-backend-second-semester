package edu.java.configuration.rate;

public record BucketProperties(
    Integer count,
    Integer seconds
) {
}
