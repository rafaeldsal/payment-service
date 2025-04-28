package com.rafaeldsal.paymentservice.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record PriceDto(
    String currency,
    String productKey,
    BigDecimal unitAmount
) {
}
