package com.rafaeldsal.paymentservice.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rafaeldsal.paymentservice.dto.PaymentDto;
import com.rafaeldsal.paymentservice.exception.KafkaSubscribeException;
import com.rafaeldsal.paymentservice.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentIntentCreationConsumer {

  private final ObjectMapper objectMapper;
  private final PaymentService paymentService;

  @KafkaListener(
      topics = "${webservices.minhaprata.kafka.topic.payment-intent-request}",
      groupId = "payment-consumer-1"
  )
  public void consumerPaymentIntent(String message) {
    try {
      PaymentDto dto = objectMapper.readValue(message, PaymentDto.class);
      log.info("Recebido evento de pagamento: {}", dto);
      paymentService.process(dto);
    } catch (JsonProcessingException e) {
      log.error("Erro ao deserializar mensagem para Kafka: {}. Mensagem: {}", e.getMessage(), message);
      throw new KafkaSubscribeException("Falha ao deserializar PaymentRequest - ERROR - " + e);
    }
  }
}
