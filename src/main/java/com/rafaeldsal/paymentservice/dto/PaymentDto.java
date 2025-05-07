package com.rafaeldsal.paymentservice.dto;

import lombok.Builder;

@Builder
public record PaymentDto(
    String transactionId,
    String timestamp,
    String userId,
    String orderId,
    String paymentMethod,
    String currency,
    Long amount
) {
}
