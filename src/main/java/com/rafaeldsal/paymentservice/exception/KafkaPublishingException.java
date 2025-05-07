package com.rafaeldsal.paymentservice.exception;

public class KafkaPublishingException extends RuntimeException {
  public KafkaPublishingException(String message) {
    super(message);
  }
}
