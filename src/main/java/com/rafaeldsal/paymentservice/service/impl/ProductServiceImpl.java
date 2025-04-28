package com.rafaeldsal.paymentservice.service.impl;

import com.rafaeldsal.paymentservice.dto.ProductDataDto;
import com.rafaeldsal.paymentservice.exception.NotFoundException;
import com.rafaeldsal.paymentservice.repository.EcommerceProductRepository;
import com.rafaeldsal.paymentservice.model.EcommerceProduct;
import com.rafaeldsal.paymentservice.service.ProductService;
import com.stripe.exception.StripeException;
import com.stripe.model.Product;
import com.stripe.param.ProductCreateParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

  private final EcommerceProductRepository ecommerceProductRepository;

  @Override
  public EcommerceProduct create(ProductDataDto product) throws StripeException {

    ProductCreateParams.Builder builder = ProductCreateParams.builder()
        .setName(product.name())
        .setDescription(product.description());

    if (product.imgUrl() != null && product.imgUrl().isBlank()) {
      builder.addAllImage(List.of(product.imgUrl()));
    }

    ProductCreateParams productParams = builder.build();
    Product productStripe = Product.create(productParams);
    String stripeProductId = productStripe.getId();

    EcommerceProduct ecommerceProduct = ecommerceProductRepository.findById(product.productId())
            .orElseThrow(() -> new NotFoundException("Produto não encontrado no banco: " + product.productId()));

    ecommerceProduct.setProductKey(stripeProductId);
    ecommerceProduct.setDtUpdated(LocalDateTime.now());

    return ecommerceProductRepository.save(ecommerceProduct);
  }

  @Override
  public void deleteStripeProduct(String stripeProductId) {
    try {
      Product stripeProduct = Product.retrieve(stripeProductId);
      stripeProduct.delete();
      log.info("Produto {} deletado do Stripe com sucesso", stripeProductId);
    } catch (StripeException e) {
      log.error("Erro ao deletar produto no Stripe durante rollback. - ERROR - {}", e.getMessage(), e);
    }
  }


}
