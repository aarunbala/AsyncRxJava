package com.microservices.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AllergenRepository extends JpaRepository<Allergen, Long> {

	List<Allergen> findByProductIdIn(List<String> productIds);
}
