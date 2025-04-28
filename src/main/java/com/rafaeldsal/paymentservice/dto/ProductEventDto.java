package com.rafaeldsal.paymentservice.dto;

public record ProductEventDto(
  String eventType,
  String timestamp,
  ProductDataDto data
) {
}
