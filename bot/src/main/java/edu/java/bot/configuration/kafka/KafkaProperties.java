package edu.java.bot.configuration.kafka;

public record KafkaProperties(
    Boolean usingQueue,
    Integer partitions,
    Integer replicas,
    String topic,
    String topicDlq,
    String groupId,
    String servers
) {
}
