package com.rafaeldsal.paymentservice.controller.impl;

import com.rafaeldsal.paymentservice.controller.PaymentController;
import com.rafaeldsal.paymentservice.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/webhooks")
@RequiredArgsConstructor
public class PaymentControllerImpl implements PaymentController {

  private final PaymentService paymentService;

  @Override
  @PostMapping("/stripe")
  public ResponseEntity<String> handleWebhook(@RequestBody String payload, @RequestHeader("Stripe-Signature") String sigHeader) {
    paymentService.processWebhookEvent(payload, sigHeader);
    return ResponseEntity.ok().build();
  }
}
