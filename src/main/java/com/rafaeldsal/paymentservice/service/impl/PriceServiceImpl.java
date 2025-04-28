package com.rafaeldsal.paymentservice.service.impl;

import com.rafaeldsal.paymentservice.dto.PriceDto;
import com.rafaeldsal.paymentservice.exception.BadRequestException;
import com.rafaeldsal.paymentservice.exception.StripeIntegrationException;
import com.rafaeldsal.paymentservice.service.PriceService;
import com.stripe.exception.StripeException;
import com.stripe.model.Price;
import com.stripe.param.PriceCreateParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Slf4j
public class PriceServiceImpl implements PriceService {

  @Override
  public Price create(PriceDto priceDto) {

    if (priceDto.currency() == null || priceDto.productKey() == null) {
      throw new BadRequestException("Moeda ou ID do produto Stripe são  obrigatórios para criação do preço");
    }

    PriceCreateParams priceParams = PriceCreateParams.builder()
        .setCurrency(priceDto.currency())
        .setUnitAmount(priceDto.unitAmount().multiply(BigDecimal.valueOf(100)).longValue())
        .setProduct(priceDto.productKey())
        .build();

    try {
      return Price.create(priceParams);
    } catch (StripeException e) {
      log.error("Erro ao criar preço no Stripe: {}", e.getMessage());
      throw new StripeIntegrationException("Erro ao criar preço no Stripe - ERROR - " + e);
    }
  }
}
