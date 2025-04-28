package com.rafaeldsal.paymentservice.dto;

import lombok.Builder;
import org.springframework.http.HttpStatus;

@Builder
public record ErrorResponseDto(
    String message,
    HttpStatus status,
    Integer statusCode) {
}
