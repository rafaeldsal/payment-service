package com.rafaeldsal.paymentservice.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ProductDataDto(
    String productId,
    String name,
    String description,
    BigDecimal price,
    Long stockQuantity,
    String currency,
    String imgUrl
) {

}
