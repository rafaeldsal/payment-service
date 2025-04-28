package com.rafaeldsal.paymentservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_product")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EcommerceProduct {

  @Id
  private String productId;

  private String name;

  private String description;

  private BigDecimal price;

  private Long stockQuantity;

  private String currency;

  private String imgUrl;

  private LocalDateTime dtCreated;

  private LocalDateTime dtUpdated;

  private String categoryId;

  private String productKey;

}
