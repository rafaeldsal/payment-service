package com.rafaeldsal.paymentservice.exception.handler;

import com.rafaeldsal.paymentservice.dto.ErrorResponseDto;
import com.rafaeldsal.paymentservice.exception.BadRequestException;
import com.rafaeldsal.paymentservice.exception.KafkaPublishingException;
import com.rafaeldsal.paymentservice.exception.KafkaSubscribeException;
import com.rafaeldsal.paymentservice.exception.NotFoundException;
import com.rafaeldsal.paymentservice.exception.ProductEventException;
import com.rafaeldsal.paymentservice.exception.StripeIntegrationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ResourceHandler {

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<ErrorResponseDto> handlerNotFoundException(NotFoundException n) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponseDto.builder()
        .message(n.getMessage())
        .status(HttpStatus.NOT_FOUND)
        .statusCode(HttpStatus.NOT_FOUND.value())
        .build());
  }

  @ExceptionHandler(ProductEventException.class)
  public ResponseEntity<ErrorResponseDto> handlerProductEventException(ProductEventException n) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponseDto.builder()
        .message(n.getMessage())
        .status(HttpStatus.NOT_FOUND)
        .statusCode(HttpStatus.NOT_FOUND.value())
        .build());
  }

  @ExceptionHandler(StripeIntegrationException.class)
  public ResponseEntity<ErrorResponseDto> handlerStripeIntegrationException(StripeIntegrationException n) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponseDto.builder()
        .message(n.getMessage())
        .status(HttpStatus.NOT_FOUND)
        .statusCode(HttpStatus.NOT_FOUND.value())
        .build());
  }

  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<ErrorResponseDto> handlerBadRequestException(BadRequestException n) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponseDto.builder()
        .message(n.getMessage())
        .status(HttpStatus.NOT_FOUND)
        .statusCode(HttpStatus.NOT_FOUND.value())
        .build());
  }

  @ExceptionHandler(KafkaSubscribeException.class)
  public ResponseEntity<ErrorResponseDto> handlerKafkaSubscribeException(KafkaSubscribeException n) {
    return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(ErrorResponseDto.builder()
        .message(n.getMessage())
        .status(HttpStatus.UNPROCESSABLE_ENTITY)
        .statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value())
        .build());
  }

  @ExceptionHandler(KafkaPublishingException.class)
  public ResponseEntity<ErrorResponseDto> handlerKafkaPublishingException(KafkaPublishingException n) {
    return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(ErrorResponseDto.builder()
        .message(n.getMessage())
        .status(HttpStatus.UNPROCESSABLE_ENTITY)
        .statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value())
        .build());
  }

}
