package com.rafaeldsal.paymentservice.mapper;

import com.rafaeldsal.paymentservice.dto.ProductDataDto;
import com.rafaeldsal.paymentservice.dto.ProductEventDto;

public class ProductMapper {

  public static ProductDataDto toProductDataDto(ProductEventDto dto) {
    return ProductDataDto.builder()
        .productId(dto.data().productId())
        .name(dto.data().name())
        .price(dto.data().price())
        .currency(dto.data().currency())
        .description(dto.data().description())
        .imgUrl(dto.data().imgUrl())
        .stockQuantity(dto.data().stockQuantity())
        .build();
  }
}
