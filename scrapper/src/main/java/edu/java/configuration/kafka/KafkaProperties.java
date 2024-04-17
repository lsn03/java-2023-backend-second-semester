package edu.java.configuration.kafka;

public record KafkaProperties(
    Boolean usingQueue,
    Integer partitions,
    Short replicas,
    String topic,
    String clientId,
    String servers
) {
}
