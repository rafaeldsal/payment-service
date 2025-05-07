package com.rafaeldsal.paymentservice.dto;

import lombok.Builder;

@Builder
public record PaymentStatusUpdate(
    String transactionId,
    String timestamp,
    String orderId,
    String paymentIntentId,
    String status
) {
}
