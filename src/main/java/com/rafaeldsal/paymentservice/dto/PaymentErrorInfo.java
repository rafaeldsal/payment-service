package com.rafaeldsal.paymentservice.dto;

import lombok.Builder;

@Builder
public record PaymentErrorInfo(
    String errorCode,
    String errorMessage,
    String stripeFailureCode
) {
}
