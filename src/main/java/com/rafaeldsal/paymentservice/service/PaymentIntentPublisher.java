package com.rafaeldsal.paymentservice.service;

import com.rafaeldsal.paymentservice.dto.PaymentResponseDto;

public interface PaymentIntentPublisher {
  void sendMessage(PaymentResponseDto dto);
}
