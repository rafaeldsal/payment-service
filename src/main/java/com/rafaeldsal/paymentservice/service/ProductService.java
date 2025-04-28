package com.rafaeldsal.paymentservice.service;

import com.rafaeldsal.paymentservice.dto.ProductDataDto;
import com.rafaeldsal.paymentservice.dto.ProductEventDto;
import com.rafaeldsal.paymentservice.model.EcommerceProduct;
import com.stripe.exception.StripeException;

public interface ProductService {

  EcommerceProduct create(ProductDataDto product) throws StripeException;
  void deleteStripeProduct(String stripeProductId);
}
