package com.rafaeldsal.paymentservice.model;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Document("payment")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Payment implements Serializable {

  @Id
  private String id;

  private String paymentMethod;
  private String transactionId;
  private String paymentIntentId;
  private String clientSecret;
  private String status;
  private BigDecimal amount;
  private String orderId;
  private String userId;
  private String currency;
  private LocalDateTime createdAt;

}
