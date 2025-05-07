package com.rafaeldsal.paymentservice.repository;

import com.rafaeldsal.paymentservice.model.Payment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends MongoRepository<Payment, String> {

  Optional<Payment> findByOrderIdAndTransactionId(String orderId, String transactionId);
  Optional<Payment> findByPaymentIntentId(String paymentIntentId);
}
