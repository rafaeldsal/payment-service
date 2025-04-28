package com.rafaeldsal.paymentservice.repository;

import com.rafaeldsal.paymentservice.model.EcommerceProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EcommerceProductRepository extends JpaRepository<EcommerceProduct, String> {
}
