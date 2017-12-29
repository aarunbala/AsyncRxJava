package com.microservices.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Allergen {
	@JsonIgnore
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String productId;
	private String ingredient;
	private String nutrientQualifier;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getIngredient() {
		return ingredient;
	}
	public void setIngredient(String ingredient) {
		this.ingredient = ingredient;
	}
	public String getNutrientQualifier() {
		return nutrientQualifier;
	}
	public void setNutrientQualifier(String nutrientQualifier) {
		this.nutrientQualifier = nutrientQualifier;
	}
	@Override
	public String toString() {
		return "Allergen [id=" + id + ", productId=" + productId + ", ingredient=" + ingredient + ", nutrientQualifier="
				+ nutrientQualifier + "]";
	}
	
	
}
