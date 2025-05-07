package com.rafaeldsal.paymentservice.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
@RequiredArgsConstructor
public class KafkaConfig {

  @Value("${webservices.minhaprata.kafka.topic.payment-intent-status-updated}")
  private String topicPaymentIntentStatusUpdated;

  @Value("${webservices.minhaprata.kafka.topic.payment-intent-response}")
  private String topicPaymentIntentResponse;

  @Value("${webservices.minhaprata.kafka.topic.partitions}")
  private Integer partitionsTopic;

  @Value("${webservices.minhaprata.kafka.topic.replication-factor}")
  private Integer replicas;

  private final KafkaProperties kafkaProperties;

  @Bean
  public Executor kafkaCallbackExecutor() {
    return Executors.newFixedThreadPool(4);
  }

  @Bean
  public ProducerFactory<String, String> producerFactory() {
    Map<String, Object> configProps = kafkaProperties.buildProducerProperties();
    return new DefaultKafkaProducerFactory<>(configProps);
  }

  @Bean
  public KafkaTemplate<String, String> kafkaTemplate() {
    return new KafkaTemplate<>(producerFactory());
  }

  @Bean
  public KafkaAdmin.NewTopics createdTopics() {
    return new KafkaAdmin.NewTopics(
        TopicBuilder.name(topicPaymentIntentStatusUpdated)
        .partitions(partitionsTopic)
        .replicas(replicas)
        .build(),

        TopicBuilder.name(topicPaymentIntentResponse)
            .partitions(partitionsTopic)
            .replicas(replicas)
            .build());
  }
}
