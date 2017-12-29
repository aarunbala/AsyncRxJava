package com.microservices.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.microservices.vo.ProductVO;

public class Products implements Comparable<Products>{

	@JsonIgnore
 	private Map<String, Product> basic = new HashMap<>();
 	private Map<String, List<Allergen>> allergen = new HashMap<>();
 	
 	private List<ProductVO> products = new ArrayList<>();
 	
	public Map<String, Product> getBasic() {
		if(this.basic == null) {
			this.basic = new HashMap<>();
		}
		return basic;
	}

	public void setBasic(Map<String, Product> basic) {
		this.basic = basic;
	}

	public Map<String, List<Allergen>> getAllergen() {
		if(allergen == null) { this.allergen = new HashMap<>();}
		return allergen;
	}

	public void setAllergen(Map<String, List<Allergen>> allergen) {
		this.allergen = allergen;
	}

	public List<ProductVO> getProducts() {
		return products;
	}

	public void setProducts(List<ProductVO> products) {
		this.products = products;
	}

	@Override
	public int compareTo(Products o) {
		// TODO Auto-generated method stub
		return 0;
	}

	
}
