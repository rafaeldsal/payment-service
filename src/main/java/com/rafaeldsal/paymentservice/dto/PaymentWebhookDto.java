package com.rafaeldsal.paymentservice.dto;

import lombok.Builder;

@Builder
public record PaymentWebhookDto(
    String transactionId,
    String status,
    String timestamp,
    String paymentIntentId,
    PaymentErrorInfo paymentErrorInfo
) {

  public static PaymentWebhookDto success(String transactionId,
                                          String paymentIntentId,
                                          String status,
                                          String timestamp) {
    return new PaymentWebhookDto(transactionId, status, timestamp, paymentIntentId, null);
  }

  public static PaymentWebhookDto error(String transactionId,
                                        String paymentIntentId,
                                        String status,
                                        String timestamp,
                                        PaymentErrorInfo paymentErrorInfo) {
    return new PaymentWebhookDto(transactionId, status, timestamp, paymentIntentId, paymentErrorInfo);
  }
}
