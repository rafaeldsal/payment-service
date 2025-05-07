package com.rafaeldsal.paymentservice.controller;

import com.rafaeldsal.paymentservice.dto.PaymentDto;
import com.rafaeldsal.paymentservice.dto.PaymentResponseDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

public interface PaymentController {

  ResponseEntity<String> handleWebhook(@RequestBody String payload, @RequestHeader("Stripe-Signature") String sigHeader);
}
