package com.rafaeldsal.paymentservice.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rafaeldsal.paymentservice.dto.PriceDto;
import com.rafaeldsal.paymentservice.dto.ProductDataDto;
import com.rafaeldsal.paymentservice.dto.ProductEventDto;
import com.rafaeldsal.paymentservice.exception.ProductEventException;
import com.rafaeldsal.paymentservice.exception.StripeIntegrationException;
import com.rafaeldsal.paymentservice.mapper.ProductMapper;
import com.rafaeldsal.paymentservice.service.PriceService;
import com.rafaeldsal.paymentservice.service.ProductService;
import com.stripe.exception.StripeException;
import com.stripe.model.Price;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductEventConsumer {

  private final ObjectMapper objectMapper;
  private final ProductService productService;
  private final PriceService priceService;

  @KafkaListener(
      topics = "${webservices.minhaprata.kafka.topic.product-created}",
      groupId = "product-created-consumer-1"
  )
  public void consumeProductCreatedEvent(String message) {
    String createdStripeProductId = null;
    try {

      ProductEventDto productEventDto = objectMapper.readValue(message, ProductEventDto.class);
      ProductDataDto dto = ProductMapper.toProductDataDto(productEventDto);
      log.info("Recebido evento de produto: {}", message);

      var ecommerceProduct = productService.create(dto);
      createdStripeProductId = ecommerceProduct.getProductKey();
      log.info("Produto criado no Stripe com ID: {}", createdStripeProductId);

      log.info("Adicionando preço ao produto de ID {}", createdStripeProductId);
      PriceDto priceDto = PriceDto.builder()
          .currency(dto.currency())
          .unitAmount(dto.price())
          .productKey(createdStripeProductId)
          .build();
      Price price = priceService.create(priceDto);
      log.info("Preco recebido: {}", productEventDto.data().price());
      log.info("Preco no DTO para Stripe: {}", priceDto.unitAmount());

      log.info("Preço {} adicionado ao produto {} com sucesso", price.getId(), ecommerceProduct.getProductKey());

    } catch (JsonProcessingException e) {
      log.error("Erro ao processar evento: {}", e);
      throw new ProductEventException("Erro ao processar evento de produto - ERROR - " + e);

    } catch (StripeException e) {
      if (createdStripeProductId != null) {
        productService.deleteStripeProduct(createdStripeProductId);
      }

      log.error("Erro ao processar evento: {}", e);
      throw new StripeIntegrationException("Erro ao criar produto/preço no Stripe - ERROR - " + e);
    }
  }

}
