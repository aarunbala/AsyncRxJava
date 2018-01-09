package com.microservices.model;

import java.util.List;

import org.springframework.cloud.sleuth.annotation.NewSpan;
import org.springframework.data.jpa.repository.JpaRepository;
@NewSpan("Allergen")
public interface AllergenRepository extends JpaRepository<Allergen, Long> {

	List<Allergen> findByProductIdIn(List<String> productIds);
}
