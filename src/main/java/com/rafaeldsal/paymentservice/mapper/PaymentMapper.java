package com.rafaeldsal.paymentservice.mapper;

import com.rafaeldsal.paymentservice.dto.PaymentDto;
import com.rafaeldsal.paymentservice.dto.PaymentErrorInfo;
import com.rafaeldsal.paymentservice.dto.PaymentResponseDto;
import com.rafaeldsal.paymentservice.dto.PaymentWebhookDto;
import com.rafaeldsal.paymentservice.model.Payment;
import com.stripe.model.PaymentIntent;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

public class PaymentMapper {

  private PaymentMapper() {
  }

  public static Payment toEntity(PaymentDto dto, PaymentIntent intent) {
    return Payment.builder()
        .id(UUID.randomUUID().toString())
        .transactionId(dto.transactionId())
        .orderId(dto.orderId())
        .userId(dto.userId())
        .status(intent.getStatus())
        .paymentMethod(dto.paymentMethod())
        .clientSecret(intent.getClientSecret())
        .createdAt(LocalDateTime.now())
        .amount(BigDecimal.valueOf(dto.amount()))
        .paymentIntentId(intent.getId())
        .currency(dto.currency())
        .build();
  }

  public static PaymentResponseDto toResponseDto(Payment payment) {
    return PaymentResponseDto.builder()
        .transactionId(payment.getTransactionId())
        .paymentIntentId(payment.getPaymentIntentId())
        .clientSecret(payment.getClientSecret())
        .userId(payment.getUserId())
        .orderId(payment.getOrderId())
        .status(payment.getStatus().toUpperCase())
        .build();
  }

  public static PaymentWebhookDto toWebhookResponse(PaymentIntent paymentIntent, String eventType) {
    if (eventType.equalsIgnoreCase("payment_intent.succeeded") ||
        eventType.equalsIgnoreCase("payment_intent.processing")) {
      return PaymentWebhookDto.success(
          paymentIntent.getMetadata().get("transactionId"),
          paymentIntent.getId(),
          paymentIntent.getStatus(),
          Instant.now().toString()
      );
    } else {
      return PaymentWebhookDto.error(
          paymentIntent.getMetadata().get("transactionId"),
          paymentIntent.getId(),
          paymentIntent.getStatus(),
          Instant.now().toString(),
          new PaymentErrorInfo(
              paymentIntent.getLastPaymentError().getCode(),
              paymentIntent.getLastPaymentError().getMessage(),
              paymentIntent.getLastPaymentError().getDeclineCode())
      );
    }
  }
}
