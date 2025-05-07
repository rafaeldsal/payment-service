package com.rafaeldsal.paymentservice.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rafaeldsal.paymentservice.dto.PaymentWebhookDto;
import com.rafaeldsal.paymentservice.exception.KafkaPublishingException;
import com.rafaeldsal.paymentservice.service.PaymentStatusNotifier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentStatusNotifierImpl implements PaymentStatusNotifier {

  @Value("${webservices.minhaprata.kafka.topic.payment-intent-status-updated}")
  private String topicPaymentIntentStatusUpdated;

  private final ObjectMapper objectMapper;
  private final KafkaTemplate<String, String> kafkaTemplate;
  private final Executor callbackExecutor;

  @Override
  public void sendMessage(PaymentWebhookDto dto) {
    String content = null;

    try {
      content = objectMapper.writeValueAsString(dto);
    } catch (JsonProcessingException e) {
      log.error("Erro ao serializar mensagem para Kafka: {}", e.getMessage());
      throw new KafkaPublishingException("Erro ao serializar mensagem para Kafka - ERROR - " + e.getMessage());
    }

    String key = dto.transactionId();

    CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(topicPaymentIntentStatusUpdated, key, content);

    future.whenCompleteAsync((result, ex) -> {
      if (ex == null) {
        log.info("Mensagem enviada com sucesso para o tópico [{}], offset [{}], partition [{}]",
            topicPaymentIntentStatusUpdated,
            result.getRecordMetadata().offset(),
            result.getRecordMetadata().partition());
      } else {
        log.error("Erro ao enviar mensagem para o tópico [{}]: {}", topicPaymentIntentStatusUpdated, ex.getMessage());
      }
    }, callbackExecutor);
  }
}
