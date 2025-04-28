package com.rafaeldsal.paymentservice.service;

import com.rafaeldsal.paymentservice.dto.PriceDto;
import com.rafaeldsal.paymentservice.dto.ProductEventDto;
import com.stripe.model.Price;

public interface PriceService {

  Price create(PriceDto priceDto);
}
