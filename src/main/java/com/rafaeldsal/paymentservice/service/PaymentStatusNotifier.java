package com.rafaeldsal.paymentservice.service;

import com.rafaeldsal.paymentservice.dto.PaymentWebhookDto;

public interface PaymentStatusNotifier {
  void sendMessage(PaymentWebhookDto dto);
}
