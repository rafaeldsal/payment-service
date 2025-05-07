package com.rafaeldsal.paymentservice.service;

import com.rafaeldsal.paymentservice.dto.PaymentDto;

public interface PaymentService {

  void process(PaymentDto dto);
  void processWebhookEvent(String payload, String sigHeader);
}
