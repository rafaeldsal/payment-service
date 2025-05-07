package com.rafaeldsal.paymentservice.exception;

public class KafkaSubscribeException extends RuntimeException {
  public KafkaSubscribeException(String message) {
    super(message);
  }
}
