package com.rafaeldsal.paymentservice.dto;

import lombok.Builder;


@Builder
public record PaymentResponseDto(
    String transactionId,
    String status,
    String orderId,
    String userId,
    String clientSecret,
    String paymentIntentId
) {
}
