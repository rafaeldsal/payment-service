package com.rafaeldsal.paymentservice.exception;

public class StripeIntegrationException extends RuntimeException {
  public StripeIntegrationException(String message) {
    super(message);
  }
}
