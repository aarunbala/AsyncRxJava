package com.microservices.model;

import org.springframework.cloud.sleuth.annotation.NewSpan;
import org.springframework.data.jpa.repository.JpaRepository;

@NewSpan("product")
public interface ProductRepository extends JpaRepository<Product, String> {

}
