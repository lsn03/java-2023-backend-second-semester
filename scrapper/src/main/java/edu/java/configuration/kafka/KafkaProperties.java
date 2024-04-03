package edu.java.configuration.kafka;

public record KafkaProperties(
    Boolean usingQueue,
    Integer partitions,
    Integer replicas,
    String topic
) {
}
